package it.univaq.seas;

import it.univaq.seas.controller.EnergyService;
import it.univaq.seas.daoImpl.RoomDaoImpl;
import it.univaq.seas.model.AdaptationMessage;
import it.univaq.seas.model.Message;
import it.univaq.seas.model.RoomData;
import it.univaq.seas.model.RoomDataRegression;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

import static it.univaq.seas.utility.Utility.convertMessageToJSONString;
import static it.univaq.seas.utility.Utility.mqttPublish;

public class RestController {
    private boolean DOCKERIZE = true;

    @GetMapping("/")
    public String greeting() {
        return "Root";
    }

    @PostMapping("/message")
    public String message(@RequestBody String body) {
        System.out.println(body);
        return body;
    }

    @PostMapping("/energyConsumptionAdaptation")
    public String consumptionPredAdaptation(@RequestBody AdaptationMessage message) {

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
        mqttPublish(jsonMessage,DOCKERIZE);
        return "Consumption adaptation planned";
    }

    @PostMapping("/status")
    public String statusAdaptation(@RequestBody AdaptationMessage message) {
        List<RoomData> rooms = new RoomDaoImpl().getRoomData();
        List<RoomData> results = EnergyService.energyStatusPolicy(rooms,(List<String>) message.getRooms());
        List<Message> writeList = new ArrayList<>();

        for (RoomData room: results){
            Message temporaryMessage = new Message();
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
        System.out.println("Stats Consumption Policy : " + jsonMessage);
        mqttPublish(jsonMessage,DOCKERIZE);
        return "Status adaptation planned";
    }
}
