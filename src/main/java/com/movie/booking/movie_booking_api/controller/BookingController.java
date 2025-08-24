package com.movie.booking.movie_booking_api.controller;

import com.movie.booking.movie_booking_api.enitity.Booking;
import com.movie.booking.movie_booking_api.enitity.Seat;
import com.movie.booking.movie_booking_api.enitity.Show;
import com.movie.booking.movie_booking_api.service.BookingService;
import com.movie.booking.movie_booking_api.service.ShowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;
    private final ShowService showService;

    public BookingController(BookingService bookingService,ShowService showService) {
        this.bookingService = bookingService;
        this.showService=showService;
    }

    @PostMapping("/confirm/{showId}")
    public ResponseEntity<?> confirmBooking(@PathVariable Long showId,
                                            @RequestBody List<Long> seatIds) {
        // Try to book or get suggestions
        Map<String, Object> result = bookingService.confirmBookingWithSuggestions(showId, seatIds);

        // If booking succeeded
        if (result.containsKey("booking")) {
            Booking booking = (Booking) result.get("booking");

            Map<String, Object> hallInfo = Map.of(
                    "id", booking.getShow().getHall().getId(),
                    "name", booking.getShow().getHall().getName(),
                    "theater", Map.of(
                            "id", booking.getShow().getHall().getTheater().getId(),
                            "name", booking.getShow().getHall().getTheater().getName(),
                            "location", booking.getShow().getHall().getTheater().getLocation()
                    )
            );

            Map<String, Object> showInfo = Map.of(
                    "id", booking.getShow().getId(),
                    "movie", booking.getShow().getMovie(),
                    "hall", hallInfo,
                    "startTime", booking.getShow().getStartTime()
            );

            Map<String, Object> response = Map.of(
                    "id", booking.getId(),
                    "bookingTime", booking.getBookingTime(),
                    "cancelled", booking.isCancelled(),
                    "show", showInfo,
                    "bookedSeats", booking.getSeats().stream()
                            .map(Seat::getSeatLabel)
                            .toList()
            );

            return ResponseEntity.ok(response);

        } else {
            // Booking failed, return suggestions in clean format
            List<Map<String, Object>> rawSuggestions = (List<Map<String, Object>>) result.get("suggestions");
            List<Map<String, Object>> suggestions = rawSuggestions.stream().map(s -> {
                Show altShow = showService.getShowById((Long) s.get("showId"));
                if (altShow == null) return null;

                Map<String, Object> hallInfo = Map.of(
                        "id", altShow.getHall().getId(),
                        "name", altShow.getHall().getName(),
                        "theater", Map.of(
                                "id", altShow.getHall().getTheater().getId(),
                                "name", altShow.getHall().getTheater().getName(),
                                "location", altShow.getHall().getTheater().getLocation()
                        )
                );

                Map<String, Object> showInfo = Map.of(
                        "id", altShow.getId(),
                        "movie", altShow.getMovie(),
                        "hall", hallInfo,
                        "startTime", altShow.getStartTime(),
                        "availableSeats", s.get("availableSeats")
                );

                return showInfo;
            }).filter(Objects::nonNull).toList();

            return ResponseEntity.ok(Map.of(
                    "message", result.get("message"),
                    "suggestions", suggestions
            ));
        }
    }



    @PostMapping("/cancel/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
        boolean success = bookingService.cancelBooking(bookingId);
        if (success) {
            return ResponseEntity.ok("Booking cancelled successfully!");
        } else {
            return ResponseEntity.badRequest().body("Invalid booking ID or already cancelled.");
        }
    }
    @GetMapping("/{id}")
    public Map<String, Object> getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);

        Map<String, Object> hallInfo = Map.of(
                "id", booking.getShow().getHall().getId(),
                "name", booking.getShow().getHall().getName(),
                "theater", Map.of(
                        "id", booking.getShow().getHall().getTheater().getId(),
                        "name", booking.getShow().getHall().getTheater().getName(),
                        "location", booking.getShow().getHall().getTheater().getLocation()
                )
        );

        Map<String, Object> showInfo = Map.of(
                "id", booking.getShow().getId(),
                "movie", booking.getShow().getMovie(),
                "hall", hallInfo
        );

        return Map.of(
                "id", booking.getId(),
                "bookingTime", booking.getBookingTime(),
                "cancelled", booking.isCancelled(),
                "show", showInfo,
                "bookedSeats", booking.getSeats().stream()
                        .map(Seat::getSeatLabel)
                        .toList()
        );
    }

}
