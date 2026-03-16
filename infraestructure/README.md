## Infraestructure

### RabbitMQ for new orders

- Image path: `infraestructure/images/rabbitmq`
- Exchange: `order.events.exchange`
- Routing key: `order.new.v1`
- Queue: `order.new.v1.queue`
- Dead-letter queue: `order.new.v1.dlq`

Start locally:

```bash
bash /home/vinicius/labs/eda-std-labs/infraestructure/database/docker-rabbitmq.sh
```

Management UI:

- URL: `http://localhost:15672`
- User: `garage_user`
- Password: `garage_password`

Environment variables expected by `ms-order` and `ms-delivery`:

- `RABBITMQ_HOST`
- `RABBITMQ_PORT`
- `RABBITMQ_USERNAME`
- `RABBITMQ_PASSWORD`
- `RABBITMQ_VHOST`
- `ORDER_EVENTS_EXCHANGE`
- `ORDER_CREATED_QUEUE`
- `ORDER_CREATED_ROUTING_KEY`
