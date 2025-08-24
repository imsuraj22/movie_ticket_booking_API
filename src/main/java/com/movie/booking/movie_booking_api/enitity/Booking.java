package com.movie.booking.movie_booking_api.enitity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime bookingTime;

    @ManyToOne
    @JsonIgnoreProperties({"seats"})
    private Show show;

    @ManyToMany
    private List<Seat> seats;

    private boolean cancelled;

    public Booking() {
    }

    public Booking(Show show, List<Seat> seats) {
        this.show = show;
        this.seats = seats;
        this.bookingTime = LocalDateTime.now();
        this.cancelled = false;
    }

    // getters and setters
    public Long getId() { return id; }
    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }
    public Show getShow() { return show; }
    public void setShow(Show show) { this.show = show; }
    public List<Seat> getSeats() { return seats; }
    public void setSeats(List<Seat> seats) { this.seats = seats; }
    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
}
