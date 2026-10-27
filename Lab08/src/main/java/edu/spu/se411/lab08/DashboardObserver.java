package edu.spu.se411.lab08;

import java.util.Locale;

public class DashboardObserver implements Observer {
    @Override public void update(Subject subject) {
        if (subject instanceof Sensor) {
            Sensor sensor = (Sensor) subject;
            System.out.printf(Locale.ROOT, "Dashboard: %s = %.2f %s%n",
                    sensor.getName(), sensor.getReading(), sensor.getUnit());
        }
    }
}
