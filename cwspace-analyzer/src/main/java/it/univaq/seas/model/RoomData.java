package it.univaq.seas.model;

/**
 * @author gianlucarea
 */
public class RoomData {
    private int roomId,batteryCapacity,sockets,energyDemand,batteryLevel,batteryOutput,batteryInput;

    private String roomName,topic;
    private boolean status;

    public RoomData() {
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public int getSockets() {
        return sockets;
    }

    public void setSockets(int sockets) {
        this.sockets = sockets;
    }

    public int getEnergyDemand() {
        return energyDemand;
    }

    public void setEnergyDemand(int energyDemand) {
        this.energyDemand = energyDemand;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public int getBatteryOutput() {
        return batteryOutput;
    }

    public void setBatteryOutput(int batteryOutput) {
        this.batteryOutput = batteryOutput;
    }

    public int getBatteryInput() {
        return batteryInput;
    }

    public void setBatteryInput(int batteryInput) {
        this.batteryInput = batteryInput;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RoomData{" +
                "roomId=" + roomId +
                ", batteryCapacity=" + batteryCapacity +
                ", sockets=" + sockets +
                ", energyDemand=" + energyDemand +
                ", batteryLevel=" + batteryLevel +
                ", batteryOutput=" + batteryOutput +
                ", batteryInput=" + batteryInput +
                ", roomName='" + roomName + '\'' +
                ", topic='" + topic + '\'' +
                ", status=" + status +
                '}';
    }
}
