#!/bin/bash

if [ $# -eq 0 ]; then
  echo "Usage: ./quickstart.sh <service-name>"
  exit 0;
fi

SERVICE=$1
echo "Configuring service $SERVICE"

files="Dockerfile kube.yml deploy.env settings.gradle src/main/webapp/WEB-INF/web.xml"
classes="src/main/java/com/newmanconnection/hello/Hello.java src/main/java/com/newmanconnection/hello/db/HelloDB.java src/main/java/com/newmanconnection/hello/rest/ServiceIdentificationFilter.java src/main/java/com/newmanconnection/hello/rest/ServiceConfiguration.java src/main/java/com/newmanconnection/hello/rest/HelloSvc.java src/main/java/com/newmanconnection/hello/db/DBUpgrade.java"

CAMEL_SERVICE=`echo $SERVICE | sed -r 's/(_|-)([a-z])/\U\2/g'`
CAP_SERVICE="$(tr '[:lower:]' '[:upper:]' <<< ${CAMEL_SERVICE:0:1})${CAMEL_SERVICE:1}"
DATE=`date +%D`
USER=`whoami`
printf "Service: $CAP_SERVICE\nCreator: $USER\nCreated: $DATE" > README.md

for file in $files; do 
  echo "Updating $file"
  sed -i "s/hello/$SERVICE/g" $file
  sed -i "s/Hello/$CAP_SERVICE/g" $file
done

for file in $classes; do 
  echo "Updating $file"
  sed -i "s/hello/$CAMEL_SERVICE/g" $file
  sed -i "s/Hello/$CAP_SERVICE/g" $file
done

mv src/main/java/com/newmanconnection/hello/Hello.java src/main/java/com/newmanconnection/hello/$CAP_SERVICE.java
mv src/main/java/com/newmanconnection/hello/db/HelloDB.java src/main/java/com/newmanconnection/hello/db/${CAP_SERVICE}DB.java
mv src/main/java/com/newmanconnection/hello/rest/HelloSvc.java src/main/java/com/newmanconnection/hello/rest/${CAP_SERVICE}Svc.java
mv src/main/java/com/newmanconnection/hello src/main/java/com/newmanconnection/$(tr '[:upper:]' '[:lower:]' $CAMEL_SERVICE)

git rm -f quick-start.sh 
git remote set-url origin git@github.com:newmanconnection/$SERVICE-svc.git
git add *
git commit -m "Service initialized"
