#!/bin/bash

docker network create msdelivery-network

docker run -d --name postgres \
  -p 5432:5432 \
  --network msdelivery-network \
  --restart always \
  -e POSTGRES_PASSWORD=2AkByM4NfHFkeJz \
  -e POSTGRES_USER=postgres \
  -v ./pgdata:/var/lib/postgresql/data postgres:15.5-alpine3.19
