package com.movie.booking.movie_booking_api.service;

import com.movie.booking.movie_booking_api.enitity.Booking;
import com.movie.booking.movie_booking_api.enitity.Seat;
import com.movie.booking.movie_booking_api.enitity.Show;
import com.movie.booking.movie_booking_api.repository.BookingRepository;
import com.movie.booking.movie_booking_api.repository.SeatRepository;
import com.movie.booking.movie_booking_api.repository.ShowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final SeatRepository seatRepository;
    private final ShowRepository showRepository;
    private final BookingRepository bookingRepository;

    public BookingService(SeatRepository seatRepository,
                          ShowRepository showRepository,
                          BookingRepository bookingRepository) {
        this.seatRepository = seatRepository;
        this.showRepository = showRepository;
        this.bookingRepository = bookingRepository;
    }

//    public Map<String, Object> confirmBookingWithSuggestions(Long showId, List<Long> seatIds) {
//        Show show = showRepository.findById(showId)
//                .orElseThrow(() -> new RuntimeException("Show not found"));
//
//        List<Seat> seatsToBook = seatRepository.findAllById(seatIds);
//
//        // Check if all seats are free
//        boolean allFree = seatsToBook.stream().allMatch(seat -> !seat.isBooked() && !seat.isReserved());
//
//        if (allFree) {
//            // Optionally check consecutiveness within same row
//            // If ok, mark booked and save
//            seatsToBook.forEach(seat -> seat.setBooked(true));
//            seatRepository.saveAll(seatsToBook);
//
//            Booking booking = new Booking();
//            booking.setShow(show);
//            booking.setSeats(seatsToBook);
//            booking.setBookingTime(LocalDateTime.now());
//            booking.setCancelled(false);
//            bookingRepository.save(booking);
//
//            return Map.of("booking", booking);
//        } else {
//            // Seats not available consecutively, suggest alternatives
//            List<Map<String, Object>> suggestions = new ArrayList<>();
//            List<Show> otherShows = showRepository.findByMovieIdAndIdNot(show.getMovie().getId(), show.getId());
//
//            for (Show altShow : otherShows) {
//                Map<Integer, List<Seat>> seatsByRow = altShow.getHall().getSeats().stream()
//                        .filter(seat -> !seat.isBooked() && !seat.isReserved())
//                        .collect(Collectors.groupingBy(Seat::getRowNumber));
//
//                for (List<Seat> rowSeats : seatsByRow.values()) {
//                    rowSeats.sort(Comparator.comparingInt(seat -> Integer.parseInt(seat.getSeatLabel().substring(1))));// ensure order
//                    List<Seat> consecutive = findConsecutiveSeats(rowSeats, seatIds.size());
//                    if (!consecutive.isEmpty()) {
//                        suggestions.add(Map.of(
//                                "showId", altShow.getId(),
//                                "startTime", altShow.getStartTime(),
//                                "availableSeats", consecutive.stream()
//                                        .map(Seat::getSeatLabel)
//                                        .toList()
//                        ));
//                    }
//                }
//            }
//
//            return Map.of(
//                    "message", "Requested seats cannot be booked together.",
//                    "suggestions", suggestions
//            );
//        }
//    }
//    private List<Seat> findConsecutiveSeats(List<Seat> rowSeats, int requiredCount) {
//        List<Seat> consecutive = new ArrayList<>();
//        for (int i = 0; i < rowSeats.size(); i++) {
//            if (rowSeats.get(i).isBooked() || rowSeats.get(i).isReserved()) {
//                consecutive.clear();
//            } else {
//                consecutive.add(rowSeats.get(i));
//                if (consecutive.size() == requiredCount) {
//                    return consecutive;
//                }
//            }
//        }
//        return Collections.emptyList();
//    }
//
@Transactional
public Map<String, Object> confirmBookingWithSuggestions(Long showId, List<Long> seatIds) {
    Show show = showRepository.findById(showId)
            .orElseThrow(() -> new RuntimeException("Show not found"));

    List<Seat> requestedSeats = seatRepository.findAllById(seatIds);

    // Check if all requested seats are free for this show
    boolean allFree = requestedSeats.stream()
            .noneMatch(seat -> show.getBookedSeats().contains(seat));

    if (allFree) {
        // Book seats by adding them to show.bookedSeats
        show.getBookedSeats().addAll(requestedSeats);
        showRepository.save(show);

        // Create booking
        Booking booking = new Booking();
        booking.setShow(show);
        booking.setSeats(requestedSeats);
        booking.setBookingTime(LocalDateTime.now());
        booking.setCancelled(false);
        bookingRepository.save(booking);

        return Map.of("booking", booking);
    } else {
        // Seats not available, find suggestions
        List<Map<String, Object>> suggestions = new ArrayList<>();

        // Include current show + other shows of same movie
        List<Show> otherShows = new ArrayList<>();
        otherShows.add(show);
        otherShows.addAll(showRepository.findByMovieIdAndIdNot(show.getMovie().getId(), show.getId()));

        for (Show altShow : otherShows) {
            // Group seats by row and filter out seats already booked for this show
            Map<Integer, List<Seat>> seatsByRow = altShow.getHall().getSeats().stream()
                    .filter(seat -> !altShow.getBookedSeats().contains(seat))
                    .collect(Collectors.groupingBy(Seat::getRowNumber));

            for (List<Seat> rowSeats : seatsByRow.values()) {
                // Sort rowSeats numerically by seat number
                rowSeats.sort(Comparator.comparingInt(seat -> {
                    String label = seat.getSeatLabel();
                    return Integer.parseInt(label.replaceAll("[^0-9]", ""));
                }));

                List<Seat> consecutive = findConsecutiveSeats(rowSeats, seatIds.size());
                if (!consecutive.isEmpty()) {
                    suggestions.add(Map.of(
                            "showId", altShow.getId(),
                            "startTime", altShow.getStartTime(),
                            "availableSeats", consecutive.stream()
                                    .map(Seat::getSeatLabel)
                                    .toList()
                    ));
                }
            }
        }

        return Map.of(
                "message", "Requested seats cannot be booked together.",
                "suggestions", suggestions
        );
    }
}

    // Helper: find consecutive free seats in a row
    private List<Seat> findConsecutiveSeats(List<Seat> rowSeats, int requiredCount) {
        List<Seat> consecutive = new ArrayList<>();
        for (Seat seat : rowSeats) {
            consecutive.add(seat);
            if (consecutive.size() == requiredCount) {
                return consecutive;
            }
        }
        return Collections.emptyList();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    }

    @Transactional
    public boolean cancelBooking(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isEmpty()) return false;

        Booking booking = bookingOpt.get();
        if (booking.isCancelled()) return false;

        // Free seats in the show
        Show show = booking.getShow();
        show.getBookedSeats().removeAll(booking.getSeats());
        showRepository.save(show);

        booking.setCancelled(true);
        bookingRepository.save(booking);

        return true;
    }

    private void rollbackReservations(List<Seat> reservedSeats) {
        for (Seat seat : reservedSeats) {
            seat.setReserved(false);
            seatRepository.save(seat);
        }
    }
}
