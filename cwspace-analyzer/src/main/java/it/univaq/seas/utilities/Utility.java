package it.univaq.seas.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.univaq.seas.model.SymptomMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author gianlucarea
 */
public class Utility {

    public static boolean dockerized = true;

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
}
