package me.zjls.bedwars.config;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.utils.Color;
import me.zjls.bedwars.worlds.GameWorld;
import me.zjls.bedwars.worlds.Island;
import me.zjls.bedwars.worlds.IslandColor;
import me.zjls.bedwars.worlds.generators.Generator;
import me.zjls.bedwars.worlds.generators.GeneratorType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    public String randomMapName() {
        //todo:
        String[] mapNames = mapsConfig.getKeys(false).toArray(new String[0]);
        return mapNames[ThreadLocalRandom.current().nextInt(mapNames.length)];
    }

    public ConfigurationSection getMapSection(String mapName) {
        if (!mapsConfig.isConfigurationSection(mapName)) {
            mapsConfig.createSection(mapName);
        }

        return mapsConfig.getConfigurationSection(mapName);
    }

    public void loadWorld(String mapName, Consumer<GameWorld> consumer) {
        GameWorld gameWorld = new GameWorld(mapName);
        gameWorld.loadWorld(gameManager, true, () -> {
            ConfigurationSection mapSection = getMapSection(mapName);
            for (String key : mapSection.getKeys(false)) {
                if (Arrays.stream(IslandColor.values()).anyMatch((t) -> t.name().equalsIgnoreCase(key))) {
                    if (!mapSection.isConfigurationSection(key)) {
                        mapSection.createSection(key);
                    }
                    Island island = loadIsland(gameWorld, mapSection.getConfigurationSection(key));
                    gameWorld.addIsland(island);
                }
            }

            if (mapSection.isConfigurationSection("generators")) {
                ConfigurationSection mapGenerators = mapSection.getConfigurationSection("generators");
                gameWorld.setGenerators(loadGenerators(gameWorld, mapGenerators, false));
            }

            ConfigurationSection lobbySection;

            if (mapSection.isConfigurationSection("waitingLobby")) {
                lobbySection = mapSection.getConfigurationSection("waitingLobby");
            } else {
                lobbySection = mapSection.createSection("waitingLobby");
            }
            if (!lobbySection.isConfigurationSection("spawnLocation")) {
                lobbySection.createSection("spawnLocation");
            }
            if (!lobbySection.isConfigurationSection("LobbyPos1")) {
                lobbySection.createSection("LobbyPos1");
            }
            if (!lobbySection.isConfigurationSection("LobbyPos2")) {
                lobbySection.createSection("LobbyPos2");
            }

            gameWorld.setWaitingLobbyLocation(locationFrom(gameWorld.getWorld(), lobbySection.getConfigurationSection("spawnLocation")));
            gameWorld.setLobbyPos1(locationFrom(gameWorld.getWorld(), lobbySection.getConfigurationSection("LobbyPos1")));
            gameWorld.setLobbyPos2(locationFrom(gameWorld.getWorld(), lobbySection.getConfigurationSection("LobbyPos2")));

            consumer.accept(gameWorld);
        });
    }

    public Island loadIsland(GameWorld world, ConfigurationSection section) {
        IslandColor color = IslandColor.valueOf(section.getName());
        Island island = new Island(gameManager, world, color);
        try {
            island.setBedLocation(locationFrom(world.getWorld(), section.getConfigurationSection("Bed")));
            island.setShopLocation(locationFrom(world.getWorld(), section.getConfigurationSection("Shop")));
            island.setUpgradeLocation(locationFrom(world.getWorld(), section.getConfigurationSection("Upgrade")));
            island.setProtectCorner1(locationFrom(world.getWorld(), section.getConfigurationSection("ProtectCorner1")));
            island.setProtectCorner2(locationFrom(world.getWorld(), section.getConfigurationSection("ProtectCorner2")));
            island.setBaseCorner1(locationFrom(world.getWorld(), section.getConfigurationSection("BaseCorner1")));
            island.setBaseCorner2(locationFrom(world.getWorld(), section.getConfigurationSection("BaseCorner2")));
            island.setSpawnLocation(locationFrom(world.getWorld(), section.getConfigurationSection("Spawn")));
            island.setGenerators(loadGenerators(world, section.getConfigurationSection("generators"), true));
        } catch (Exception e) {
            Bukkit.getServer().getLogger().severe(Color.str(world.getName() + " 中的 " + color.getName() + " &c队(岛) 未配置好"));
            e.printStackTrace();
        }
        return island;
    }

    public List<Generator> loadGenerators(GameWorld world, ConfigurationSection section, boolean isInIsland) {
        List<Generator> generators = section.getKeys(false).stream().map((key) -> {
            ConfigurationSection generatorSection = section.getConfigurationSection(key);

            Location location = locationFrom(world.getWorld(), generatorSection.getConfigurationSection("location"));
            String type = generatorSection.getString("type");

            if (Arrays.stream(GeneratorType.values()).noneMatch((t) -> t.name().equalsIgnoreCase(type))) {
                return null;
            }

            GeneratorType generatorType = GeneratorType.valueOf(type);
            return new Generator(gameManager, location, generatorType, isInIsland);
        }).collect(Collectors.toList());

        return generators;
    }

    public void saveWorld(GameWorld world) {
        ConfigurationSection mapSection = getMapSection(world.getName());
        ConfigurationSection lobbySection;

        if (mapSection.isConfigurationSection("waitingLobby")) {
            lobbySection = mapSection.getConfigurationSection("waitingLobby");
        } else {
            lobbySection = mapSection.createSection("waitingLobby");
        }

        writeLocation(world.getWaitingLobbyLocation(), lobbySection.createSection("spawnLocation"));
        writeLocation(world.getLobbyPos1(), lobbySection.createSection("LobbyPos1"));
        writeLocation(world.getLobbyPos2(), lobbySection.createSection("LobbyPos2"));

        gameManager.getPlugin().saveConfig();
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
        if (mapSection.isConfigurationSection("generators")) {
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

        ConfigurationSection colorSection; //team
        if (mapSection.isConfigurationSection(island.getColor().toString())) {
            colorSection = mapSection.getConfigurationSection(island.getColor().toString());
        } else {
            colorSection = mapSection.createSection(island.getColor().toString());
        }

        Map<String, Location> locationToWrite = new HashMap<>();
        locationToWrite.put("Bed", island.getBedLocation());
        locationToWrite.put("Spawn", island.getSpawnLocation());
        locationToWrite.put("Shop", island.getShopLocation());
        locationToWrite.put("Upgrade", island.getUpgradeLocation());
        locationToWrite.put("ProtectCorner1", island.getProtectCorner1());
        locationToWrite.put("ProtectCorner2", island.getProtectCorner2());
        locationToWrite.put("BaseCorner1", island.getBaseCorner1());
        locationToWrite.put("BaseCorner2", island.getBaseCorner2());

        for (Map.Entry<String, Location> entry : locationToWrite.entrySet()) {
            ConfigurationSection section;
            if (colorSection.isConfigurationSection(entry.getKey())) {
                section = colorSection.getConfigurationSection(entry.getKey());
            } else {
                section = colorSection.createSection(entry.getKey());
            }

            if (entry.getValue() != null) {
                writeLocation(entry.getValue(), section);
            }
        }

        colorSection.set("generators", null);
//        ConfigurationSection generatorSection;
//        if (colorSection.isConfigurationSection("generators")) {
//            generatorSection = colorSection.getConfigurationSection("generators");
//        } else
        ConfigurationSection generatorSection = colorSection.createSection("generators");
//        }

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

    public Location locationFrom(World world, ConfigurationSection section) {
        return new Location(world, section.getDouble("x"), section.getDouble("y"), section.getDouble("z"),
                (float) section.getDouble("yaw"), (float) section.getDouble("pitch"));
    }


}
