# Base image with WildFly
FROM quay.io/wildfly/wildfly:33.0.0.Final-jdk21

# Switch to root for configuration
USER root

# Download PostgreSQL JDBC driver to deployments (will be auto-deployed as a driver)
RUN curl -L https://jdbc.postgresql.org/download/postgresql-42.7.2.jar -o /opt/jboss/wildfly/standalone/deployments/postgresql-42.7.2.jar

# Create customization directory and CLI script
COPY datasource.cli /opt/jboss/wildfly/customization/execute.cli

# Run the configuration script to set up datasource
RUN /opt/jboss/wildfly/bin/jboss-cli.sh --file=/opt/jboss/wildfly/customization/execute.cli

# Fix ownership
RUN chown -R jboss:jboss /opt/jboss/wildfly

# Switch back to jboss user
USER jboss

# Expose port 8080 (HTTP) and 9990 (Admin Console)
EXPOSE 8080 9990

# Clear out old configuration history
RUN rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history/current/* || true

# Start WildFly in standalone mode
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]