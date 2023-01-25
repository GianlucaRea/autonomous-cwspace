FROM openjdk:8
VOLUME /tmp
ADD target/wcwspace-proactive-planner.jar cwspace-proactive-planner.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","cwspace-proactive-planner.jar"]