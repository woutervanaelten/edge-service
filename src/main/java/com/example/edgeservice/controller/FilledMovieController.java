package com.example.edgeservice.controller;

import com.example.edgeservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FilledMovieController {
    
    @Autowired
    private RestTemplate restTemplate;

    @Value("${castservice.baseurl}")
    private String castServiceBaseUrl;

    @Value("${movieservice.baseurl}")
    private String movieServiceBaseUrl;

    @Value("${genreservice.baseurl}")
    private String genreServiceBaseUrl;

    @GetMapping("/genres/genre/{name}")
    public List<FilledGenre> getMoviesByGenre(@PathVariable String name){
        List<FilledGenre> returnList = new ArrayList<>();

        ResponseEntity<List<Genre>> responseEntityGenres =
                restTemplate.exchange("http://" + genreServiceBaseUrl + "/genres/name/{name}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Genre>>() {
                        }, name);

        List<Genre> genres = responseEntityGenres.getBody();
        for(Genre genre: genres){
            ResponseEntity<List<Movie>> movies = restTemplate.exchange("http://" + movieServiceBaseUrl + "/movies/category/{category}",
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Movie>>() {
                    }, genre.getName());
            returnList.add(new FilledGenre(genre, movies.getBody()));
        }
        return returnList;
    }

    @GetMapping("/movies/movie/{title}")
    public List<FilledMovie> getCastByMovie(@PathVariable String title){
        List<FilledMovie> returnList = new ArrayList<>();

        ResponseEntity<List<Movie>> responseEntityMovies =
                restTemplate.exchange("http://" + movieServiceBaseUrl + "/movies/title/{title}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Movie>>() {
                        }, title);

        List<Movie> movies = responseEntityMovies.getBody();
        for(Movie movie: movies){
            ResponseEntity<List<Cast>> cast = restTemplate.exchange("http://" + castServiceBaseUrl + "/cast/movie/{movieID}",
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Cast>>() {
                    }, movie.getId());
            returnList.add(new FilledMovie(movie, cast.getBody()));
        }
        return returnList;
    }

    @GetMapping("/cast/character/{character}")
    public List<FilledCast> getMovieByCharacter(@PathVariable String character){
        List<FilledCast> returnList = new ArrayList<>();

        ResponseEntity<List<Cast>> responseEntityCasts =
                restTemplate.exchange("http://" + castServiceBaseUrl + "/cast/character/{character}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Cast>>() {
                        }, character);

        List<Cast> casts = responseEntityCasts.getBody();
        for(Cast cast: casts){
            ResponseEntity<List<Movie>> movies = restTemplate.exchange("http://" + movieServiceBaseUrl + "/movies/movie/{movieID}",
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Movie>>() {
                    }, cast.getMovieId());
            returnList.add(new FilledCast(cast, movies.getBody()));
        }
        return returnList;
    }

    @GetMapping("/genres/abbreviation/{abbreviation}")
    public List<FilledGenre> getMoviesByGenreAbbreviation(@PathVariable String abbreviation){
        List<FilledGenre> returnList = new ArrayList<>();

        ResponseEntity<List<Genre>> responseEntityGenres =
                restTemplate.exchange("http://" + genreServiceBaseUrl + "/genres/abbreviation/{abbreviation}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Genre>>() {
                        }, abbreviation);

        List<Genre> genres = responseEntityGenres.getBody();
        for(Genre genre: genres){
            ResponseEntity<List<Movie>> movies = restTemplate.exchange("http://" + movieServiceBaseUrl + "/movies/category/{category}",
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Movie>>() {
                    }, genre.getName());
            returnList.add(new FilledGenre(genre, movies.getBody()));
        }
        return returnList;
    }

    @PostMapping("/movies")
    public FilledGenre addMovie(@RequestParam String title, @RequestParam int year, @RequestParam String category, @RequestParam int minutes, @RequestParam String imdbID){
        Movie newMovie = new Movie(title, year, category, minutes, imdbID);
        Movie movie =
                restTemplate.postForObject("http://"+movieServiceBaseUrl+"/movies", newMovie,Movie.class);

        Genre genre =
                restTemplate.getForObject("http://" + genreServiceBaseUrl + "/genres/genre/{name}",
                        Genre.class,category);

        return new FilledGenre(genre, newMovie);
    }

    @PutMapping("/movies")
    public FilledGenre updateMovie(@RequestParam String title, @RequestParam int year, @RequestParam String category, @RequestParam int minutes, @RequestParam String imdbID){

        Movie movie =
                restTemplate.getForObject("http://" + movieServiceBaseUrl + "movies/imdbID/" + imdbID,
                        Movie.class);
        movie.setCategory(category);
        movie.setMinutes(minutes);
        movie.setTitle(title);
        movie.setYear(year);

        ResponseEntity<Movie> responseEntityMovie =
                restTemplate.exchange("http://" + movieServiceBaseUrl + "/movies",
                        HttpMethod.PUT, new HttpEntity<>(movie), Movie.class);

        Movie retrievedMovie = responseEntityMovie.getBody();

        Genre genre =
                restTemplate.getForObject("http://" + genreServiceBaseUrl + "/genres/genre/{name}",
                        Genre.class,category);

        return new FilledGenre(genre, retrievedMovie);
    }

    @DeleteMapping("/movies/movie/{imdbID}")
    public ResponseEntity deleteMovie(@PathVariable String imdbID){

        restTemplate.delete("http://" + movieServiceBaseUrl + "/movies/movie/" +imdbID);

        return ResponseEntity.ok().build();
    }
}
