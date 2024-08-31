#!/usr/bin/env bash

if [ -z "$PROFILE" ]; then
    echo "Empty profile"
    export PROFILE="aws"
fi

echo "Starting service with profile: $PROFILE"
exec java -Dspring.profiles.active=${PROFILE} \
    -jar /srv/iam-service-0.0.1-SNAPSHOT.jar
