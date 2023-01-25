package it.univaq.seas.model;

/**
 * @author gianlucarea
 */
public class AdaptationMessage {
    private String symptomId;
    private int alert;
    private Object rooms;

    public AdaptationMessage() {
    }

    public String getSymptomId() {
        return symptomId;
    }

    public void setSymptomId(String symptomId) {
        this.symptomId = symptomId;
    }

    public int getAlert() {
        return alert;
    }

    public void setAlert(int alert) {
        this.alert = alert;
    }

    public Object getRooms() {
        return rooms;
    }

    public void setRooms(Object rooms) {
        this.rooms = rooms;
    }

    @Override
    public String toString() {
        return "AdaptationMessage{" +
                "symptomId='" + symptomId + '\'' +
                ", alert=" + alert +
                ", rooms=" + rooms +
                '}';
    }


}
