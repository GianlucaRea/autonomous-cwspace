package it.univaq.seas;

import it.univaq.seas.job.Scheduler;
import it.univaq.seas.utilities.Utility;

public class App {
    public static void main(String[] args) {
        Utility.dockerized = true;
        Scheduler.schedule();
    }
}