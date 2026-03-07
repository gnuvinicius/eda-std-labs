FROM gnuvinicius/eda-lab-soa:latest

USER jboss

COPY target/msorder.war /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080 9990

RUN rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history/current/* || true

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]