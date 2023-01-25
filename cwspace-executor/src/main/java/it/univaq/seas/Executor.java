package it.univaq.seas;

import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author gianlucarea
 */
public class Executor implements MqttCallback {
    private MqttClient plannerClient = null , systemClient = null;
    private static Executor ExecutorInstance = null;
    private boolean stop , dockerized;
    private String url;

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        System.out.println("Executor: A new message arrived from the topic: \"" + s + "\". The payload of the message is " + mqttMessage.toString());
        execute(mqttMessage.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    public static Executor getInstance(Boolean dockerize){
        if(ExecutorInstance == null){
            try {
                ExecutorInstance = new Executor(dockerize);
                ExecutorInstance.dockerized = dockerize;
            } catch (MqttException e){
                e.printStackTrace();
            }
        }
        return ExecutorInstance;
    }

    private Executor(Boolean dockerize) throws MqttException {
        // Set url
        if (dockerize) { this.url = "tcp://mosquitto:1883"; } else { this.url = "tpc://localhost:1883"; }
        stop = false;

        this.systemClient = new MqttClient(this.url,"Executor_SystemClient");
        this.plannerClient = new MqttClient(this.url, "Executor_PlannerClient");
        systemClient.connect();
        connectAndSubscribe();
    }

    public void connectAndSubscribe() {
        try {
            this.plannerClient.setCallback(this);
            this.plannerClient.connect();
            this.plannerClient.subscribe("home/planner_executor");
        } catch (MqttException e) { e.printStackTrace(); }
    }

    private void publish(String topic, String data) {
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(data.getBytes());
            systemClient.publish(topic, message);
        } catch (MqttException e) { e.printStackTrace(); }
    }

    private void publishParameterValues(JSONObject jsonObj, String parameter, String value) {

        Object param = jsonObj.get(parameter);
        if(param.toString().equals("null")) return;
        if (param != null) {
            ExecutionData executionData = new ExecutionData();
            executionData.setParameter(parameter);
            executionData.setValue(jsonObj.getInt(value));
            String objToString = new JSONObject(executionData).toString();
            publish(jsonObj.getString("topic").replace("sensors", "acting"), objToString);
            System.out.println("Execution data published: " + objToString);
        }
    }

    private void execute(String s){
        JSONArray jsonArray = new JSONArray(s);
        for(Object obj: jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
            publishParameterValues(jsonObject, "batteryInput", "batteryInput");
            publishParameterValues(jsonObject, "batteryOutput", "batteryOutput");
            publishParameterValues(jsonObject, "status", "status");
        }
    }

    public void stop(){
        this.stop = true;
    }


}
