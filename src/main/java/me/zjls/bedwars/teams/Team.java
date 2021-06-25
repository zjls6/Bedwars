package me.zjls.bedwars.teams;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private TeamColor color;
    private List<Player> players = new ArrayList<>();

    public Team(TeamColor color) {
        this.color = color;
    }

    public TeamColor getColor() {
        return color;
    }

    public String getName() {
        return getColor().getName();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isMember(Player p) {
        return players.contains(p);
    }

    public boolean isBedPlaced(){
        return false;
    }

    public int alivePlayerCount(){
        return players.stream().filter(player -> player.getGameMode() != GameMode.SPECTATOR).toArray().length;
    }
}
