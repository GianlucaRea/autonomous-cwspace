package it.univaq.seas.model;

/**
 * @author gianlucarea
 */
public class RoomUpdate {
    private int newBatteryInput, newBatteryOutput,roomId;
    private String topic;

    public RoomUpdate() {
    }

    public int getNewBatteryInput() {
        return newBatteryInput;
    }

    public void setNewBatteryInput(int newBatteryInput) {
        this.newBatteryInput = newBatteryInput;
    }

    public int getNewBatteryOutput() {
        return newBatteryOutput;
    }

    public void setNewBatteryOutput(int newBatteryOutput) {
        this.newBatteryOutput = newBatteryOutput;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
