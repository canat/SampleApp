#!/bin/bash


SRC=target/PSCP.war

TARGET=$TMPDIR
TARGET=~/softwares/

DEST=$TARGET/ant-media-server/

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
#./shutdown.sh


#start red5
./start-debug.sh &

sleep 50


