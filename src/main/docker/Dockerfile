FROM java:latest

ADD server-0.0.1-SNAPSHOT.jar shiro-server.jar

RUN bash -c 'touch /shiro-server.jar'

ENTRYPOINT ["java","-jar","/shiro-server.jar"]