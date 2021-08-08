package me.zjls.bedwars.worlds;

import lombok.Getter;
import lombok.Setter;
import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.utils.Bedwars;
import me.zjls.bedwars.utils.Color;
import me.zjls.bedwars.worlds.generators.Generator;
import me.zjls.bedwars.worlds.generators.GeneratorTier;
import me.zjls.bedwars.worlds.generators.GeneratorType;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class GameWorld {

    private String name;
    private World world;
    private int maxTeamSize = 1;

    private Location WaitingLobbyLocation;
    private Location LobbyPos1;
    private Location LobbyPos2;

    private GeneratorTier diamondTier = GeneratorTier.ONE;
    private GeneratorTier emeraldTier = GeneratorTier.ONE;

    private List<Island> islands = new ArrayList<>();
    private List<Generator> generators = new ArrayList<>();

    public GameWorld(String name) {
        this.name = name;
    }

    public void loadWorld(GameManager gameManager, boolean usePlayingWorld, Runnable runnable) {
        File src = null;
        try {
            src = new File(
                    gameManager.getPlugin().getDataFolder().getCanonicalPath() + File.separator + ".." + File.separator + ".." + File.separator + name);

        } catch (IOException e) {
            e.printStackTrace();
        }
        File dest = new File(src.getPath() + (usePlayingWorld ? "_playing" : ""));

        try {
            copyFolder(src, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        WorldCreator creator = new WorldCreator(name + (usePlayingWorld ? "_playing" : ""));
        world = creator.createWorld();
        world.setAutoSave(false);
        world.setClearWeatherDuration(1000);
//        world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DISABLE_RAIDS, true);


        runnable.run();
    }

    private void copyFolder(File src, File dest) throws IOException {

        if (src.isDirectory()) {
            if (!dest.exists()) {
                dest.mkdir();
                Bukkit.getLogger().info("§a文件夹已复制 §8(从 §7" + src + " §8到§7 " + dest + "§8)");
            }

            String[] files = src.list();
            for (String file : files) {
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);

                copyFolder(srcFile, destFile);
            }
        } else {
            FileInputStream in = new FileInputStream(src);
            FileOutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;

            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();
        }
    }

    public void resetWorld() {
        if (world == null) return;

        String worldName = world.getName();

        Bukkit.unloadWorld(worldName, false);
        File file = new File(Bukkit.getWorldContainer().getAbsolutePath().replace(".", "") + world.getName());

        if (delete(file)) {
            System.out.println("删除成功");
        } else {
            System.out.println("删除失败");
        }

    }

    public boolean delete(File toDelete) {
        File[] files = toDelete.listFiles();

        if (files != null) {
            for (File file : files) {
                delete(file);
            }
        }

        return toDelete.delete();
    }


    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }

    public Location getWaitingLobbyLocation() {
        return WaitingLobbyLocation;
    }

    public void setWaitingLobbyLocation(Location lobbyLocation) {
        this.WaitingLobbyLocation = lobbyLocation;
    }

    public Island getIsland(Location bedLocation) {
        Optional<Island> islandOptional = islands.stream().filter(island -> {
            Location location = island.getBedLocation();
            return location.equals(bedLocation) || location.equals(bedLocation.clone().add(0, 0, 1)) || location.equals(bedLocation.clone().add(0, 0, -1))
                    || location.equals(bedLocation.clone().add(1, 0, 0)) || location.equals(bedLocation.clone().add(-1, 0, 0));
        }).findFirst();

        return islandOptional.orElse(null);
    }

    public Island getIsland(Player p) {
        return islands.stream().filter(island -> island.isMember(p)).findFirst().orElse(null);
    }

    public Island getIsland(IslandColor color) {
        return islands.stream().filter(island -> island.getColor().equals(color)).findFirst().orElse(null);
    }

    public List<Generator> getGenerators() {
        return generators;
    }

    public void setGenerators(List<Generator> generators) {
        this.generators = generators;
    }

    public void addIsland(Island island) {
        islands.add(island);
    }

    public List<Island> getActiveIslands() {
        return islands.stream().filter(island -> {
            if (island.isBedPlaced() && island.alivePlayerCount() != 0) {
                return true;
            } else return !island.isBedPlaced() && island.alivePlayerCount() != 0;
        }).collect(Collectors.toList());
    }

    public List<Island> getIslands() {
        return islands;
    }

    public int getMaxTeamSize() {
        return maxTeamSize;
    }


    public void tick(int currentSecond) {
        if (currentSecond == 6 * 60) {
            diamondTier = GeneratorTier.TWO;
            Bedwars.bc(Color.str("&b钻石生成点 &e已经升至 &c" + diamondTier.getName() + " &e级"));
        }
        if (currentSecond == 12 * 60) {
            emeraldTier = GeneratorTier.TWO;
            Bedwars.bc(Color.str("&a绿宝石生成点 &e已经升至 &c" + emeraldTier.getName() + " &e级"));
        }
        if (currentSecond == 18 * 60) {
            diamondTier = GeneratorTier.THREE;
            Bedwars.bc(Color.str("&b钻石生成点 &e已经升至 &c" + diamondTier.getName() + " &e级"));
        }
        if (currentSecond == 24 * 60) {
            emeraldTier = GeneratorTier.THREE;
            Bedwars.bc(Color.str("&a绿宝石生成点 &e已经升至 &c" + emeraldTier.getName() + " &e级"));
        }
        if (currentSecond == 30 * 60) {
            //todo:龙 ，床自毁
        }
        for (Island island : getIslands()) {
            for (Generator generator : island.getGenerators()) {
                generator.spawn();
            }
        }

        for (Generator generator : getGenerators()) {
            generator.setActive(true);

            if (generator.getType().equals(GeneratorType.DIAMOND)) {
                generator.setTier(diamondTier);
            }
            if (generator.getType().equals(GeneratorType.EMERALD)) {
                generator.setTier(emeraldTier);

            }

            generator.spawn();
        }
    }


}
