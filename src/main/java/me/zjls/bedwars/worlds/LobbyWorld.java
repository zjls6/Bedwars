package me.zjls.bedwars.worlds;

import me.zjls.bedwars.teams.TeamColor;
import org.bukkit.Location;
import org.bukkit.World;

public class LobbyWorld extends GameWorld{
    @Override
    public boolean generateWorld(Runnable runnable) {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public World getWorld() {
        return null;
    }

    @Override
    public Location getLobbyLocation() {
        return null;
    }

    @Override
    public Location getTeamSpawn(TeamColor color) {
        return null;
    }

}
