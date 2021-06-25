package me.zjls.bedwars.teams;

import me.zjls.bedwars.games.GameManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TeamManager {

    private List<Team> teams = new ArrayList<>();

    private GameManager gameManager;

    public TeamManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public List<Team> getAliveTeams(){
        return teams.stream().filter(team -> team.isBedPlaced() && team.alivePlayerCount()!=0).collect(Collectors.toList());
    }
}
