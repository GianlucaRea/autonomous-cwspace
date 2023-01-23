package it.univaq.seas.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.univaq.seas.dao.RoomDao;
import it.univaq.seas.daoImpl.RoomDaoImpl;
import it.univaq.seas.model.RoomData;
import it.univaq.seas.model.SymptomId;
import it.univaq.seas.model.SymptomMessage;
import it.univaq.seas.service.RoomService;
import it.univaq.seas.utilities.HttpConnection;
import it.univaq.seas.utilities.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author gianlucarea
 */
public class RoomServiceImpl implements RoomService {

    private RoomDao roomDao = new RoomDaoImpl();
    private String DOCKERURL = ""; //TODO change this
    private String LOCALURL = "http://localhost;8081/";
    private Boolean DOCKERIZED = Utility.dockerized;

    @Override
    public void checkBatteryLessThan(int battery_level) throws JsonProcessingException, ExecutionException, InterruptedException {
        List<String> topics = roomDao.getRoomsWithBatteryLessThan(battery_level);
        if(!topics.isEmpty()){
            SymptomMessage symptomMessage = new SymptomMessage();
            symptomMessage.setRooms(topics);
            symptomMessage.setSymptomId(SymptomId.BATTERY_EMPTY_LEVEL_WARNING);
            symptomMessage.setAlert(battery_level);
            HttpConnection.invoke(Utility.convertMessageToJSONString(symptomMessage));
        }
    }

    @Override
    public void checkBatteryGreaterThan(int battery_level) throws JsonProcessingException, ExecutionException, InterruptedException {
        List<String> topics = roomDao.getRoomsWithBatteryGreaterThan(battery_level);
        if(!topics.isEmpty()){
            SymptomMessage symptomMessage = new SymptomMessage();
            symptomMessage.setRooms(topics);
            symptomMessage.setSymptomId(SymptomId.BATTERY_FULL_LEVEL_WARNING);
            symptomMessage.setAlert(battery_level);
            HttpConnection.invoke(Utility.convertMessageToJSONString(symptomMessage));
        }
    }

    @Override
    public void energyConsuptionAdaptation() throws JsonProcessingException, ExecutionException, InterruptedException {
        String endpoint = "energyConsuptionAdaptation";
        String URL = (DOCKERIZED) ? (DOCKERURL + endpoint) : (LOCALURL + endpoint);
        int value = roomDao.checkEnergyConsuptionAdaptation();
        if (value != 0) {
            SymptomMessage symptomMessage = new SymptomMessage();
            symptomMessage.setRooms(null);
            symptomMessage.setSymptomId(SymptomId.ENERGY_CONSUMPTION_ADAPTATION_REQUESTED);
            symptomMessage.setAlert(value);
            HttpConnection.invoke(Utility.convertMessageToJSONString(symptomMessage));
        }
    }

    @Override
    public void setStatus() throws JsonProcessingException, ExecutionException, InterruptedException {
        String endpoint = "status";
        String URL = (DOCKERIZED) ? (DOCKERURL + endpoint) : (LOCALURL + endpoint);
        List<RoomData> rooms = roomDao.getRoomData();
        List<RoomData> roomsWithStatusChange = new ArrayList<>();

        for (RoomData room : rooms){
            if ((room.getEnergyDemand() >= (room.getSockets() * 30)) && room.isStatus()){
                System.out.println("Status change 1 " + room.getTopic() + " " + room.getEnergyDemand() + " " + room.getSockets());
                roomsWithStatusChange.add(room);
            } else if (room.getBatteryLevel() == 0 && room.isStatus()) {
                System.out.println("Status change 2 " + room.getTopic() + " " + room.getEnergyDemand());
                roomsWithStatusChange.add(room);
            }
        }

        if(!roomsWithStatusChange.isEmpty()){
            SymptomMessage symptomMessage = new SymptomMessage();
            List<String> topics = new ArrayList<>();
            for (RoomData roomData : roomsWithStatusChange){
                topics.add(roomData.getTopic());
            }
            symptomMessage.setAlert(0);
            symptomMessage.setSymptomId(SymptomId.STATUS_WARNING);
            symptomMessage.setRooms(topics);
            System.out.println("Status adaptation for" + symptomMessage.getRooms().toString());
            HttpConnection.invoke(Utility.convertMessageToJSONString(symptomMessage),URL);
        }
    }
//EOF
}
