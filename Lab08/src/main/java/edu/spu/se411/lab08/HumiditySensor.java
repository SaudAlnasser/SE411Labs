package edu.spu.se411.lab08;

public class HumiditySensor extends Sensor {
    @Override public String getName() { return "Humidity"; }
    @Override public String getUnit() { return "%"; }
    @Override public HumiditySensor clone() { return (HumiditySensor) super.clone(); }
}
