package me.zjls.bedwars.events;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.gui.SetupIslandGUI;
import me.zjls.bedwars.worlds.Island;
import me.zjls.bedwars.worlds.generators.Generator;
import me.zjls.bedwars.worlds.generators.GeneratorType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteract implements Listener {

    private GameManager gameManager;

    public PlayerInteract(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (!e.hasItem()) return;
        if (!gameManager.getSetupManager().isSetuping(e.getPlayer())) return;
        ItemStack item = e.getItem();
        if (item == null) return;
//        if (item.hasItemMeta()) return;
        if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            return;
        }

        Player p = e.getPlayer();

        String itemName = item.getItemMeta().getDisplayName();
        itemName = ChatColor.stripColor(itemName);

        Location currentLocation = p.getLocation();
        Location clickedLocation = null;
        if (e.getClickedBlock() != null) {
            clickedLocation = e.getClickedBlock().getLocation();
        }

        Island island = gameManager.getSetupManager().getIsland(p);

        switch (itemName) {
            case "设置钻石生成点":
                Generator diamondGenerator = new Generator(currentLocation, GeneratorType.DIAMOND);
                gameManager.getConfigManager().saveWorldGenerator(p.getWorld().getName(), diamondGenerator);
                p.sendMessage("设置钻石生成点成功！" + diamondGenerator.getLocation());
                break;
            case "设置绿宝石生成点":
                Generator emeraldGenerator = new Generator(currentLocation, GeneratorType.EMERALD);
                gameManager.getConfigManager().saveWorldGenerator(p.getWorld().getName(), emeraldGenerator);
                p.sendMessage("设置绿宝石生成点成功！" + emeraldGenerator.getLocation());
                break;
            case "更改队伍":
                SetupIslandGUI gui = new SetupIslandGUI(gameManager);
                gameManager.getGuiManager().setGUI(p, gui);
                break;
            case "定点棒1":
                if (clickedLocation != null) {
                    island.setProtectCorner1(clickedLocation);
                    p.sendMessage("左下位置" + clickedLocation);
                }
                break;
            case "定点棒2":
                if (clickedLocation != null) {
                    island.setProtectCorner2(clickedLocation);
                    p.sendMessage("右上位置" + clickedLocation);
                }
                break;
            case "设置物品商店位置":
                island.setShopLocation(currentLocation);
                p.sendMessage("设置物品商店位置成功" + currentLocation);
                break;
            case "设置升级商店位置":
                island.setUpgradeLocation(currentLocation);
                p.sendMessage("设置升级商店位置成功" + currentLocation);
                break;
            case "设置资源生成点": {
                Generator islandGenerator = new Generator(currentLocation, GeneratorType.IRON);
                island.addGenerator(islandGenerator);
            }
            {
                Generator islandGenerator = new Generator(currentLocation, GeneratorType.GOLD);
                island.addGenerator(islandGenerator);
            }
            {
                Generator islandGenerator = new Generator(currentLocation, GeneratorType.EMERALD);
                island.addGenerator(islandGenerator);
            }
            p.sendMessage("设置资源生成点成功！" + currentLocation);
            break;
            case "设置床":
                if (clickedLocation != null) {
                    island.setBedLocation(clickedLocation);
                    p.sendMessage("设置床位置成功！" + clickedLocation);
                }
                break;
            case "设置队伍出生点":
                island.setSpawnLocation(currentLocation);
                p.sendMessage("设置队伍出生点成功！" + currentLocation);
                break;
            case "保存":
                gameManager.getConfigManager().saveIsland(island);
                gameManager.getSetupManager().worldSetup(p, island.getGameWorld());
            default:
                return;
        }
        e.setCancelled(true);
    }
}
