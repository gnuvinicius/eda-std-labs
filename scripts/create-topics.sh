#!/bin/bash
set -euo pipefail

BOOTSTRAP="${BOOTSTRAP_SERVERS:-kafka:9092}"

# Espera simples até o broker responder (timeout ~60s)
echo "Aguardando Kafka em $BOOTSTRAP..."
for i in $(seq 1 30); do
  if kafka-topics --bootstrap-server "$BOOTSTRAP" --list >/dev/null 2>&1; then
    echo "Kafka disponível."
    break
  fi
  echo "Aguardando... ($i)"
  sleep 2
done

# Cria tópicos com configurações customizadas
# Exemplo 1: tópico com retenção de 7 dias (ms) e cleanup policy delete
kafka-topics --create \
  --bootstrap-server "$BOOTSTRAP" \
  --replication-factor 1 \
  --partitions 3 \
  --topic pedidos \
  --config retention.ms=604800000 \
  --config cleanup.policy=delete

# Exemplo 2: tópico compacto (compaction) com 6 partições
kafka-topics --create \
  --bootstrap-server "$BOOTSTRAP" \
  --replication-factor 1 \
  --partitions 6 \
  --topic clientes-por-chave \
  --config cleanup.policy=compact \
  --config segment.ms=86400000

# Se quiser alterar configs depois da criação:
# kafka-configs --bootstrap-server "$BOOTSTRAP" --alter --entity-type topics --entity-name pedidos --add-config retention.ms=86400000

echo "Tópicos criados."