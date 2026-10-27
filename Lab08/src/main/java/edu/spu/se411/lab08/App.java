package edu.spu.se411.lab08;

import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private App() { }

    public static void main(String[] args) {
        logger.info("Application is starting...");
        TemperatureSensor temp = new TemperatureSensor();
        HumiditySensor humidity = new HumiditySensor();
        Observer dashboard = new DashboardObserver();
        Observer readingsLogger = new LoggerObserver();
        for (Sensor sensor : new Sensor[] {temp, humidity}) {
            sensor.register(dashboard);
            sensor.register(readingsLogger);
        }

        Random random = new Random();
        int iteration = 0;
        while (iteration < 10) {
            temp.setReading(20 + random.nextDouble() * 15);
            humidity.setReading(40 + random.nextDouble() * 20);
            iteration++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Simulation interrupted", e);
                break;
            }
        }

        TemperatureSensor clonedTemp = temp.clone();
        System.out.println("Clone changes silently before it has observers:");
        clonedTemp.setReading(25);
        System.out.println("Clone now has its own dashboard observer:");
        clonedTemp.register(new DashboardObserver());
        clonedTemp.setReading(26);
        logger.info("Application is stopping...");
    }
}
