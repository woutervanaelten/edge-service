package com.example.edgeservice;

import com.example.edgeservice.model.Cast;
import com.example.edgeservice.model.Genre;
import com.example.edgeservice.model.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

@SpringBootTest
@AutoConfigureMockMvc
public class FilledMovieControllerUnitTests {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${castservice.baseurl}")
    private String castServiceBaseUrl;

    @Value("${movieservice.baseurl}")
    private String movieServiceBaseUrl;

    @Value("${genreservice.baseurl}")
    private String genreServiceBaseUrl;

    @Autowired
    private MockMvc mockMvc;


    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    private Movie movie1 = new Movie(1, "Movie1", 2000, "Genre1", 60, "tt01");
    private Movie movie2 = new Movie(2, "Movie2", 2010, "Genre2", 60, "tt02");
    private Movie movie3 = new Movie(3, "Movie3", 2020, "Genre3", 60, "tt03");

    private Genre genre1 = new Genre("Genre1", "G1");
    private Genre genre2 = new Genre("Genre2", "G2");

    private Cast cast1 = new Cast("1",1, "nm0000375", "Character1", "Robert", "Downey Jr.", 55, "Manhatten, New York City, New York, USA");
    private Cast cast2 = new Cast(null,2, "nm0262635", "Steve Rogers/Captain America", "Chris", "Evans", 39, "Boston, Massachusetts, USA");
    private Cast cast3 = new Cast(null,3, "nm0749263", "Bruce Banner/The Hulk", "Mark", "Ruffalo", 53, "Kenosha, Wisconsin, USA");
    private Cast cast4 = new Cast(null,4, "nm0424060", "Natasha Romanoff/Black Widow", "Scarlett", "Johansson", 37, "Manhatten, New York City, New York, USA");


    private List<Movie> allMovies = Arrays.asList(movie1, movie2, movie3);
    private List<Genre> allGenres = Arrays.asList(genre1, genre2);
    private List<Cast> allCastWithMovieMovie1 = Arrays.asList(cast1);
    private List<Cast> allCastWithCharacterCharacter1 = Arrays.asList(cast1);
    private List<Movie> allMoviesWithMovieID1 = Arrays.asList(movie1);
    private List<Movie> allMoviesWithCharacterCast1 = Arrays.asList(movie1);
    private List<Movie> allMoviesWithGenreGenre1 = Arrays.asList(movie1);
    private List<Genre> allGenresWithNameGenre1 = Arrays.asList(genre1);
    private List<Genre> allGenresWithAbbreviationG1 = Arrays.asList(genre1);
    private List<Cast> allCastWithCharacter1 = Arrays.asList(cast1);

    @BeforeEach
    public void initializeMockserver() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void whenGetAllGenres_thenReturnGenresJson() throws Exception {
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + genreServiceBaseUrl + "/genres/all")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allGenres))
                );

        mockMvc.perform(get("/genres/all"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Genre1")))
                .andExpect(jsonPath("$[0].abbreviation", is("G1")));
    }

    @Test
    public void whenGetAllMovies_thenReturnMoviesJson() throws Exception {
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + movieServiceBaseUrl + "/movies/all")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allMovies))
                );

        mockMvc.perform(get("/movies/all"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].title", is("Movie1")))
                .andExpect(jsonPath("$[0].year", is(2000)))
                .andExpect(jsonPath("$[0].category", is("Genre1")))
                .andExpect(jsonPath("$[0].minutes", is(60)))
                .andExpect(jsonPath("$[0].imdbID", is("tt01")));
    }

    @Test
    public void whenGetCastByMovie_thenReturnFilledMovieJson() throws Exception {
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + movieServiceBaseUrl + "/movies/title/Movie1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allMoviesWithCharacterCast1))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + castServiceBaseUrl + "/cast/movie/1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allCastWithMovieMovie1))
                );


        mockMvc.perform(get("/movies/movie/{title}", "Movie1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].movie.title", is("Movie1")))
                .andExpect(jsonPath("$[0].movie.year", is(2000)))
                .andExpect(jsonPath("$[0].movie.category", is("Genre1")))
                .andExpect(jsonPath("$[0].movie.minutes", is(60)))
                .andExpect(jsonPath("$[0].movie.imdbID", is("tt01")))
                .andExpect(jsonPath("$[0].casts[0].movieId", is(1)))
                .andExpect(jsonPath("$[0].casts[0].iMDB", is("nm0000375")))
                .andExpect(jsonPath("$[0].casts[0].character", is("Character1")))
                .andExpect(jsonPath("$[0].casts[0].firstName", is("Robert")))
                .andExpect(jsonPath("$[0].casts[0].lastName", is("Downey Jr.")))
                .andExpect(jsonPath("$[0].casts[0].age", is(55)))
                .andExpect(jsonPath("$[0].casts[0].birthPlace", is("Manhatten, New York City, New York, USA")));
    }

    @Test
    public void whenGetMovieByCharacter_thenReturnFilledCastJson() throws Exception {
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + castServiceBaseUrl + "/cast/character/Character1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allCastWithCharacterCharacter1))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + movieServiceBaseUrl + "/movies/movie/1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allCastWithMovieMovie1))
                );


        mockMvc.perform(get("/cast/character/{character}", "Character1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].cast.movieId", is(1)))
                .andExpect(jsonPath("$[0].cast.iMDB", is("nm0000375")))
                .andExpect(jsonPath("$[0].cast.character", is("Character1")))
                .andExpect(jsonPath("$[0].cast.firstName", is("Robert")))
                .andExpect(jsonPath("$[0].cast.lastName", is("Downey Jr.")))
                .andExpect(jsonPath("$[0].cast.age", is(55)))
                .andExpect(jsonPath("$[0].cast.birthPlace", is("Manhatten, New York City, New York, USA")));
    }

    @Test
    public void whenGetMoviesByGenre_thenReturnFilledGenreJson() throws Exception {
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + genreServiceBaseUrl + "/genres/name/Genre1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allGenresWithNameGenre1))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + movieServiceBaseUrl + "/movies/category/Genre1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allMoviesWithGenreGenre1))
                );


        mockMvc.perform(get("/genres/genre/{name}", "Genre1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].genre.name", is("Genre1")))
                .andExpect(jsonPath("$[0].genre.abbreviation", is("G1")))
                .andExpect(jsonPath("$[0].movies[0].title", is("Movie1")))
                .andExpect(jsonPath("$[0].movies[0].year", is(2000)))
                .andExpect(jsonPath("$[0].movies[0].category", is("Genre1")))
                .andExpect(jsonPath("$[0].movies[0].minutes", is(60)))
                .andExpect(jsonPath("$[0].movies[0].imdbID", is("tt01")));
    }

    @Test
    public void whenGetMoviesByAbbreviation_thenReturnFilledGenreJson() throws Exception {
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + genreServiceBaseUrl + "/genres/abbreviation/G1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allGenresWithAbbreviationG1))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + movieServiceBaseUrl + "/movies/category/Genre1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allMoviesWithGenreGenre1))
                );


        mockMvc.perform(get("/genres/abbreviation/{abbreviation}", "G1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].genre.name", is("Genre1")))
                .andExpect(jsonPath("$[0].genre.abbreviation", is("G1")))
                .andExpect(jsonPath("$[0].movies[0].title", is("Movie1")))
                .andExpect(jsonPath("$[0].movies[0].year", is(2000)))
                .andExpect(jsonPath("$[0].movies[0].category", is("Genre1")))
                .andExpect(jsonPath("$[0].movies[0].minutes", is(60)))
                .andExpect(jsonPath("$[0].movies[0].imdbID", is("tt01")));
    }

    @Test
    public void whenAddMovie_thenReturnFilledGenreJson() throws Exception {
        Movie newMovie = new Movie(4, "Nieuwe film", 2021, "Genre1", 60, "tt99");
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + movieServiceBaseUrl + "/movies")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(newMovie))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + genreServiceBaseUrl + "/genres/genre/Genre1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(genre1))
                );

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String movie = ow.writeValueAsString(newMovie);

        mockMvc.perform(post("/movies")
                .content(movie)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movies[0].title", is("Nieuwe film")))
                .andExpect(jsonPath("$.movies[0].year", is(2021)))
                .andExpect(jsonPath("$.movies[0].category", is("Genre1")))
                .andExpect(jsonPath("$.movies[0].minutes", is(60)))
                .andExpect(jsonPath("$.movies[0].imdbID", is("tt99")));
    }

    @Test
    public void whenUpdateMovie_thenReturnFilledMovieJson() throws Exception {
        Movie update = new Movie(1, "Update", 2000, "Genre2", 60, "tt01");
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + movieServiceBaseUrl + "/movies/imdbID/tt01")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(movie1))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + movieServiceBaseUrl + "/movies")))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(update))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + genreServiceBaseUrl + "/genres/genre/Genre2")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(movie1))
                );

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String movie = ow.writeValueAsString(update);

        mockMvc.perform(put("/movies")
                .content(movie)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movies[0].title", is("Update")))
                .andExpect(jsonPath("$.movies[0].year", is(2000)))
                .andExpect(jsonPath("$.movies[0].category", is("Genre2")))
                .andExpect(jsonPath("$.movies[0].minutes", is(60)))
                .andExpect(jsonPath("$.movies[0].imdbID", is("tt01")));

    }

    @Test
    public void whenDeleteMovie_thenReturnStatusOk() throws Exception {
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + movieServiceBaseUrl + "/movies/movie/tt099")))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                );

        mockMvc.perform(delete("/movies/movie/{imdbID}", "tt099"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenAddCast_thenReturnFilledCastJson() throws Exception {
        Cast newCast = new Cast(null,1, "nm99", "Character1", "Robert", "Downey Jr.", 55, "Manhatten, New York City, New York, USA");
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + castServiceBaseUrl + "/cast")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(newCast))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + movieServiceBaseUrl + "/movies/movie/1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(movie1))
                );

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String cast = ow.writeValueAsString(newCast);

        mockMvc.perform(post("/cast")
                .content(cast)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieId", is(1)))
                .andExpect(jsonPath("$.iMDB", is("nm99")))
                .andExpect(jsonPath("$.character", is("Character1")))
                .andExpect(jsonPath("$.firstName", is("Robert")))
                .andExpect(jsonPath("$.lastName", is("Downey Jr.")))
                .andExpect(jsonPath("$.age", is(55)))
                .andExpect(jsonPath("$.birthPlace", is("Manhatten, New York City, New York, USA")));
    }

    @Test
    public void whenUpdateCast_thenReturnFilledCastJson() throws Exception {
        Cast updateCast = new Cast("1",1, "nm0000375", "Character1", "Robert", "Downey Jr.", 55, "Manhatten, New York City, New York, USA");
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + castServiceBaseUrl + "/cast/imdb/nm0000375")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(updateCast))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + castServiceBaseUrl + "/cast")))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(updateCast))
                );

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String cast = ow.writeValueAsString(updateCast);

        mockMvc.perform(put("/cast")
                .content(cast)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieId", is(1)))
                .andExpect(jsonPath("$.iMDB", is("nm0000375")))
                .andExpect(jsonPath("$.character", is("Character1")))
                .andExpect(jsonPath("$.firstName", is("Robert")))
                .andExpect(jsonPath("$.lastName", is("Downey Jr.")))
                .andExpect(jsonPath("$.age", is(55)))
                .andExpect(jsonPath("$.birthPlace", is("Manhatten, New York City, New York, USA")));
    }

    @Test
    public void whenDeleteCast_thenReturnStatusOk() throws Exception {
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + castServiceBaseUrl + "/cast/imdb/nm99")))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                );

        mockMvc.perform(delete("/cast/imdb/{iMDB}", "nm99"))
                .andExpect(status().isOk());
    }

}
