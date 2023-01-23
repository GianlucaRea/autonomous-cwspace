package it.univaq.seas.dao;

import it.univaq.seas.model.RoomData;

import java.util.List;

/**
 * @author gianlucarea
 */
public interface RoomDao {

    public List<String> getRoomsWithBatteryLessThan(int battery_level);
    public List<String> getRoomsWithBatteryGreaterThan(int battery_level);
    public int checkEnergyConsuptionAdaptation();
    public List<RoomData> getRoomData();
}
