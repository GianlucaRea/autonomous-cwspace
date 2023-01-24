package it.univaq.seas.dao;

import it.univaq.seas.model.RoomData;
import it.univaq.seas.model.RoomDataRegression;

import java.util.List;

/**
 * @author gianlucarea
 */
public interface RoomDao {
    public List<RoomData> getRoomData();
    public List<RoomDataRegression> getRoomDataRegression();
}

