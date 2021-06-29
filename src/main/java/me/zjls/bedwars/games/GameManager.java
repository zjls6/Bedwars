package me.zjls.bedwars.games;

import dev.jcsoftware.jscoreboards.JPerPlayerScoreboard;
import me.zjls.bedwars.Main;
import me.zjls.bedwars.config.ConfigManager;
import me.zjls.bedwars.gui.GUIManager;
import me.zjls.bedwars.utils.SetupManager;

import java.util.Arrays;

public class GameManager {

    private Main plugin;
    private JPerPlayerScoreboard scoreboard;

    private SetupManager setupManager;
    private ConfigManager configManager;
    private GUIManager guiManager;

    private GameState state;

    public GameManager(Main plugin) {
        this.plugin = plugin;

        this.setupManager = new SetupManager(this);
        this.configManager = new ConfigManager(this);
        this.guiManager = new GUIManager();

        this.scoreboard = new JPerPlayerScoreboard(
                (player) -> "&6起床战争",
                (player) -> {
                    return Arrays.asList("1", "");
                }
        );
    }

    public GameState getState() {
        return state;
    }


    public void setState(GameState state) {
        this.state = state;

        switch (state) {
            case PRELOBBY:
                break;
            case LOBBY:
                break;
            case STARTING:
                break;
            case ACTIVE:
                break;
            case WON:
                break;
        }
    }

    public JPerPlayerScoreboard getScoreboard() {
        return scoreboard;
    }

    public Main getPlugin() {
        //todo: this.plugin?
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

}
