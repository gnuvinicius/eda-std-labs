#!/bin/bash
# Script to configure PostgreSQL datasource in WildFly

# Wait for WildFly to be ready
sleep 10

# Add PostgreSQL module (if not already present)
/opt/jboss/wildfly/bin/jboss-cli.sh --connect --commands="
module add --name=org.postgres --resources=/opt/jboss/wildfly/standalone/deployments/postgresql-42.7.2.jar --dependencies=javax.api,javax.transaction.api
"

# Configure datasource
/opt/jboss/wildfly/bin/jboss-cli.sh --connect --commands="
/subsystem=datasources/jdbc-driver=postgres:add(driver-name=postgres,driver-module-name=org.postgres,driver-class-name=org.postgresql.Driver),
data-source add --name=PostgresDS --jndi-name=java:jboss/datasources/PostgresDS --driver-name=postgres --connection-url=jdbc:postgresql://host.docker.internal:5432/msregister_db --user-name=postgres --password=2AkByM4NfHFkeJz --validate-on-match=true --background-validation=false --valid-connection-checker-class-name=org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLValidConnectionChecker --exception-sorter-class-name=org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLExceptionSorter
"

