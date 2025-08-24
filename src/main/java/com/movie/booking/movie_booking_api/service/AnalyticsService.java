package com.movie.booking.movie_booking_api.service;

import com.movie.booking.movie_booking_api.enitity.Booking;
import com.movie.booking.movie_booking_api.enitity.Movie;
import com.movie.booking.movie_booking_api.repository.BookingRepository;
import com.movie.booking.movie_booking_api.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private final BookingRepository bookingRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public AnalyticsService(BookingRepository bookingRepository,
                            MovieRepository movieRepository) {
        this.bookingRepository = bookingRepository;
        this.movieRepository = movieRepository;
    }

    public Map<String, Object> getMovieBookingStats(Long movieId, LocalDate startDate, LocalDate endDate) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        // Fetch all bookings for this movie in the period
        List<Booking> bookings = bookingRepository.findByShow_Movie_IdAndBookingTimeBetween(
                movieId,
                startDate.atStartOfDay(),
                endDate.atTime(LocalTime.MAX)
        );

        int totalTickets = bookings.stream()
                .mapToInt(booking -> booking.getSeats().size())
                .sum();

        double gmv = totalTickets * movie.getPrice();

        return Map.of(
                "movie", movie.getTitle(),
                "period", startDate + " to " + endDate,
                "totalTicketsBooked", totalTickets,
                "gmv", gmv
        );
    }
}
