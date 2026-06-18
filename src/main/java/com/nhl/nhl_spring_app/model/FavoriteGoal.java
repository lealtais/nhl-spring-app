package com.nhl.nhl_spring_app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "favorite_goal")
public class FavoriteGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Column(name = "event_id", nullable = false)
    private Integer eventId;

    @Column(name = "player_name")
    private String playerName;

    @Column(name = "player_photo")
    private String playerPhoto;

    @Column(name = "team")
    private String team;

    @Column(name = "period")
    private Integer period;

    @Column(name = "time_in_period")
    private String timeInPeriod;

    @Column(name = "x")
    private Double x;

    @Column(name = "y")
    private Double y;

    @Column(name = "shot_type")
    private String shotType;

    public FavoriteGoal() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AppUser getUser() { return user; }
    public void setUser(AppUser user) { this.user = user; }

    public Long getGameId() { return gameId; }
    public void setGameId(Long gameId) { this.gameId = gameId; }

    public Integer getEventId() { return eventId; }
    public void setEventId(Integer eventId) { this.eventId = eventId; }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public String getPlayerPhoto() { return playerPhoto; }
    public void setPlayerPhoto(String playerPhoto) { this.playerPhoto = playerPhoto; }

    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }

    public Integer getPeriod() { return period; }
    public void setPeriod(Integer period) { this.period = period; }

    public String getTimeInPeriod() { return timeInPeriod; }
    public void setTimeInPeriod(String timeInPeriod) { this.timeInPeriod = timeInPeriod; }

    public Double getX() { return x; }
    public void setX(Double x) { this.x = x; }

    public Double getY() { return y; }
    public void setY(Double y) { this.y = y; }

    public String getShotType() { return shotType; }
    public void setShotType(String shotType) { this.shotType = shotType; }
}
