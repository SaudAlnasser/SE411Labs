package edu.psu.se411.lab07;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private App() { }

    public static BigDecimal computeTotalPrice(Booking booking) throws MissingInformationException {
        return booking.calculateTotalPrice();
    }

    public static void main(String[] args) {
        LOGGER.info("Application is starting...");
        try {
            LocalDate date = LocalDate.of(2026, 11, 1);
            FlightBooking flight = new FlightBooking("F001", "Saud Alnasser", date, "Jeddah", new BigDecimal("500.00"));
            TrainBooking standard = new TrainBooking("T001", "Saud Alnasser", date, "Dammam", SeatClass.STANDARD);
            TrainBooking firstClass = new TrainBooking("T002", "Saud Alnasser", date, "Dammam", SeatClass.FIRST_CLASS);
            CarRentalBooking car = new CarRentalBooking("C001", "Saud Alnasser", date, "Riyadh", new BigDecimal("150.00"));

            // Demonstrate the required exceptions and allow the program to continue.
            try {
                computeTotalPrice(flight);
            } catch (MissingInformationException ex) {
                LOGGER.warn("Missing booking information", ex);
                System.out.println("Expected missing information: " + ex.getMessage());
            }
            try {
                car.setRentalDays(31);
            } catch (InvalidArgumentException ex) {
                LOGGER.warn("Invalid booking argument", ex);
                System.out.println("Expected invalid argument: " + ex.getMessage());
            }

            flight.setLuggageWeight(20);
            standard.setDistance(400);
            firstClass.setDistance(400);
            car.setRentalDays(3);
            Booking[] bookings = {flight, standard, firstClass, car};
            for (Booking booking : bookings) {
                System.out.printf("%s | %s | %s | %s | %s | Total: %s%n",
                        booking.getBookingId(), booking.getClass().getSimpleName(),
                        booking.getCustomerFullName(), booking.getTravelDate(), booking.getDestinationCity(),
                        computeTotalPrice(booking).setScale(2, RoundingMode.HALF_UP));
            }
        } catch (InvalidArgumentException | MissingInformationException ex) {
            LOGGER.error("Booking demonstration failed", ex);
            throw new IllegalStateException("Booking demonstration failed", ex);
        } finally {
            LOGGER.info("Application is stopping...");
        }
    }
}
