FROM rabbitmq:3.13-management

COPY rabbitmq.conf /etc/rabbitmq/rabbitmq.conf
COPY enabled_plugins /etc/rabbitmq/enabled_plugins

# Loads garage_user (admin) with full permissions on root vhost (/) at startup.
COPY definitions.json /etc/rabbitmq/definitions.json

EXPOSE 5672 15672
