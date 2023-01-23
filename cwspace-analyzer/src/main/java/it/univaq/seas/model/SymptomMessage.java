package it.univaq.seas.model;

import java.util.List;

/**
 * @author gianlucarea
 */

public class SymptomMessage {
    private SymptomId symptomId;
    private int alert;
    private List<String> rooms;

    public SymptomMessage() {
    }

    public SymptomMessage(SymptomId symptomId, int alert, List<String> rooms) {
        this.symptomId = symptomId;
        this.alert = alert;
        this.rooms = rooms;
    }

    public SymptomId getSymptomId() {
        return symptomId;
    }

    public void setSymptomId(SymptomId symptomId) {
        this.symptomId = symptomId;
    }

    public int getAlert() {
        return alert;
    }

    public void setAlert(int alert) {
        this.alert = alert;
    }

    public List<String> getRooms() {
        return rooms;
    }

    public void setRooms(List<String> rooms) {
        this.rooms = rooms;
    }

    @Override
    public String toString() {
        return "SymptomMessage{" +
                "symptomId=" + symptomId +
                ", alert=" + alert +
                ", rooms=" + rooms +
                '}';
    }
}
