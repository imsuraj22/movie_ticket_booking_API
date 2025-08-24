package com.movie.booking.movie_booking_api.service;

import com.movie.booking.movie_booking_api.enitity.Movie;
import com.movie.booking.movie_booking_api.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie updateMovie(Long id, Movie updatedMovie) {
        return movieRepository.findById(id).map(movie -> {
            movie.setTitle(updatedMovie.getTitle());
            movie.setPrice(updatedMovie.getPrice());
            movie.setDuration(updatedMovie.getDuration());
            return movieRepository.save(movie);
        }).orElse(null);
    }
    public Movie getMovieByName(String name) {
        return movieRepository.findByTitleContainingIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("Movie not found with name: " + name));
    }


    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
}
