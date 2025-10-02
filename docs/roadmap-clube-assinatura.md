Perfeito 🚀 Então vamos montar um **roadmap prático de estudo** usando o **cenário de Clube de Assinaturas** com **arquitetura orientada a eventos**, focado em **Java (Quarkus) + PostgreSQL + Kafka (ou RabbitMQ)**.

---

# 📌 Roadmap – Clube de Assinaturas com Event-Driven Architecture

### 🔹 1. Infraestrutura base (via Docker Compose)

* PostgreSQL (armazenar usuários, assinaturas, pagamentos, envios).
* Kafka + Zookeeper (stream de eventos) → ou RabbitMQ para comparar.
* Debezium (fazer CDC do Postgres e publicar eventos no Kafka).
* Opcional: Kafdrop / Kafka UI (monitorar tópicos).

---

### 🔹 2. Serviços iniciais (Quarkus)

1. **User Service**

   * Cadastro de clientes (`UserRegistered`).
   * Usa Postgres.
   * Outbox Pattern: grava evento no `outbox_event`.

2. **Subscription Service**

   * Criação, renovação e cancelamento de assinaturas (`SubscriptionCreated`, `SubscriptionCanceled`, `SubscriptionRenewed`).
   * Consome evento `UserRegistered` para ativar assinatura.
   * Outbox Pattern para publicar mudanças.

3. **Billing Service**

   * Processa pagamentos (`PaymentProcessed`, `PaymentFailed`).
   * Pode simular um gateway de pagamento.
   * Consome `SubscriptionCreated` e dispara cobrança.

4. **Shipping Service**

   * Dispara envio de kit (`BoxShipped`, `DeliveryConfirmed`).
   * Consome `PaymentProcessed`.

5. **Notification Service**

   * Consome todos os eventos relevantes.
   * Envia email/sms push (pode ser só logar no console).

6. **Analytics Service**

   * Mantém dashboards de MRR, churn, receita.
   * Consome `PaymentProcessed`, `SubscriptionCanceled`.

---

### 🔹 3. Eventos principais (CloudEvents)

Cada serviço publica eventos com metadados padronizados:

```json
{
  "id": "uuid",
  "source": "subscription-service",
  "type": "SubscriptionCreated",
  "time": "2025-10-01T10:00:00Z",
  "data": {
    "subscriptionId": "123",
    "userId": "456",
    "plan": "premium",
    "startDate": "2025-10-01"
  }
}
```

---

### 🔹 4. Padrões para explorar

* **Outbox Pattern + Debezium (CDC)** → garantir consistência entre Postgres e eventos.
* **CQRS** → dashboards e relatórios.
* **Event Sourcing (opcional)** → histórico de assinaturas.
* **Coreografia de eventos** (serviços reagem a eventos uns dos outros).
* **Comparação Kafka vs RabbitMQ** → streams vs filas.

---

### 🔹 5. Roadmap de aprendizado

1. Subir **infra mínima** no Docker Compose (Postgres + Kafka + Debezium).
2. Criar **User Service** com Quarkus + Panache + REST.
3. Implementar **Outbox Pattern** no User Service.
4. Configurar **Debezium** para capturar `outbox_event`.
5. Criar **Subscription Service** que consome `UserRegistered`.
6. Adicionar **Billing Service** e simular falhas de pagamento.
7. Adicionar **Shipping Service** e encadear evento de envio após pagamento aprovado.
8. Criar **Notification Service** que consome tudo.
9. Criar **Analytics Service** que calcula métricas.
10. Comparar **Kafka vs RabbitMQ** para alguns fluxos (ex.: notificações no Rabbit, analytics no Kafka).

---

### 🔹 6. Exemplo de Docker Compose inicial

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
      POSTGRES_DB: subscriptions
    ports:
      - "5432:5432"

  zookeeper:
    image: confluentinc/cp-zookeeper:7.6.0
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
    ports:
      - "2181:2181"

  kafka:
    image: confluentinc/cp-kafka:7.6.0
    depends_on:
      - zookeeper
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:9093
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
    ports:
      - "9092:9092"
      - "9093:9093"

  debezium:
    image: debezium/connect:2.7
    depends_on:
      - kafka
      - postgres
    environment:
      BOOTSTRAP_SERVERS: kafka:9092
      GROUP_ID: 1
      CONFIG_STORAGE_TOPIC: debezium_connect_configs
      OFFSET_STORAGE_TOPIC: debezium_connect_offsets
      STATUS_STORAGE_TOPIC: debezium_connect_status
      KEY_CONVERTER: org.apache.kafka.connect.json.JsonConverter
      VALUE_CONVERTER: org.apache.kafka.connect.json.JsonConverter
      VALUE_CONVERTER_SCHEMAS_ENABLE: "false"
      KEY_CONVERTER_SCHEMAS_ENABLE: "false"
    ports:
      - "8083:8083"

  kafdrop:
    image: obsidiandynamics/kafdrop
    depends_on:
      - kafka
    environment:
      KAFKA_BROKERCONNECT: kafka:9092
    ports:
      - "9000:9000"
```

---

👉 Esse **laboratório do Clube de Assinaturas** é bem rico: você vai praticar **coreografia de eventos, consistência eventual, CDC, CQRS, CloudEvents** e ainda comparar **RabbitMQ vs Kafka**.

Quer que eu prepare o **primeiro microserviço (User Service em Quarkus)** com **Outbox Pattern** já integrado ao Postgres?




Perfeito! Vamos detalhar o **User Service** do **Clube de Assinaturas** como **primeiro microserviço do laboratório**, usando **Java + Quarkus + PostgreSQL + Outbox Pattern + Debezium**.
Vou incluir **roadmap, pacotes e classes sugeridas**, focando em padrões de **event-driven architecture**.

---

# 📌 Roadmap do User Service

### **Objetivo**

Gerenciar usuários do clube e publicar eventos quando um usuário é registrado (`UserRegistered`) usando **Outbox Pattern**, para que outros serviços (Subscription, Notification, Analytics) possam reagir.

---

### 🔹 1. Inicialização do Projeto

* Criar projeto Quarkus via **Maven**:

```bash
mvn io.quarkus:quarkus-maven-plugin:create \
    -DprojectGroupId=com.clubedeassinaturas \
    -DprojectArtifactId=user-service \
    -DclassName="com.clubedeassinaturas.user.UserResource" \
    -Dextensions="resteasy-reactive,jdbc-postgresql,hibernate-orm-panache,smallrye-reactive-messaging,smallrye-reactive-messaging-kafka"
```

* Configurar **application.properties**:

```properties
# PostgreSQL
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=user
quarkus.datasource.password=password
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/subscriptions

# Hibernate
quarkus.hibernate-orm.database.generation=update

# Kafka Outgoing
mp.messaging.outgoing.outbox-events.connector=smallrye-kafka
mp.messaging.outgoing.outbox-events.topic=outbox-events
mp.messaging.outgoing.outbox-events.value.serializer=io.cloudevents.kafka.CloudEventSerializer
```

---

### 🔹 2. Estrutura de Pacotes e Classes

```
com.clubedeassinaturas.user
├── entity
│   ├── User.java               // Entidade principal do usuário
│   └── OutboxEvent.java        // Entidade para Outbox Pattern
├── repository
│   ├── UserRepository.java     // Operações DB para User
│   └── OutboxRepository.java   // Operações DB para OutboxEvent
├── service
│   └── UserService.java        // Lógica de negócio (criação de usuário + outbox)
├── resource
│   └── UserResource.java       // REST endpoints (/users)
├── messaging
│   └── OutboxPublisher.java    // Publica eventos do Outbox no Kafka
└── dto
    └── UserDTO.java            // DTO para requisições/respostas REST
```

---

### 🔹 3. Classes Principais

#### **User.java**

```java
package com.clubedeassinaturas.user.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class User extends PanacheEntity {
    public String name;
    public String email;
    public boolean active = true;
}
```

#### **OutboxEvent.java**

```java
package com.clubedeassinaturas.user.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import java.time.Instant;

@Entity
public class OutboxEvent extends PanacheEntity {
    public String type;
    public String payload;
    public boolean published = false;
    public Instant createdAt = Instant.now();
}
```

#### **UserService.java**

```java
package com.clubedeassinaturas.user.service;

import com.clubedeassinaturas.user.entity.User;
import com.clubedeassinaturas.user.entity.OutboxEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService {

    @Transactional
    public User createUser(String name, String email) {
        User user = new User();
        user.name = name;
        user.email = email;
        user.persist();

        // Outbox event
        OutboxEvent event = new OutboxEvent();
        event.type = "UserRegistered";
        event.payload = "{\"id\":" + user.id + ",\"name\":\"" + user.name + "\",\"email\":\"" + user.email + "\"}";
        event.persist();

        return user;
    }
}
```

#### **UserResource.java**

```java
package com.clubedeassinaturas.user.resource;

import com.clubedeassinaturas.user.service.UserService;
import com.clubedeassinaturas.user.entity.User;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @POST
    public Response createUser(User user) {
        User created = userService.createUser(user.name, user.email);
        return Response.ok(created).status(201).build();
    }
}
```

#### **OutboxPublisher.java**

```java
package com.clubedeassinaturas.user.messaging;

import com.clubedeassinaturas.user.entity.OutboxEvent;
import io.smallrye.reactive.messaging.annotations.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class OutboxPublisher {

    @Channel("outbox-events")
    Emitter<String> emitter;

    @Transactional
    public void publishPendingEvents() {
        List<OutboxEvent> events = OutboxEvent.list("published", false);
        for (OutboxEvent event : events) {
            emitter.send(event.payload);
            event.published = true; // Persistido pelo Panache automatic transaction
        }
    }
}
```

---

### 🔹 4. Próximos passos

1. Testar **UserService** com POST `/users`.
2. Configurar **Debezium** para monitorar `outbox_event` e publicar para Kafka.
3. Criar **Subscription Service** que consome `UserRegistered` (próximo microserviço).
4. Adicionar testes de integração para eventos publicados corretamente.

---

Se você quiser, posso montar **uma imagem do Docker Compose mínima só com User Service + Postgres + Kafka + Debezium**, já pronta para rodar o laboratório de eventos. Isso vai te dar o **primeiro microserviço funcional** do clube de assinaturas.

Quer que eu faça isso?
