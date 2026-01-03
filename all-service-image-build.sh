#!/bin/sh

./gradlew :membership-service:jibDockerBuild
./gradlew :banking-service:jibDockerBuild
./gradlew :money-service:jibDockerBuild
./gradlew :logging-consumer:jibDockerBuild
