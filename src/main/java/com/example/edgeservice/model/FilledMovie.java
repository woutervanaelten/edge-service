package com.example.edgeservice.model;

import java.util.ArrayList;
import java.util.List;

public class FilledMovie {

    private Movie movie;
    private List<Cast> casts;

    public FilledMovie(Movie movie, List<Cast> castList){
        setMovie(movie);
        casts = new ArrayList<>();
        castList.forEach(cast->{
            casts.add(new Cast(cast.getId(), cast.getMovieId(), cast.getiMDB(), cast.getCharacter(), cast.getFirstName(), cast.getLastName(), cast.getAge(), cast.getBirthPlace()));
        });
        setCasts(casts);
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public List<Cast> getCasts() {
        return casts;
    }

    public void setCasts(List<Cast> casts) {
        this.casts = casts;
    }
}
