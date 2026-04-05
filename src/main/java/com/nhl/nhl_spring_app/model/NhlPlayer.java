package com.nhl.nhl_spring_app.model;

public class NhlPlayer {
    private long id;
    private String name;
    private String team;
    private int number;
    private String position;
    private String nationality;
    private String image;
    private String description;
    private int ranking;
    private double score;

    public NhlPlayer() {
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }
    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getRanking() { return ranking; }
    public void setRanking(int ranking) { this.ranking = ranking; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
}
