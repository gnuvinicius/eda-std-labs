# MSRegister - Serviço SOAP com WildFly

## ✅ Migração Concluída

O serviço MSRegister foi migrado com sucesso de **Tomcat 10.1** para **WildFly 33.0.0.Final**.

## 🚀 Quick Start

### 1. Certifique-se que o banco de dados está rodando

```bash
# Verificar PostgreSQL
psql -h localhost -p 5432 -U postgres -l

# Criar banco se necessário
createdb -h localhost -p 5432 -U postgres msregister_db
```

### 2. Deploy do serviço

```bash
# No diretório raiz do projeto
cd /home/vinicius/labs/eda-std-labs

# Parar, reconstruir e iniciar
docker compose down msregister && \
docker rmi eda-std-labs-msregister 2>/dev/null || true && \
docker compose up -d --build msregister

# Acompanhar logs
docker compose logs -f msregister
```

### 3. Acessar o WSDL

Após o serviço iniciar (aguarde ~10 segundos):

```
http://localhost:8085/CustomerService?wsdl
```

## 📋 Principais Mudanças

### Arquivo | Mudança
---|---
`Dockerfile` | Tomcat → WildFly 33.0.0.Final + PostgreSQL driver auto-deploy
`pom.xml` | Dependências Jakarta EE marcadas como `provided`
`persistence.xml` | RESOURCE_LOCAL → JTA + DataSource JNDI
`web.xml` | Simplificado (JAX-WS gerenciado pelo WildFly)
`sun-jaxws.xml` | ❌ Removido (não necessário)
`jboss-web.xml` | ✅ Criado (context-root: "/")
`docker-compose.yml` | Adicionado `extra_hosts` para Linux + porta 9990

## 🔌 Endpoints

### WSDL
```
http://localhost:8085/CustomerService?wsdl
```

### Admin Console WildFly
```
http://localhost:9990
```

Para criar usuário admin:
```bash
docker exec -it msregister /opt/jboss/wildfly/bin/add-user.sh
```

## 🛠️ Operações SOAP Disponíveis

**Namespace:** `http://service.garage474.dev.br/`

| Operação | Parâmetros | Retorno |
|----------|-----------|---------|
| `createCustomer` | CreateCustomerDto, UUID tenantId | CustomerDto |
| `getCustomerById` | UUID customerId | CustomerDto |
| `getAllCustomers` | UUID tenantId | List<CustomerDto> |

## 🗄️ Configuração do Banco

| Propriedade | Valor |
|-------------|-------|
| Host | `host.docker.internal` |
| Porta | `5432` |
| Database | `msregister_db` |
| Usuário | `postgres` |
| Senha | `2AkByM4NfHFkeJz` |
| DataSource JNDI | `java:jboss/datasources/PostgresDS` |

## ⚠️ Importante

### Database é Obrigatório

O serviço **não iniciará completamente** sem conexão com o banco de dados PostgreSQL. Certifique-se de que:

1. ✅ PostgreSQL está rodando na porta 5432
2. ✅ Banco `msregister_db` existe
3. ✅ Credenciais estão corretas
4. ✅ `host.docker.internal` é acessível (Linux: configurado via `extra_hosts`)

### Linux - host.docker.internal

No docker-compose.yml já está configurado:
```yaml
extra_hosts:
  - "host.docker.internal:host-gateway"
```

## 🐛 Troubleshooting

### Erro: PersistenceUnit unable to build SessionFactory

**Sintoma:** Container inicia mas mostra erro de conexão com banco

**Solução:**
```bash
# 1. Verificar se PostgreSQL está rodando
sudo systemctl status postgresql

# 2. Verificar se consegue conectar
psql -h localhost -p 5432 -U postgres -c "SELECT version();"

# 3. Verificar se banco existe
psql -h localhost -p 5432 -U postgres -l | grep msregister_db

# 4. Criar banco se necessário
createdb -h localhost -p 5432 -U postgres msregister_db
```

### WSDL retorna erro 404

**Verificar:**
```bash
# Container está rodando?
docker ps | grep msregister

# Ver logs completos
docker compose logs msregister | less

# Procurar por "CustomerService" nos logs
docker compose logs msregister | grep -i customer
```

### Container reiniciando constantemente

```bash
# Ver motivo da falha
docker compose logs msregister --tail=100

# Reiniciar do zero
docker compose down msregister
docker rmi eda-std-labs-msregister
docker compose up -d --build msregister
```

## 📊 Vantagens do WildFly

| Recurso | Tomcat | WildFly |
|---------|--------|---------|
| Jakarta EE Full Profile | ❌ | ✅ |
| JAX-WS nativo | ❌ | ✅ |
| EJB Support | ❌ | ✅ |
| JTA Transactions | ❌ | ✅ |
| DataSource Management | Manual | ✅ Gerenciado |
| Admin Console | Limitado | ✅ Completo |
| Clustering | Complexo | ✅ Nativo |

## 📚 Documentação Adicional

- [MIGRATION_SUMMARY.md](MIGRATION_SUMMARY.md) - Resumo completo da migração
- [MIGRATION_WILDFLY.md](MIGRATION_WILDFLY.md) - Detalhes técnicos da migração
- [WildFly Documentation](https://docs.wildfly.org/33/)

## 🔄 Comandos Úteis

```bash
# Reconstruir imagem
docker compose build msregister

# Iniciar serviço
docker compose up -d msregister

# Parar serviço
docker compose down msregister

# Ver logs em tempo real
docker compose logs -f msregister

# Entrar no container
docker exec -it msregister bash

# Ver logs do WildFly dentro do container
docker exec -it msregister tail -f /opt/jboss/wildfly/standalone/log/server.log

# Testar WSDL
curl http://localhost:8085/CustomerService?wsdl
```

## 📝 Notas Técnicas

1. **Build Time:** ~20-30s com cache Maven
2. **Image Size:** ~900MB (WildFly base + app)
3. **Startup Time:** ~7-10s
4. **JDK Version:** OpenJDK 21 (Temurin)
5. **WildFly Version:** 33.0.0.Final
6. **PostgreSQL Driver:** 42.7.2

---

**Status:** ✅ Pronto para uso  
**Última atualização:** 2026-02-21  
**Ambiente:** Docker + WildFly + PostgreSQL

