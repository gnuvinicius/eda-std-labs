# Stage 1: Build
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build the WAR file
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime with WildFly
FROM quay.io/wildfly/wildfly:33.0.0.Final-jdk21

# Switch to root for configuration
USER root

# Download PostgreSQL JDBC driver to deployments (will be auto-deployed as a driver)
RUN curl -L https://jdbc.postgresql.org/download/postgresql-42.7.2.jar -o /opt/jboss/wildfly/standalone/deployments/postgresql-42.7.2.jar

# Create customization directory and CLI script
RUN mkdir -p /opt/jboss/wildfly/customization && \
    echo 'embed-server --std-out=echo' > /opt/jboss/wildfly/customization/execute.cli && \
    echo 'data-source add --name=PostgresDS --jndi-name=java:jboss/datasources/PostgresDS --driver-name=postgresql-42.7.2.jar --connection-url=jdbc:postgresql://postgres:5432/msregister_db --user-name=postgres --password=2AkByM4NfHFkeJz --use-ccm=false --max-pool-size=25 --blocking-timeout-wait-millis=5000 --enabled=true' >> /opt/jboss/wildfly/customization/execute.cli && \
    echo 'stop-embedded-server' >> /opt/jboss/wildfly/customization/execute.cli

# Run the configuration script to set up datasource
RUN /opt/jboss/wildfly/bin/jboss-cli.sh --file=/opt/jboss/wildfly/customization/execute.cli

# Fix ownership
RUN chown -R jboss:jboss /opt/jboss/wildfly

# Switch back to jboss user
USER jboss

# Copy the WAR file from build stage to WildFly deployments
COPY --from=build /app/target/msregister.war /opt/jboss/wildfly/standalone/deployments/

# Expose port 8080 (HTTP) and 9990 (Admin Console)
EXPOSE 8080 9990

# Start WildFly in standalone mode
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

