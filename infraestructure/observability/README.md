# Observability Stack

This stack provisions Grafana, Prometheus, Loki, Tempo, cAdvisor, and postgres-exporter.

Included coverage:

- Container-level monitoring for `ms-catalog-container`, `ms-delivery-container`, `ms-recommendation-container`, `postgres`, and `rabbitmq` via cAdvisor.
- PostgreSQL metrics via `postgres-exporter`.
- RabbitMQ broker metrics via `rabbitmq_prometheus` on port `15692`.
- Grafana datasources and dashboards provisioned from disk.

Provisioned Grafana dashboards:

- `Container Overview`
- `PostgreSQL Overview`
- `RabbitMQ Overview`

Notes:

- `ms-catalog`, `ms-delivery`, and `ms-recommendation` are currently monitored at container level. They do not expose application metrics in this repository yet.
- To add application-level metrics later:
  - add `spring-boot-starter-actuator` and Micrometer Prometheus to `ms-catalog` and `ms-delivery`
  - add `django-prometheus` or OTEL metrics export to `ms-recommendation`