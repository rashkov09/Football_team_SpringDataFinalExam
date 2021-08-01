package com.example.football.models.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "teams")
public class Team extends BaseEntity{
    private String name;
    private String stadiumName;
    private Integer fanBase;
    private String history;
    private Set<Player> players;
    private Town town;

    public Team() {
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "stadium_name")
    public String getStadiumName() {
        return stadiumName;
    }

    public void setStadiumName(String stadiumName) {
        this.stadiumName = stadiumName;
    }

    @Column(name = "fan_base")
    public Integer getFanBase() {
        return fanBase;
    }

    public void setFanBase(Integer fanBase) {
        this.fanBase = fanBase;
    }

    @Column(name = "history",columnDefinition = "TEXT")
    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    @OneToMany(mappedBy = "team")
    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    @ManyToOne
    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }
}
