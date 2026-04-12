# GitHub Actions Workflows

Este documento descreve todos os workflows CI/CD configurados para o projeto.

## 📋 Organização

Os workflows estão divididos em três categorias:

- **Microserviços (Produção - Branch `main`)**: Disparados automaticamente ao fazer push na branch `main`
- **Microserviços (Homologação - Branch `develop`)**: Disparados automaticamente ao fazer push na branch `develop`
- **Infraestrutura**: Disparados ao fazer push em diretórios específicos da infraestrutura

---

## 🚀 Microserviços - Produção

### `ci-ms-catalog.yml`
**Branch**: `main`  
**Gatilho**: Push com alterações em `ms-catalog/**`  
**Jobs**:
1. `maven-build`: Compila o projeto Maven (JDK 21) e gera o JAR
2. `docker-build`: Constrói a imagem Docker do ms-catalog

**Saída**: Imagem Docker `mscatalog:latest`

---

### `ci-ms-delivery.yml`
**Branch**: `main`  
**Gatilho**: Push com alterações em `ms-delivery/**`  
**Jobs**:
1. `maven-build`: Compila o projeto Maven (JDK 21) e gera o JAR
2. `docker-build`: Constrói a imagem Docker do ms-delivery

**Saída**: Imagem Docker `msdelivery:latest`

---

### `ci-ms-order.yml`
**Branch**: N/A  
**Gatilho**: `workflow_dispatch` (manual - via interface do GitHub)  
**Jobs**:
1. `maven-build`: Compila o projeto Maven (JDK 21) e gera o WAR
2. `docker-build`: Constrói a imagem Docker do ws-order e faz push para AWS ECR

**Saída**: Imagem Docker no AWS ECR

---

## 🧪 Microserviços - Homologação

### `ci-ms-catalog-hmo.yml`
**Branch**: `develop`  
**Gatilho**: Push com alterações em `ms-catalog/**`  
**Jobs**:
1. `maven-build`: Compila o projeto Maven (JDK 21) com perfil de homologação
2. `docker-build`: Constrói a imagem Docker localmente (registry local `127.0.0.1:5000`)
3. `deploy`: Reinicia o container em ambiente DSV

**Saída**: Container rodando em ambiente de desenvolvimento (DSV)

---

### `ci-ms-delivery-hmo.yml`
**Branch**: `develop`  
**Gatilho**: Push com alterações em `ms-delivery/**`  
**Jobs**:
1. `maven-build`: Compila o projeto Maven (JDK 21)
2. `docker-build`: Constrói a imagem Docker do ms-delivery
3. `deploy`: Reinicia o container em ambiente DSV

**Saída**: Container rodando em ambiente de desenvolvimento (DSV)

---

### `ci-ms-recommendation-hmo.yml`
**Branch**: `develop`  
**Gatilho**: Push com alterações em `ms-recommendation/**`  
**Jobs**:
1. `docker-build`: Constrói a imagem Docker do ms-recommendation (Python)
2. `deploy`: Reinicia o container com controle de concorrência para evitar conflitos

**Saída**: Container rodando em ambiente de desenvolvimento (DSV)

---

## 🏗️ Infraestrutura

### `ci-infra-soa.yml`
**Gatilho**: Push com alterações em `infraestructure/images/wildfly-soa/**`  
**Jobs**:
1. `build-image`: Constrói a imagem Docker do WildFly SOA
2. `docker-push`: Faz push da imagem para AWS ECR

**Saída**: Imagem Docker no AWS ECR (`wildfly-soa:latest`)

---

### `ci-infra-api-gateway.yml`
**Gatilho**: Push com alterações em `infraestructure/images/api-gateway/**`  
**Jobs**:
1. `build-and-run`: Constrói a imagem Docker do API Gateway
2. Deploy automático em ambiente DSV (cria a rede Docker compartilhada se necessária)

**Saída**: Container `api-gateway` rodando na rede `garage-shared-net`

---

### `ci-infra-rabbitmq.yml`
**Gatilho**: Push com alterações em `infraestructure/images/rabbitmq/**`  
**Jobs**:
1. `build-and-run`: Constrói a imagem Docker do RabbitMQ
2. Deploy automático em ambiente DSV (cria a rede Docker compartilhada se necessária)

**Saída**: Container `rabbitmq` rodando na rede `garage-shared-net`

---

### `ci-infra-portainer.yml`
**Gatilho**: Push com alterações em `infraestructure/images/portainer/**`  
**Jobs**:
1. `build-and-run`: Constrói a imagem Docker do Portainer
2. Deploy automático em ambiente DSV (cria a rede Docker compartilhada e volume de dados)

**Saída**: Container `portainer` rodando na rede `garage-shared-net`

---

## 🔑 Variáveis de Ambiente Comuns

| Variável | Valor | Contexto |
|----------|-------|---------|
| `SHARED_NETWORK` | `garage-shared-net` | Rede Docker compartilhada entre containers em DSV |
| `RABBITMQ_HOST` | `rabbitmq` | Hostname do RabbitMQ |
| `API_GATEWAY_CONTAINER_NAME` | `api-gateway` | Nome do container do API Gateway |

---

## 🔐 Secrets Utilizados

- `AWS_ACCESS_KEY_ID`: Credencial para AWS
- `AWS_SECRET_ACCESS_KEY`: Chave secreta para AWS
- `AWS_REGION`: Região AWS (us-east-1)

---

## 💡 Notas Importantes

- **HMO (Homologação)**: Workflows executados em branch `develop` para testes em ambiente DSV
- **Produção**: Workflows em branch `main` para builds finais
- **Registry Local**: Alguns workflows (HMO) usam registry Docker local (`127.0.0.1:5000`)
- **ECR (AWS)**: Workflows de produção fazem push para AWS ECR
- **Concorrência**: O workflow `ms-recommendation-hmo` utiliza concorrência controlada para evitar conflitos
