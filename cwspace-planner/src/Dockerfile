FROM openjdk:8
VOLUME /tmp
ADD target/wcwspace-planner.jar cwspace-planner.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","cwspace-planner.jar"]