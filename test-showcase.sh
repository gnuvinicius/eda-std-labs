#!/bin/bash

# Script de teste do Showcase Endpoint
# Uso: ./test-showcase.sh

BASE_URL="http://localhost:8080/api/v1/showcase/products"
TENANT_ID="f81d4fae-7dec-11d0-a765-00a0c91e6bf6"

echo "======================================"
echo "Teste do Showcase Endpoint"
echo "======================================"
echo ""

# 1. Listar primeira página de produtos
echo "1️⃣ Listar produtos (página 0, 20 itens)"
echo "GET $BASE_URL?page=0&size=20"
echo ""
curl -X GET "$BASE_URL?page=0&size=20" \
  -H "X-Tenant-ID: $TENANT_ID" \
  -H "Content-Type: application/json" \
  -s | jq .
echo ""
echo ""

# 2. Buscar por termo
echo "2️⃣ Buscar produtos por termo 'notebook'"
echo "GET $BASE_URL?search=notebook&page=0&size=10"
echo ""
curl -X GET "$BASE_URL?search=notebook&page=0&size=10" \
  -H "X-Tenant-ID: $TENANT_ID" \
  -H "Content-Type: application/json" \
  -s | jq .
echo ""
echo ""

# 3. Filtrar por brand (substitua pela UUID real)
echo "3️⃣ Filtrar por brand específica"
BRAND_ID="550e8400-e29b-41d4-a716-446655440000"
echo "GET $BASE_URL?brandId=$BRAND_ID&page=0&size=20"
echo ""
curl -X GET "$BASE_URL?brandId=$BRAND_ID&page=0&size=20" \
  -H "X-Tenant-ID: $TENANT_ID" \
  -H "Content-Type: application/json" \
  -s | jq .
echo ""
echo ""

# 4. Filtrar por categoria (substitua pela UUID real)
echo "4️⃣ Filtrar por categoria específica"
CATEGORY_ID="660f9500-f39c-52e4-b827-557766551111"
echo "GET $BASE_URL?categoryId=$CATEGORY_ID&page=0&size=20"
echo ""
curl -X GET "$BASE_URL?categoryId=$CATEGORY_ID&page=0&size=20" \
  -H "X-Tenant-ID: $TENANT_ID" \
  -H "Content-Type: application/json" \
  -s | jq .
echo ""
echo ""

# 5. Combinação de filtros
echo "5️⃣ Buscar com múltiplos filtros"
echo "GET $BASE_URL?search=samsung&brandId=$BRAND_ID&categoryId=$CATEGORY_ID&page=0&size=15"
echo ""
curl -X GET "$BASE_URL?search=samsung&brandId=$BRAND_ID&categoryId=$CATEGORY_ID&page=0&size=15" \
  -H "X-Tenant-ID: $TENANT_ID" \
  -H "Content-Type: application/json" \
  -s | jq .
echo ""
echo ""

# 6. Obter detalhes de um produto
echo "6️⃣ Obter detalhes de um produto específico"
PRODUCT_ID="123e4567-e89b-12d3-a456-426614174000"
echo "GET $BASE_URL/$PRODUCT_ID"
echo ""
curl -X GET "$BASE_URL/$PRODUCT_ID" \
  -H "Content-Type: application/json" \
  -s | jq .
echo ""
echo ""

# 7. Testar paginação
echo "7️⃣ Testar paginação - página 2 com 10 itens"
echo "GET $BASE_URL?page=2&size=10"
echo ""
curl -X GET "$BASE_URL?page=2&size=10" \
  -H "X-Tenant-ID: $TENANT_ID" \
  -H "Content-Type: application/json" \
  -s | jq .
echo ""
echo ""

# 8. Testar paginação inválida
echo "8️⃣ Testar paginação inválida (size > 100) - esperado: 400"
echo "GET $BASE_URL?page=0&size=150"
echo ""
curl -X GET "$BASE_URL?page=0&size=150" \
  -H "X-Tenant-ID: $TENANT_ID" \
  -H "Content-Type: application/json" \
  -s | jq .
echo ""
echo ""

echo "======================================"
echo "Testes concluídos!"
echo "======================================"

