package com.example.edgeservice.model;

import java.util.ArrayList;
import java.util.List;

public class FilledGenre {

    private String nameGenre;
    private List<Movie> movies;

    public FilledGenre(Genre genre, List<Movie> movieList) {
        setNameGenre(genre.getName());
        movies = new ArrayList<>();
        movieList.forEach(movie -> {
            movies.add(new Movie(movie.getTitle(), movie.getYear(), movie.getCategory(), movie.getMinutes(), movie.getImdbID()));
        });
        setMovies(movies);
    }

    public FilledGenre(Genre genre, Movie movie) {
        setNameGenre(genre.getName());
        movies = new ArrayList<>();
        movies.add(new Movie(movie.getTitle(), movie.getYear(), movie.getCategory(), movie.getMinutes(), movie.getImdbID()));
        setMovies(movies);
    }

    public String getNameGenre() {
        return nameGenre;
    }

    public void setNameGenre(String nameGenre) {
        this.nameGenre = nameGenre;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
