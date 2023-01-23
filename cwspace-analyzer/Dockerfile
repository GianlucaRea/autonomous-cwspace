FROM openjdk:8
VOLUME /tmp
ADD target/cwspace-analyzer.jar cwspace-analyzer.jar
ENTRYPOINT ["java","-jar","cwspace-analyzer.jar"]