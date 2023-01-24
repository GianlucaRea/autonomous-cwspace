package it.univaq.seas.model;

import java.util.List;
/**
 * @author gianlucarea
 */
public class RoomDataRegression extends RoomData{
    private List<Integer> energyDemandHistory;

    public List<Integer> getEnergyDemandHistory() {
        return energyDemandHistory;
    }

    public void setEnergyDemandHistory(List<Integer> energyDemandHistory) {
        this.energyDemandHistory = energyDemandHistory;
    }
}
