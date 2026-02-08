# CRUD de ProductVariant - Implementação Completa

## 📋 Resumo das Implementações

### 1. **DTOs (Data Transfer Objects)**

#### `CreateProductVariantRequest.java`
- DTO para requisição de criação/atualização de ProductVariant
- Campos:
  - `skuCode`: Código SKU único
  - `barcode`: Código de barras (opcional)
  - `price`: Valor do preço
  - `priceCurrency`: Moeda do preço
  - `promotionalPrice`: Preço promocional (opcional)
  - `promotionalPriceCurrency`: Moeda do preço promocional
  - `weight`, `height`, `width`, `depth`: Dimensões físicas

#### `ProductVariantResponse.java`
- DTO para resposta do ProductVariant
- Nested records:
  - `MoneyResponse`: Encapsula valor e moeda
  - `DimensionsResponse`: Encapsula dimensões físicas
- Retorna dados completos da variante incluindo produto

---

### 2. **Domain Service**

#### `ProductVariantService.java`
- **Responsabilidade**: Encapsular regras de negócio para ProductVariant
- **Método principal**: `validateProductVariantCreation(...)`
- **Regras de Negócio**:
  1. ✅ SKU Code não pode ser vazio ou nulo
  2. ✅ SKU Code deve ser único no catálogo
  3. ✅ SKU Code deve ter 3-50 caracteres
  4. ✅ SKU Code válido (apenas letras maiúsculas, números e hífen)
  5. ✅ Preço deve ser maior que zero
  6. ✅ Preço promocional não pode ser maior que preço regular
  7. ✅ Dimensões não podem ser negativas

---

### 3. **Use Cases (Application Layer)**

#### `CreateProductVariantUseCase.java`
**Fluxo**:
1. Valida que o produto existe
2. Executa validações via ProductVariantService
3. Cria entidade ProductVariant
4. Cria Value Objects: Money, Dimensions
5. Persiste via ProductRepository
6. Retorna resposta DTO

**Command**: `CreateProductVariantCommand`
```java
record CreateProductVariantCommand(
    UUID productId,
    String skuCode,
    String barcode,
    BigDecimal price,
    String priceCurrency,
    BigDecimal promotionalPrice,
    String promotionalPriceCurrency,
    Double weight,
    Double height,
    Double width,
    Double depth
)
```

---

#### `ListProductVariantsByProductUseCase.java`
**Fluxo**:
1. Valida que o produto existe
2. Busca todas as variantes do produto
3. Converte e retorna lista de respostas

**Query**: `ListProductVariantsQuery`
```java
record ListProductVariantsQuery(UUID productId)
```

---

#### `UpdateProductVariantUseCase.java`
**Fluxo**:
1. Busca variante existente
2. Valida que o produto existe
3. Executa validações via ProductVariantService
4. Atualiza campos da variante
5. Atualiza Value Objects
6. Persiste via ProductRepository
7. Retorna resposta DTO

**Command**: `UpdateProductVariantCommand`
```java
record UpdateProductVariantCommand(
    UUID variantId,
    UUID productId,
    String skuCode,
    String barcode,
    BigDecimal price,
    String priceCurrency,
    BigDecimal promotionalPrice,
    String promotionalPriceCurrency,
    Double weight,
    Double height,
    Double width,
    Double depth
)
```

---

#### `DeleteProductVariantUseCase.java`
**Fluxo**:
1. Valida que a variante existe
2. Deleta via ProductRepository

**Command**: `DeleteProductVariantCommand`
```java
record DeleteProductVariantCommand(UUID variantId)
```

---

### 4. **REST Controller - Endpoints**

#### `ProductController.java`

**ENDPOINTS DE VARIANTES**:

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| POST | `/api/v1/products/{productId}/variants` | Criar variante | 201 Created |
| GET | `/api/v1/products/{productId}/variants` | Listar variantes | 200 OK |
| PUT | `/api/v1/products/{productId}/variants/{variantId}` | Atualizar variante | 200 OK |
| DELETE | `/api/v1/products/{productId}/variants/{variantId}` | Deletar variante | 204 No Content |

---

### 5. **Integração com Repositories**

Todos os Use Cases utilizam a interface `ProductRepository` (Domain Layer):

```java
// Métodos utilizados
ProductVariant saveProductVariant(ProductVariant variant);
Optional<ProductVariant> findProductVariantById(UUID id);
List<ProductVariant> findProductVariantsByProductId(UUID productId);
Optional<ProductVariant> findProductVariantBySkuCode(String skuCode);
void deleteProductVariant(UUID id);
boolean existsProductVariant(UUID id);

// Também usam métodos do Product
Optional<Product> findProductById(UUID id);
boolean existsProduct(UUID id);
```

A implementação JPA (`JpaProductRepository`) já contém toda a lógica de persistência necessária.

---

## 🏗️ Arquitetura - Clean Architecture + DDD

```
┌─────────────────────────────────────────────────────────────┐
│ ADAPTER LAYER (In)                                          │
│ ┌──────────────────────────────────────────────────────┐    │
│ │ ProductController                                    │    │
│ │ - Recebe requisições HTTP                          │    │
│ │ - Delega para UseCases                             │    │
│ └──────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│ APPLICATION LAYER                                           │
│ ┌──────────────────────────────────────────────────────┐    │
│ │ UseCases:                                            │    │
│ │ - CreateProductVariantUseCase                       │    │
│ │ - ListProductVariantsByProductUseCase               │    │
│ │ - UpdateProductVariantUseCase                       │    │
│ │ - DeleteProductVariantUseCase                       │    │
│ └──────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│ DOMAIN LAYER                                                │
│ ┌──────────────────────────────────────────────────────┐    │
│ │ Service: ProductVariantService                      │    │
│ │ - Valida regras de negócio                         │    │
│ │                                                      │    │
│ │ Repository: ProductRepository (Interface)          │    │
│ │ - Define contrato de persistência                 │    │
│ └──────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│ ADAPTER LAYER (Out)                                         │
│ ┌──────────────────────────────────────────────────────┐    │
│ │ JpaProductRepository                                │    │
│ │ - Implementação JPA do ProductRepository           │    │
│ │ - Converte Domain ↔ Persistence                   │    │
│ │ - Utiliza ProductVariantJpaRepository             │    │
│ └──────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

---

## 📝 Exemplos de Uso

### 1. Criar uma Variante
```bash
POST /api/v1/products/{productId}/variants
Content-Type: application/json

{
  "skuCode": "PROD-SKU-001",
  "barcode": "1234567890123",
  "price": 99.99,
  "priceCurrency": "BRL",
  "promotionalPrice": 79.99,
  "promotionalPriceCurrency": "BRL",
  "weight": 0.5,
  "height": 10.0,
  "width": 5.0,
  "depth": 3.0
}

Response: 201 Created
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "productId": "550e8400-e29b-41d4-a716-446655440001",
  "skuCode": "PROD-SKU-001",
  "barcode": "1234567890123",
  "price": {
    "amount": 99.99,
    "currency": "BRL"
  },
  "promotionalPrice": {
    "amount": 79.99,
    "currency": "BRL"
  },
  "dimensions": {
    "weight": 0.5,
    "height": 10.0,
    "width": 5.0,
    "depth": 3.0
  }
}
```

### 2. Listar Variantes de um Produto
```bash
GET /api/v1/products/{productId}/variants

Response: 200 OK
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "productId": "550e8400-e29b-41d4-a716-446655440001",
    "skuCode": "PROD-SKU-001",
    "barcode": "1234567890123",
    "price": { "amount": 99.99, "currency": "BRL" },
    "promotionalPrice": { "amount": 79.99, "currency": "BRL" },
    "dimensions": { "weight": 0.5, "height": 10.0, "width": 5.0, "depth": 3.0 }
  }
]
```

### 3. Atualizar uma Variante
```bash
PUT /api/v1/products/{productId}/variants/{variantId}
Content-Type: application/json

{
  "skuCode": "PROD-SKU-001",
  "barcode": "1234567890123",
  "price": 89.99,
  "priceCurrency": "BRL",
  "promotionalPrice": 69.99,
  "promotionalPriceCurrency": "BRL",
  "weight": 0.6,
  "height": 11.0,
  "width": 5.5,
  "depth": 3.5
}

Response: 200 OK
```

### 4. Deletar uma Variante
```bash
DELETE /api/v1/products/{productId}/variants/{variantId}

Response: 204 No Content
```

---

## ✅ Validações Implementadas

### SKU Code
- ✅ Não pode ser vazio
- ✅ Tamanho entre 3 e 50 caracteres
- ✅ Deve ser único no catálogo
- ✅ Formato válido: `[A-Z0-9]+([-][A-Z0-9]+)*`
  - Exemplo válido: `PROD-SKU-001`
  - Exemplo inválido: `-PROD-SKU-001` (começa com hífen)

### Preço
- ✅ Deve ser maior que zero
- ✅ Preço promocional não pode ser maior que preço regular
- ✅ Ambos devem ter moeda definida

### Dimensões
- ✅ Peso não pode ser negativo
- ✅ Altura não pode ser negativa
- ✅ Largura não pode ser negativa
- ✅ Profundidade não pode ser negativa

### Relacionamentos
- ✅ Produto deve existir antes de criar variante
- ✅ Variante deve existir antes de atualizar/deletar

---

## 🔄 Fluxo Completo de Requisição

```
1. Cliente → HTTP Request ao Controller
   POST /api/v1/products/{productId}/variants
   
2. ProductController
   - Recebe request com CreateProductVariantRequest
   - Monta CreateProductVariantCommand
   - Delega para CreateProductVariantUseCase.execute()
   
3. CreateProductVariantUseCase
   - Valida que product existe
   - Chama ProductVariantService.validateProductVariantCreation()
   - Cria entidade ProductVariant
   - Cria Value Objects (Money, Dimensions)
   - Chama ProductRepository.saveProductVariant()
   
4. ProductRepository (JpaProductRepository)
   - Converte domain entity para persistence entity
   - Persiste no banco via ProductVariantJpaRepository
   - Converte resultado para domain entity
   
5. UseCase
   - Converte ProductVariant para ProductVariantResponse
   - Retorna resposta para Controller
   
6. Controller
   - Retorna ResponseEntity com status HTTP 201 Created
   - Body com ProductVariantResponse (JSON)
   
7. Cliente ← HTTP Response com dados da variante criada
```

---

## 📦 Compilação e Status

✅ **Compilação**: SUCCESS
- Todos os arquivos compilam sem erros
- Warnings apenas sobre linhas em branco em comentários (não relevantes)

---

## 🎯 Próximos Passos Sugeridos

1. **Testes Unitários**: Criar testes para UseCases e Services
2. **Testes de Integração**: Testar endpoints com MockMvc
3. **Error Handling**: Implementar GlobalExceptionHandler para exceções
4. **Logging**: Adicionar logs nos UseCases e Services
5. **Documentação OpenAPI**: Adicionar @OpenAPI annotations para Swagger
6. **Validação**: Adicionar @Validated e @Valid nos DTOs

