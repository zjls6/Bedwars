package me.zjls.bedwars.games;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import dev.jcsoftware.jscoreboards.JPerPlayerScoreboard;
import dev.jcsoftware.jscoreboards.JScoreboardTeam;
import lombok.Getter;
import me.zjls.bedwars.Main;
import me.zjls.bedwars.config.ConfigManager;
import me.zjls.bedwars.gui.GUIManager;
import me.zjls.bedwars.gui.types.ArmorType;
import me.zjls.bedwars.gui.types.ProtectionTier;
import me.zjls.bedwars.players.PlayerManager;
import me.zjls.bedwars.tasks.GameStarting;
import me.zjls.bedwars.tasks.GameTick;
import me.zjls.bedwars.utils.Bedwars;
import me.zjls.bedwars.utils.Color;
import me.zjls.bedwars.utils.CompactTower;
import me.zjls.bedwars.utils.SetupManager;
import me.zjls.bedwars.worlds.GameWorld;
import me.zjls.bedwars.worlds.Island;
import me.zjls.bedwars.worlds.IslandColor;
import me.zjls.bedwars.worlds.generators.GeneratorTier;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;

import java.text.SimpleDateFormat;
import java.util.*;

@Getter
public class GameManager {

    public HashMap<Silverfish, Island> silverfishTeamMap = new HashMap<>();
    private Main plugin;

    private JPerPlayerScoreboard scoreboard;
    private JScoreboardTeam team;
    private List<JScoreboardTeam> teams = new ArrayList<>();
    private Map<IslandColor, JScoreboardTeam> colorTeamMap = new HashMap<>();
    private Bedwars bedwars;
    private Map<IslandColor, Hologram> colorHologramMap = new HashMap<>();
    private Set<Block> protectedBlock = new HashSet<>();
    private List<UUID> playerInGame = new ArrayList<>();
    private List<Island> sharpnessTeam = new ArrayList<>();
    private Map<Island, ProtectionTier> islandProtectionTierMap = new HashMap<>();

    private SetupManager setupManager;
    private ConfigManager configManager;
    private GUIManager guiManager;
    private PlayerManager playerManager;
    private CompactTower compactTower;

    private GameStarting gameStartingTask;
    private GameTick gameTickTask;

    private GameWorld gameWorld;

    private GameState state;

    public GameManager(Main plugin) {
        this.plugin = plugin;

        this.setupManager = new SetupManager(this);
        this.configManager = new ConfigManager(this);
        this.playerManager = new PlayerManager(this);
        this.guiManager = new GUIManager();
        this.bedwars = new Bedwars(this);

        this.compactTower = new CompactTower(this);

        this.scoreboard = new JPerPlayerScoreboard(
                (player) -> "&e&l起床战争",
                (player) -> {

                    Long timeStamp = System.currentTimeMillis();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String sd = sdf.format(new Date(timeStamp));

                    List<String> lines = new ArrayList<>();
                    if (getState().equals(GameState.LOBBY)) {
                        lines.add("&7" + sd);
                        lines.add("");
                        lines.add("&f地图： &a灯塔");
                        lines.add("&f玩家： &6" + Bukkit.getOnlinePlayers().size() + "&7/&a" + (gameWorld.getMaxTeamSize() * gameWorld.getIslands().size()));
                        lines.add("");
                        lines.add("&f等待中...");
                        lines.add("");
                        lines.add("&f模式： &a单挑");
                        lines.add("");
                        lines.add("&etest.server");
                    } else if (getState().equals(GameState.STARTING)) {
                        lines.add("&7" + sd);
                        lines.add("");
                        lines.add("&f地图： &a灯塔");
                        lines.add("&f玩家： &6" + Bukkit.getOnlinePlayers().size() + "&7/&a" + (gameWorld.getMaxTeamSize() * gameWorld.getIslands().size()));
                        lines.add("");
                        lines.add("&f即将开始： &a" + gameStartingTask.getTime() + " 秒");
                        lines.add("");
                        lines.add("&f模式： &a单挑");
                        lines.add("");
                        lines.add("&etest.server");
                    } else {
                        lines.add("&7" + sd);
                        lines.add("");
                        if (gameTickTask != null) {
                            int currentSecond = gameTickTask.getCurrentSecond();
                            if (gameWorld.getDiamondTier().equals(GeneratorTier.ONE) && gameWorld.getEmeraldTier().equals(GeneratorTier.ONE)) {
                                lines.add("&f钻石生成点&6II&f级 &7- &a" + getDate(6 * 60 - currentSecond));
                            } else if (gameWorld.getDiamondTier().equals(GeneratorTier.TWO) && gameWorld.getEmeraldTier().equals(GeneratorTier.ONE)) {
                                lines.add("&f绿宝石生成点&6II&f级 &7- &a" + getDate(12 * 60 - currentSecond));
                            } else if (gameWorld.getDiamondTier().equals(GeneratorTier.TWO) && gameWorld.getEmeraldTier().equals(GeneratorTier.TWO)) {
                                lines.add("&f钻石生成点&6III&f级 &7- &a" + getDate(18 * 60 - currentSecond));
                            } else if (gameWorld.getDiamondTier().equals(GeneratorTier.THREE) && gameWorld.getEmeraldTier().equals(GeneratorTier.TWO)) {
                                lines.add("&f绿宝石生成点&6III&f级 &7- &a" + getDate(24 * 60 - currentSecond));
                            } else if (gameWorld.getDiamondTier().equals(GeneratorTier.THREE) && gameWorld.getEmeraldTier().equals(GeneratorTier.THREE) && currentSecond < 30 * 60) {
                                lines.add("&c床自毁&f &7- &a" + getDate(30 * 60 - currentSecond));
                            } else if (currentSecond >= 30 * 60) {
                                lines.add("&c&l游戏结束&f &7- &a" + getDate(60 * 60 - currentSecond));
                            }
                            lines.add("");
                        }
                        for (Island island : gameWorld.getIslands()) {
                            StringBuilder builder = new StringBuilder();
                            ChatColor teamColor = island.getColor().getChatColor();

                            builder.append(Color.of(island.getColor().getColor())).append(island.getColor().name().charAt(0)).append(" ");
                            builder.append(Color.str("&f" + island.getColor().getName() + "队&7: "));

                            if (island.isBedPlaced() && island.alivePlayerCount() != 0) {
                                builder.append(Color.str("&a✔"));
                            } else {
                                if (island.alivePlayerCount() == 0) {
                                    builder.append(Color.str("&c✘"));
                                } else {
                                    builder.append(Color.str("&c" + island.alivePlayerCount()));
                                }
                            }
                            if (island.isMember(player)) {
                                builder.append(" &7你");
                            }
                            lines.add(builder.toString());
                        }
                        lines.add("");
                        lines.add("&etest.server");
                    }
                    return lines;
                }
        );

        this.configManager.loadWorld(this.configManager.randomMapName(), gameWorld -> {
            this.gameWorld = gameWorld;

            setState(GameState.LOBBY);

            int i = 0;
            for (Island island : gameWorld.getIslands()) {
                ChatColor teamColor = island.getColor().getChatColor();
                System.out.println("注册队伍 " + teamColor + island.getColor().getName() + "队");
                teams.add(this.scoreboard.createTeam(island.getColor().name(), teamColor + "&l" + island.getColor().getName() + " ", teamColor));
                colorTeamMap.put(island.getColor(), teams.get(i));
                i++;
            }
        });

    }

    public GameState getState() {
        return state;
    }

    public static String getDate(Integer time) {
        if (time < 60) {
            return "0:" + time;
        } else if (time < 3600) {

            int m = time / 60;
            int s = time % 60;
            return m + ":" + s;
        } else {
            int h = time / 3600;
            int m = (time % 3600) / 60;
            int s = (time % 3600) % 60;
            return h + ":" + m + ":" + s;
        }
    }

    public void endGame() {
        if (state != GameState.ACTIVE) {
            return;
        }
        if (getGameWorld().getActiveIslands().size() > 1) {
            return;
        }

        setState(GameState.WON);

    }

    public GameStarting getGameStartingTask() {
        return gameStartingTask;
    }

    public JPerPlayerScoreboard getScoreboard() {
        return scoreboard;
    }

    public Map<IslandColor, JScoreboardTeam> getColorTeamMap() {
        return colorTeamMap;
    }

    public List<JScoreboardTeam> getTeams() {
        return teams;
    }

    public Main getPlugin() {
        return this.plugin;
    }

    public SetupManager getSetupManager() {
        return setupManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public GUIManager getGuiManager() {
        return guiManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public void setState(GameState state) {
        this.state = state;
        switch (state) {
            case LOBBY:
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.teleport(gameWorld.getWaitingLobbyLocation());
                    playerManager.giveTeamSelector(p);
                }
                break;
            case STARTING:
                this.gameStartingTask = new GameStarting(this);
                this.gameStartingTask.runTaskTimer(plugin, 0, 20);
                break;
            case ACTIVE:
                if (this.gameStartingTask != null) {
                    this.gameStartingTask.cancel();
                }
                this.gameStartingTask = null;

                this.scoreboard.updateScoreboard();
                this.gameTickTask = new GameTick(this);
                gameTickTask.runTaskTimer(plugin, 0, 20);

                for (Player p : Bukkit.getOnlinePlayers()) {
                    Bukkit.getScheduler().runTaskLater(plugin, bukkitTask -> p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1), 3);
                    Island island = gameWorld.getIsland(p);
                    if (island == null) {
                        //todo:随机分配
                        Optional<Island> optionalIsland = gameWorld.getIslands().stream().filter(found -> found.getPlayers().size() < gameWorld.getMaxTeamSize()).findFirst();

                        if (!optionalIsland.isPresent()) {
                            p.kickPlayer(Color.str("&c没有足够的岛！"));
                            continue;
                        }

                        Island island1 = optionalIsland.get();
                        island1.getPlayers().add(p.getUniqueId());
                        getColorTeamMap().get(island1.getColor()).addPlayer(p);

                    }

                    p.sendMessage("");
                    p.sendMessage(Color.str("&#CAF0F8█&#BCECF6█&#90E0EF█&#6CD5EA█&#48CAE4█&#24BFDE█&#00B4D8█&#00A5D0█&#0096C7█&#0087BF█&#0077B6█&#0169AB█&#015BA0█&#024D95█&#024690█&#02428D█&#023E8A█&#033B88█&#033785█&#03307F█"));
                    p.sendMessage("");
                    p.sendMessage(Color.str("                 &#D8DCFF&l起床战争"));
                    p.sendMessage("");
                    p.sendMessage(Color.str("        &#F9DC5C保护你的床并摧毁敌人的床"));
                    p.sendMessage(Color.str("      &#F9DC5C收集资源，使自身和队伍变得更强"));
                    p.sendMessage("");
                    p.sendMessage(Color.str("&#CAF0F8█&#BCECF6█&#90E0EF█&#6CD5EA█&#48CAE4█&#24BFDE█&#00B4D8█&#00A5D0█&#0096C7█&#0087BF█&#0077B6█&#0169AB█&#015BA0█&#024D95█&#024690█&#02428D█&#023E8A█&#033B88█&#033785█&#03307F█"));
                    p.sendMessage("");

                }
                for (Island island : gameWorld.getIslands()) {
                    island.spawnShops();

                    Hologram bedHologram = HologramsAPI.createHologram(getPlugin(), island.getBedLocation().clone().add(0.5, 1, 0.5));
                    bedHologram.getVisibilityManager().setVisibleByDefault(false);
                    bedHologram.appendTextLine(Color.str("&c保护你的床"));

                    colorHologramMap.put(island.getColor(), bedHologram);

                    playerInGame.addAll(island.getPlayers());
                }
                for (Player p : Bukkit.getOnlinePlayers()) {
                    playerManager.getUuidArmorTypeMap().put(p.getUniqueId(), ArmorType.LEATHER);
                    playerManager.setPlaying(p);
                }

                BlockVector3 one = BlockVector3.at(gameWorld.getLobbyPos1().getX(), gameWorld.getLobbyPos1().getY(), gameWorld.getLobbyPos1().getZ());
                BlockVector3 two = BlockVector3.at(gameWorld.getLobbyPos2().getX(), gameWorld.getLobbyPos2().getY(), gameWorld.getLobbyPos2().getZ());

                CuboidRegion region = new CuboidRegion(one, two);

                for (BlockVector3 point : region) {
                    Location loc = new Location(gameWorld.getWorld(), point.getBlockX(), point.getBlockY(), point.getBlockZ());
                    loc.getBlock().setType(Material.AIR);
                }
                break;
            case WON:
                if (this.gameTickTask != null) {
                    this.gameTickTask.cancel();
                    this.gameTickTask = null;
                }

                Optional<Island> optionalIsland = getGameWorld().getActiveIslands().stream().findFirst();
                if (!optionalIsland.isPresent()) {
                    Bedwars.bc(Color.str("&a平局，游戏结束！"));
                } else {
                    Island island = optionalIsland.get();
                    Bedwars.bc(Color.str("&6游戏结束，" + island.getColor().getChatColor() + island.getColor().getName() + "队 &a获胜！"));
                }

                this.scoreboard.updateScoreboard();
                Bukkit.getServer().getScheduler().runTaskLater(plugin, bukkitTask -> setState(GameState.RESET), 20 * 15);

                break;
            case RESET:
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.kickPlayer("游戏正在重置");
                }
                //回档
                Bukkit.getServer().getScheduler().runTaskLater(plugin, bukkitTask -> {
                    gameWorld.resetWorld();
                    Bukkit.getServer().shutdown();
                }, 20);
                break;
        }
    }
}
