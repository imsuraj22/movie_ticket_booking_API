package com.movie.booking.movie_booking_api.service;

import com.movie.booking.movie_booking_api.enitity.Seat;
import com.movie.booking.movie_booking_api.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    public Seat getSeatById(Long id) {
        return seatRepository.findById(id).orElse(null);
    }

    public Seat createSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    public Seat updateSeat(Long id, Seat updatedSeat) {
        return seatRepository.findById(id).map(seat -> {
            seat.setRowNumber(updatedSeat.getRowNumber());
            seat.setSeatLabel(updatedSeat.getSeatLabel());
            seat.setBooked(updatedSeat.isBooked());
            return seatRepository.save(seat);
        }).orElse(null);
    }

    public void deleteSeat(Long id) {
        seatRepository.deleteById(id);
    }
}
