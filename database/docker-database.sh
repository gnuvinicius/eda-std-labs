#!/bin/bash

docker network create msdelivery-network

docker run -d --name postgres \
  -p 5432:5432 \
  --network msdelivery-network \
  --restart always \
  -e POSTGRES_PASSWORD=2AkByM4NfHFkeJz \
  -e POSTGRES_USER=postgres \
  -v ./pgdata:/var/lib/postgresql/data postgres:15.5-alpine3.19

# wait for postgres to accept connections (with timeout)
echo "Waiting for postgres to be ready..."
MAX_RETRIES=60
RETRY_INTERVAL=1
i=0
until docker exec postgres pg_isready -U postgres >/dev/null 2>&1; do
  i=$((i+1))
  if [ $i -ge $MAX_RETRIES ]; then
    echo "Postgres did not become ready after $MAX_RETRIES seconds" >&2
    exit 1
  fi
  sleep $RETRY_INTERVAL
done
echo "Postgres is ready. Creating databases..."

# rodar o comando abaixo para criar os bancos de dados
docker exec postgres bash -c "psql -U postgres -c 'CREATE DATABASE msregister_db;'"
docker exec postgres bash -c "psql -U postgres -c 'CREATE DATABASE msorder_db;'"
docker exec postgres bash -c "psql -U postgres -c 'CREATE DATABASE mscatalog_db;'"
docker exec postgres bash -c "psql -U postgres -c 'CREATE DATABASE msdelivery_db;'"