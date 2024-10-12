package core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BookingCreate {
    private int bookingid;
    private Booking booking;

    @JsonCreator
    public BookingCreate(
            @JsonProperty("bookingid") int bookingid,
            @JsonProperty("booking") Booking booking) {

        this.bookingid = bookingid;
        this.booking = booking;
    }

    public int getBookingid() {
        return bookingid;
    }

    public Booking getBooking() {
        return booking;
    }
}