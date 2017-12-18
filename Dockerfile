FROM openjdk
MAINTAINER Sam <sam.lo.office@gmail.com>
ADD target/identity-image-service.jar identity-image-service.jar
ENTRYPOINT ["java", "-jar", "/identity-image-service.jar"]
EXPOSE 9876