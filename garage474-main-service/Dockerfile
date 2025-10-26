# Use uma versão do WildFly compatível com Java 21 (ajuste a tag conforme necessário)
ARG WILDFLY_VERSION=38.0.0.Final-jdk21
FROM quay.io/wildfly/wildfly:${WILDFLY_VERSION}

ENV WILDFLY_HOME=/opt/jboss/wildfly
ENV JBOSS_HOME=${WILDFLY_HOME}

# Versão do driver PostgreSQL (ajuste conforme necessário)
ARG POSTGRESQL_DRIVER_VERSION=42.6.0

# Cria diretórios e adiciona o driver PostgreSQL como módulo
RUN mkdir -p ${WILDFLY_HOME}/modules/system/layers/base/org/postgresql/main \
 && curl -L -o /tmp/postgresql.jar https://repo1.maven.org/maven2/org/postgresql/postgresql/${POSTGRESQL_DRIVER_VERSION}/postgresql-${POSTGRESQL_DRIVER_VERSION}.jar \
 && mv /tmp/postgresql.jar ${WILDFLY_HOME}/modules/system/layers/base/org/postgresql/main/postgresql.jar

# Copia o module.xml do driver (substitua pelo arquivo module.xml abaixo)
COPY modules/org/postgresql/main/module.xml ${WILDFLY_HOME}/modules/system/layers/base/org/postgresql/main/module.xml

# Copia o standalone.xml customizado (substitua pelo arquivo standalone.xml abaixo)
COPY configuration/standalone.xml ${WILDFLY_HOME}/standalone/configuration/standalone.xml

# Copia o WAR gerado pelo Maven (o .war deve existir em target/ no contexto de build)
# Execute `mvn clean package` antes de `docker build`
COPY target/legacy-soap-app.war ${WILDFLY_HOME}/standalone/deployments/legacy-soap-app.war
# .dodeploy garante que o WildFly implante o .war em startup
RUN touch ${WILDFLY_HOME}/standalone/deployments/legacy-soap-app.war.dodeploy

EXPOSE 8080 9990

# Start WildFly
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]