FROM public.ecr.aws/u1o3k2v3/wildfly-soa:latest

USER jboss

COPY target/msorder.war /opt/jboss/wildfly/standalone/deployments/

ENV RABBITMQ_HOST=rabbitmq \
	RABBITMQ_PORT=5672 \
	RABBITMQ_USERNAME=garage_user \
	RABBITMQ_PASSWORD=garage_password \
	RABBITMQ_VHOST=/ \
	ORDER_EVENTS_EXCHANGE=order.events.exchange \
	ORDER_NEW_QUEUE=order.new.v1.queue \
	ORDER_NEW_ROUTING_KEY=order.new.v1

EXPOSE 8080 9990

RUN rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history/current/* || true

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]