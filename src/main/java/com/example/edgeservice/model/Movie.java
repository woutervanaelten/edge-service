package com.example.edgeservice.model;

public class Movie {
    private int id;
    private String title;
    private int year;
    private String category;
    private int minutes;
    private String imdbID;

    public Movie() {
    }

    public Movie(String title, int year, String category, int minutes, String imdbID) {
        setTitle(title);
        setYear(year);
        setCategory(category);
        setMinutes(minutes);
        setImdbID(imdbID);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }
}

