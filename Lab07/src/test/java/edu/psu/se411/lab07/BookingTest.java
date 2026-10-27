package edu.psu.se411.lab07;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookingTest {
    private final LocalDate date = LocalDate.of(2026, 11, 1);
    private FlightBooking flight() throws Exception {
        return new FlightBooking("F1", "Test Customer", date, "Jeddah", new BigDecimal("500"));
    }
    private TrainBooking train(SeatClass seat) throws Exception {
        return new TrainBooking("T1", "Test Customer", date, "Dammam", seat);
    }
    private CarRentalBooking car() throws Exception {
        return new CarRentalBooking("C1", "Test Customer", date, "Riyadh", new BigDecimal("150"));
    }
    private void price(String expected, Booking booking) throws Exception {
        assertEquals(0, new BigDecimal(expected).compareTo(App.computeTotalPrice(booking)));
    }

    @Test void pricesDispatchThroughBookingReferences() throws Exception {
        FlightBooking f = flight(); f.setLuggageWeight(20);
        TrainBooking s = train(SeatClass.STANDARD); s.setDistance(400);
        TrainBooking t = train(SeatClass.FIRST_CLASS); t.setDistance(400);
        CarRentalBooking c = car(); c.setRentalDays(3);
        Booking[] bookings = {f, s, t, c};
        String[] expected = {"600", "200", "320", "450"};
        for (int i = 0; i < bookings.length; i++) price(expected[i], bookings[i]);
    }
    @Test void missingValuesAreDistinctFromZero() throws Exception {
        for (Booking b : new Booking[]{flight(), train(SeatClass.STANDARD), car()})
            assertThrows(MissingInformationException.class, () -> App.computeTotalPrice(b));
        FlightBooking f = flight(); f.setLuggageWeight(0); price("500", f);
    }
    @Test void luggageBoundariesAndInvalidValues() throws Exception {
        FlightBooking f = flight();
        f.setLuggageWeight(40); price("700", f);
        f.setLuggageWeight(0); price("500", f);
        for (double value : new double[]{-0.1, 40.1, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
            assertThrows(InvalidArgumentException.class, () -> f.setLuggageWeight(value));
        price("500", f);
    }
    @Test void distanceBoundariesForBothSeatClasses() throws Exception {
        for (SeatClass seat : SeatClass.values()) {
            TrainBooking t = train(seat);
            t.setDistance(2000); price(seat == SeatClass.STANDARD ? "1000" : "1600", t);
            t.setDistance(1); price(seat == SeatClass.STANDARD ? "0.5" : "0.8", t);
            for (double value : new double[]{0, -1, 2000.1, Double.NaN, Double.POSITIVE_INFINITY})
                assertThrows(InvalidArgumentException.class, () -> t.setDistance(value));
            price(seat == SeatClass.STANDARD ? "0.5" : "0.8", t);
        }
    }
    @Test void rentalDayBoundariesAndFailedUpdates() throws Exception {
        CarRentalBooking c = car();
        c.setRentalDays(30); price("4500", c);
        c.setRentalDays(1); price("150", c);
        for (int value : new int[]{-1, 0, 31})
            assertThrows(InvalidArgumentException.class, () -> c.setRentalDays(value));
        price("150", c);
    }
    @Test void fractionalWeightsAndDistances() throws Exception {
        FlightBooking f = flight(); f.setLuggageWeight(1.25); price("506.25", f);
        TrainBooking t = train(SeatClass.FIRST_CLASS); t.setDistance(1.5); price("1.20", t);
    }
    @Test void invalidFirstUpdatesLeaveValuesMissing() throws Exception {
        FlightBooking f = flight(); TrainBooking t = train(SeatClass.STANDARD); CarRentalBooking c = car();
        assertThrows(InvalidArgumentException.class, () -> f.setLuggageWeight(41));
        assertThrows(InvalidArgumentException.class, () -> t.setDistance(2001));
        assertThrows(InvalidArgumentException.class, () -> c.setRentalDays(31));
        for (Booking b : new Booking[]{f, t, c})
            assertThrows(MissingInformationException.class, b::calculateTotalPrice);
    }
    @Test void validatesSharedFieldsAndPreservesMetadata() throws Exception {
        assertThrows(InvalidArgumentException.class, () -> new FlightBooking(" ", "Customer", date, "City", BigDecimal.ONE));
        assertThrows(InvalidArgumentException.class, () -> new FlightBooking("F", null, date, "City", BigDecimal.ONE));
        assertThrows(InvalidArgumentException.class, () -> new FlightBooking("F", "Customer", null, "City", BigDecimal.ONE));
        assertThrows(InvalidArgumentException.class, () -> new FlightBooking("F", "Customer", date, " ", BigDecimal.ONE));
        Booking b = flight();
        assertEquals("F1", b.getBookingId()); assertEquals("Test Customer", b.getCustomerFullName());
        assertEquals(date, b.getTravelDate()); assertEquals("Jeddah", b.getDestinationCity());
    }
    @Test void validatesRatesAndSeatClass() throws Exception {
        for (BigDecimal rate : new BigDecimal[]{null, new BigDecimal("-1")}) {
            assertThrows(InvalidArgumentException.class, () -> new FlightBooking("F", "Customer", date, "City", rate));
            assertThrows(InvalidArgumentException.class, () -> new CarRentalBooking("C", "Customer", date, "City", rate));
        }
        assertThrows(InvalidArgumentException.class, () -> train(null));
        FlightBooking f = new FlightBooking("F", "Customer", date, "City", BigDecimal.ZERO);
        f.setLuggageWeight(0); price("0", f);
        CarRentalBooking c = new CarRentalBooking("C", "Customer", date, "City", BigDecimal.ZERO);
        c.setRentalDays(1); price("0", c);
    }
}
