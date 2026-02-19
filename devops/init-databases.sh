#!/bin/bash
set -e

# This script creates multiple databases in PostgreSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE msregister_db;
    CREATE DATABASE msorder_db;
    CREATE DATABASE mscatalog_db;
    CREATE DATABASE msdelivery_db;
EOSQL

echo "Multiple databases created successfully!"

