# Autonomous Coworking Space
> Project developed for the Software Engineering for Autonomous System course - University of L'Aquila

This repository contains the implementation of ***Autonomous Coworking Space**: Project developed for the Software Engineering for Autonomous System course - University of L'Aquila

## Installation Requirements
1. Install the latest version of Docker - https://docs.docker.com/get-docker/
2. Install the latest version of Docker Compose (if not already installed) - https://docs.docker.com/compose/install/

## Project Description

This project consists in a set of containers running in Docker and automatically configured using the file *docker-compose.yml*. In the following the description of them:

 - **MAPE-K-Loop / Managing System containers**
	 - *Monitor*: The monitor has been realized using <a href="https://www.influxdata.com/time-series-platform/telegraf/">Telegraf</a>. It gets the data from the MQTT Broker and store it in the knowledge (InfluxDB)  
	 - *Analyzer*: The analyzer is a Java application and it is contained in the *./cwspace-analyzer/* subfolder.
	 - *Planner*: The planner is a Spring Boot Application and it is stored in the *./cwspace-planner/* subfolder in this repo.
	 - *Planner*: The planner is a Spring Boot Application to and it is stored in the *./cwspace-proactive-planner/* subfolder in this repo. On default this is commented in the *docker-compose.yml* file, if we want to use it instead of the default planner we can decomment this proactive service and commend the other one.
	 - *Executor*: This is a Java Application and it is stored under the path *./cwspace-executor/*.
	 - *Knowledge*: The knowledge of the loop has been realized using InfluxDB (https://www.influxdata.com),
 - **Managed System containers**:
	 - *cwspace-enviroment*: A Nodejs application which simulate the enviroment of the office rooms.
	 - *node-red*: A NodeRed which simulate the sensors and actuators of the office rooms.

 - **Other services containers**
	 - *grafana_container*: A dashboard which allow us to graphically monitor the system and check if it is working properly.
	 - *mosquitto_container*: The <a href="https://mosquitto.org">Mosquitto</a> MQTT Broker which manages all the MQTT messages exchanged in the system (almost all the components communicate through MQTT protocol).

*More informations on the documentation in this repo (**Documentation/Report.pdf**) and to see the images and diagrams in detail you can check(**Documentation/Images/**) and (**Documentation/Diagrams/**)*.

## Configuration
The configuration of the system is mainly contained in the *docker-compose.yml* file. Be sure that all the exposed mapped ports are free on your environment:
- **8081** for the Planner
- **1883** and **9001** for Mosquitto MQTT Broker
- **8086** for InfluxDB
- **3000** for the Grafana dashboard

If you are not able to free all the listed ports, you can change the mapping in the compose file.

In addition, in this repo there are some files for configuration purposes which docker will map in the containers as volumes:
- *mosquitto.conf* file is the configuration file for the Mosquitto Broker container
- *telegraf.conf* file is the configuration file for Telegraf

## Running 
First of all you have to open your terminal on this folder and build all the images running the following command:

    docker-compose build
Then, you can run all the containers with the following command:

    docker-compose up -d
If you want to stop all, you can use this:

    docker-compose down

## Author
This project has been realized by:
- Gianluca Rea (<a href="https://github.com/GianlucaRea">GitHub Profile</a>)