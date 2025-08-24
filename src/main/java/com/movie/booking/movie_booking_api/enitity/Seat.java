package com.movie.booking.movie_booking_api.enitity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rowNumber;
    private String seatLabel;
    private boolean booked;
    private boolean isAisle;
    private boolean reserved;


    @ManyToOne
    @JsonBackReference
    private Hall hall;
    public Seat(int rowNumber, String seatLabel, boolean isAisle, boolean booked, Hall hall) {
        this.rowNumber = rowNumber;
        this.seatLabel = seatLabel;
        this.isAisle = isAisle;
        this.booked = booked;
        this.hall = hall;
        this.reserved=false;
    }
    public Seat() {}

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public boolean isAisle() {
        return isAisle;
    }

    public void setAisle(boolean aisle) {
        isAisle = aisle;
    }

    public Long getId() {
        return id;
    }


    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getSeatLabel() {
        return seatLabel;
    }

    public void setSeatLabel(String seatLabel) {
        this.seatLabel = seatLabel;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }
}

