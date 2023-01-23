package it.univaq.seas.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.univaq.seas.model.RoomData;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author gianlucarea
 */
public interface RoomService {

    public void checkBatteryLessThan(int battery_level) throws JsonProcessingException, ExecutionException, InterruptedException;
    public void checkBatteryGreaterThan(int battery_level) throws JsonProcessingException, ExecutionException, InterruptedException;
    public void energyConsuptionAdaptation() throws JsonProcessingException, ExecutionException, InterruptedException;
    public void setStatus() throws JsonProcessingException, ExecutionException, InterruptedException;
}
