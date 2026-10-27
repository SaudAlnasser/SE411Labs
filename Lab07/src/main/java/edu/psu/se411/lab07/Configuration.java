package edu.psu.se411.lab07;

import java.math.BigDecimal;

/** Shared rates are illustrative amounts in one consistent currency. */
public final class Configuration {
    public static final BigDecimal EXTRA_LUGGAGE_RATE = new BigDecimal("5.00");
    public static final BigDecimal TRAIN_STANDARD_RATE = new BigDecimal("0.50");
    public static final BigDecimal TRAIN_FIRST_CLASS_RATE = new BigDecimal("0.80");
    public static final int MAX_LUGGAGE_WEIGHT = 40;
    public static final int MAX_TRAIN_DISTANCE = 2000;
    public static final int MAX_RENTAL_DAYS = 30;
    private Configuration() { }
}
