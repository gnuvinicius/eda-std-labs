# Deployment do msregister com Docker

## Visão Geral

O serviço `msregister` é uma API SOAP para cadastro de clientes do delivery, rodando em um container Tomcat 10.1 com Java 21.

## Configuração

### Pré-requisitos

1. Docker e Docker Compose instalados
2. Rede Docker criada: `docker network create msdelivery-network`

### Arquitetura do Container

- **Base Image**: Tomcat 10.1 com JDK 21 (Eclipse Temurin)
- **Build**: Multi-stage build com Maven
- **Porta Exposta**: 8085 (host) → 8080 (container)
- **Database**: PostgreSQL (msregister_db)

## Como Subir o Serviço

### 1. Criar a rede Docker (se ainda não existir)

```bash
docker network create msdelivery-network
```

### 2. Subir todos os serviços

```bash
docker compose up -d
```

Ou apenas o msregister e postgres:

```bash
docker compose up -d postgres msregister
```

### 3. Verificar o status

```bash
docker compose ps
docker logs msregister
```

## Banco de Dados

O serviço se conecta ao PostgreSQL com as seguintes configurações:

- **Host**: postgres (dentro da rede Docker)
- **Porta**: 5432
- **Database**: msregister_db
- **Usuário**: postgres
- **Senha**: 2AkByM4NfHFkeJz

A inicialização automática dos bancos de dados é feita pelo script `devops/init-databases.sh`.

## Endpoints

O serviço SOAP estará disponível em:

```
http://localhost:8085/
```

## Logs

Para visualizar os logs do container:

```bash
docker logs -f msregister
```

## Parar o Serviço

```bash
docker compose down
```

Para remover volumes (incluindo dados do PostgreSQL):

```bash
docker compose down -v
```

## Rebuild

Se houver alterações no código, rebuilde a imagem:

```bash
docker compose build msregister
docker compose up -d msregister
```

## Troubleshooting

### Container não inicia

1. Verifique os logs: `docker logs msregister`
2. Verifique se o PostgreSQL está rodando: `docker ps | grep postgres`
3. Teste a conexão com o banco: `docker exec -it postgres psql -U postgres -l`

### Problemas de conexão com o banco

1. Verifique se o banco foi criado: `docker exec -it postgres psql -U postgres -c "\l"`
2. Verifique a rede: `docker network inspect msdelivery-network`

### Rebuild completo

```bash
docker compose down
docker rmi $(docker images -q msregister)
docker compose build --no-cache msregister
docker compose up -d
```

