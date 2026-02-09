# Showcase Endpoint - Documentação

## Visão Geral

O endpoint **Showcase** foi criado para fornecer uma interface pública (sem autenticação) para clientes frontend/mobile consultarem produtos de forma otimizada. Este é um endpoint **somente leitura (GET)** que permite listar, filtrar, paginar e detalhar produtos.

## Características

✅ **Paginação obrigatória** - Evita sobrecarga com grandes volumes de dados
✅ **Filtros opcionais** - Busca por termo, brand e categoria
✅ **Isolamento por Tenant** - Cada cliente vê apenas seus produtos
✅ **Preços otimizados** - Mostra menor preço e preço promocional das variantes
✅ **Dados completos** - Inclui todas as variantes e suas especificações

---

## Endpoints

### 1. Listar Produtos com Paginação e Filtros

**Endpoint:** `GET /api/v1/showcase/products`

**Headers Obrigatórios:**
```http
X-Tenant-ID: {UUID do tenant}
Content-Type: application/json
```

**Parâmetros de Query (todos opcionais):**

| Parâmetro | Tipo | Descrição | Padrão |
|-----------|------|-----------|--------|
| `page` | int | Número da página (0-based) | 0 |
| `size` | int | Quantidade de produtos por página (máx: 100) | 20 |
| `search` | string | Busca em nome e descrição do produto | - |
| `brandId` | UUID | Filtrar por marca específica | - |
| `categoryId` | UUID | Filtrar por categoria específica | - |

**Exemplos de Requisições:**

```bash
# Listar primeira página com 20 produtos
curl -X GET "http://localhost:8080/api/v1/showcase/products?page=0&size=20" \
  -H "X-Tenant-ID: f81d4fae-7dec-11d0-a765-00a0c91e6bf6"

# Buscar produtos por termo
curl -X GET "http://localhost:8080/api/v1/showcase/products?search=notebook&page=0&size=10" \
  -H "X-Tenant-ID: f81d4fae-7dec-11d0-a765-00a0c91e6bf6"

# Filtrar por brand
curl -X GET "http://localhost:8080/api/v1/showcase/products?brandId=550e8400-e29b-41d4-a716-446655440000&page=0&size=20" \
  -H "X-Tenant-ID: f81d4fae-7dec-11d0-a765-00a0c91e6bf6"

# Filtrar por categoria
curl -X GET "http://localhost:8080/api/v1/showcase/products?categoryId=550e8400-e29b-41d4-a716-446655440000&page=0&size=20" \
  -H "X-Tenant-ID: f81d4fae-7dec-11d0-a765-00a0c91e6bf6"

# Combinações de filtros
curl -X GET "http://localhost:8080/api/v1/showcase/products?search=samsung&brandId=550e8400-e29b-41d4-a716-446655440000&categoryId=660f9500-f39c-52e4-b827-557766551111&page=0&size=15" \
  -H "X-Tenant-ID: f81d4fae-7dec-11d0-a765-00a0c91e6bf6"
```

**Resposta (200 OK):**

```json
{
  "content": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "name": "Notebook Dell XPS 13",
      "description": "Ultra-portátil com tela 4K e processador Intel i7",
      "slug": "notebook-dell-xps-13",
      "brand": {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "name": "Dell"
      },
      "category": {
        "id": "660f9500-f39c-52e4-b827-557766551111",
        "name": "Eletrônicos"
      },
      "tags": ["bestseller", "ultrabook", "high-performance"],
      "price": {
        "basePrice": null,
        "lowestVariantPrice": {
          "amount": 5999.99,
          "currency": "BRL"
        },
        "lowestPromotionalPrice": {
          "amount": 4999.99,
          "currency": "BRL"
        }
      },
      "variants": [
        {
          "id": "223f5678-f9ac-23e4-b567-537726285111",
          "productId": "123e4567-e89b-12d3-a456-426614174000",
          "skuCode": "DELL-XPS-13-512GB",
          "barcode": "5901234123457",
          "price": {
            "amount": 5999.99,
            "currency": "BRL"
          },
          "promotionalPrice": {
            "amount": 4999.99,
            "currency": "BRL"
          },
          "dimensions": {
            "weight": 1.2,
            "height": 0.7,
            "width": 30.0,
            "depth": 20.0
          }
        }
      ]
    }
  ],
  "pageInfo": {
    "page": 0,
    "size": 20,
    "totalPages": 5,
    "totalElements": 95
  }
}
```

**Códigos de Resposta:**

| Código | Descrição |
|--------|-----------|
| 200 | Sucesso - Produtos encontrados |
| 400 | Requisição inválida (tenantId, paginação ou UUIDs inválidos) |
| 500 | Erro interno do servidor |

---

### 2. Obter Detalhes de um Produto

**Endpoint:** `GET /api/v1/showcase/products/{id}`

**Headers Obrigatórios:**
```http
Content-Type: application/json
```

**Parâmetros de Caminho:**

| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| `id` | UUID | ID do produto a detalhar |

**Exemplos de Requisições:**

```bash
# Obter detalhes de um produto específico
curl -X GET "http://localhost:8080/api/v1/showcase/products/123e4567-e89b-12d3-a456-426614174000"

# Com formatação
curl -X GET "http://localhost:8080/api/v1/showcase/products/123e4567-e89b-12d3-a456-426614174000" | jq
```

**Resposta (200 OK):**

```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Notebook Dell XPS 13",
  "description": "Ultra-portátil com tela 4K e processador Intel i7",
  "slug": "notebook-dell-xps-13",
  "brand": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Dell"
  },
  "category": {
    "id": "660f9500-f39c-52e4-b827-557766551111",
    "name": "Eletrônicos"
  },
  "tags": ["bestseller", "ultrabook", "high-performance"],
  "price": {
    "basePrice": null,
    "lowestVariantPrice": {
      "amount": 5999.99,
      "currency": "BRL"
    },
    "lowestPromotionalPrice": {
      "amount": 4999.99,
      "currency": "BRL"
    }
  },
  "variants": [
    {
      "id": "223f5678-f9ac-23e4-b567-537726285111",
      "productId": "123e4567-e89b-12d3-a456-426614174000",
      "skuCode": "DELL-XPS-13-512GB",
      "barcode": "5901234123457",
      "price": {
        "amount": 5999.99,
        "currency": "BRL"
      },
      "promotionalPrice": {
        "amount": 4999.99,
        "currency": "BRL"
      },
      "dimensions": {
        "weight": 1.2,
        "height": 0.7,
        "width": 30.0,
        "depth": 20.0
      }
    },
    {
      "id": "334a6789-g0bd-34f5-c678-648837396222",
      "productId": "123e4567-e89b-12d3-a456-426614174000",
      "skuCode": "DELL-XPS-13-1TB",
      "barcode": "5901234123458",
      "price": {
        "amount": 7999.99,
        "currency": "BRL"
      },
      "promotionalPrice": {
        "amount": 6999.99,
        "currency": "BRL"
      },
      "dimensions": {
        "weight": 1.2,
        "height": 0.7,
        "width": 30.0,
        "depth": 20.0
      }
    }
  ]
}
```

**Códigos de Resposta:**

| Código | Descrição |
|--------|-----------|
| 200 | Sucesso - Produto encontrado |
| 404 | Produto não encontrado |
| 500 | Erro interno do servidor |

---

## Casos de Uso

### 1. Página de Listagem de Produtos (Frontend)

```javascript
// Requisição com filtros
const tenantId = 'f81d4fae-7dec-11d0-a765-00a0c91e6bf6';
const params = new URLSearchParams({
  page: 0,
  size: 20,
  search: 'notebook',
  brandId: '550e8400-e29b-41d4-a716-446655440000'
});

fetch(`/api/v1/showcase/products?${params}`, {
  headers: {
    'X-Tenant-ID': tenantId,
    'Content-Type': 'application/json'
  }
})
.then(res => res.json())
.then(data => {
  console.log(`Total de produtos: ${data.pageInfo.totalElements}`);
  console.log(`Página ${data.pageInfo.page + 1} de ${data.pageInfo.totalPages}`);
  data.content.forEach(product => {
    console.log(`${product.name} - R$ ${product.price.lowestVariantPrice.amount}`);
  });
});
```

### 2. Página de Detalhes do Produto

```javascript
// Ao clicar em um produto na lista
const productId = '123e4567-e89b-12d3-a456-426614174000';

fetch(`/api/v1/showcase/products/${productId}`)
  .then(res => res.json())
  .then(product => {
    // Exibir informações completas do produto
    console.log(product.name);
    console.log(product.description);
    
    // Mostrar todas as variantes disponíveis
    product.variants.forEach(variant => {
      console.log(`SKU: ${variant.skuCode} - R$ ${variant.price.amount}`);
    });
  });
```

### 3. Filtros Avançados (Mobile App)

```javascript
// Filtrar por categoria e marca
const categoryId = '660f9500-f39c-52e4-b827-557766551111';
const brandId = '550e8400-e29b-41d4-a716-446655440000';

const params = new URLSearchParams({
  page: 0,
  size: 50,
  categoryId,
  brandId
});

fetch(`/api/v1/showcase/products?${params}`, {
  headers: { 'X-Tenant-ID': tenantId }
})
.then(res => res.json())
.then(data => {
  // Renderizar grid de produtos
});
```

---

## Notas Importantes

### Multi-Tenancy

- **Header obrigatório:** `X-Tenant-ID` com UUID válido
- Cada tenant vê apenas seus próprios produtos
- Sem tenantId válido → Resposta 400 Bad Request

### Paginação

- **Página** é zero-based (0, 1, 2, ...)
- **Tamanho máximo** é 100 produtos por página
- Requisições com paginação inválida retornam 400 Bad Request

### Performance

- Búsqueda full-text é case-insensitive
- Filtros com UUIDs inválidos retornam 400
- Use paginação adequada para grandes conjuntos de dados

### Estrutura de Preços

- **lowestVariantPrice**: Menor preço entre todas as variantes
- **lowestPromotionalPrice**: Menor preço promocional (se existir)
- **variants**: Lista completa de todas as variantes com seus preços individuais

---

## Fluxo Arquitetural

```
HTTP Request (GET)
    ↓
ShowcaseController
    ↓
ListShowcaseProductsUseCase / GetShowcaseProductDetailsUseCase
    ↓
ProductRepository (interface de domínio)
    ↓
JpaProductRepository (implementação)
    ↓
ProductJpaRepository (Spring Data JPA)
    ↓
Database
    ↓
Domain Entity (Product, ProductVariant)
    ↓
ShowcaseProductResponse DTO
    ↓
HTTP Response (JSON)
```

---

## Status da Implementação

✅ Endpoint de listagem com paginação e filtros
✅ Endpoint de detalhes do produto
✅ Isolamento por tenant
✅ Tratamento de erros
✅ Logging estruturado
✅ Query otimizada com JPA

---

**Última atualização:** Fevereiro 2026

