FROM gradle:8.4-jdk17 AS build
WORKDIR /app
COPY build.gradle.kts .
COPY src ./src
RUN gradle build -x test

FROM quay.io/wildfly/wildfly:27.0.1.Final-jdk17
COPY --from=build /app/build/libs/backend.war /opt/jboss/wildfly/standalone/deployments/

RUN /opt/jboss/wildfly/bin/add-user.sh admin admin123 --silent

EXPOSE 8080 9990

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]