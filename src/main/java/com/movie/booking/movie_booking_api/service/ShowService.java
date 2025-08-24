package com.movie.booking.movie_booking_api.service;

import com.movie.booking.movie_booking_api.dto.ShowRequestDTO;
import com.movie.booking.movie_booking_api.enitity.Hall;
import com.movie.booking.movie_booking_api.enitity.Movie;
import com.movie.booking.movie_booking_api.enitity.Seat;
import com.movie.booking.movie_booking_api.enitity.Show;
import com.movie.booking.movie_booking_api.repository.HallRepository;
import com.movie.booking.movie_booking_api.repository.MovieRepository;
import com.movie.booking.movie_booking_api.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ShowService {

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;

    public ShowService(ShowRepository showRepository,
                       MovieRepository movieRepository,
                       HallRepository hallRepository) {
        this.showRepository = showRepository;
        this.movieRepository = movieRepository;
        this.hallRepository = hallRepository;
    }

    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    public Show getShowById(Long id) {
        return showRepository.findById(id).orElse(null);
    }

    public Show createShow(ShowRequestDTO dto) {
        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        Hall hall = hallRepository.findById(dto.getHallId())
                .orElseThrow(() -> new RuntimeException("Hall not found"));

        Show show = new Show();
        show.setMovie(movie);
        show.setHall(hall);
        show.setStartTime(dto.getStartTime());

        return showRepository.save(show);
    }

    public Show updateShow(Long id, ShowRequestDTO dto) {
        return showRepository.findById(id).map(show -> {

            Movie movie = movieRepository.findById(dto.getMovieId())
                    .orElseThrow(() -> new RuntimeException("Movie not found"));

            Hall hall = hallRepository.findById(dto.getHallId())
                    .orElseThrow(() -> new RuntimeException("Hall not found"));

            show.setMovie(movie);
            show.setHall(hall);
            show.setStartTime(dto.getStartTime());

            return showRepository.save(show);

        }).orElseThrow(() -> new RuntimeException("Show not found"));
    }


    public void deleteShow(Long id) {
        showRepository.deleteById(id);
    }

    public Show getShowByIdForResponse(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));

        // Copy hall to avoid modifying the DB entity
        Hall hallCopy = new Hall();
        hallCopy.setId(show.getHall().getId());
        hallCopy.setName(show.getHall().getName());
        hallCopy.setTheater(show.getHall().getTheater());

        // Only include seats not booked for this show
        List<Seat> availableSeats = show.getHall().getSeats().stream()
                .filter(seat -> !show.getBookedSeats().contains(seat))
                .toList();
        hallCopy.setSeats(availableSeats);

        // Replace hall in show for response
        show.setHall(hallCopy);

        return show;
    }

}
