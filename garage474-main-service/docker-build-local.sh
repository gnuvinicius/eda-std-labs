#!/bin/bash

docker build -t legacy-soap-wildfly:local .

docker run --network ear-network --rm -e DB_HOST=postgres -e DB_PORT=5432 \
  -e DB_NAME=legacydb \
  -e DB_USER=postgres \
  -e DB_PASSWORD=2AkByM4NfHFkeJz \
  -p 8080:8080 \
  -p 9990:9990 \
  legacy-soap-wildfly:local
