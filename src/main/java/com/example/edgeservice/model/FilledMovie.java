package com.example.edgeservice.model;

import java.util.ArrayList;
import java.util.List;

public class FilledMovie {

    private String title;
    private List<Cast> casts;

    public FilledMovie(Movie movie, List<Cast> castList){
        setTitle(movie.getTitle());
        casts = new ArrayList<>();
        castList.forEach(cast->{
            casts.add(new Cast(cast.getMovieId(), cast.getiMDB(), cast.getCharacter(), cast.getFirstName(), cast.getLastName(), cast.getAge(), cast.getBirthPlace()));
        });
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Cast> getCasts() {
        return casts;
    }

    public void setCasts(List<Cast> casts) {
        this.casts = casts;
    }
}
