package com.movie.booking.movie_booking_api.repository;

import com.movie.booking.movie_booking_api.enitity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByShow_Movie_IdAndBookingTimeBetween(
            Long movieId,
            LocalDateTime start,
            LocalDateTime end
    );
}
