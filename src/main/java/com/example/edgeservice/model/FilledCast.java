package com.example.edgeservice.model;

import java.util.ArrayList;
import java.util.List;

public class FilledCast {
    private String nameCast;
    private List<Movie> movies;

    public FilledCast(Cast cast, List<Movie> movieList){
        setNameCast(cast.getFirstName() + " " + cast.getLastName() + " speelt " + cast.getCharacter());
        movies = new ArrayList<>();
        movieList.forEach(movie -> {
            movies.add(new Movie(movie.getTitle(), movie.getYear(), movie.getCategory(), movie.getMinutes(), movie.getImdbID()));
        });
        setMovies(movies);
    }

    public FilledCast() {
    }

    public String getNameCast() {
        return nameCast;
    }

    public void setNameCast(String nameCast) {
        this.nameCast = nameCast;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
