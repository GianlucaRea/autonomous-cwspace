package it.univaq.seas;

import it.univaq.seas.controller.EnergyService;
import it.univaq.seas.daoImpl.RoomDaoImpl;
import it.univaq.seas.model.*;
import org.eclipse.paho.client.mqttv3.*;

import java.util.ArrayList;
import java.util.List;

import static it.univaq.seas.utility.Utility.convertMessageToJSONString;
import static it.univaq.seas.utility.Utility.mqttPublish;

import it.univaq.seas.controller.EnergyService;
import it.univaq.seas.daoImpl.RoomDaoImpl;
import it.univaq.seas.model.AdaptationMessage;
import it.univaq.seas.model.Message;
import it.univaq.seas.model.RoomData;

import org.eclipse.paho.client.mqttv3.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

import static it.univaq.seas.utility.Utility.convertMessageToJSONString;
import static it.univaq.seas.utility.Utility.mqttPublish;

public class Planner implements MqttCallback {

    private final String ENERGY_MESSAGE = "ENERGY_CONSUMPTION_ADAPTATION_REQUESTED";
    private final String STATUS_MESSAGE = "STATUS_WARNING";
    private MqttClient analyzerClientStatus = null, analyzerClientEnergy= null , systemClient = null;
    private static Planner plannerInstance = null;
    private boolean dockerized;
    private String url;

    public static Planner getInstance(Boolean dockerize){
        if(plannerInstance == null){
            try {
                plannerInstance = new Planner(dockerize);
                plannerInstance.dockerized = dockerize;
            } catch (MqttException e){
                e.printStackTrace();
            }
        }
        return plannerInstance;
    }

    private Planner(Boolean dockerize) throws MqttException {
        // Set url
        if (dockerize) { this.url = "tcp://mosquitto:1883"; } else { this.url = "tpc://localhost:1883"; }

        this.systemClient = new MqttClient(this.url,"Executor_SystemClient");
        this.analyzerClientStatus = new MqttClient(this.url, "Planner_AnalyzerClientStatus");
        this.analyzerClientEnergy = new MqttClient(this.url, "Planner_AnalyzerClientEnergy");
        systemClient.connect();
        connectAndSubscribe();
    }

    public void connectAndSubscribe() {
        try {
            this.analyzerClientStatus.setCallback(this);
            this.analyzerClientStatus.connect();
            this.analyzerClientStatus.subscribe("home/analyzer/status");
            this.analyzerClientEnergy.setCallback(this);
            this.analyzerClientEnergy.connect();
            this.analyzerClientEnergy.subscribe("home/analyzer/energyConsumption");
        } catch (MqttException e) { e.printStackTrace(); }
    }

    private JSONObject execute(String s){
        JSONObject jsonObject= new JSONObject(s);
        String flag = (String) jsonObject.get("symptomId");
        AdaptationMessage adaptationMessage = new AdaptationMessage();
        if(flag.equals(ENERGY_MESSAGE)){
            adaptationMessage.setAlert(jsonObject.getInt("alert"));
            adaptationMessage.setRooms(jsonObject.get("rooms"));
            adaptationMessage.setSymptomId(flag);
            consumptionPredAdaptation(adaptationMessage);
        } else if(flag.equals(STATUS_MESSAGE)){
            adaptationMessage.setAlert(jsonObject.getInt("alert"));
            adaptationMessage.setRooms(jsonObject.get("rooms"));
            JSONArray temporaryArray = (JSONArray) jsonObject.get("rooms");
            adaptationMessage.setRooms(temporaryArray);
            adaptationMessage.setSymptomId(flag);
            statusAdaptation(adaptationMessage);
        }

        return null;
    }

    public String consumptionPredAdaptation( AdaptationMessage message) {

        List<RoomDataRegression> rooms = new RoomDaoImpl().getRoomDataRegression();
        int initEnergy = message.getAlert();

        List<RoomData> results = EnergyService.energyConsumptionPredPolicy(rooms,initEnergy);
        List<Message> writeList = new ArrayList<>();

        for(RoomData room : results){
            Message  temporaryMessage = new Message();
            temporaryMessage.setRoomId(room.getRoomId());
            temporaryMessage.setBatteryOutput(room.getBatteryOutput());
            temporaryMessage.setTopic(room.getTopic());
            temporaryMessage.setMessage("EnergyAdaptation");
            writeList.add(temporaryMessage);
        }
        String jsonMessage = convertMessageToJSONString(writeList);
        System.out.println("Energy Consumption Policy : " + jsonMessage);
        mqttPublish(jsonMessage,dockerized);
        return "Consumption adaptation planned";
    }


    public String statusAdaptation(AdaptationMessage message) {
        JSONArray jsonArray = (JSONArray) message.getRooms();
        List<String> messageRooms = new ArrayList<>();
        if (jsonArray != null) {
            for (int i=0;i<jsonArray.length();i++){
                messageRooms.add((String) jsonArray.get(i));
            }
        }

        List<RoomData> rooms = new RoomDaoImpl().getRoomData();
        List<RoomData> results = EnergyService.energyStatusPolicy(rooms,messageRooms);
        List<Message> writeList = new ArrayList<>();

        for (RoomData room: results){
            Message  temporaryMessage = new Message();
            temporaryMessage.setRoomId(room.getRoomId());
            temporaryMessage.setBatteryInput(room.getBatteryInput());
            temporaryMessage.setTopic(room.getTopic());
            temporaryMessage.setMessage("StatusAdaptation");
            if(room.isStatus()){
                temporaryMessage.setStatus(1);
            } else {
                temporaryMessage.setStatus(0);
            }
            writeList.add(temporaryMessage);
        }
        String jsonMessage = convertMessageToJSONString(writeList);
        System.out.println("Status Consumption Policy : " + jsonMessage);
        mqttPublish(jsonMessage,dockerized);
        return "Status adaptation planned";
    }

    @Override
    public void connectionLost(Throwable cause) {
        cause.printStackTrace();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("Planner: A new message arrived from the topic: \"" + topic + "\". The payload of the message is " + message.toString());
        execute(message.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("Delivery Complete");
    }


}
