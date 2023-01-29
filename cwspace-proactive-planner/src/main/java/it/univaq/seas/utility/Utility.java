package it.univaq.seas.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.univaq.seas.model.Message;
import it.univaq.seas.model.RoomData;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utility {

    public static boolean dockerized = true;

    public static String convertMessageToJSONString(List<Message> messages){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(messages);
        } catch (JsonProcessingException ex) {
            // Do notting
        }
        return null;
    }

    public static void mqttPublish(String data, boolean DOCKERIZE) {
        try {
            String serverURI = (DOCKERIZE) ? "tcp://mosquitto:1883" : "tcp://localhost:1883";
            MqttClient sensingClient = new MqttClient(serverURI, "cwspace-planner");
            sensingClient.connect();
            MqttMessage message = new MqttMessage();
            message.setPayload(data.getBytes());
            sensingClient.publish("home/planner_executor", message);
            sensingClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
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

    public static Double valuePredictor(List<Integer> x, List<Integer> y, int predictForDependentVariable) {
        if (x.size() != y.size()) { throw new IllegalStateException("Must have equal X and Y data points"); }
        Integer numberOfDataValues = x.size();

        List<Double> xSquared = x.stream().map(position -> Math.pow(position, 2)).collect(Collectors.toList());
        List<Integer> xMultipliedByY = IntStream.range(0, numberOfDataValues).map(i -> x.get(i) * y.get(i)).boxed().collect(Collectors.toList());
        Integer xSummed = x.stream().reduce((prev, next) -> prev + next).get();
        Integer ySummed = y.stream().reduce((prev, next) -> prev + next).get();
        Double sumOfXSquared = xSquared.stream().reduce((prev, next) -> prev + next).get();
        Integer sumOfXMultipliedByY = xMultipliedByY.stream().reduce((prev, next) -> prev + next).get();

        int slopeNominator = numberOfDataValues * sumOfXMultipliedByY - ySummed * xSummed;
        Double slopeDenominator = numberOfDataValues * sumOfXSquared - Math.pow(xSummed, 2);
        Double slope = slopeNominator / slopeDenominator;
        double interceptNominator = ySummed - slope * xSummed;
        double interceptDenominator = numberOfDataValues;
        Double intercept = interceptNominator / interceptDenominator;

        return (slope * predictForDependentVariable) + intercept;
    }

    public static Object cloneObject(Object obj){
        try{
            Object clone = obj.getClass().newInstance();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if(field.get(obj) == null || Modifier.isFinal(field.getModifiers())){
                    continue;
                }
                if(field.getType().isPrimitive() || field.getType().equals(String.class)
                        || field.getType().getSuperclass().equals(Number.class)
                        || field.getType().equals(Boolean.class)){
                    field.set(clone, field.get(obj));
                }else{
                    Object childObj = field.get(obj);
                    if(childObj == obj){
                        field.set(clone, clone);
                    }else{
                        field.set(clone, cloneObject(field.get(obj)));
                    }
                }
            }
            return clone;
        }catch(Exception e){
            return null;
        }
    }
}
