package main

import (
	"github.com/gorilla/mux"
	"github.com/gocql/gocql"
	"net/http"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"time"
	"os"
)

// Acceleration Data
type data struct {
	Timestamp int64 `json:"timestamp"`
	X float64 `json:"x"`
	Y float64 `json:"y"`
	Z float64 `json:"z"`
}

// Metadata for training acceleration.
type accelerationTrainingData struct {
	UserId string `json:"userID"`
	Activity string `json:"activity"`
	StartTime int64 `json:"starttime"`
	CurData data `json:"acceleration"`
}

// Metadata for production acceleration.
type accelerationProductionData struct {
	UserId string `json:"userID"`
	CurData data `json:"acceleration"`
}


var session *gocql.Session

func main() {
	credentials := gocql.PasswordAuthenticator{Username: os.Getenv("CASSANDRA_USERNAME"), Password: os.Getenv("CASSANDRA_PASSWORD")}
	cluster := gocql.NewCluster("cassandra")
	if len(credentials.Username) > 0 {
		cluster.Authenticator = credentials
	}
	cluster.Timeout = time.Second * 10
	cluster.ProtoVersion = 4
	var err error
	session, err = cluster.CreateSession()
	for err != nil {
		fmt.Println("Error when connecting for keyspace creation. Trying again in 2 seconds.")
		fmt.Println(err)
		err = nil
		session, err = cluster.CreateSession()
		time.Sleep(time.Second * 2)
	}

	err = initKeyspace()
	if err != nil {
		fmt.Println(fmt.Println("Error when creating keyspace:"))
		fmt.Println(err)
		return
	}

	session.Close()

	cluster = gocql.NewCluster("cassandra")
	if len(credentials.Username) > 0 {
		cluster.Authenticator = credentials
	}
	cluster.Timeout = time.Second * 10
	cluster.ProtoVersion = 4
	cluster.Keyspace = "activitytracking"
	session, err = cluster.CreateSession()
	for err != nil {
		fmt.Println("Error when connecting for active use. Trying again in 2 seconds.")
		fmt.Println(err)
		err = nil
		session, err = cluster.CreateSession()
		time.Sleep(time.Second * 2)
	}

	// Create tables if non-existent.
	err = initAccelerationProductionTable()
	if err != nil {
		fmt.Println(err)
		return
	}
	err = initAccelerationTrainingTable()
	if err != nil {
		fmt.Println(err)
		return
	}

	fmt.Println("Initialization complete.")

	m := mux.NewRouter()
	m.HandleFunc("/production/acceleration", handleAccelerationProduction)
	m.HandleFunc("/training/acceleration", handleAccelerationTraining)
	http.ListenAndServe(":3000", m)
}

func initKeyspace() error {
	err := session.Query(`CREATE KEYSPACE IF NOT EXISTS activitytracking WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };`).Exec()
	if err != nil {
		return err
	}
	return nil
}

func initAccelerationTrainingTable() error {
	// Create the Cassandra table if not there already.
	err := session.Query(`CREATE TABLE IF NOT EXISTS trainingAcceleration (userid text, activity text, starttime timestamp, time timestamp, x double, y double, z double, PRIMARY KEY (userid, starttime, time));`).Exec()
	if err != nil {
		return err
	}
	return nil
}

func initAccelerationProductionTable() error {
	// Create the Cassandra table if not there already.
	err := session.Query(`CREATE TABLE IF NOT EXISTS productionAcceleration (userid text, time timestamp, x double, y double, z double, PRIMARY KEY (userid, time));`).Exec()
	if err != nil {
		return err
	}
	return nil
}

func handleAccelerationProduction(w http.ResponseWriter, r *http.Request) {
	// Read and parse request data.
	myData := &accelerationProductionData{}
	data, err := ioutil.ReadAll(r.Body)
	if err != nil {
		fmt.Println(err)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
	err = json.Unmarshal(data, &myData)
	if err != nil {
		fmt.Println(err)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}

	// Insert data into Cassandra.
	err = session.Query(`INSERT INTO productionAcceleration (userid, timestamp, x, y, z) VALUES (?, ?, ?, ?, ?, ?)`,
		myData.UserId,
		myData.CurData.Timestamp,
		myData.CurData.X,
		myData.CurData.Y,
		myData.CurData.Z,
	).Exec()
	if err != nil {
		fmt.Println("Error when inserting:")
		fmt.Println(err)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}

	// Android app expects the Status Created code for responses signaling success.
	w.WriteHeader(http.StatusCreated)
}

func handleAccelerationTraining(w http.ResponseWriter, r *http.Request) {
	// Read and parse request data.
	myData := &accelerationTrainingData{}
	data, err := ioutil.ReadAll(r.Body)
	if err != nil {
		fmt.Println(err)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
	err = json.Unmarshal(data, &myData)
	if err != nil {
		fmt.Println(err)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}

	// Insert data into Cassandra.
	err = session.Query(`INSERT INTO trainingAcceleration (userid, activity, starttime, time, x, y, z) VALUES (?, ?, ?, ?, ?, ?, ?);`,
		myData.UserId,
		myData.Activity,
		myData.StartTime,
		myData.CurData.Timestamp,
		myData.CurData.X,
		myData.CurData.Y,
		myData.CurData.Z,
	).Exec()
	if err != nil {
		fmt.Println("Error when inserting:")
		fmt.Println(err)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}

	// Android app expects the Status Created code for responses signaling success.
	w.WriteHeader(http.StatusCreated)
}

