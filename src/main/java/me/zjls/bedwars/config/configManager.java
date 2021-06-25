package me.zjls.bedwars.config;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.worlds.GameWorld;
import me.zjls.bedwars.worlds.Island;
import me.zjls.bedwars.worlds.generators.Generator;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class configManager {

    private GameManager gameManager;
    private ConfigurationSection mapsConfig;

    public configManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.mapsConfig = gameManager.getPlugin().getConfig().getConfigurationSection("maps");
    }

    public void saveIsland(Island island) {
        GameWorld gameWorld = island.getGameWorld();
        String worldName = gameWorld.getConfigName();

        if (mapsConfig.isConfigurationSection(worldName)) {
            mapsConfig.createSection(worldName);
        }

        ConfigurationSection mapSection = mapsConfig.getConfigurationSection(worldName);

        Map<String, Location> locationToWrite = new HashMap<>();
        locationToWrite.put("Upgrade", island.getUpgradeLocation());
        locationToWrite.put("Bed", island.getBedLocation());
        locationToWrite.put("Spawn", island.getSpawnLocation());
        locationToWrite.put("Shop", island.getShopLocation());
        locationToWrite.put("ProtectCorner1", island.getProtectCorner1());
        locationToWrite.put("ProtectCorner2", island.getProtectCorner2());

        for (Map.Entry<String, Location> entry : locationToWrite.entrySet()) {
            ConfigurationSection section;
            if (mapSection.isConfigurationSection(entry.getKey())) {
                section = mapSection.getConfigurationSection(entry.getKey());
            } else {
                section = mapSection.createSection(entry.getKey());
            }

            writeLocation(entry.getValue(), section);
        }

        mapSection.set("generators", null);
        ConfigurationSection generatorSection = mapSection.createSection("generators");

        int index = 0;
        for (Generator generator : island.getGenerators()) {
            ConfigurationSection section = generatorSection.createSection(String.valueOf(index));
            section.set("type", generator.getType().toString());
            writeLocation(generator.getLocation(), section.createSection("location"));

            index += 1;
        }

        gameManager.getPlugin().saveConfig();
    }


    public void writeLocation(Location loc, ConfigurationSection section) {
        section.set("x", loc.getX());
        section.set("y", loc.getY());
        section.set("z", loc.getZ());
        section.set("yaw", loc.getYaw());
        section.set("pitch", loc.getPitch());
    }

    protected Location locationFrom(World world, ConfigurationSection section) {
        return new Location(world, section.getDouble("x"), section.getDouble("y"), section.getDouble("z"),
                (float) section.getDouble("yaw"), (float) section.getDouble("pitch"));
    }


}
