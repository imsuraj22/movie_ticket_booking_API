package com.movie.booking.movie_booking_api.controller;

import com.movie.booking.movie_booking_api.dto.HallRequest;
import com.movie.booking.movie_booking_api.dto.HallResponse;
import com.movie.booking.movie_booking_api.enitity.Hall;
import com.movie.booking.movie_booking_api.service.HallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/halls")
public class HallController {

    @Autowired
    private HallService hallService;

    @GetMapping
    public ResponseEntity<List<Hall>> getAllHalls() {
        List<Hall> halls = hallService.getAllHalls();
        return ResponseEntity.ok(halls);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HallResponse> getHall(@PathVariable Long id) {
        Hall hall = hallService.getHallById(id);
        if (hall == null) {
            return ResponseEntity.notFound().build();
        }

        HallResponse response = new HallResponse(
                hall.getId(),
                hall.getName(),
                hall.getTheater(),
                hall.getSeats()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Hall> createHall(@RequestBody HallRequest request) {
        Hall hall = hallService.createHall(request);
        return ResponseEntity.ok(hall);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hall> updateHall(@PathVariable Long id, @RequestBody Hall hall) {
        Hall updatedHall = hallService.updateHall(id, hall);
        if (updatedHall == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedHall);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHall(@PathVariable Long id) {
        hallService.deleteHall(id);
        return ResponseEntity.noContent().build();
    }
}
