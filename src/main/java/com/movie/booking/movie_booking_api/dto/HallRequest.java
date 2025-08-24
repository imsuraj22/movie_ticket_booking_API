package com.movie.booking.movie_booking_api.dto;

import java.util.Map;

public class HallRequest {
    private String name;
    private int rows;
    private Map<Integer, Integer> rowSeatCounts; // key=rowNumber, value=seats
    private Long theaterId;

    // Getters & Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getRows() {
        return rows;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }

    public Map<Integer, Integer> getRowSeatCounts() {
        return rowSeatCounts;
    }
    public void setRowSeatCounts(Map<Integer, Integer> rowSeatCounts) {
        this.rowSeatCounts = rowSeatCounts;
    }

    public Long getTheaterId() {
        return theaterId;
    }
    public void setTheaterId(Long theaterId) {
        this.theaterId = theaterId;
    }
}
