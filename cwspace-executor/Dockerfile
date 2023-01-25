FROM openjdk:8
VOLUME /tmp
ADD target/cwspace-executor.jar cwspace-executor.jar
ENTRYPOINT ["java","-jar","cwspace-executor.jar"]