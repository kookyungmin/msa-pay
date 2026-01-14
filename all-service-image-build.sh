#!/bin/sh

./service-image-build.sh membership-service
./service-image-build.sh banking-service
./service-image-build.sh money-service
./service-image-build.sh remittance-service
./service-image-build.sh payment-service

./service-image-build.sh logging-consumer
./service-image-build.sh task-consumer
./service-image-build.sh api-gateway

