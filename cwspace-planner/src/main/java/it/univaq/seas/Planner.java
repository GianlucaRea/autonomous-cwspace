package it.univaq.seas;

import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class Planner implements MqttCallback {

    private final String ENERGY_MESSAGE = "ENERGY_CONSUMPTION_ADAPTATION_REQUESTED";
    private final String STATUS_MESSAGE = "STATUS_WARNING"
    private MqttClient analyzerClientStatus = null, analyzerClientEnergy= null , systemClient = null;
    private static Planner plannerInstance = null;
    private boolean  dockerized;
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
        JSONArray jsonArray = new JSONArray(s);

        for(Object obj: jsonArray) {

            JSONObject jsonObject = (JSONObject) obj;
            String flag = (String) jsonObject.get("symptomId");

            if(flag.equals(ENERGY_MESSAGE)){

            } else if(flag.equals(STATUS_MESSAGE)){

            }
          /**  int statusFlag = 1;
            if (!JSONObject.NULL.equals(jsonObject.get("status"))) {
                statusFlag = (Integer) jsonObject.get("status");
            }
            if(flag.equals("EnergyAdaptation") || statusFlag != 0) {
                publishParameterValues(jsonObject, "batteryOutput", "batteryOutput");
            } else {
                publishParameterValues(jsonObject, "status", "status");
            }
           **/

        }
        return null;
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
