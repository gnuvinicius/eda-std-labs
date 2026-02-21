# MSRegister - Migração para WildFly/JBoss

## Mudanças Realizadas

### 1. Dockerfile
- **Alterado de**: Tomcat 10.1
- **Alterado para**: WildFly 33.0.0.Final com JDK 21
- **Adicionado**: Configuração automática do driver PostgreSQL
- **Adicionado**: Configuração automática do datasource PostgreSQL

### 2. Docker Compose
- **Atualizado**: Exposição da porta 9990 para Admin Console do WildFly
- **Mantido**: Porta 8085 mapeada para 8080 (HTTP)

### 3. Webapp (src/main/webapp/WEB-INF/)
- **web.xml**: Simplificado - removido servlet JAX-WS (WildFly gerencia automaticamente)
- **sun-jaxws.xml**: Removido - não necessário no WildFly
- **jboss-web.xml**: Criado - define context-root como "/"
- **beans.xml**: Mantido - necessário para CDI

### 4. pom.xml
- **Dependências**: Alteradas para scope "provided" (fornecidas pelo WildFly):
  - jakarta.enterprise.cdi-api
  - jakarta.xml.ws-api
  - jakarta.jws-api
  - jakarta.xml.bind-api
  - jakarta.annotation-api
  - jakarta.ejb-api
  - jakarta.persistence-api
  - hibernate-core
  - hibernate-validator
  - jakarta.transaction-api
  - jboss-logging
- **Removido**: 
  - weld-servlet-core
  - jaxws-rt
  - jaxb-impl
  - glassfish expressly
- **Plugin**: Substituído tomcat7-maven-plugin por wildfly-maven-plugin

### 5. persistence.xml
- **Alterado**: transaction-type de "RESOURCE_LOCAL" para "JTA"
- **Alterado**: Uso de datasource JNDI ao invés de propriedades JDBC diretas
- **DataSource**: java:jboss/datasources/PostgresDS

### 6. CustomerService.java
- **Removido**: atributo `endpointInterface` da anotação @WebService (não necessário para implementações diretas)

## Como Usar

### Pré-requisitos

**IMPORTANTE**: O serviço requer que o banco de dados PostgreSQL esteja acessível. Certifique-se de que:

1. O PostgreSQL está rodando
2. O banco `msregister_db` existe
3. As tabelas foram criadas

Caso o banco não esteja disponível, o deploy falhará com erro de conexão.

### Build e Deploy com Docker

```bash
# Parar, remover imagem antiga e recriar
docker compose down msregister && \
docker rmi eda-std-labs-msregister && \
docker compose up -d --build msregister

# Ver logs
docker compose logs -f msregister
```

### Configuração para Linux

No Linux, `host.docker.internal` pode não funcionar por padrão. Você tem duas opções:

**Opção 1: Adicionar ao docker compose**
```yaml
msregister:
  extra_hosts:
    - "host.docker.internal:host-gateway"
```

**Opção 2: Usar o IP da interface docker0**
```bash
# Descobrir o IP
ip addr show docker0 | grep -Po 'inet \K[\d.]+'

# Atualizar no Dockerfile a connection-url para usar o IP descoberto
# Exemplo: jdbc:postgresql://172.17.0.1:5432/msregister_db
```

### Acessar o WSDL

Após o serviço estar rodando, o WSDL estará disponível em:

```
http://localhost:8085/CustomerService?wsdl
```

### Admin Console do WildFly

O Admin Console está disponível em:
```
http://localhost:9990
```

**Nota**: Por padrão, o WildFly não cria usuário admin automaticamente. Para acessar o console, você precisaria criar um usuário executando dentro do container:

```bash
docker exec -it msregister /opt/jboss/wildfly/bin/add-user.sh
```

## Endpoints SOAP Disponíveis

### CustomerService

**Namespace**: `http://service.garage474.dev.br/`

**Operações**:

1. **createCustomer**
   - Parâmetros:
     - customerDto (CreateCustomerDto)
     - tenantId (UUID)
   - Retorno: CustomerDto

2. **getCustomerById**
   - Parâmetros:
     - customerId (UUID)
   - Retorno: CustomerDto

3. **getAllCustomers**
   - Parâmetros:
     - tenantId (UUID)
   - Retorno: List<CustomerDto>

## Estrutura do Banco de Dados

O serviço conecta ao banco PostgreSQL com as seguintes configurações:

- **Host**: host.docker.internal (acessa banco fora do Docker)
- **Porta**: 5432
- **Database**: msregister_db
- **Usuário**: postgres
- **Senha**: 2AkByM4NfHFkeJz

## Troubleshooting

### Erro ao conectar ao banco
Certifique-se de que:
1. O PostgreSQL está rodando
2. O banco `msregister_db` existe
3. As credenciais estão corretas
4. O host.docker.internal está acessível (Linux pode precisar de configuração extra)

### WSDL não está disponível
1. Verifique se o container está rodando: `docker ps`
2. Verifique os logs: `docker compose logs -f msregister`
3. Aguarde alguns segundos após o container iniciar - o WildFly pode levar um tempo para fazer o deploy

### Erro de deploy
Verifique os logs do WildFly:
```bash
docker exec -it msregister tail -f /opt/jboss/wildfly/standalone/log/server.log
```

## Vantagens do WildFly sobre Tomcat

1. **Jakarta EE Completo**: Suporte nativo a JAX-WS, EJB, JTA, etc.
2. **Gerenciamento**: Admin Console para gerenciar deployments e configurações
3. **Performance**: Melhor gerenciamento de recursos e transações
4. **Datasources**: Configuração centralizada de datasources com pool de conexões
5. **Clustering**: Suporte nativo para clustering (se necessário no futuro)


