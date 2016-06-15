package main

import (
	"github.com/gorilla/mux"
	"github.com/gocql/gocql"
	"net/http"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"os"
)

type data struct {
	Timestamp int64 `json:"timestamp"`
	X float64 `json:"x"`
	Y float64 `json:"y"`
	Z float64 `json:"z"`
}

type trainingData struct {
	UserId string `json:"userID"`
	Activity string `json:"activity"`
	CurData data `json:"acceleration"`
}

var session gocql.Session

func main() {
	cluster := gocql.NewCluster(os.Args[1])

	cluster.Keyspace = "activitytracking"
	var err error
	session, err = cluster.CreateSession()
	if err != nil {
		fmt.Println(err)
		return
	}
	m := mux.NewRouter()
	m.HandleFunc("/acceleration", handleAcceleration)
	m.HandleFunc("/training", handleTraining)
	http.ListenAndServe(":3000", m)
}

func handleAcceleration(w http.ResponseWriter, r *http.Request) {
	myData := &data{}
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
	fmt.Println(myData.Timestamp, ":", myData.X, ":", myData.Y, ":", myData.Z)
	w.WriteHeader(http.StatusCreated)
}

func handleTraining(w http.ResponseWriter, r *http.Request) {
	myData := &trainingData{}
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
	session.

	w.WriteHeader(http.StatusCreated)
}
