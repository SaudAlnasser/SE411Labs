package edu.psu.se411.lab07;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class FlightBooking extends Booking {
    private final BigDecimal baseTicketPrice;
    private Double luggageWeight;

    public FlightBooking(String id, String customer, LocalDate date, String destination,
                         BigDecimal baseTicketPrice) throws InvalidArgumentException {
        super(id, customer, date, destination);
        this.baseTicketPrice = requireRate(baseTicketPrice, "Base ticket price");
    }

    public void setLuggageWeight(double weight) throws InvalidArgumentException {
        requireRange(weight, 0, Configuration.MAX_LUGGAGE_WEIGHT, "Luggage weight (kg)");
        luggageWeight = weight;
    }

    @Override
    public BigDecimal calculateTotalPrice() throws MissingInformationException {
        if (luggageWeight == null) throw new MissingInformationException("Luggage weight is missing.");
        return baseTicketPrice.add(BigDecimal.valueOf(luggageWeight).multiply(Configuration.EXTRA_LUGGAGE_RATE));
    }
}
