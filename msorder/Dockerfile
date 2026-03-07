FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM gnuvinicius/eda-lab-soa:latest

USER jboss

COPY --from=build /app/target/msorder.war /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080 9990

RUN rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history/current/* || true

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]