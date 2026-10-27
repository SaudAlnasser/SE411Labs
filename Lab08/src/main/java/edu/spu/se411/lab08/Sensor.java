package edu.spu.se411.lab08;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Objects;

/** Shared observable reading and independent clone subscriptions. */
public abstract class Sensor implements Subject, Cloneable {
    private Set<Observer> observers = new LinkedHashSet<>();
    private double reading;
    private boolean hasReading;

    public abstract String getName();
    public abstract String getUnit();
    public double getReading() { return reading; }

    public void setReading(double value) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Reading must be finite");
        }
        if (!hasReading || Double.compare(reading, value) != 0) {
            reading = value;
            hasReading = true;
            notifyObservers();
        }
    }

    @Override public void register(Observer o) {
        observers.add(Objects.requireNonNull(o, "observer"));
    }
    @Override public void unregister(Observer o) { observers.remove(o); }
    @Override public void notifyObservers() {
        // A callback may register or unregister without invalidating iteration.
        for (Observer observer : new LinkedHashSet<>(observers)) {
            observer.update(this);
        }
    }
    @Override public Sensor clone() {
        try {
            Sensor copy = (Sensor) super.clone();
            copy.observers = new LinkedHashSet<>();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}
