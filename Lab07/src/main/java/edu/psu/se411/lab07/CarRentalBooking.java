package edu.psu.se411.lab07;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class CarRentalBooking extends Booking {
    private final BigDecimal dailyRentalRate;
    private Integer rentalDays;

    public CarRentalBooking(String id, String customer, LocalDate date, String destination,
                            BigDecimal dailyRentalRate) throws InvalidArgumentException {
        super(id, customer, date, destination);
        this.dailyRentalRate = requireRate(dailyRentalRate, "Daily rental rate");
    }

    public void setRentalDays(int days) throws InvalidArgumentException {
        requireRange(days, 1, Configuration.MAX_RENTAL_DAYS, "Rental days");
        rentalDays = days;
    }

    @Override
    public BigDecimal calculateTotalPrice() throws MissingInformationException {
        if (rentalDays == null) throw new MissingInformationException("Rental days are missing.");
        return dailyRentalRate.multiply(BigDecimal.valueOf(rentalDays));
    }
}
