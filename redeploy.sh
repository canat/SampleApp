#!/bin/bash

mvn clean package -Dmaven.javadoc.skip=true -Dmaven.test.skip=true

USER_HOME_DIR=/Users/mekya

SRC=target/PSCP.war

DEST=~/softwares/ant-media-server/webapps/


#copy red5 jar from target dir to red5 dir

rm -rf $DEST/PSCP
cp  $SRC  $DEST



#go to red5 dir
cd ~/softwares/ant-media-server

#shutdown red5 
./stop.sh


#start red5
./start-debug.sh

