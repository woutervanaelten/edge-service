package com.example.edgeservice;

import com.example.edgeservice.model.Cast;
import com.example.edgeservice.model.Genre;
import com.example.edgeservice.model.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private Movie movie1 = new Movie("Movie1", 2000, "Genre1", 60, "tt01");
    private Movie movie2 = new Movie("Movie2", 2010, "Genre2", 60, "tt02");
    private Movie movie3 = new Movie("Movie3", 2020, "Genre3", 60, "tt03");

    private Genre genre1 = new Genre("Genre1", "G1");
    private Genre genre2 = new Genre("Genre2", "G2");

    private Cast cast1 = new Cast(1, "nm0000375", "Character1", "Robert", "Downey Jr.", 55, "Manhatten, New York City, New York, USA");
    private Cast cast2 = new Cast(2, "nm0262635", "Steve Rogers/Captain America", "Chris", "Evans", 39, "Boston, Massachusetts, USA");
    private Cast cast3 = new Cast(3, "nm0749263", "Bruce Banner/The Hulk", "Mark", "Ruffalo", 53, "Kenosha, Wisconsin, USA");
    private Cast cast4 = new Cast(4, "nm0424060", "Natasha Romanoff/Black Widow", "Scarlett", "Johansson", 37, "Manhatten, New York City, New York, USA");


    private List<Movie> allMoviesWithGenreGenre1 = Arrays.asList(movie1);
    private List<Genre> allGenresWithNameGenre1 = Arrays.asList(genre1);
    private List<Genre> allGenresWithAbbreviationG1 = Arrays.asList(genre1);
    private List<Cast> allCastWithCharacter1 = Arrays.asList(cast1);

    @BeforeEach
    public void initializeMockserver() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
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
                .andExpect(jsonPath("$[0].nameGenre", is("Genre1")))
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
                .andExpect(jsonPath("$[0].nameGenre", is("Genre1")))
                .andExpect(jsonPath("$[0].movies[0].title", is("Movie1")))
                .andExpect(jsonPath("$[0].movies[0].year", is(2000)))
                .andExpect(jsonPath("$[0].movies[0].category", is("Genre1")))
                .andExpect(jsonPath("$[0].movies[0].minutes", is(60)))
                .andExpect(jsonPath("$[0].movies[0].imdbID", is("tt01")));
    }

    @Test
    public void whenAddRanking_thenReturnFilledGenreJson() throws Exception {
        Movie newMovie = new Movie("Nieuwe film", 2021, "Genre1", 60, "tt99");
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


        mockMvc.perform(post("/movies")
                .param("title", newMovie.getTitle())
                .param("year", String.valueOf(newMovie.getYear()))
                .param("category", newMovie.getCategory())
                .param("minutes", String.valueOf(newMovie.getMinutes()))
                .param("imdbID", newMovie.getImdbID())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameGenre", is("Genre1")))
                .andExpect(jsonPath("$.movies[0].title", is("Nieuwe film")))
                .andExpect(jsonPath("$.movies[0].year", is(2021)))
                .andExpect(jsonPath("$.movies[0].category", is("Genre1")))
                .andExpect(jsonPath("$.movies[0].minutes", is(60)))
                .andExpect(jsonPath("$.movies[0].imdbID", is("tt99")));
    }

    @Test
    public void whenUpdateMovie_thenReturnFilledBookReviewJson() throws Exception {

        Movie update = new Movie("Update", 2000, "Genre2", 60, "tt01");
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

        mockMvc.perform(put("/movies")
                .param("title", update.getTitle())
                .param("year", String.valueOf(update.getYear()))
                .param("category", update.getCategory())
                .param("minutes", String.valueOf(update.getMinutes()))
                .param("imdbID", update.getImdbID())
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

        // DELETE review from User 999 of Book with ISBN9 as ISBN
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + movieServiceBaseUrl + "/movies/movie/tt099")))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                );

        mockMvc.perform(delete("/movies/movie/{imdbID}", "tt099"))
                .andExpect(status().isOk());
    }

}
