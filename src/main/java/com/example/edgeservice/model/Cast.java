package com.example.edgeservice.model;

public class Cast {
    private String id;
    private Integer movieId;
    private String iMDB;
    private String character;
    private String firstName;
    private String lastName;
    private Integer age;
    private String birthPlace;

    public Cast(Integer movieId, String iMDB, String character, String firstName, String lastName, Integer age, String birthPlace) {
        this.movieId = movieId;
        this.iMDB = iMDB;
        this.character = character;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.birthPlace = birthPlace;
    }

    public Cast(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) { this.movieId = movieId; }

    public String getiMDB() { return iMDB; }

    public void setiMDB(String iMDB) { this.iMDB = iMDB; }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBirthPlace() { return birthPlace; }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }
}
