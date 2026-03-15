FROM rabbitmq:3.13-management

# Local/default credentials for development.
ENV RABBITMQ_DEFAULT_USER=garage_user
ENV RABBITMQ_DEFAULT_PASS=garage_password
ENV RABBITMQ_DEFAULT_VHOST=/

COPY rabbitmq.conf /etc/rabbitmq/rabbitmq.conf
COPY enabled_plugins /etc/rabbitmq/enabled_plugins
COPY definitions.json /etc/rabbitmq/definitions.json

EXPOSE 5672 15672

