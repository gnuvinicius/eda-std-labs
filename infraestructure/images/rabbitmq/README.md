# RabbitMQ Image (Order Events)

This image boots RabbitMQ with a preconfigured topology for new order events.

## Topology

- Exchange: `order.events.exchange` (topic)
- Routing key: `order.new.v1`
- Queue: `order.new.v1.queue`
- DLX: `order.events.dlx`
- DLQ: `order.new.v1.dlq`

## Build

```bash
docker build -t garage/rabbitmq-orders:local /home/vinicius/labs/eda-std-labs/infraestructure/images/rabbitmq
```

## Run

```bash
docker run -d --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  garage/rabbitmq-orders:local
```

## Management UI

- URL: `http://localhost:15672`
- User: `garage_user`
- Password: `garage_password`

## Microservice env vars

Use these values in `ms-delivery` and `ms-order`:

- `RABBITMQ_HOST=rabbitmq` (or `localhost` when running outside Docker network)
- `RABBITMQ_PORT=5672`
- `RABBITMQ_USERNAME=garage_user`
- `RABBITMQ_PASSWORD=garage_password`
- `RABBITMQ_VHOST=/`
- `ORDER_EVENTS_EXCHANGE=order.events.exchange`
- `ORDER_CREATED_ROUTING_KEY=order.created.v1`
- `ORDER_CREATED_QUEUE=order.created.v1.queue`

