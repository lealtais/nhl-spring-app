package com.nhl.nhl_spring_app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "favorite_player")
public class FavoritePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Column(name = "player_name")
    private String playerName;

    @Column(name = "team")
    private String team;

    @Column(name = "position")
    private String position;

    @Column(name = "player_number")
    private Integer playerNumber;

    @Column(name = "image")
    private String image;

    public FavoritePlayer() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AppUser getUser() { return user; }
    public void setUser(AppUser user) { this.user = user; }

    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public Integer getPlayerNumber() { return playerNumber; }
    public void setPlayerNumber(Integer playerNumber) { this.playerNumber = playerNumber; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
