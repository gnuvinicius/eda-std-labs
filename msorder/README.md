# MS Pedido - Microserviço de Gerenciamento de Pedidos

Microserviço responsável por gerenciar carrinhos de compras, pedidos e processamento assíncrono de pedidos através de fila RabbitMQ.

## 📋 Funcionalidades

### Carrinho de Compras
- ✅ Criar carrinho
- ✅ Listar carrinhos (do cliente ou todos)
- ✅ Adicionar itens ao carrinho
- ✅ Remover itens do carrinho
- ✅ Atualizar quantidade de itens
- ✅ Limpar carrinho
- ✅ Deletar carrinho

### Pedidos
- ✅ Criar pedido a partir do carrinho
- ✅ Listar pedidos (do cliente ou todos)
- ✅ Buscar pedido por ID
- ✅ Filtrar pedidos por status
- ✅ Confirmar pedido
- ✅ Cancelar pedido
- ✅ Finalizar pedido (envia para processamento assíncrono)

### Processamento Assíncrono
- ✅ Fila RabbitMQ para processar pedidos
- ✅ Integração com sistema de entrega e pagamento

## 🏗️ Arquitetura

### Estrutura de Pastas

```
src/
├── main/
│   ├── java/br/dev/garage474/msorder/
│   │   ├── adapters/
│   │   │   └── in/web/
│   │   │       ├── controller/      # Controllers REST
│   │   │       └── exception/       # Tratamento de exceções
│   │   ├── config/                  # Configurações (RabbitMQ, etc)
│   │   ├── dto/                     # Data Transfer Objects
│   │   ├── models/                  # Entidades de domínio
│   │   ├── repositories/            # Repositórios de dados
│   │   ├── services/                # Interfaces de serviços
│   │   ├── services/impl/           # Implementações de serviços
│   │   └── tenancy/                 # Contexto de multi-tenancy
│   └── resources/
│       ├── db/migration/            # Scripts de migração Flyway
│       ├── application.properties    # Configurações
│       └── application-dev.properties # Configurações de dev
└── test/
    └── java/br/dev/garage474/msorder/
        └── services/impl/           # Testes unitários
```

## 🗄️ Modelo de Dados

### Tabelas Principais

#### cart
```sql
- id (UUID) - PK
- tenant_id (UUID) - Multi-tenancy
- customer_id (UUID) - Cliente
- status (VARCHAR) - ACTIVE, ABANDONED, CONVERTED
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)
```

#### cart_item
```sql
- id (UUID) - PK
- tenant_id (UUID)
- cart_id (UUID) - FK
- product_id (UUID) - Referência mscatalog
- product_variant_id (UUID) - Referência mscatalog
- quantity (INTEGER)
- unit_price_amount (DECIMAL)
- unit_price_currency (VARCHAR)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)
```

#### orders
```sql
- id (UUID) - PK
- tenant_id (UUID)
- cart_id (UUID)
- customer_id (UUID)
- status (VARCHAR) - PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED
- total_amount (DECIMAL)
- total_currency (VARCHAR)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)
```

#### order_item
```sql
- id (UUID) - PK
- tenant_id (UUID)
- order_id (UUID) - FK
- product_id (UUID) - Referência mscatalog
- product_variant_id (UUID) - Referência mscatalog
- quantity (INTEGER)
- unit_price_amount (DECIMAL)
- unit_price_currency (VARCHAR)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)
```

## 🔌 API REST

### Base URL
```
/api/v1
```

### Headers Obrigatórios
```http
tenantId: f81d4fae-7dec-11d0-a765-00a0c91e6bf6
Content-Type: application/json
```

### Endpoints de Carrinho

#### Criar Carrinho
```http
POST /api/v1/carts
Content-Type: application/json
tenantId: {UUID}

{
  "customer_id": "{UUID}"
}

Response: 201 Created
{
  "id": "{UUID}",
  "customer_id": "{UUID}",
  "status": "ACTIVE",
  "items": [],
  "total_items": 0,
  "created_at": "2026-02-13T...",
  "updated_at": "2026-02-13T..."
}
```

#### Listar Carrinhos
```http
GET /api/v1/carts?page=0&size=10
GET /api/v1/carts/customer/{customerId}?page=0&size=10

Response: 200 OK
{
  "content": [...],
  "totalElements": 5,
  "totalPages": 1,
  "number": 0,
  "size": 10
}
```

#### Buscar Carrinho por ID
```http
GET /api/v1/carts/{cartId}

Response: 200 OK
```

#### Adicionar Item ao Carrinho
```http
POST /api/v1/carts/{cartId}/items
Content-Type: application/json

{
  "product_id": "{UUID}",
  "product_variant_id": "{UUID}",
  "quantity": 2,
  "unit_price_amount": 100.00,
  "unit_price_currency": "BRL"
}

Response: 201 Created
```

#### Remover Item do Carrinho
```http
DELETE /api/v1/carts/{cartId}/items/{itemId}

Response: 200 OK
```

#### Atualizar Quantidade de Item
```http
PATCH /api/v1/carts/{cartId}/items/{itemId}?quantity=5

Response: 200 OK
```

#### Limpar Carrinho
```http
DELETE /api/v1/carts/{cartId}/items

Response: 200 OK
```

#### Deletar Carrinho
```http
DELETE /api/v1/carts/{cartId}

Response: 204 No Content
```

### Endpoints de Pedidos

#### Criar Pedido
```http
POST /api/v1/orders
Content-Type: application/json

{
  "cart_id": "{UUID}",
  "customer_id": "{UUID}"
}

Response: 201 Created
{
  "id": "{UUID}",
  "cart_id": "{UUID}",
  "customer_id": "{UUID}",
  "status": "PENDING",
  "total_amount": 200.00,
  "total_currency": "BRL",
  "items": [...],
  "total_items": 2,
  "created_at": "2026-02-13T...",
  "updated_at": "2026-02-13T..."
}
```

#### Listar Pedidos
```http
GET /api/v1/orders?page=0&size=10
GET /api/v1/orders/customer/{customerId}?page=0&size=10
GET /api/v1/orders/status/{status}?page=0&size=10

Response: 200 OK
```

#### Buscar Pedido por ID
```http
GET /api/v1/orders/{orderId}

Response: 200 OK
```

#### Confirmar Pedido
```http
PUT /api/v1/orders/{orderId}/confirm

Response: 200 OK
{
  "status": "CONFIRMED",
  ...
}
```

#### Cancelar Pedido
```http
PUT /api/v1/orders/{orderId}/cancel

Response: 200 OK
{
  "status": "CANCELLED",
  ...
}
```

#### Finalizar Pedido (Enviar para Fila)
```http
PUT /api/v1/orders/{orderId}/finalize

Response: 200 OK
{
  "status": "PROCESSING",
  ...
}
```

## 🐰 Fila RabbitMQ

### Configuração
- **Exchange**: `order.exchange` (Direct)
- **Queue**: `order.queue`
- **Routing Key**: `order.created`

### Mensagem de Pedido

```json
{
  "orderId": "550e8400-e29b-41d4-a716-446655440000",
  "customerId": "550e8400-e29b-41d4-a716-446655440001",
  "tenantId": "550e8400-e29b-41d4-a716-446655440002",
  "status": "PROCESSING",
  "totalAmount": 200.00,
  "totalCurrency": "BRL",
  "items": [
    {
      "productId": "550e8400-e29b-41d4-a716-446655440003",
      "productVariantId": "550e8400-e29b-41d4-a716-446655440004",
      "quantity": 2,
      "unitPrice": 100.00
    }
  ]
}
```

## 🔐 Multi-Tenancy

Todo request deve incluir o header `tenantId` com um UUID válido. Este contexto é:
- Validado no filtro `TenantIdFilter`
- Armazenado em `ThreadLocal` via `TenantContext`
- Automaticamente adicionado a todos os registros de banco de dados
- Garantindo isolamento total de dados entre tenants

## 🛠️ Technologias

- **Framework**: Spring Boot 3.5.10
- **JPA/Hibernate**: Persistência de dados
- **PostgreSQL**: Banco de dados
- **RabbitMQ**: Fila de mensagens
- **Flyway**: Migrations de banco de dados
- **Lombok**: Redução de boilerplate
- **JUnit 5 + Mockito**: Testes unitários
- **SLF4J + Logback**: Logging

## 📝 Logging

A aplicação usa SLF4J com Logback. Todo método de serviço possui:
- Log de entrada (INFO)
- Log de sucesso (INFO)
- Log de erro com stack trace (ERROR)

```java
try {
    log.info("Iniciando operação...");
    // operação
    log.info("Operação concluída com sucesso");
} catch (Exception e) {
    log.error("Erro ao realizar operação: {}", e.getMessage(), e);
    throw e;
}
```

## 🧪 Testes

### Executar testes
```bash
mvn test
```

### Testes inclusos
- `CartServiceImplTest`: Testes do serviço de carrinho
- `OrderServiceImplTest`: Testes do serviço de pedidos

## 📦 Configuração de Propriedades

### application.properties
```properties
# PostgreSQL
spring.datasource.url=jdbc:postgresql://postgres:5432/msorder_db
spring.datasource.username=postgres
spring.datasource.password=2AkByM4NfHFkeJz

# RabbitMQ
spring.rabbitmq.host=rabbitmq
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
```

## 🚀 Execução

### Com Docker Compose
```bash
docker-compose up -d
```

### Build local
```bash
mvn clean package
```

### Executar
```bash
java -jar target/msorder-0.0.1-SNAPSHOT.jar
```

## 📚 Referências

- [Documentação Spring Boot](https://spring.io/projects/spring-boot)
- [Documentação RabbitMQ](https://www.rabbitmq.com/documentation.html)
- [Documentação Flyway](https://flywaydb.org/documentation/)
- [DDD e Domain-Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html)

