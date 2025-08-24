package com.movie.booking.movie_booking_api.repository;

import com.movie.booking.movie_booking_api.enitity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> { }
