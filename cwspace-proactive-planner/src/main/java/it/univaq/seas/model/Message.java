package it.univaq.seas.model;

/**
 * @author gianlucarea
 */
public class Message {

    Integer roomId,status,batteryInput,batteryOutput;
    String topic;
    String message;

    public Message() {
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBatteryInput() {
        return batteryInput;
    }

    public void setBatteryInput(Integer batteryInput) {
        this.batteryInput = batteryInput;
    }

    public Integer getBatteryOutput() {
        return batteryOutput;
    }

    public void setBatteryOutput(Integer batteryOutput) {
        this.batteryOutput = batteryOutput;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
