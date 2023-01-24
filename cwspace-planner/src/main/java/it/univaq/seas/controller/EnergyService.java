package it.univaq.seas.controller;

import it.univaq.seas.model.RoomData;
import it.univaq.seas.model.RoomDataRegression;

import java.util.*;
import java.util.stream.IntStream;

import static it.univaq.seas.utility.Utility.cloneObject;
import static it.univaq.seas.utility.Utility.valuePredictor;

/**
 * @author gianlucarea
 */
public class EnergyService {

    public static List<RoomData> energyConsumptionPredPolicy(List<RoomDataRegression> rooms, int mainEnergy) {
        Map<RoomData, Integer> optimalOutput = new HashMap<>();
        int currentOutput = 0;
        List<RoomData> results = new ArrayList<>();

        for (RoomDataRegression room : rooms) {
            if (room.getRoomId() != 0 && room.isStatus() == true) {
                List<Integer> temporaryList = new ArrayList<>();
                for (int i : IntStream.range(0, room.getEnergyDemandHistory().size()).toArray()) temporaryList.add(i);
                Integer predictedValue = (valuePredictor(temporaryList, room.getEnergyDemandHistory(), room.getEnergyDemandHistory().size())).intValue();
                optimalOutput.put((RoomData) room, predictedValue + (predictedValue / 100 * 20));
                currentOutput += predictedValue + (predictedValue / 100 * 20);
            }
        }

        while (currentOutput >= mainEnergy) {
            for (RoomData room : rooms) {
                if (room.getRoomId() != 0 && room.isStatus() == true) {
                    int requireOutput = optimalOutput.get(room);
                    int rmValue = 10;
                    if (requireOutput > 10) {
                        optimalOutput.put(room, requireOutput - rmValue);
                        currentOutput -= rmValue;
                    }
                    currentOutput -= rmValue;
                    if(currentOutput <= mainEnergy){
                        break;
                    }
                }
            }
        }

        for (RoomData room: rooms) {
            room.setEnergyDemand(optimalOutput.get(room));
        }

        for (RoomData roomData : optimalOutput.keySet()) {
            RoomData temporaryRoom = new RoomData();
            int roomID = roomData.getRoomId();
            temporaryRoom.setTopic(roomData.getTopic());
            temporaryRoom.setRoomId(roomID);
            temporaryRoom.setEnergyDemand(roomData.getEnergyDemand());
            temporaryRoom.setStatus(roomData.isStatus());
            temporaryRoom.setRoomName("room"+roomID);
            temporaryRoom.setBatteryInput(roomData.getBatteryInput());
            temporaryRoom.setBatteryOutput(roomData.getBatteryOutput());
            temporaryRoom.setSockets(roomData.getSockets());
            results.add(temporaryRoom);
            System.out.println("OUTPUT TO SET " + roomData.getBatteryOutput());
        }
        return results;
    }

    public static List<RoomData> energyConsumptionPolicy(List<RoomDataRegression> rooms, int mainEnergy) {
        Map<RoomData, Integer> optimalOutput = new HashMap<>();
        int currentOutput = 0;
        List<RoomData> results = new ArrayList<>();

        for (RoomData room : rooms) {
            if (room.getRoomId() != 0 && room.isStatus() == true) {
                optimalOutput.put(room, room.getEnergyDemand() + (room.getEnergyDemand() / 100 * 20));
                currentOutput += room.getEnergyDemand() + (room.getEnergyDemand() / 100 * 20);
            }
        }

        while (currentOutput >= mainEnergy) {
            for (RoomData room : rooms) {
                if (room.getRoomId() != 0 && room.isStatus() == true) {
                    int requireOutput = optimalOutput.get(room);
                    int rmValue = 10;
                    if (requireOutput > 10) {
                        optimalOutput.put(room, requireOutput - rmValue);
                        currentOutput -= rmValue;
                    }
                    currentOutput -= rmValue;
                    if(currentOutput <= mainEnergy){
                        break;
                    }
                }
            }
        }

        for (RoomData room: rooms) {
            room.setEnergyDemand(optimalOutput.get(room));
        }

        for (RoomData roomData : optimalOutput.keySet()) {
            RoomData temporaryRoom =(RoomData) cloneObject(roomData);
            int roomID = roomData.getRoomId();
            temporaryRoom.setRoomName("room"+roomID);
            temporaryRoom.setBatteryOutput(roomData.getBatteryOutput());
            results.add(temporaryRoom);
            System.out.println("OUTPUT TO SET " + roomData.getBatteryOutput());
        }
        return results;
    }


    public static List<RoomData> energyStatusPolicy(List<RoomData> rooms, List<String> topics) {
        List<RoomData> results = new ArrayList<>();
        for (RoomData room : rooms) {
            if(topics.contains(room.getTopic())) {
                RoomData local = (RoomData) cloneObject(room);
                local.setStatus(false);
                local.setBatteryInput(0);
                results.add(local);
            }
        }
        return results;
    }


}
