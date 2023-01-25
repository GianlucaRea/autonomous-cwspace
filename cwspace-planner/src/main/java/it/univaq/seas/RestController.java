package it.univaq.seas;

import it.univaq.seas.controller.EnergyService;
import it.univaq.seas.daoImpl.RoomDaoImpl;
import it.univaq.seas.model.AdaptationMessage;
import it.univaq.seas.model.RoomData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

import static it.univaq.seas.utility.Utility.convertMessageToJSONString;
import static it.univaq.seas.utility.Utility.mqttPublish;

/**
 * @author gianlucarea
 */
@org.springframework.web.bind.annotation.RestController
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

    @PostMapping("/energyConsuptionAdaptation")
    public String consumptionAdaptation(@RequestBody AdaptationMessage message) {

        List<RoomData> rooms = new RoomDaoImpl().getRoomData();
        int initEnergy = message.getAlert();

        List<RoomData> results = EnergyService.energyConsumptionPolicy(rooms,initEnergy);
        List<RoomData> writeList = new ArrayList<>();

        for(RoomData room : results){
            RoomData temporaryRoom = new RoomData();
            temporaryRoom.setRoomId(room.getRoomId());
            temporaryRoom.setBatteryOutput(room.getBatteryOutput());
            temporaryRoom.setTopic(room.getTopic());
            writeList.add(temporaryRoom);
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
        List<RoomData> writeList = new ArrayList<>();

        for (RoomData room: results){
            RoomData temporaryRoom = new RoomData();
            temporaryRoom.setRoomId(room.getRoomId());
            temporaryRoom.setStatus(room.isStatus());
            temporaryRoom.setBatteryInput(room.getBatteryInput());
            temporaryRoom.setTopic(room.getTopic());
            writeList.add(temporaryRoom);
        }
        String jsonMessage = convertMessageToJSONString(writeList);
        System.out.println("Stats Consumption Policy : " + jsonMessage);
        mqttPublish(jsonMessage,DOCKERIZE);
        return "Status adaptation planned";
    }
}
