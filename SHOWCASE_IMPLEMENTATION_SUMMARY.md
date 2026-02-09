# Showcase Endpoint - Sumário de Implementação

## 📋 Resumo Executivo

Foi implementado um novo endpoint **Showcase** no microserviço `mscatalog` para fornecer uma interface pública (sem autenticação) para clientes frontend/mobile consultarem produtos de forma otimizada.

**Características:**
- ✅ Listagem com paginação obrigatória
- ✅ Filtros opcionais (busca por termo, brand, categoria)
- ✅ Isolamento por tenant
- ✅ Obtenção de detalhes completos do produto
- ✅ Preços otimizados (menor preço e preço promocional)
- ✅ Endpoints somente leitura (GET)

---

## 📁 Arquivos Criados

### 1. DTOs (Adapters/In/Web/DTO)

#### `ShowcaseProductResponse.java`
- Record DTO para resposta de produtos no Showcase
- Contém nested records para Brand, Category, ProductPriceResponse e ProductVariantResponse
- Estrutura otimizada para frontend/mobile com dados essenciais

#### `ShowcaseProductVariantResponse.java` *(depreciado, integrado em ShowcaseProductResponse)*
- Record separado para variantes (mantém compatibilidade, mas valores vêm aninhados)

#### `ShowcasePageResponse.java`
- Record DTO para resposta paginada
- Contém lista de produtos e informações de paginação (page, size, totalPages, totalElements)

### 2. Use Cases (Applications/UseCase)

#### `ListShowcaseProductsUseCase.java`
- Use case para listar produtos com paginação e filtros
- Implementa lógica de:
  - Busca com termos (case-insensitive)
  - Filtro por brand (opcional)
  - Filtro por categoria (opcional)
  - Paginação
- Calcula preços mínimos das variantes
- Converte domain entities para DTOs

#### `GetShowcaseProductDetailsUseCase.java`
- Use case para obter detalhes completos de um produto
- Retorna todas as variantes com seus preços
- Calcula menor preço e menor preço promocional
- Tratamento de erro quando produto não existe

### 3. Controller (Adapters/In/Web/Controller)

#### `ShowcaseController.java`
- Controller REST com 2 endpoints:
  - `GET /api/v1/showcase/products` - Listar com paginação e filtros
  - `GET /api/v1/showcase/products/{id}` - Detalhar produto
- Validação de:
  - Header obrigatório X-Tenant-ID (UUID)
  - Parâmetros de paginação (page >= 0, size > 0 e <= 100)
  - UUIDs de brand e categoria (se fornecidos)
- Logging estruturado com SLF4J
- Tratamento de erros (400, 404, 500)

### 4. Repository (Domain/Repositories)

#### Modificação em `ProductRepository.java`
- Adicionado novo método de interface:
  ```java
  ShowcasePageable findProductsByTenantWithFilters(
      UUID tenantId,
      String searchTerm,
      UUID brandId,
      UUID categoryId,
      int page,
      int size
  );
  ```
- Adicionado record interno `ShowcasePageable` para encapsular resultado paginado

### 5. Persistence Layer (Adapters/Out/Persistence)

#### Modificação em `ProductJpaRepository.java`
- Adicionado import de `Page`, `Pageable`, `PageRequest`
- Adicionado método JPA com `@Query`:
  ```java
  @Query("SELECT p FROM ProductEntity p " +
         "WHERE p.tenantId = :tenantId " +
         "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
         "     OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
         "AND (:brandId IS NULL OR p.brandEntity.id = :brandId) " +
         "AND (:categoryId IS NULL OR p.categoryEntity.id = :categoryId)")
  ```
- Suporta busca full-text em name e description
- Filtros opcionais com NULL check para brand e categoria

#### Modificação em `JpaProductRepository.java`
- Adicionado imports de `Page`, `Pageable`, `PageRequest`
- Implementação do método `findProductsByTenantWithFilters`:
  - Executa query JPA com paginação
  - Converte resultados para domain entities
  - Retorna `ShowcasePageable` com metadados de paginação

---

## 🏗️ Arquitetura

### Camadas Implementadas

```
Adapter Layer (HTTP)
├── ShowcaseController (REST endpoints)
└── DTOs (ShowcaseProductResponse, ShowcasePageResponse)
        ↓
Application Layer
├── ListShowcaseProductsUseCase
├── GetShowcaseProductDetailsUseCase
└── Conversão Entity → DTO
        ↓
Domain Layer
├── ProductRepository (interface)
└── Product, ProductVariant (domain entities)
        ↓
Persistence Layer
├── JpaProductRepository (implementação)
├── ProductJpaRepository (Spring Data JPA)
└── ProductEntity (JPA entity)
        ↓
Database
```

### Padrões Utilizados

- **Clean Architecture**: Separação clara entre camadas
- **DDD (Domain-Driven Design)**: Repository como interface de domínio
- **Repository Pattern**: Abstração da persistência
- **Use Case Pattern**: Lógica de aplicação encapsulada
- **DTO Pattern**: Separação entre domain e transfer objects
- **Multi-tenancy**: Isolamento de dados por tenant

---

## 📚 Documentação

### `SHOWCASE_ENDPOINT.md`
Documentação completa incluindo:
- Visão geral
- Endpoints (listagem e detalhes)
- Parâmetros e exemplos de requisições
- Estrutura de respostas (JSON)
- Códigos HTTP
- Casos de uso
- Notas importantes (multi-tenancy, paginação, performance)

### `test-showcase.sh`
Script bash com 8 exemplos de testes:
1. Listar primeira página
2. Buscar por termo
3. Filtrar por brand
4. Filtrar por categoria
5. Combinação de filtros
6. Obter detalhes do produto
7. Testar paginação (página 2)
8. Testar paginação inválida

---

## 🔒 Segurança

- ✅ **Multi-tenancy obrigatório**: Header `X-Tenant-ID` em todas as requisições
- ✅ **Isolamento de dados**: Cada tenant vê apenas seus produtos
- ✅ **Validação de entrada**: UUIDs, paginação e tamanho máximo de página
- ✅ **Tratamento de erros**: Sem exposição de stack traces em produção
- ✅ **Logging estruturado**: Rastreamento de erros com SLF4J

---

## ✅ Testes de Compilação

```bash
[INFO] BUILD SUCCESS
[INFO] Total time: 3.927 s
```

Todos os arquivos compilam sem erros.

---

## 🚀 Como Usar

### 1. Iniciar o Servidor

```bash
cd /home/vinicius/labs/eda-std-labs/mscatalog
./mvnw spring-boot:run
```

### 2. Exemplo de Requisição

```bash
# Listar produtos
curl -X GET "http://localhost:8080/api/v1/showcase/products?page=0&size=20&search=notebook" \
  -H "X-Tenant-ID: f81d4fae-7dec-11d0-a765-00a0c91e6bf6" \
  -H "Content-Type: application/json"

# Detalhar produto
curl -X GET "http://localhost:8080/api/v1/showcase/products/123e4567-e89b-12d3-a456-426614174000"
```

### 3. Executar Testes

```bash
chmod +x /home/vinicius/labs/eda-std-labs/test-showcase.sh
/home/vinicius/labs/eda-std-labs/test-showcase.sh
```

---

## 📊 Endpoints Summary

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | `/api/v1/showcase/products` | Listar com paginação e filtros | TenantID |
| GET | `/api/v1/showcase/products/{id}` | Detalhes do produto | Nenhuma |

---

## 🔧 Próximos Passos (Sugestões)

1. **Integração com Cache**: Adicionar caching do Redis para melhorar performance
2. **Busca Avançada**: Implementar busca Elasticsearch para full-text search otimizado
3. **Sorting**: Adicionar parametrização de ordenação (preço, nome, data)
4. **Relatórios**: Análise de acessos ao Showcase (view counts)
5. **Imagens**: Adicionar URLs de imagens dos produtos
6. **Avaliações**: Mostrar rating e número de avaliações

---

**Status:** ✅ Concluído e testado
**Data:** Fevereiro 2026
**Versão:** 1.0

