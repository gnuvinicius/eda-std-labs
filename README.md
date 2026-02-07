# EDA Study Labs - Delivery SaaS

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)
![Status](https://img.shields.io/badge/status-development-orange)

## 📖 Sobre o Projeto

**EDA Study Labs** é um projeto de laboratório focado no estudo e implementação de uma **Arquitetura Orientada a Eventos (EDA)** e **Microsserviços**. O sistema simula um **SaaS de Delivery de Produtos** completo, projetado para ser escalável, resiliente e desacoplado.

O objetivo principal é explorar cenários complexos de engenharia de software, como consistência eventual, transações distribuídas, tolerância a falhas e observabilidade em sistemas distribuídos.

## 🏗️ Arquitetura e Design

O projeto segue os princípios de **Clean Architecture** e **Domain-Driven Design (DDD)**, utilizando padrões de integração modernos.

### Padrões e Práticas
*   **Event-Driven Architecture (EDA)**
*   **Saga Pattern** (Coreografia/Orquestração) para transações distribuídas.
*   **CQRS** (Command Query Responsibility Segregation).
*   **Transactional Outbox Pattern** para garantia de entrega de eventos.
*   **API Gateway** & **Service Discovery**.
*   **Circuit Breaker** & **Retry Mechanisms**.

## 🚀 Stack Tecnológico

### Core & Frameworks
*   **Java 17+ / Kotlin**
*   **Spring Boot 3.x**
*   **Spring Cloud** (Gateway, OpenFeign, Config)

### Persistência & Cache
*   **PostgreSQL** (Banco de dados relacional por serviço)
*   **MongoDB** (Read Models / Logs)
*   **Redis** (Cache distribuído e Rate Limiting)

### Mensageria & Streaming
*   **Apache Kafka** (Event Streaming de alta vazão)
*   **RabbitMQ** (Filas de processamento e DLQs)

### Infraestrutura & DevOps (Roadmap)
*   **Docker** & **Docker Compose**
*   **Kubernetes (K8s)**
*   **Helm Charts**
*   **CI/CD** (GitHub Actions)

### Observabilidade (Roadmap)
*   **Prometheus** & **Grafana** (Métricas)
*   **ELK Stack** (Elasticsearch, Logstash, Kibana) ou **Loki**
*   **OpenTelemetry** / **Zipkin** / **Jaeger** (Distributed Tracing)

### Segurança
*   **Keycloak** / **OAuth2** / **OIDC**

## 📦 Serviços do Domínio (Microservices)

1.  **Order Service**: Gerenciamento do ciclo de vida do pedido.
2.  **Catalog/Inventory Service**: Gestão de produtos e controle de estoque em tempo real.
3.  **Payment Service**: Processamento de pagamentos e integração com gateways.
4.  **Delivery/Logistics Service**: Roteirização e gestão de entregadores.
5.  **Notification Service**: Envio de emails, SMS e Push Notifications.

## 📊 Status do MVP

O desenvolvimento do MVP (Minimum Viable Product) está em andamento.

**Progresso Geral:**
![Progress](https://geps.dev/progress/15) **15%**

### Checklist de Funcionalidades MVP
- [ ] Definição da Arquitetura Base
- [ ] Configuração do Discovery & Gateway
- [ ] Serviço de Catálogo (CRUD)
- [ ] Serviço de Pedidos (Criação básica)
- [ ] Integração Kafka/RabbitMQ
- [ ] Fluxo de Pagamento (Mock)
- [ ] Fluxo de Baixa de Estoque
- [ ] Observabilidade Básica

## 🛠️ Como Executar

*(Instruções futuras para rodar com Docker Compose)*

```bash
docker-compose up -d
```

## 🤝 Contribuição

Este é um projeto de estudo open-source. Sinta-se à vontade para abrir Issues ou Pull Requests para discutir padrões e implementações.
