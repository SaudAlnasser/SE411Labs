package edu.psu.se411.lab07;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class TrainBooking extends Booking {
    private final SeatClass seatClass;
    private Double distance;

    public TrainBooking(String id, String customer, LocalDate date, String destination,
                        SeatClass seatClass) throws InvalidArgumentException {
        super(id, customer, date, destination);
        if (seatClass == null) throw new InvalidArgumentException("Seat class is required.");
        this.seatClass = seatClass;
    }

    public void setDistance(double kilometers) throws InvalidArgumentException {
        requireRange(kilometers, 1, Configuration.MAX_TRAIN_DISTANCE, "Distance (km)");
        distance = kilometers;
    }

    @Override
    public BigDecimal calculateTotalPrice() throws MissingInformationException {
        if (distance == null) throw new MissingInformationException("Train distance is missing.");
        BigDecimal rate = seatClass == SeatClass.STANDARD
                ? Configuration.TRAIN_STANDARD_RATE : Configuration.TRAIN_FIRST_CLASS_RATE;
        return BigDecimal.valueOf(distance).multiply(rate);
    }
}
