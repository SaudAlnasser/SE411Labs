package edu.spu.se411.lab08;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.atomic.AtomicInteger;

class SensorTest {
    @Test void bothSensorsNotifyBothObserversOnChangesOnly() {
        for (Sensor sensor : new Sensor[] {new TemperatureSensor(), new HumiditySensor()}) {
            AtomicInteger first = new AtomicInteger();
            AtomicInteger second = new AtomicInteger();
            sensor.register(subject -> { assertSame(sensor, subject); first.incrementAndGet(); });
            sensor.register(subject -> second.incrementAndGet());
            sensor.setReading(0);
            sensor.setReading(0);
            sensor.setReading(12.5);
            assertEquals(2, first.get());
            assertEquals(2, second.get());
        }
    }
    @Test void unregisterAndDuplicateRegistration() {
        Sensor sensor = new TemperatureSensor();
        AtomicInteger calls = new AtomicInteger();
        Observer observer = subject -> calls.incrementAndGet();
        sensor.register(observer);
        sensor.register(observer);
        sensor.setReading(1);
        sensor.unregister(observer);
        sensor.setReading(2);
        assertEquals(1, calls.get());
    }
    @Test void clonesKeepValueAndTypeButHaveIndependentObservers() {
        for (Sensor original : new Sensor[] {new TemperatureSensor(), new HumiditySensor()}) {
            AtomicInteger originalCalls = new AtomicInteger();
            AtomicInteger cloneCalls = new AtomicInteger();
            original.setReading(20);
            original.register(subject -> originalCalls.incrementAndGet());
            Sensor clone = original.clone();
            assertNotSame(original, clone);
            assertEquals(original.getClass(), clone.getClass());
            assertEquals(20, clone.getReading());
            clone.setReading(21);
            assertEquals(0, originalCalls.get());
            clone.register(subject -> cloneCalls.incrementAndGet());
            clone.setReading(22);
            original.setReading(23);
            assertEquals(1, cloneCalls.get());
            assertEquals(1, originalCalls.get());
            assertEquals(22, clone.getReading());
        }
    }
    @Test void callbacksCanUnregister() {
        Sensor sensor = new HumiditySensor();
        AtomicInteger calls = new AtomicInteger();
        Observer observer = new Observer() {
            public void update(Subject subject) {
                calls.incrementAndGet(); subject.unregister(this);
            }
        };
        sensor.register(observer);
        sensor.setReading(40);
        sensor.setReading(41);
        assertEquals(1, calls.get());
    }
    @Test void invalidReadingsAndNullObserversAreRejected() {
        Sensor sensor = new TemperatureSensor();
        sensor.setReading(10);
        for (double value : new double[] {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}) {
            assertThrows(IllegalArgumentException.class, () -> sensor.setReading(value));
        }
        assertEquals(10, sensor.getReading());
        assertThrows(NullPointerException.class, () -> sensor.register(null));
    }
}
