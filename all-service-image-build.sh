#!/bin/sh

./gradlew :membership-service:jibDockerBuild
./gradlew :banking-service:jibDockerBuild
