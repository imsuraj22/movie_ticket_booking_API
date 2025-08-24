package com.movie.booking.movie_booking_api.controller;

import com.movie.booking.movie_booking_api.dto.ShowRequestDTO;
import com.movie.booking.movie_booking_api.enitity.Seat;
import com.movie.booking.movie_booking_api.enitity.Show;
import com.movie.booking.movie_booking_api.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    @Autowired
    private ShowService showService;

    @GetMapping
    public ResponseEntity<List<Show>> getAllShows() {
        List<Show> shows = showService.getAllShows();
        return ResponseEntity.ok(shows);
    }

    @GetMapping("/{showId}")
    public ResponseEntity<?> getShow(@PathVariable Long showId) {
        Show show = showService.getShowByIdForResponse(showId);
        if (show == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> hallInfo = Map.of(
                "id", show.getHall().getId(),
                "name", show.getHall().getName(),
                "theater", Map.of(
                        "id", show.getHall().getTheater().getId(),
                        "name", show.getHall().getTheater().getName(),
                        "location", show.getHall().getTheater().getLocation()
                ),
                "seats", show.getHall().getSeats().stream().map(seat -> Map.of(
                        "id", seat.getId(),
                        "rowNumber", seat.getRowNumber(),
                        "seatLabel", seat.getSeatLabel(),
                        "aisle", seat.isAisle(),
                        "reserved", seat.isReserved()
                )).toList()
        );

        Map<String, Object> response = Map.of(
                "id", show.getId(),
                "movie", show.getMovie(),
                "hall", hallInfo,
                "startTime", show.getStartTime(),
                "bookedSeats", show.getBookedSeats().stream()
                        .map(Seat::getSeatLabel)
                        .toList()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createShow(@RequestBody ShowRequestDTO dto) {
        try {
            Show show = showService.createShow(dto);
            return ResponseEntity.ok(show);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateShow(@PathVariable Long id, @RequestBody ShowRequestDTO dto) {
        try {
            Show updatedShow = showService.updateShow(id, dto);
            if (updatedShow == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedShow);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShow(@PathVariable Long id) {
        showService.deleteShow(id);
        return ResponseEntity.noContent().build();
    }
}
