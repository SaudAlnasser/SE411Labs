package edu.spu.se411.lab08;

import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerObserver implements Observer {
    private static final Logger logger = LoggerFactory.getLogger(LoggerObserver.class);

    @Override public void update(Subject subject) {
        if (subject instanceof Sensor) {
            Sensor sensor = (Sensor) subject;
            System.out.printf(Locale.ROOT, "Logger: %s = %.2f %s%n",
                    sensor.getName(), sensor.getReading(), sensor.getUnit());
            logger.info("{} reading changed to {} {}", sensor.getName(),
                    sensor.getReading(), sensor.getUnit());
        }
    }
}
