package com.movie.booking.movie_booking_api.controller;

import com.movie.booking.movie_booking_api.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @Autowired
    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<?> getMovieStats(
            @PathVariable Long movieId,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        Map<String, Object> stats = analyticsService.getMovieBookingStats(movieId, start, end);
        return ResponseEntity.ok(stats);
    }
}
