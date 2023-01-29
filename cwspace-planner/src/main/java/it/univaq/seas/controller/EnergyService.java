package it.univaq.seas.controller;

import it.univaq.seas.model.RoomData;

import java.util.*;


import static it.univaq.seas.utility.Utility.cloneObject;

/**
 * @author gianlucarea
 */
public class EnergyService {

    public static List<RoomData> energyConsumptionPolicy(List<RoomData> rooms, int mainEnergy) {
        Map<RoomData, Integer> optimalOutput = new HashMap<>();
        int currentOutput = 0;
        List<RoomData> results = new ArrayList<>();

        for (RoomData room : rooms) {
            if (room.getRoomId() != 0 && room.isStatus()) {
                optimalOutput.put(room, room.getEnergyDemand() + (room.getEnergyDemand() / 100 * 20));
                currentOutput += room.getEnergyDemand() + (room.getEnergyDemand() / 100 * 20);
            }
        }

        while (currentOutput >= mainEnergy) {
            for (RoomData room : rooms) {
                if (room.getRoomId() != 0 && room.isStatus()) {
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
            if (room.getRoomId() != 0 && room.isStatus()) {
                room.setEnergyDemand(optimalOutput.get(room));
            }
        }

        for (RoomData roomData : optimalOutput.keySet()) {
            RoomData temporaryRoom =(RoomData) cloneObject(roomData);
            int roomID = roomData.getRoomId();
            temporaryRoom.setRoomName("room"+roomID);
            temporaryRoom.setBatteryOutput(optimalOutput.get(roomData));
            results.add(temporaryRoom);
            System.out.println("OUTPUT TO SET " + temporaryRoom.getBatteryOutput());
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
