# Stage 1: Build
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build the WAR file
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime with Tomcat
FROM tomcat:10.1-jdk21-temurin

# Remove default Tomcat applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the WAR file from build stage to Tomcat webapps
# Deploy as ROOT.war so it's accessible at /
COPY --from=build /app/target/msregister.war /usr/local/tomcat/webapps/ROOT.war

# Expose port 8080
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]

