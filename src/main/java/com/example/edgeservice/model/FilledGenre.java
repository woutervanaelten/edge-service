package com.example.edgeservice.model;

import java.util.ArrayList;
import java.util.List;

public class FilledGenre {

    private Genre genre;
    private List<Movie> movies;

    public FilledGenre(Genre genre, List<Movie> movieList) {
        setGenre(genre);
        movies = new ArrayList<>();
        movieList.forEach(movie -> {
            movies.add(new Movie(movie.getId(), movie.getTitle(), movie.getYear(), movie.getCategory(), movie.getMinutes(), movie.getImdbID()));
        });
        setMovies(movies);
    }

    public FilledGenre(Genre genre, Movie movie) {
        setGenre(genre);
        movies = new ArrayList<>();
        movies.add(new Movie(movie.getId(), movie.getTitle(), movie.getYear(), movie.getCategory(), movie.getMinutes(), movie.getImdbID()));
        setMovies(movies);
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
