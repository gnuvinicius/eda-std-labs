FROM quay.io/wildfly/wildfly:33.0.0.Final-jdk21 AS builder

USER root

RUN curl -L https://jdbc.postgresql.org/download/postgresql-42.7.2.jar -o /opt/jboss/wildfly/standalone/deployments/postgresql-42.7.2.jar
COPY datasource.cli /opt/jboss/wildfly/customization/execute.cli
RUN /opt/jboss/wildfly/bin/jboss-cli.sh --file=/opt/jboss/wildfly/customization/execute.cli -D-Dinvalid.property.resolution=ignore

FROM eclipse-temurin:21-jre-alpine

COPY --from=builder /opt/jboss/wildfly /opt/jboss/wildfly

RUN addgroup -S jboss && adduser -S jboss -G jboss && \
    chown -R jboss:jboss /opt/jboss/wildfly


USER jboss
WORKDIR /opt/jboss/wildfly
EXPOSE 8080 9990


CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]