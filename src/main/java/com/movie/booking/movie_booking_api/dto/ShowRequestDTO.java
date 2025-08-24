package com.movie.booking.movie_booking_api.dto;

import java.time.LocalDateTime;

public class ShowRequestDTO {
    private Long movieId;
    private Long hallId;
    private LocalDateTime startTime;

    // constructors
    public ShowRequestDTO() {}

    public ShowRequestDTO(Long movieId, Long hallId, LocalDateTime startTime) {
        this.movieId = movieId;
        this.hallId = hallId;
        this.startTime = startTime;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getHallId() {
        return hallId;
    }

    public void setHallId(Long hallId) {
        this.hallId = hallId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
