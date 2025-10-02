# Roadmap de Estudos em Event-Driven Architecture (EDA) com Quarkus + PostgreSQL

## 1. Fundamentos de Event-Driven Architecture

### - Conceitos Básicos
- **O que é EDA**: diferença de arquiteturas síncronas (REST) x assíncronas (eventos)

### - Conceitos-chave
- Evento, Comando e Mensagem
- Produtor x Consumidor
- Event Bus, Event Stream, Event Store

### - Padrões
- **Event Notification**
- **Event-Carried State Transfer**
- **Event Sourcing** (quando usar ou não)
- **CQRS** (Command Query Responsibility Segregation)

### - Atividade no repo:
> Criar documentação (`docs/fundamentos.md`) com exemplos conceituais de fluxos de eventos (sequência de um pedido → pagamento → entrega).

---

## 2. Java + Quarkus (base para EDA)

### - Quarkus Fundamentals
- Configuração de projeto com Maven
- RESTEasy Reactive (endpoints REST)
- Injeção de dependência (CDI)

### - Extensões relevantes
- `quarkus-kafka-client` (ou smallrye-reactive-messaging-kafka)
- `quarkus-hibernate-orm-panache` (para persistência)
- `quarkus-postgresql-client`
- `quarkus-flyway` (migração de schema)

### - Programação reativa
- Quarkus com Mutiny (opcional, mas recomendado)

### - Atividade no repo:
> Criar um microserviço simples (`order-service`) que persiste pedidos no PostgreSQL via Quarkus ORM.

---

## 3. Integração de Mensageria

### - Escolher o broker
- **Kafka**: para streams de eventos e alta performance
- **RabbitMQ**: para filas simples e roteamento

### - Conceitos do Kafka
- Tópicos, Partições, Offsets
- Produtores e Consumidores
- Consumer Groups

### - No Quarkus
- Produzir eventos quando algo muda no banco
- Consumir eventos e reagir (ex: enviar email ou atualizar outro microserviço)

### - Atividade no repo:
> - Configurar docker-compose com Kafka + PostgreSQL
> - Criar um produtor (Order Service) e um consumidor (Billing Service)

---

## 4. PostgreSQL na EDA

### - Outbox Pattern
- Usar uma tabela "outbox" para registrar eventos no mesmo commit da transação
- Um job/processo lê a tabela e envia os eventos para Kafka (garante consistência)

### - Debezium (CDC – Change Data Capture)
- Captura mudanças no banco (via WAL log) e envia para Kafka
- Elimina necessidade de programar outbox manual

### - Quando usar Postgres + Kafka juntos
- **Postgres** mantém a persistência dos dados de negócio
- **Kafka** mantém o fluxo dos eventos entre serviços

### - Atividade no repo:
> - Implementar Outbox Pattern com Quarkus + Postgres
> - Depois evoluir para usar Debezium no docker-compose

---

## 5. Arquitetura de Serviços

### - Microserviços baseados em eventos
- **order-service**: cria pedido → gera evento
- **billing-service**: consome evento de pedido → processa pagamento → gera evento
- **shipping-service**: consome evento de pagamento confirmado → organiza entrega

### - Padrões importantes
- **Saga Pattern** (coordenação de eventos para processos longos)
- **Choreografia x Orquestração**

### - Observabilidade
- Logs centralizados
- Tracing (ex: OpenTelemetry no Quarkus)
- Métricas (Prometheus + Grafana)

### - Atividade no repo:
> Criar saga de "Compra" → do pedido até a entrega, coordenado apenas via eventos Kafka.

---

## 6. Resiliência e Escalabilidade

### - Principais conceitos
- **Idempotência**: garantir que consumidores não processem o mesmo evento várias vezes
- **Retry e Dead Letter Queue (DLQ)**
- **Partitioning e paralelismo no Kafka**
- **Backpressure e controle de fluxo**

### - Atividade no repo:
> Implementar idempotência no billing-service e criar DLQ para eventos inválidos.

---

## 7. Infraestrutura e Deploy

### - Containers e Orquestração
- **Docker / Docker Compose** (fase local)
- **Kubernetes** (produção)

### - Infra-as-Code
- YAML para serviços
- Helm charts (opcional)

### - Banco + Broker
- **Postgres** como serviço Stateful
- **Kafka** com Strimzi Operator

### - Atividade no repo:
> Criar manifests Kubernetes para order-service, billing-service, shipping-service + Kafka + Postgres.

---

## 8. Estudos Avançados (para masterizar)

### - Tópicos avançados
- **Event Sourcing** com Postgres ou Event Store
- **CQRS** com Projeções (ex: consultas rápidas em tabelas denormalizadas)
- **Domain-Driven Design (DDD)** aplicado a eventos
- **Streaming Analytics** com Kafka Streams ou Flink

---

## - Estrutura sugerida do repositório-laboratório

```
event-driven-lab/
 ├── docs/                  # Documentação e conceitos
 ├── docker-compose.yml     # Kafka + Postgres + Debezium
 ├── k8s/                   # Manifests Kubernetes
 ├── order-service/         # Microserviço Quarkus
 ├── billing-service/       # Microserviço Quarkus
 ├── shipping-service/      # Microserviço Quarkus
 └── outbox-service/        # Serviço de integração com Kafka (opcional)
```