# AGENTS.md

## Guia de boas práticas para desenvolvimento de microserviços em clean architecture em Java com Spring Boot

- Estrutura de pastas alinhada com Clean Architecture
- adapters: para integrações externas (bancos, APIs, etc.)
- adapters/in/web: para controllers, rotas e validação de requests
- adapters/out/persistence: para repositórios e mapeamento de entidades
- applications: para lógica de negócios e casos de uso
- applications/usecases: para casos de uso específicos

- domain: para entidades, agregados e regras de negócio
- domain/entities: para entidades e agregados
- domain/services: para serviços de domínio e regras de negócio complexas
- domain/repositories: para interfaces de repositórios e contratos de persistência

## Domain Drive Design (DDD) e multi-tenancy

- Padrões DDD com separação de domínio, aplicação e infraestrutura
- Entities e agregados projetados para refletir o modelo de negócio, garantindo encapsulamento e consistência
- Contexto de multi-tenancy integrado desde o início do design, garantindo que cada tenant tenha seus dados isolados e seguros
- Multi-tenancy com header tenantId UUID obrigatório
- Implementar os entities e aggregates considerando o tenantId para garantir isolamento de dados
- Configurar o contexto de tenantId para ser acessível em toda a aplicação, garantindo que todas as operações sejam realizadas no contexto correto do tenant
- Garantir que os repositórios e serviços de domínio sejam projetados para operar com o tenantId, evitando vazamento de dados entre tenants

## Multi-Tenancy

### Regras Críticas

> **CRÍTICO:** O isolamento de dados por tenant é **não-negociável**.  
> Qualquer falha causa vazamento de dados entre clientes.

#### Header Obrigatório

Todo request para o microserviço DEVE incluir:

```http
tenantId: f81d4fae-7dec-11d0-a765-00a0c91e6bf6
Content-Type: application/json
```

**Validação:**
- UUID válido (v4 recomendado)
- Nulo, vazio ou formato inválido → Retornar `400 Bad Request`

---

## Exemplos práticos:

- Controller RESTful com validação de tenant
- Configuração de rotas /api/v1/
- Boas práticas de testes, DTOs e naming
- Checklist para PRs garantindo conformidade com padrões

### 2️⃣ Métodos HTTP

| Operação | Método | Status | Exemplo |
|----------|--------|--------|---------|
| Criar | `POST` | 201 | `POST /api/v1/produtos` |
| Listar | `GET` | 200 | `GET /api/v1/produtos` |
| Buscar um | `GET` | 200 | `GET /api/v1/produtos/{id}` |
| Atualizar | `PUT` | 200 | `PUT /api/v1/produtos/{id}` |
| Atualizar parcial | `PATCH` | 200 | `PATCH /api/v1/produtos/{id}` |
| Deletar | `DELETE` | 204 | `DELETE /api/v1/produtos/{id}` |

## 🎯 Convenções de Código

### Nomenclatura

| Tipo | Convenção | Exemplo |
|------|-----------|---------|
| Classe | PascalCase | `ProdutoController`, `BuscarProdutoUseCase` |
| Variável | camelCase | `produtoId`, `nomeProduto` |
| Constante | UPPER_SNAKE_CASE | `MAX_PRODUCT_NAME_LENGTH` |
| Package | lowercase.dot | `br.com.seuprojeto.domain.entities` |
| Arquivo | PascalCase | `Produto.java`, `ProdutoRepository.java` |

## Loggar erros de forma consistente usando SLF4J:

- todo codigo que possa lançar uma exceção deve ser envolvido em um bloco try-catch
- no catch, logar o erro usando log.error com a mensagem e a stack trace da exceção
- rethrow a exceção para garantir que ela seja propagada corretamente

```java
try {
    // código para cadastrar novo produto
} catch (Exception e) {
    log.error("erro ao cadastrar novo produto: {}", e.getMessage(), e);
    throw e;
}