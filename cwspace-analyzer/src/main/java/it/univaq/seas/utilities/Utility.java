package it.univaq.seas.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.univaq.seas.model.SymptomMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;

/**
 * @author gianlucarea
 */
public class Utility {

    public static boolean dockerized = true;
    private static MqttClient systemClient = null;

    private static String url;


    public static String convertMessageToJSONString(SymptomMessage message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
    }


    public static Integer intcast(Object var) {
        if (var instanceof Double) {
            return ( (Double) var).intValue();
        }
        if (var instanceof String) {
            return Integer.parseInt((String) var);
        }
        else return (Integer) var;
    }

    public static boolean booleanCast(Object var) {
        int checkValue = 0;
        if (var instanceof Double) {
            checkValue= ( (Double) var).intValue();
        }
        if (var instanceof String) {
            checkValue = Integer.parseInt((String) var);
        }
        if(checkValue == 0) {
            return false;
        } else return true;
    }

    public static void publish(String topic, String data, boolean dockerize) {
        try {
            if (dockerize) { url = "tcp://mosquitto:1883"; } else { url = "tpc://localhost:1883"; }
            systemClient = new MqttClient(url,"Analyzer_SystemClient");
            systemClient.connect();
            MqttMessage message = new MqttMessage();
            message.setPayload(data.getBytes());
            systemClient.publish(topic, message);

        } catch (MqttException e) { e.printStackTrace(); }
    }
}
