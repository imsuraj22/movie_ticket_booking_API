package com.movie.booking.movie_booking_api.repository;

import com.movie.booking.movie_booking_api.enitity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HallRepository extends JpaRepository<Hall, Long> {
    List<Hall> findByTheaterId(Long theaterId);
}
