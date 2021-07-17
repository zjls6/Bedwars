package me.zjls.bedwars.games;

import dev.jcsoftware.jscoreboards.JPerPlayerScoreboard;
import dev.jcsoftware.jscoreboards.JScoreboardTeam;
import me.zjls.bedwars.Main;
import me.zjls.bedwars.config.ConfigManager;
import me.zjls.bedwars.gui.GUIManager;
import me.zjls.bedwars.players.PlayerManager;
import me.zjls.bedwars.tasks.GameStarting;
import me.zjls.bedwars.tasks.GameTick;
import me.zjls.bedwars.utils.Bedwars;
import me.zjls.bedwars.utils.Color;
import me.zjls.bedwars.utils.SetupManager;
import me.zjls.bedwars.worlds.GameWorld;
import me.zjls.bedwars.worlds.Island;
import me.zjls.bedwars.worlds.IslandColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;

public class GameManager {

    private Main plugin;

    private JPerPlayerScoreboard scoreboard;
    private JScoreboardTeam team;
    private List<JScoreboardTeam> teams = new ArrayList<>();
    private Map<IslandColor, JScoreboardTeam> colorTeamMap = new HashMap<>();

    private SetupManager setupManager;
    private ConfigManager configManager;
    private GUIManager guiManager;
    private PlayerManager playerManager;

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

        this.scoreboard = new JPerPlayerScoreboard(
                (player) -> "&e&l起床战争",
                (player) -> {
//                    Island playerIsland = gameWorld.getIsland(player);
//                    ChatColor teamColor = playerIsland.getColor().getChatColor();
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
                    Bukkit.getScheduler().runTaskLater(plugin, bukkitTask -> p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1), 5);
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
                    playerManager.setPlaying(p);
                }

                for (Island island : getGameWorld().getIslands()) {
                    island.spawnShops();
                }

            case WON:
                Optional<Island> optionalIsland = getGameWorld().getActiveIslands().stream().findFirst();
                if (!optionalIsland.isPresent()) {
                    Bedwars.bc(Color.str("&a平局，游戏结束！"));
                } else {
                    Island island = optionalIsland.get();
                    Bedwars.bc(Color.str("&6游戏结束，" + island.getColor().getChatColor() + island.getColor().getName() + "队 &a获胜！"));
                }

                if (this.gameTickTask != null) {
                    this.gameTickTask.cancel();
                    this.gameTickTask = null;
                }

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

    public void endGame() {
        if (state != GameState.ACTIVE) {
            return;
        }
        if (getGameWorld().getActiveIslands().size() > 1) {
            System.out.println(gameWorld.getActiveIslands().size());
            System.out.println(gameWorld.getActiveIslands());
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
}
