#!/usr/bin/env bash

REPOSITORY=/deploy/backend
cd $REPOSITORY

cp /deploy/application-secret.properties /deploy/backend/src/main/resources

PROGRAM_NAME=demo-0.0.1-SNAPSHOT
JAR_NAME=$(ls $REPOSITORY/build/libs | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $PROGRAM_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> Nothing to end."
else
  echo "> sudo kill -15 $CURRENT_PID"
  sudo kill -15 $CURRENT_PID
  sleep 5
fi

nohup java -jar $JAR_PATH > $REPOSITORY/nohup.out 2>&1 &
