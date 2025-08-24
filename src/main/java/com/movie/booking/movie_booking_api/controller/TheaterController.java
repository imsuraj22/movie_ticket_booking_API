package com.movie.booking.movie_booking_api.controller;

import com.movie.booking.movie_booking_api.enitity.Theater;
import com.movie.booking.movie_booking_api.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/theaters")
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

    @GetMapping
    public ResponseEntity<List<Theater>> getAllTheaters() {
        List<Theater> theaters = theaterService.getAllTheaters();
        return ResponseEntity.ok(theaters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTheaterById(@PathVariable Long id) {
        Theater theater = theaterService.getTheaterById(id);
        if (theater == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(theater);
    }

    @PostMapping
    public ResponseEntity<?> createTheater(@RequestBody Theater theater) {
        try {
            Theater createdTheater = theaterService.createTheater(theater);
            return ResponseEntity.ok(createdTheater);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTheater(@PathVariable Long id, @RequestBody Theater updatedTheater) {
        Theater theater = theaterService.updateTheater(id, updatedTheater);
        if (theater == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(theater);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheater(@PathVariable Long id) {
        theaterService.deleteTheater(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{theaterId}/movies")
    public ResponseEntity<?> getMoviesAndShows(@PathVariable Long theaterId) {
        try {
            List<Map<String, Object>> response = theaterService.getMoviesAndShows(theaterId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}
