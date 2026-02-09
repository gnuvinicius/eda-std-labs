# Showcase Endpoint - Guia Visual de Implementação

## 🎯 Fluxo de Requisição

```
┌─────────────────────────────────────────────────────────────┐
│                   CLIENTE (Web/Mobile)                      │
└─────────────────────────────────────────────────────────────┘
         ↓
         │ HTTP Request
         │ GET /api/v1/showcase/products
         │ X-Tenant-ID: {uuid}
         │ page=0&size=20&search=notebook
         ↓
┌─────────────────────────────────────────────────────────────┐
│           ShowcaseController                                │
│  • Validação de X-Tenant-ID                                 │
│  • Validação de paginação (page, size)                      │
│  • Validação de UUIDs (brandId, categoryId)                 │
│  • Delegação para Use Cases                                 │
└─────────────────────────────────────────────────────────────┘
         ↓
         ├─────────────────────────────────────┐
         │                                     │
         ↓                                     ↓
┌──────────────────────────────┐   ┌─────────────────────────┐
│ ListShowcaseProductsUseCase  │   │ GetShowcaseProductDetails│
│                              │   │ UUseCase                │
│ • Executa query com filtros  │   │                         │
│ • Busca por termo            │   │ • Busca produto por ID  │
│ • Filtro brand/categoria     │   │ • Calcula preços mínimos│
│ • Paginação                  │   │ • Retorna detalhes      │
│ • Calcula preços mínimos     │   │                         │
│ • Converte para DTOs         │   │                         │
└──────────────────────────────┘   └─────────────────────────┘
         │                                     │
         │     ProductRepository (Interface)   │
         │     ↓                               │
         └─────────┬───────────────────────────┘
                   ↓
         ┌─────────────────────────────────┐
         │   JpaProductRepository          │
         │  (Spring Data JPA + Custom JPA) │
         │                                 │
         │  • Converts Domain ↔ Persistence│
         │  • findProductsByTenantWithFilter│
         │  • findProductById              │
         └─────────────────────────────────┘
                   ↓
         ┌─────────────────────────────────┐
         │   ProductJpaRepository          │
         │   (Spring Data JPA Interface)   │
         │                                 │
         │  @Query("SELECT p FROM ...")    │
         │  • LOWER(name) LIKE             │
         │  • LOWER(description) LIKE      │
         │  • brandId = ? OR NULL          │
         │  • categoryId = ? OR NULL       │
         │  • tenantId = ?                 │
         └─────────────────────────────────┘
                   ↓
         ┌─────────────────────────────────┐
         │      PostgreSQL Database        │
         │                                 │
         │  [product]                      │
         │  [product_variant]              │
         │  [brand]                        │
         │  [category]                     │
         └─────────────────────────────────┘
                   ↓ (Results)
         ┌─────────────────────────────────┐
         │   ProductEntity (JPA)           │
         │   ProductVariantEntity (JPA)    │
         └─────────────────────────────────┘
                   ↓ (Convert)
         ┌─────────────────────────────────┐
         │   Product (Domain Entity)       │
         │   ProductVariant (Domain Entity)│
         └─────────────────────────────────┘
                   ↓ (Convert)
         ┌─────────────────────────────────┐
         │  ShowcaseProductResponse        │
         │  ShowcasePageResponse           │
         └─────────────────────────────────┘
                   ↓
         HTTP Response (JSON)
         200 OK
         ↓
┌─────────────────────────────────────────────────────────────┐
│                CLIENTE (Web/Mobile)                         │
│              Renderiza Lista de Produtos                    │
└─────────────────────────────────────────────────────────────┘
```

---

## 📦 Estrutura de Pacotes

```
mscatalog/
├── src/main/java/br/dev/garage474/mscatalog/
│
├── adapters/
│   ├── in/
│   │   └── web/
│   │       ├── controller/
│   │       │   ├── ProductController.java
│   │       │   ├── BrandController.java
│   │       │   ├── CategoryController.java
│   │       │   └── ShowcaseController.java                 ✨ NOVO
│   │       │
│   │       └── dto/
│   │           ├── ProductResponse.java
│   │           ├── ProductVariantResponse.java
│   │           ├── ShowcaseProductResponse.java            ✨ NOVO
│   │           ├── ShowcaseProductVariantResponse.java     ✨ NOVO (depreciado)
│   │           └── ShowcasePageResponse.java               ✨ NOVO
│   │
│   └── out/
│       └── persistence/
│           ├── entity/
│           ├── repository/
│           │   ├── ProductJpaRepository.java               📝 MODIFICADO
│           │   └── JpaProductRepository.java               📝 MODIFICADO
│           └── vo/
│
├── applications/
│   └── usecase/
│       ├── CreateProductUseCase.java
│       ├── ListProductsByTenantUseCase.java
│       ├── CreateProductVariantUseCase.java
│       ├── ListProductVariantsByProductUseCase.java
│       ├── UpdateProductVariantUseCase.java
│       ├── DeleteProductVariantUseCase.java
│       ├── ListShowcaseProductsUseCase.java                ✨ NOVO
│       └── GetShowcaseProductDetailsUseCase.java           ✨ NOVO
│
└── domain/
    ├── entities/
    ├── repositories/
    │   └── ProductRepository.java                          📝 MODIFICADO
    ├── services/
    └── vo/
```

---

## 🔄 Diagrama de Conversões

```
┌──────────────────────────────┐
│  ProductEntity (JPA)         │
│  - id                        │
│  - name                      │
│  - description               │
│  - slug                      │
│  - tenantId                  │
│  - brandEntity               │
│  - categoryEntity            │
│  - variants: List            │
└──────────────────────────────┘
         ↓ (convertProductToEntity)
┌──────────────────────────────┐
│  Product (Domain Entity)     │
│  - id                        │
│  - name                      │
│  - description               │
│  - slug                      │
│  - brand: Brand              │
│  - category: Category        │
│  - tags: Tags (VO)           │
│  - variants: List            │
└──────────────────────────────┘
         ↓ (convertToShowcaseResponse)
┌──────────────────────────────────────┐
│  ShowcaseProductResponse             │
│  - id                                │
│  - name                              │
│  - description                       │
│  - slug                              │
│  - brand: BrandResponse              │
│  - category: CategoryResponse        │
│  - tags: List<String>                │
│  - price: ProductPriceResponse       │
│    ├── basePrice                     │
│    ├── lowestVariantPrice            │
│    └── lowestPromotionalPrice        │
│  - variants: List<ProductVariant>    │
│    └── MoneyResponse, Dimensions     │
└──────────────────────────────────────┘
```

---

## 📋 Query SQL Executada

```sql
SELECT p 
FROM ProductEntity p 
WHERE p.tenantId = :tenantId 
  AND (
    LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) 
    OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
  )
  AND (:brandId IS NULL OR p.brandEntity.id = :brandId)
  AND (:categoryId IS NULL OR p.categoryEntity.id = :categoryId)
ORDER BY p.createdAt DESC
LIMIT :size OFFSET :page * :size
```

**Parâmetros:**
- `:tenantId` - UUID do tenant (obrigatório)
- `:searchTerm` - Termo de busca (case-insensitive)
- `:brandId` - ID da brand (opcional, NULL ignora)
- `:categoryId` - ID da categoria (opcional, NULL ignora)
- `:page` - Página atual (0-based)
- `:size` - Quantidade de registros por página

---

## 🎭 Cenários de Teste

### Teste 1: Listar Primeira Página
```
GET /api/v1/showcase/products?page=0&size=20
X-Tenant-ID: f81d4fae-7dec-11d0-a765-00a0c91e6bf6

Resposta:
- 20 produtos
- Página 0 de X
- Total de Y produtos
```

### Teste 2: Buscar por Termo
```
GET /api/v1/showcase/products?search=notebook&page=0&size=10
X-Tenant-ID: f81d4fae-7dec-11d0-a765-00a0c91e6bf6

Resposta:
- Produtos com "notebook" no nome ou descrição
- 10 itens por página
```

### Teste 3: Filtrar por Brand
```
GET /api/v1/showcase/products?brandId=550e8400-e29b-41d4-a716-446655440000&page=0&size=20
X-Tenant-ID: f81d4fae-7dec-11d0-a765-00a0c91e6bf6

Resposta:
- Produtos apenas desta brand
```

### Teste 4: Filtrar por Categoria
```
GET /api/v1/showcase/products?categoryId=660f9500-f39c-52e4-b827-557766551111&page=0&size=20
X-Tenant-ID: f81d4fae-7dec-11d0-a765-00a0c91e6bf6

Resposta:
- Produtos apenas desta categoria
```

### Teste 5: Múltiplos Filtros
```
GET /api/v1/showcase/products?search=samsung&brandId=550e8400-e29b-41d4-a716-446655440000&categoryId=660f9500-f39c-52e4-b827-557766551111&page=0&size=15
X-Tenant-ID: f81d4fae-7dec-11d0-a765-00a0c91e6bf6

Resposta:
- Produtos que atendem TODOS os critérios
```

### Teste 6: Detalhes do Produto
```
GET /api/v1/showcase/products/123e4567-e89b-12d3-a456-426614174000

Resposta:
- Produto completo com todas as variantes
- Preços de cada variante
- Especificações (dimensões, peso)
```

### Teste 7: Tratamento de Erro - Paginação Inválida
```
GET /api/v1/showcase/products?page=0&size=150
X-Tenant-ID: f81d4fae-7dec-11d0-a765-00a0c91e6bf6

Resposta: 400 Bad Request
(size > 100 não permitido)
```

### Teste 8: Tratamento de Erro - TenantId Inválido
```
GET /api/v1/showcase/products?page=0&size=20
X-Tenant-ID: invalid-uuid

Resposta: 400 Bad Request
(UUID inválido)
```

---

## 💾 Estrutura da Resposta JSON

### Listagem com Paginação

```json
{
  "content": [
    {
      "id": "uuid",
      "name": "Produto 1",
      "description": "Descrição detalhada",
      "slug": "produto-1",
      "brand": {
        "id": "uuid-brand",
        "name": "Brand Name"
      },
      "category": {
        "id": "uuid-category",
        "name": "Category Name"
      },
      "tags": ["tag1", "tag2", "tag3"],
      "price": {
        "basePrice": null,
        "lowestVariantPrice": {
          "amount": 1999.99,
          "currency": "BRL"
        },
        "lowestPromotionalPrice": {
          "amount": 1499.99,
          "currency": "BRL"
        }
      },
      "variants": [
        {
          "id": "uuid-variant",
          "productId": "uuid",
          "skuCode": "SKU-001",
          "barcode": "1234567890123",
          "price": {
            "amount": 1999.99,
            "currency": "BRL"
          },
          "promotionalPrice": {
            "amount": 1499.99,
            "currency": "BRL"
          },
          "dimensions": {
            "weight": 2.5,
            "height": 20.0,
            "width": 15.0,
            "depth": 10.0
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

---

## 🔐 Validações Implementadas

| Validação | Onde | Erro |
|-----------|------|------|
| X-Tenant-ID obrigatório | ShowcaseController | 400 Bad Request |
| X-Tenant-ID UUID válido | ShowcaseController | 400 Bad Request |
| page >= 0 | ShowcaseController | 400 Bad Request |
| size > 0 | ShowcaseController | 400 Bad Request |
| size <= 100 | ShowcaseController | 400 Bad Request |
| brandId UUID válido (se fornecido) | ShowcaseController | 400 Bad Request |
| categoryId UUID válido (se fornecido) | ShowcaseController | 400 Bad Request |
| Produto existe | GetShowcaseProductDetailsUseCase | 404 Not Found |

---

## 🚀 Performance

### Índices Recomendados (no banco de dados)

```sql
-- Índice composto para busca rápida com filtros
CREATE INDEX idx_product_tenant_name_desc 
ON product(tenant_id, LOWER(name), LOWER(description));

-- Índice para filtro de brand
CREATE INDEX idx_product_tenant_brand 
ON product(tenant_id, brand_id);

-- Índice para filtro de categoria
CREATE INDEX idx_product_tenant_category 
ON product(tenant_id, category_id);

-- Índice para busca por slug
CREATE INDEX idx_product_slug 
ON product(slug);
```

### Estimativa de Tempo de Resposta

| Operação | Sem Índice | Com Índice |
|----------|-----------|-----------|
| Listar 10 de 1000 | ~500ms | ~50ms |
| Buscar por termo | ~800ms | ~100ms |
| Filtro brand | ~600ms | ~30ms |
| Detalhes produto | ~200ms | ~20ms |

---

**Última atualização:** Fevereiro 2026
**Status:** ✅ Implementado e Testado

