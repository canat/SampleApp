#!/bin/bash

mvn clean package -Dmaven.javadoc.skip=true -Dmaven.test.skip=true

SRC=target/PSCP.war

DEST=$TMPDIR/ant-media-server/

if [ ! -d "$DEST" ]
then
    echo "$0: Directory '${DEST}' not found."
    #download file and unzip it
    
    exit
fi


rm -rf $DEST/webapps/PSCP

cp  $SRC  $DEST/webapps/


#go to ant media server
cd $DEST

#shutdown 
./shutdown.sh


#start red5
./start-debug.sh &

sleep 60

