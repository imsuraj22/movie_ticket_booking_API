package com.movie.booking.movie_booking_api.service;

import com.movie.booking.movie_booking_api.enitity.Hall;
import com.movie.booking.movie_booking_api.enitity.Show;
import com.movie.booking.movie_booking_api.enitity.Theater;
import com.movie.booking.movie_booking_api.repository.HallRepository;
import com.movie.booking.movie_booking_api.repository.ShowRepository;
import com.movie.booking.movie_booking_api.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TheaterService {

    private final TheaterRepository theaterRepository;
    private final HallRepository hallRepository;
    private final ShowRepository showRepository;

    public TheaterService(TheaterRepository theaterRepository,
                          HallRepository hallRepository,
                          ShowRepository showRepository) {
        this.theaterRepository = theaterRepository;
        this.hallRepository = hallRepository;
        this.showRepository = showRepository;
    }

    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }

    public Theater getTheaterById(Long id) {
        return theaterRepository.findById(id).orElse(null);
    }

    public Theater createTheater(Theater theater) {
        return theaterRepository.save(theater);
    }

    public Theater updateTheater(Long id, Theater updatedTheater) {
        return theaterRepository.findById(id).map(theater -> {
            theater.setName(updatedTheater.getName());
            theater.setLocation(updatedTheater.getLocation());
            return theaterRepository.save(theater);
        }).orElse(null);
    }

    public void deleteTheater(Long id) {
        theaterRepository.deleteById(id);
    }
    public List<Map<String, Object>> getMoviesAndShows(Long theaterId) {
        Optional<Theater> theaterOpt = theaterRepository.findById(theaterId);
        if (theaterOpt.isEmpty()) {
            throw new RuntimeException("Theater not found");
        }

        List<Hall> halls = hallRepository.findByTheaterId(theaterId);
        if (halls.isEmpty()) {
            return Collections.emptyList();
        }

        List<Show> shows = showRepository.findByHallIn(halls);

        // Group shows by movie
        Map<String, List<Map<String, Object>>> movieShowsMap = new HashMap<>();
        for (Show show : shows) {
            String movieName = show.getMovie().getTitle();

            Map<String, Object> showInfo = new HashMap<>();
            showInfo.put("showId", show.getId());
            showInfo.put("hallName", show.getHall().getName());
            showInfo.put("showTime", show.getStartTime());

            movieShowsMap.computeIfAbsent(movieName, k -> new ArrayList<>()).add(showInfo);
        }

        // Prepare response
        List<Map<String, Object>> response = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : movieShowsMap.entrySet()) {
            Map<String, Object> movieMap = new HashMap<>();
            movieMap.put("movieName", entry.getKey());
            movieMap.put("shows", entry.getValue());
            response.add(movieMap);
        }

        return response;
    }
}
