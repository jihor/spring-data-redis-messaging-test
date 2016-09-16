FROM docstore.rgs.ru:5000/centos-java:latest
MAINTAINER "jihor"

# add jar
ADD backend-service.jar /

# expose ports
EXPOSE 8080
#remote debugging port, shouldn't be used in production
#EXPOSE 5005

# setup startup command
CMD ["bash", "-c", "java -jar  ${JAVA_OPTS} /backend-service.jar"]
