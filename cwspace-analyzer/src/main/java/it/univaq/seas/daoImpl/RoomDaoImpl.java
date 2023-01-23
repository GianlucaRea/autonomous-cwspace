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
import java.util.Map;

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
        List<String> topics = new ArrayList<>();
        InfluxDB influxDBConnection = InfluxDBFactory.connect(serverUrl,username,password);
        String command = "SELECT last(batteryLevel) FROM room WHERE roomId != 0 AND batteryLevel <=" + battery_level + " AND status= 1 GROUP BY  topic";
        QueryResult queryResult = influxDBConnection.query(new Query(command,"telegraf"));

        if(!queryResult.getResults().isEmpty()){
            List<Result> series = queryResult.getResults();
            for(Result result : series){
                if(result.getSeries() != null && !result.getSeries().isEmpty()){
                    for (Series singleSerie : result.getSeries()){
                        if(singleSerie.getTags().containsKey("topic")){
                            topics.add(singleSerie.getTags().get("topic"));
                        }
                    }
                }
            }
        }
        Runtime.getRuntime().addShutdownHook(new Thread(influxDBConnection::close));
        return topics;
    }

    @Override
    public List<String> getRoomsWithBatteryGreaterThan(int battery_level) {
        List<String> topics = new ArrayList<>();
        InfluxDB influxDBConnection = InfluxDBFactory.connect(serverUrl,username,password);
        String command = "SELECT last(batteryLevel) FROM room WHERE roomId != 0 AND batteryLevel >=" + battery_level + " AND status= 1 GROUP BY  topic";
        QueryResult queryResult = influxDBConnection.query(new Query(command,"telegraf"));

        if(!queryResult.getResults().isEmpty()){
            List<Result> series = queryResult.getResults();
            for(Result result : series){
                if(result.getSeries() != null && !result.getSeries().isEmpty()){
                    for (Series singleSerie : result.getSeries()){
                        if(singleSerie.getTags().containsKey("topic")){
                            topics.add(singleSerie.getTags().get("topic"));
                        }
                    }
                }
            }
        }
        Runtime.getRuntime().addShutdownHook(new Thread(influxDBConnection::close));
        return topics;
    }

    @Override
    public int checkEnergyConsuptionAdaptation() {
        InfluxDB influxDBConnection = InfluxDBFactory.connect(serverUrl,username,password);
        String command = "SELECT roomId, last(energyDemand), batteryOutput FROM room WHERE status = 1 GROUP BY topic";
        QueryResult queryResult = influxDBConnection.query(new Query(command,"telegraf"));

        if(!queryResult.getResults().isEmpty()){
            List<Result> series = queryResult.getResults();
            for(Result result : series){
                if(result.getSeries() != null && !result.getSeries().isEmpty()){
                    Integer mainOutput = null;
                    List<QueryResult.Series> res = result.getSeries();
                    boolean found = false;

                    for (Series singleSerie : res){
                        List<Object> tuple = singleSerie.getValues().get(0);
                        if(Utility.intcast(tuple.get(1)) == 0){
                            found = true;
                            mainOutput = Utility.intcast(tuple.get(3));
                            break;
                        }
                    }

                    if(found){
                        Integer finalMainOutput = mainOutput;
                        for(Series singleSerie: res){
                            List<Object> tuple = singleSerie.getValues().get(0);
                            Integer roomId = Utility.intcast(tuple.get(1));
                            Integer energyDemand = Utility.intcast(tuple.get(2));
                            if(roomId != 0 && energyDemand >= (finalMainOutput / (res.size() -1))){
                                return finalMainOutput;
                            }
                        }
                    } else return 0;
                }
            }
        }
        return 0;
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
