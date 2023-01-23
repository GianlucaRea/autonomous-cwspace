package it.univaq.seas.daoImpl;

import it.univaq.seas.dao.RoomDao;
import it.univaq.seas.model.RoomData;
import it.univaq.seas.utilities.Utility;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gianlucarea
 */
public class RoomDaoImpl implements RoomDao {

    private String serverUrl,username,password;
    private static final String urlDOcker = "http://influxdb:8086", urlLocalhost = "http://localhost:8086";

    public RoomDaoImpl() {
        //Check docker-compose-file to have this data;
        serverUrl = (Utility.dockerized) ? urlDOcker : urlLocalhost;
        username = "telegraf";
        password="secretpassword";
    }

    @Override
    public List<String> getRoomsWithBatteryLessThan(int battery_level) {
        return null;
    }

    @Override
    public List<String> getRoomsWithBatteryGreaterThan(int battery_level) {
        return null;
    }

    @Override
    public List<String> checkEnergyConsuptionAdaptation() {
        return null;
    }

    @Override
    public List<RoomData> getRoomData() {
        InfluxDB influxDBConnection = InfluxDBFactory.connect(serverUrl,username,password);
        String command = "SELECT last(*) FROM room WHERE roomId != 0 AND time >= now() - 7d GROUP BY topic";
        List<RoomData> rooms = new ArrayList<>();
        QueryResult queryResult = influxDBConnection.query(new Query(command,"telegraf"));

        if(!queryResult.getResults().isEmpty()){
            for (Result series: queryResult.getResults()){
                for(Series singleSeries: series.getSeries()){
                    List<Object> tuple = singleSeries.getValues().get(0);
                    RoomData temporaryRoom = new RoomData();
                    int roomId = Utility.intcast(tuple.get(6));
                    if(roomId != 0){
                        temporaryRoom.setTopic(singleSeries.getTags().get("topic"));
                        temporaryRoom.setBatteryCapacity(Utility.intcast(tuple.get(1)));
                        temporaryRoom.setBatteryInput(Utility.intcast(tuple.get(2)));
                        temporaryRoom.setBatteryLevel(Utility.intcast(tuple.get(3)));
                        temporaryRoom.setBatteryOutput(Utility.intcast(tuple.get(4)));
                        temporaryRoom.setEnergyDemand(Utility.intcast(tuple.get(5)));
                        temporaryRoom.setSockets(Utility.intcast(tuple.get(7)));
                        temporaryRoom.setStatus(Utility.booleanCast(tuple.get(8)));
                        temporaryRoom.setRoomName("room"+roomId);
                        rooms.add(temporaryRoom);
                    }
                }
            }
        }
        return rooms;
    }
}
