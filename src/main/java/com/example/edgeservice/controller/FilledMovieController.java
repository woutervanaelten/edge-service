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

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/genres/all")
    public List<Genre> getGenres(){
        ResponseEntity<List<Genre>> responseEntityGenres =
                restTemplate.exchange("http://" + genreServiceBaseUrl + "/genres/all",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Genre>>() {
                        });

        List<Genre> genres = responseEntityGenres.getBody();
        return genres;
    }

    @CrossOrigin(origins = "http://localhost:4200")
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

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/movies/all")
    public List<Movie> getMovies(){
        ResponseEntity<List<Movie>> responseEntityMovies =
                restTemplate.exchange("http://" + movieServiceBaseUrl + "/movies/all",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Movie>>() {
                        });

        List<Movie> movies = responseEntityMovies.getBody();
        return movies;
    }

    @CrossOrigin(origins = "http://localhost:4200")
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

    @CrossOrigin(origins = "http://localhost:4200")
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

    @CrossOrigin(origins = "http://localhost:4200")
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

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/movies")
    public FilledGenre addMovie(@RequestBody Movie movie){
        Movie newMovie = new Movie(0, movie.getTitle(), movie.getYear(), movie.getCategory(), movie.getMinutes(), movie.getImdbID());
        Movie addedMovie =
                restTemplate.postForObject("http://" + movieServiceBaseUrl + "/movies", newMovie,Movie.class);

        Genre genre =
                restTemplate.getForObject("http://" + genreServiceBaseUrl + "/genres/genre/{name}",
                        Genre.class, movie.getCategory());

        return new FilledGenre(genre, newMovie);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/movies")
    public FilledGenre updateMovie(@RequestBody Movie oldMovie) {

        Movie movie =
                restTemplate.getForObject("http://" + movieServiceBaseUrl + "/movies/imdbID/" + oldMovie.getImdbID(),
                        Movie.class);
        movie.setCategory(oldMovie.getCategory());
        movie.setMinutes(oldMovie.getMinutes());
        movie.setTitle(oldMovie.getTitle());
        movie.setYear(oldMovie.getYear());

        ResponseEntity<Movie> responseEntityMovie =
                restTemplate.exchange("http://" + movieServiceBaseUrl + "/movies",
                        HttpMethod.PUT, new HttpEntity<>(movie), Movie.class);

        Movie retrievedMovie = responseEntityMovie.getBody();

        Genre genre =
                restTemplate.getForObject("http://" + genreServiceBaseUrl + "/genres/genre/{name}",
                        Genre.class,oldMovie.getCategory());

        return new FilledGenre(genre, retrievedMovie);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/movies/movie/{imdbID}")
    public ResponseEntity deleteMovie(@PathVariable String imdbID){

        restTemplate.delete("http://" + movieServiceBaseUrl + "/movies/movie/" +imdbID);

        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/cast")
    public Cast addCast(@RequestBody Cast cast){
        Cast newCast = new Cast(null, cast.getMovieId(), cast.getiMDB(), cast.getCharacter(), cast.getFirstName(), cast.getLastName(), cast.getAge(), cast.getBirthPlace());
        Cast addedCast =
                restTemplate.postForObject("http://" + castServiceBaseUrl + "/cast", newCast,Cast.class);

        return addedCast;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/cast")
    public Cast updateCast(@RequestBody Cast oldCast) {

        Cast cast =
                restTemplate.getForObject("http://" + castServiceBaseUrl + "/cast/imdb/" + oldCast.getiMDB(),
                        Cast.class);
        cast.setId(cast.getId());
        cast.setiMDB(cast.getiMDB());
        cast.setFirstName(oldCast.getFirstName());
        cast.setLastName(oldCast.getLastName());
        cast.setCharacter(oldCast.getCharacter());
        cast.setAge(oldCast.getAge());
        cast.setBirthPlace(oldCast.getBirthPlace());

        ResponseEntity<Cast> responseEntityCast =
                restTemplate.exchange("http://" + castServiceBaseUrl + "/cast",
                        HttpMethod.PUT, new HttpEntity<>(cast), Cast.class);

        Cast retrievedCast = responseEntityCast.getBody();

        return retrievedCast;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/cast/imdb/{iMDB}")
    public ResponseEntity deleteCast(@PathVariable String iMDB){

        restTemplate.delete("http://" + castServiceBaseUrl + "/cast/imdb/" + iMDB);

        return ResponseEntity.ok().build();
    }
}
