#!/bin/bash

echo "Iniciando limpeza completa do Docker..."

# 1. Parar todos os containers em execucao
echo "Parando containers..."
docker stop $(docker ps -aq) 2>/dev/null

# 2. Remover todos os containers
echo "Removendo containers..."
docker rm $(docker ps -aq) 2>/dev/null

# 3. Remover todas as imagens
echo "Removendo imagens..."
docker rmi -f $(docker images -aq) 2>/dev/null

# 4. Remover todos os volumes
echo "Removendo volumes..."
docker volume rm $(docker volume ls -q) 2>/dev/null

# 5. Remover todas as networks personalizadas
echo "Removendo networks..."
docker network prune -f

# 6. Limpeza do sistema (build cache e outros residuos)
echo "Limpando cache de build e residuos extras..."
docker system prune -a -f --volumes

echo "Limpeza concluida com sucesso."