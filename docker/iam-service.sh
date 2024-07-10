#!/usr/bin/env bash

if [ -z "$PROFILE" ]; then
    echo "Empty profile"
    export PROFILE="dev"
fi

if [ -z "$POSTGRES_USERNAME" ]; then
    echo "Empty POSTGRES_USERNAME"
    export POSTGRES_USERNAME="postgres"
fi

if [ -z "$POSTGRES_PASSWORD" ]; then
    echo "Empty POSTGRES_PASSWORD"
    export POSTGRES_PASSWORD="Kolya8787"
fi

if [ -z "$POSTGRES_DB" ]; then
    echo "Empty POSTGRES_DB"
    export POSTGRES_DB="mydatabase"
fi

if [ -z "$SPRING_DATASOURCE_URL" ]; then
    echo "Empty SPRING_DATASOURCE_URL"
    export SPRING_DATASOURCE_URL="jdbc:postgresql://postgres:5432/$POSTGRES_DB"
fi

echo "Starting service with profile: $PROFILE"
exec java -Dspring.profiles.active=${PROFILE} \
    -Dspring.datasource.username=${POSTGRES_USERNAME} \
    -Dspring.datasource.password=${POSTGRES_PASSWORD} \
    -Dspring.datasource.url=${SPRING_DATASOURCE_URL} \
    -jar /srv/security-jwt-0.0.1-SNAPSHOT.jar
