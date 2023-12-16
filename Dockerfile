# Use Maven image as build system
FROM maven:3.8.6-amazoncorretto-17 as build

# Copy source code
COPY src /pdfapp/src
COPY pom.xml /pdfapp

WORKDIR /pdfapp

#  Build .war archive with Maven
RUN mvn clean package --batch-mode

FROM openjdk:17-jdk-alpine
COPY --from=build pdfapp/target/*.jar pdfapp.jar
ENTRYPOINT ["java","-jar","/pdfapp.jar"]
