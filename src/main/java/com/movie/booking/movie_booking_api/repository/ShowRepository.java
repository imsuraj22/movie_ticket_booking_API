package com.movie.booking.movie_booking_api.repository;

import com.movie.booking.movie_booking_api.enitity.Hall;
import com.movie.booking.movie_booking_api.enitity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByHallIn(List<Hall> halls);
    List<Show> findByMovieIdAndIdNot(Long movieId, Long showId);
}

