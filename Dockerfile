FROM maven:3.5.2-jdk-8-alpine AS MAVEN_BUILD
WORKDIR /app
COPY . src
CMD cd src ; ./mvnw spring-boot:run
# RUN ./mvnw spring-boot:run
# WORKDIR /app
# RUN ./mvnw package
# FROM openjdk:8-jre-alpine
# COPY --from=MAVEN_BUILD /build/target/*.jar /app/
# # ENTRYPOINT ["./mvnw", "spring-boot:run"]
# ENTRYPOINT ["java","-jar","/*.jar"]