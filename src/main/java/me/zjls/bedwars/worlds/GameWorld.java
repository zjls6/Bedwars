package me.zjls.bedwars.worlds;

import me.zjls.bedwars.teams.TeamColor;
import org.bukkit.Location;
import org.bukkit.World;

public abstract class GameWorld {

    public abstract boolean generateWorld(Runnable runnable);

    public abstract String getName();

    public abstract String getConfigName();

    public abstract World getWorld();

    public abstract Location getLobbyLocation();

    public abstract Location getTeamSpawn(TeamColor color);

}
