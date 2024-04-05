FROM amazoncorretto:17-alpine-jdk
MAINTAINER EQUIPO3
COPY target/vortex-gamex-0.0.1-SNAPSHOT.jar vortex-app.jar
ENTRYPOINT ["java","-jar", "/vortex-app.jar"]