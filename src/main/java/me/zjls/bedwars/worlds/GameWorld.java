package me.zjls.bedwars.worlds;

import me.zjls.bedwars.teams.TeamColor;
import me.zjls.bedwars.worlds.generators.Generator;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.util.List;

public class GameWorld {

    private String name;
    private World world;

    public GameWorld(String name) {
        this.name = name;
    }

    public void loadWorld(Runnable runnable) {
        WorldCreator creator = new WorldCreator(name);
        world = creator.createWorld();
        runnable.run();
    }

    public String getName() {
        return name;
    }


    public World getWorld() {
        return world;
    }

    public Location getLobbyLocation() {
        return null;
    }

    public List<Generator> getGenerators() {
        return null;
    }

}
