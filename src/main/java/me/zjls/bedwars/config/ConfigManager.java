package me.zjls.bedwars.config;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.worlds.Island;
import me.zjls.bedwars.worlds.generators.Generator;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConfigManager {

    private GameManager gameManager;
    private ConfigurationSection mapsConfig;

    public ConfigManager(GameManager gameManager) {
        this.gameManager = gameManager;
        FileConfiguration fileConfiguration = gameManager.getPlugin().getConfig();
        if (!fileConfiguration.isConfigurationSection("maps")) {
            mapsConfig = fileConfiguration.createSection("maps");
        } else {
            mapsConfig = fileConfiguration.getConfigurationSection("maps");
        }

        gameManager.getPlugin().saveConfig();
    }

    public ConfigurationSection getMapSection(String mapName) {
        if (!mapsConfig.isConfigurationSection(mapName)) {
            mapsConfig.createSection(mapName);
        }

        return mapsConfig.getConfigurationSection(mapName);
    }

    public void saveWorldGenerator(String worldName, Generator generator) {
        ConfigurationSection mapSection = getMapSection(worldName);
/*
        ConfigurationSection generatorSection = mapSection.getConfigurationSection("generators");

        if (generatorSection == null) {
            generatorSection = mapsConfig.createSection("generators");
        }
*/

        ConfigurationSection generatorSection;
        if (mapSection.isConfigurationSection("generators") ){
            generatorSection = mapSection.getConfigurationSection("generators");
        } else {
            generatorSection = mapSection.createSection("generators");
        }

        ConfigurationSection section = generatorSection.createSection(UUID.randomUUID().toString());
        section.set("type", generator.getType().toString());
        writeLocation(generator.getLocation(), section.createSection("location"));

        gameManager.getPlugin().saveConfig();

    }

    public void saveIsland(Island island) {
        ConfigurationSection mapSection = getMapSection(island.getGameWorld().getName());

        ConfigurationSection colorSection;
        if (mapSection.isConfigurationSection(island.getColor().toString())) {
            colorSection = mapSection.getConfigurationSection(island.getColor().toString());
        } else {
            colorSection = mapSection.createSection(island.getColor().toString());
        }

        Map<String, Location> locationToWrite = new HashMap<>();
        locationToWrite.put("Upgrade", island.getUpgradeLocation());
        locationToWrite.put("Bed", island.getBedLocation());
        locationToWrite.put("Spawn", island.getSpawnLocation());
        locationToWrite.put("Shop", island.getShopLocation());
        locationToWrite.put("ProtectCorner1", island.getProtectCorner1());
        locationToWrite.put("ProtectCorner2", island.getProtectCorner2());

        for (Map.Entry<String, Location> entry : locationToWrite.entrySet()) {
            ConfigurationSection section;
            if (colorSection.isConfigurationSection(entry.getKey())) {
                section = colorSection.getConfigurationSection(entry.getKey());
            } else {
                section = colorSection.createSection(entry.getKey());
            }

            writeLocation(entry.getValue(), section);
        }

        colorSection.set("generators", null);
        ConfigurationSection generatorSection = mapSection.createSection("generators");


        for (Generator generator : island.getGenerators()) {
            ConfigurationSection section = generatorSection.createSection(UUID.randomUUID().toString());
            section.set("type", generator.getType().toString());
            writeLocation(generator.getLocation(), section.createSection("location"));

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
