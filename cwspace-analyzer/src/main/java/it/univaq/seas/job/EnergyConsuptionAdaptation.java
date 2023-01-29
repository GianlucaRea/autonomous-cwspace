package it.univaq.seas.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.univaq.seas.service.RoomService;
import it.univaq.seas.serviceImpl.RoomServiceImpl;

import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class EnergyConsuptionAdaptation extends TimerTask {

    @Override
    public void run() {
        RoomService roomService = new RoomServiceImpl();
        try {
            roomService.energyConsuptionAdaptation();
            roomService.setStatus();
        } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
