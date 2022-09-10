#!/bin/bash

cd "$(dirname "$0")"


#export JDK_8=/opt/jdk/jdk8/

if [ "$(uname)" = "Darwin" ]; then
	./gradlew `cat mac_targets.txt`
else 
./gradlew publishAllPublicationsToMavenRepository
fi



