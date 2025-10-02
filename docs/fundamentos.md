o que é Debezium e CDC?

Boa pergunta 👌, vamos por partes:

---

## 🔹 O que é CDC (Change Data Capture)?

**CDC = Change Data Capture** → é um padrão de arquitetura que permite **capturar as alterações feitas no banco de dados** (inserts, updates, deletes) e enviar essas mudanças para outros sistemas de forma **assíncrona e em tempo real**.

👉 Exemplo prático:

* Você tem um **banco PostgreSQL** onde os pedidos são salvos.
* Em vez de escrever código no app para publicar um evento toda vez que alguém cria um pedido, o **CDC observa o log de transações do banco** (no caso do Postgres, o WAL – *Write Ahead Log*).
* Assim que um pedido novo aparece, o CDC detecta a mudança e envia um **evento para Kafka**.

### Benefícios do CDC:

* Garantia de **consistência** entre banco e eventos (porque a captura vem direto do log transacional).
* Evita duplicar lógica de publicação de eventos na aplicação.
* Muito útil para **integração entre sistemas legados e modernos**.

---

## 🔹 O que é Debezium?

**Debezium** é uma **plataforma open-source** construída em cima do **Kafka Connect** que implementa CDC para vários bancos de dados (PostgreSQL, MySQL, MongoDB, SQL Server, Oracle, etc.).

👉 Ele roda como um **conector do Kafka**, fica "escutando" as mudanças no banco, e quando detecta uma alteração:

* Converte a mudança em um **evento JSON**.
* Publica no **Kafka Topic** correspondente.

### Exemplo de fluxo com Debezium:

1. Usuário cria um novo pedido no `order-service`.
2. O pedido é salvo no Postgres.
3. O Debezium detecta o `INSERT` no log do Postgres.
4. Ele gera um evento JSON, algo como:

```json
{
  "before": null,
  "after": {
    "id": 101,
    "customer_id": 500,
    "status": "CREATED",
    "total": 250.75
  },
  "op": "c",
  "ts_ms": 1696150400000
}
```

5. Esse JSON vai para o Kafka no tópico `dbserver1.public.orders`.
6. Outro serviço (ex: `billing-service`) consome o evento e processa o pagamento.

---

## 🔹 Quando usar CDC / Debezium?

✅ Bom para:

* Garantir **eventual consistency** entre microserviços.
* **Evitar implementar o Outbox Pattern manualmente** (salvar evento numa tabela e depois enviar).
* **Integração com sistemas legados** que não foram feitos para publicar eventos.

⚠️ Cuidados:

* Pode ser mais complexo de operar (infra extra: Kafka Connect + Debezium).
* Nem sempre é necessário se você já tem controle sobre as aplicações (às vezes o **Outbox Pattern** dentro do Quarkus já resolve).

---

👉 Resumindo:

* **CDC** é a técnica.
* **Debezium** é a ferramenta que implementa essa técnica e integra bancos → Kafka.

---

Quer que eu desenhe um **fluxo visual comparando Outbox Pattern x CDC com Debezium** pra você ver as diferenças mais claramente?
