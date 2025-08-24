package com.movie.booking.movie_booking_api.dto;

import com.movie.booking.movie_booking_api.enitity.Seat;
import com.movie.booking.movie_booking_api.enitity.Theater;
import java.util.List;

public class HallResponse {
    private Long id;
    private String name;
    private Theater theater;
    private List<Seat> seats; // directly include Seat entities

    public HallResponse(Long id, String name, Theater theater, List<Seat> seats) {
        this.id = id;
        this.name = name;
        this.theater = theater;
        this.seats = seats;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public Theater getTheater() { return theater; }
    public List<Seat> getSeats() { return seats; }
}
