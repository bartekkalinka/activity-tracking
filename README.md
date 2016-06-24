# Introduction
...

# Setting up
Clone the repo, and *docker-compose up* in the repo root.

It exposes the 9042 cassandra port and the 3000 server port.

# Backing up/ Restoring cassandra

## Backing up

To backup the cassandra files launch:

```
docker run --rm -v activitytracking_cassandraData:/var/lib/cassandra -v LOCALDIRECTORY:/backup ubuntu tar cvf /backup/backup.tar /var/lib/cassandra
```
where LOCALDIRECTORY is a directory on your system.

1. Launches ubuntu container which will get destroyed after exiting (--rm)
2. Mounts *activitytracking_cassandraData* volume to */var/lib/cassandra* in the container.
3. Mounts your **local directory** (for example /opt/data on linux/OSX, or c:\opt\data on windows).
4. Packs the *activitytracking_cassandraData* to the backup.tar file in your **local directory**.

## Restoring

To restore the cassandra files launch:

```
docker volume create --name activitytracking_cassandraData

docker run --rm -v activitytracking_cassandraData:/var/lib/cassandra -v LOCALDIRECTORY:/backup ubuntu bash -c "cd /var && tar xvf /backup/backup.tar --strip 1"
```
where LOCALDIRECTORY is a directory on your system.

1. Launches ubuntu container which will get destroyed after exiting (--rm)
2. Mounts *activitytracking_cassandraData* volume to */var/lib/cassandra* in the container.
3. Mounts your **local directory** (for example /opt/data on linux/OSX, or c:\opt\data on windows).
4. Unpacks the backup file from your local directory into the volume.
