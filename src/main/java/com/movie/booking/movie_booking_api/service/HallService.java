package com.movie.booking.movie_booking_api.service;

import com.movie.booking.movie_booking_api.dto.HallRequest;
import com.movie.booking.movie_booking_api.enitity.Hall;
import com.movie.booking.movie_booking_api.enitity.Seat;
import com.movie.booking.movie_booking_api.enitity.Theater;
import com.movie.booking.movie_booking_api.repository.HallRepository;
import com.movie.booking.movie_booking_api.repository.SeatRepository;
import com.movie.booking.movie_booking_api.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class HallService {

    private final HallRepository hallRepository;
    private final SeatRepository seatRepository;
    private final TheaterRepository theaterRepository;

    @Autowired
    public HallService(HallRepository hallRepository, SeatRepository seatRepository, TheaterRepository theaterRepository) {
        this.hallRepository = hallRepository;
        this.seatRepository = seatRepository;
        this.theaterRepository = theaterRepository;
    }

    public List<Hall> getAllHalls() {
        return hallRepository.findAll();
    }

    public Hall getHallById(Long id) {
        return hallRepository.findById(id).orElse(null);
    }

    public Hall createHall(HallRequest request) {
        Theater theater = theaterRepository.findById(request.getTheaterId())
                .orElseThrow(() -> new RuntimeException("Theater not found"));

        Hall hall = new Hall();
        hall.setName(request.getName());
        hall.setTheater(theater);

        Hall savedHall = hallRepository.save(hall);

        // Generate seats row by row
        List<Seat> allSeats = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : request.getRowSeatCounts().entrySet()) {
            int rowNum = entry.getKey();
            int seatCount = entry.getValue();
            if (seatCount < 6) {
                throw new IllegalArgumentException("Each row must have at least 6 seats");
            }
            List<Seat> rowSeats = generateRowSeats(rowNum, seatCount, savedHall);
            allSeats.addAll(rowSeats);
        }

        seatRepository.saveAll(allSeats);
        savedHall.setSeats(allSeats);

        return savedHall;
    }

    private List<Seat> generateRowSeats(int rowNumber, int seatCount, Hall hall) {
        List<Seat> seats = new ArrayList<>();

        int baseSize = seatCount / 3;
        int remainder = seatCount % 3;

        int[] columnSizes = new int[3];
        for (int i = 0; i < 3; i++) {
            columnSizes[i] = baseSize + (remainder-- > 0 ? 1 : 0);
        }

        int seatNumberCounter = 1;
        for (int col = 0; col < 3; col++) {
            for (int i = 0; i < columnSizes[col]; i++) {
                boolean isAisle = (i == 0 || i == columnSizes[col] - 1);
                String seatLabel = (char)('A' + rowNumber - 1) + String.valueOf(seatNumberCounter++);

                Seat seat = new Seat();
                seat.setHall(hall);
                seat.setRowNumber(rowNumber);
                seat.setSeatLabel(seatLabel);
                seat.setAisle(isAisle);
                seat.setBooked(false);
                seat.setReserved(false);

                seats.add(seat);
            }
        }

        return seats;
    }

    public Hall updateHall(Long id, Hall updatedHall) {
        return hallRepository.findById(id).map(hall -> {
            hall.setName(updatedHall.getName());
            // Updating seats should be handled separately if needed
            return hallRepository.save(hall);
        }).orElse(null);
    }

    public void deleteHall(Long id) {
        hallRepository.deleteById(id);
    }
}
