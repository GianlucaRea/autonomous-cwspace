package it.univaq.seas.job;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author gianlucarea
 */
public class Scheduler {

    public static void schedule() {
        Timer timer = new Timer();
        TimerTask energyConsuptionAdaptation = new EnergyConsuptionAdaptation();
        timer.scheduleAtFixedRate(energyConsuptionAdaptation,0,10000);
    }

}
