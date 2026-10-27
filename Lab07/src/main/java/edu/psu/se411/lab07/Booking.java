package edu.psu.se411.lab07;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Booking {
    private final String bookingId;
    private final String customerFullName;
    private final LocalDate travelDate;
    private final String destinationCity;

    protected Booking(String bookingId, String customerFullName,
                      LocalDate travelDate, String destinationCity) throws InvalidArgumentException {
        this.bookingId = requireText(bookingId, "Booking ID");
        this.customerFullName = requireText(customerFullName, "Customer full name");
        if (travelDate == null) throw new InvalidArgumentException("Travel date is required.");
        this.travelDate = travelDate;
        this.destinationCity = requireText(destinationCity, "Destination city");
    }

    private static String requireText(String value, String name) throws InvalidArgumentException {
        if (value == null || value.trim().isEmpty())
            throw new InvalidArgumentException(name + " is required.");
        return value.trim();
    }

    protected static BigDecimal requireRate(BigDecimal value, String name) throws InvalidArgumentException {
        if (value == null || value.signum() < 0)
            throw new InvalidArgumentException(name + " must be non-negative.");
        return value;
    }

    protected static void requireRange(double value, double min, double max, String name)
            throws InvalidArgumentException {
        if (!Double.isFinite(value) || value < min || value > max)
            throw new InvalidArgumentException(name + " must be between " + min + " and " + max + ".");
    }

    public String getBookingId() { return bookingId; }
    public String getCustomerFullName() { return customerFullName; }
    public LocalDate getTravelDate() { return travelDate; }
    public String getDestinationCity() { return destinationCity; }

    public abstract BigDecimal calculateTotalPrice() throws MissingInformationException;
}
