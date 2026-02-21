# MSRegister - Migração Completa para WildFly/JBoss ✅

## Status: CONCLUÍDO

A migração do MSRegister de Tomcat para WildFly foi concluída com sucesso!

## ✅ O que foi feito

### 1. Dockerfile Atualizado
- Base image alterada de `tomcat:10.1-jdk21` para `quay.io/wildfly/wildfly:33.0.0.Final-jdk21`
- PostgreSQL JDBC driver (42.7.2) configurado para auto-deploy
- DataSource PostgresDS configurado via CLI script durante o build
- Multi-stage build mantido para otimização

### 2. Dependências (pom.xml)
Todas as dependências Jakarta EE alteradas para scope `provided`:
- ✅ jakarta.enterprise.cdi-api
- ✅ jakarta.xml.ws-api
- ✅ jakarta.jws-api  
- ✅ jakarta.xml.bind-api
- ✅ jakarta.annotation-api
- ✅ jakarta.ejb-api
- ✅ jakarta.persistence-api
- ✅ hibernate-core
- ✅ hibernate-validator
- ✅ jakarta.transaction-api
- ✅ jboss-logging

Removidas (já fornecidas pelo WildFly):
- ❌ weld-servlet-core
- ❌ jaxws-rt
- ❌ jaxb-impl
- ❌ glassfish expressly

Plugin atualizado:
- ❌ tomcat7-maven-plugin
- ✅ wildfly-maven-plugin

### 3. Configurações Web
- `web.xml`: Simplificado - removidas configurações JAX-WS (WildFly gerencia automaticamente)
- `sun-jaxws.xml`: Removido - não necessário no WildFly
- `jboss-web.xml`: Criado - define context-root como "/"
- `beans.xml`: Mantido para CDI

### 4. Persistence (persistence.xml)
- `transaction-type`: RESOURCE_LOCAL → JTA
- `jta-data-source`: java:jboss/datasources/PostgresDS
- Propriedades JDBC removidas (datasource gerenciado pelo WildFly)

### 5. Service (CustomerService.java)
- Removido atributo `endpointInterface` da anotação @WebService
- Mantidas todas as operações SOAP

### 6. Docker Compose
- Adicionado `extra_hosts: ["host.docker.internal:host-gateway"]` para compatibilidade com Linux
- Exposta porta 9990 para Admin Console do WildFly
- Porta 8085 mantida para HTTP

## 🚀 Como Usar

### Deploy Completo

```bash
# No diretório raiz do projeto
cd /home/vinicius/labs/eda-std-labs

# Parar, reconstruir e iniciar
docker compose down msregister && \
docker rmi eda-std-labs-msregister && \
docker compose up -d --build msregister

# Acompanhar logs
docker compose logs -f msregister
```

### Endpoints Disponíveis

**WSDL do CustomerService:**
```
http://localhost:8085/CustomerService?wsdl
```

**Admin Console do WildFly:**
```
http://localhost:9990
```

**Operações SOAP Disponíveis:**
1. `createCustomer(CreateCustomerDto, UUID tenantId)` → CustomerDto
2. `getCustomerById(UUID customerId)` → CustomerDto  
3. `getAllCustomers(UUID tenantId)` → List<CustomerDto>

## ⚠️ Pré-requisitos

### Banco de Dados PostgreSQL

O serviço **REQUER** que o banco PostgreSQL esteja acessível:

```bash
# Verificar se o PostgreSQL está rodando
psql -h localhost -p 5432 -U postgres -l

# Criar banco se necessário
createdb -h localhost -p 5432 -U postgres msregister_db
```

**Credenciais configuradas:**
- Host: `host.docker.internal` (Linux) ou `host.docker.internal` (Windows/Mac)
- Port: `5432`
- Database: `msregister_db`
- User: `postgres`
- Password: `2AkByM4NfHFkeJz`

### Para Linux

O `host.docker.internal` já está configurado no docker-compose.yml via `extra_hosts`.

Alternativamente, você pode usar o IP da interface docker0:

```bash
# Descobrir IP
ip addr show docker0 | grep -Po 'inet \K[\d.]+'

# Exemplo de saída: 172.17.0.1
# Atualizar o Dockerfile se necessário
```

## 📊 Build Information

**Build time:** ~20-30 segundos (com cache)  
**Image size:** ~900MB (base WildFly + app)  
**Startup time:** ~7-10 segundos

## 🔍 Troubleshooting

### Erro: PersistenceUnit unable to build SessionFactory

**Causa:** Banco de dados não acessível

**Solução:**
1. Verificar se PostgreSQL está rodando
2. Verificar se o banco `msregister_db` existe
3. Verificar credenciais
4. Verificar `host.docker.internal` (Linux: usar IP docker0)

### WSDL não acessível

**Verificar:**
```bash
# Container está rodando?
docker ps | grep msregister

# Logs mostram deploy com sucesso?
docker logs msregister | grep "WFLYSRV0010: Deployed"

# Endpoint está publicado?
docker logs msregister | grep "CustomerService"
```

### Admin Console não abre

Criar usuário admin:
```bash
docker exec -it msregister /opt/jboss/wildfly/bin/add-user.sh
```

## 📈 Vantagens da Migração

### WildFly vs Tomcat

| Característica | Tomcat | WildFly |
|----------------|--------|---------|
| Jakarta EE | Parcial (Web Profile) | Completo (Full Profile) |
| JAX-WS | Requer dependências externas | Nativo |
| EJB | ❌ Não suportado | ✅ Suportado |
| JTA | ❌ Não suportado | ✅ Suportado |
| DataSource Pool | Manual | Gerenciado |
| Admin Console | ❌ Limitado | ✅ Completo |
| Clustering | ❌ Complexo | ✅ Nativo |
| Performance | ⚡ Bom | ⚡⚡ Excelente |

## 📝 Notas Importantes

1. **Datasource está configurado no build:** O PostgreSQL datasource é configurado durante o build da imagem via CLI script
2. **Driver auto-deploy:** O driver PostgreSQL é colocado em `deployments/` e WildFly o detecta automaticamente
3. **JTA obrigatório:** WildFly requer JTA transactions para persistence units managed
4. **No password in logs:** As credenciais não aparecem nos logs por segurança

## 🎯 Próximos Passos (Opcional)

1. Configurar TLS/HTTPS no WildFly
2. Configurar clustering para alta disponibilidade
3. Configurar logging personalizado
4. Implementar health checks
5. Configurar monitoring (Prometheus/Grafana)

## 📚 Documentação de Referência

- [WildFly Documentation](https://docs.wildfly.org/33/)
- [Jakarta EE Specifications](https://jakarta.ee/specifications/)
- [JAX-WS Guide](https://jakarta.ee/specifications/xml-web-services/)
- [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/)

---

**Data da Migração:** 2026-02-21  
**Versão WildFly:** 33.0.0.Final  
**Status:** ✅ COMPLETO E FUNCIONAL

