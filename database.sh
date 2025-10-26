#!/bin/bash

docker run -d --name postgres \
  -p 5432:5432 \
  --network ear-network \
  --restart always \
  -e POSTGRES_PASSWORD=2AkByM4NfHFkeJz \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_DB=garage474_db \
  -v ./pgdata:/var/lib/postgresql/data postgres:15.5-alpine3.19
