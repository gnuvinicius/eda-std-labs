# AGENTS.md

## Guia de boas práticas para desenvolvimento de microserviços em Java com Spring Boot

- Estrutura de pastas alinhada com three-tier architecture e DDD
- Padrões de nomenclatura para classes, métodos e variáveis, utilize o idioma inglês para nomes de classes, métodos e variáveis, seguindo as convenções de nomenclatura do Java (PascalCase para classes, camelCase para métodos e variáveis)
- Convenções para endpoints RESTful, incluindo métodos HTTP e status codes
- Validação de dados de entrada usando Bean Validation (javax.validation)
- package models para as entidades de domínio, package repositories para os repositórios, package services para a lógica de negócio e package controllers para as APIs REST
- DTOs para transferência de dados entre camadas, evitando exposição direta das entidades de domínio
- Testes unitários para serviços e controladores, utilizando JUnit e Mockito
- Configuração de logging consistente usando SLF4J e Logback
- Documentação clara e concisa, incluindo JavaDocs para classes e métodos públicos, e README.md para o projeto

## Domain Drive Design (DDD) e multi-tenancy

- Padrões DDD com separação de domínio, aplicação e infraestrutura
- Contexto de multi-tenancy integrado desde o início do design, garantindo que cada tenant tenha seus dados isolados e seguros
- Multi-tenancy com header tenantId UUID obrigatório
- Configurar o contexto de tenantId para ser acessível em toda a aplicação, garantindo que todas as operações sejam realizadas no contexto correto do tenant
- Garantir que os repositórios e services sejam projetados para operar com o tenantId, evitando vazamento de dados entre tenants

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

| Tipo | Convenção | Exemplo                                  |
|------|-----------|------------------------------------------|
| Classe | PascalCase | `ProdutoController`, `ProdutoService`    |
| Variável | camelCase | `produtoId`, `nomeProduto`               |
| Constante | UPPER_SNAKE_CASE | `MAX_PRODUCT_NAME_LENGTH`                |
| Package | lowercase.dot | `br.com.seuprojeto.domain.entities`      |
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