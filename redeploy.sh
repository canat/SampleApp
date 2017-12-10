#!/bin/bash

mvn clean install -Dmaven.javadoc.skip=true -Dmaven.test.skip=true -DskipITs

USER_HOME_DIR=/Users/mekya

SRC=target/LiveApp.war

DEST=~/softwares/ant-media-server/webapps/


#copy red5 jar from target dir to red5 dir

rm -rf $DEST/LiveApp
cp  $SRC  $DEST



#go to red5 dir
cd ~/softwares/ant-media-server

#shutdown red5 
./stop.sh


#start red5
./start-debug.sh

