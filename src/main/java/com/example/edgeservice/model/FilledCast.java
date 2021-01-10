package com.example.edgeservice.model;

import java.util.ArrayList;
import java.util.List;

public class FilledCast {
    private Cast cast;
    private List<Movie> movies;

    public FilledCast(Cast cast, List<Movie> movieList){
        setCast(cast);
        movies = new ArrayList<>();
        movieList.forEach(movie -> {
            movies.add(new Movie(movie.getId(), movie.getTitle(), movie.getYear(), movie.getCategory(), movie.getMinutes(), movie.getImdbID()));
        });
        setMovies(movies);
    }

    public Cast getCast() {
        return cast;
    }

    public void setCast(Cast cast) {
        this.cast = cast;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
