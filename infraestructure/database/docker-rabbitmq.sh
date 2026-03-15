#!/usr/bin/env bash
set -euo pipefail

IMAGE_NAME="garage/rabbitmq-orders:local"
CONTAINER_NAME="rabbitmq"
NETWORK_NAME="eda-network"
RABBITMQ_DOCKERFILE_DIR="$(cd "$(dirname "$0")/../images/rabbitmq" && pwd)"

# Create shared network for microservices if it does not exist.
if ! docker network inspect "$NETWORK_NAME" >/dev/null 2>&1; then
  docker network create "$NETWORK_NAME"
fi

# Build RabbitMQ image with predefined exchange/queues/bindings.
docker build -t "$IMAGE_NAME" "$RABBITMQ_DOCKERFILE_DIR"

# Recreate container to guarantee fresh topology load.
if docker ps -a --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
  docker rm -f "$CONTAINER_NAME"
fi

docker run -d \
  --name "$CONTAINER_NAME" \
  --network "$NETWORK_NAME" \
  -p 5672:5672 \
  -p 15672:15672 \
  "$IMAGE_NAME"

echo "RabbitMQ is up"
echo "AMQP: amqp://garage_user:garage_password@localhost:5672/"
echo "UI:   http://localhost:15672 (garage_user / garage_password)"

