package com.movie.booking.movie_booking_api.repository;

import com.movie.booking.movie_booking_api.enitity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByTitleContainingIgnoreCase(String name);

}