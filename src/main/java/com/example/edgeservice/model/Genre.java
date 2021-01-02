package com.example.edgeservice.model;

public class Genre {
    private Long id;
    private String name;
    private String abbreviation;

    public Genre(){}

    public Genre(String name, String abbreviation){
        setName(name);
        setAbbreviation(abbreviation);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}
