package edu.spu.se411.lab08;

public class TemperatureSensor extends Sensor {
    @Override public String getName() { return "Temperature"; }
    @Override public String getUnit() { return "C"; }
    @Override public TemperatureSensor clone() { return (TemperatureSensor) super.clone(); }
}
