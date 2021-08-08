package me.zjls.bedwars.events;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.games.GameState;
import me.zjls.bedwars.gui.SetupIslandGUI;
import me.zjls.bedwars.gui.TeamPickerGUI;
import me.zjls.bedwars.utils.Bedwars;
import me.zjls.bedwars.worlds.GameWorld;
import me.zjls.bedwars.worlds.Island;
import me.zjls.bedwars.worlds.generators.Generator;
import me.zjls.bedwars.worlds.generators.GeneratorType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
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
        Player p = e.getPlayer();
        if (gameManager.getState().equals(GameState.ACTIVE)) {
            if (!e.hasItem()) return;
            ItemStack item = e.getItem();
            if (item == null) return;
            if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                return;
            }

            if (item.getType().equals(Material.FIRE_CHARGE)) {
                e.setCancelled(true);
                Location eyeLocation = p.getEyeLocation();
                Location add = eyeLocation.add(eyeLocation.getDirection().multiply(1.2));
                Fireball fireball = (Fireball) add.getWorld().spawnEntity(add, EntityType.FIREBALL);
                fireball.setVelocity(add.getDirection().normalize().multiply(0.5));
                fireball.setShooter(p);
                Bedwars.takeItem(p, e.getItem());
//                if (e.getItem().getAmount() == 1) {
//                    if (p.getInventory().getItemInOffHand().getType().equals(Material.FIRE_CHARGE)) {
//                        p.getInventory().setItemInOffHand(null);
//                    }
//                    p.getInventory().setItemInMainHand(null);
//                } else {
//                    item.setAmount(item.getAmount() - 1);
//                }
            }
            return;
        }
        if (!e.hasItem()) return;
        ItemStack item = e.getItem();
        if (item == null) return;

        if (!item.hasItemMeta()) return;
        if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            return;
        }

        String itemName = item.getItemMeta().getDisplayName();
        itemName = ChatColor.stripColor(itemName);

        Location currentLocation = p.getLocation();
        Location clickedLocation = null;
        if (e.getClickedBlock() != null) {
            clickedLocation = e.getClickedBlock().getLocation();
        }

        Island island = gameManager.getSetupManager().getIsland(p);

        e.setCancelled(true);

        if (gameManager.getSetupManager().isSetuping(p)) {
            GameWorld world;
            switch (itemName) {
                case "设置钻石生成点":
                    Generator diamondGenerator = new Generator(gameManager, currentLocation, GeneratorType.DIAMOND, false);
                    gameManager.getConfigManager().saveWorldGenerator(p.getWorld().getName(), diamondGenerator);
                    p.sendMessage("设置钻石生成点成功！" + diamondGenerator.getLocation());
                    break;
                case "设置绿宝石生成点":
                    Generator emeraldGenerator = new Generator(gameManager, currentLocation, GeneratorType.EMERALD, false);
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
//                if (island.getProtectCorner2() != null) {
//                    new BukkitRunnable() {
//                        @Override
//                        public void run() {
//
//                            p.spawnParticle(Particle.VILLAGER_HAPPY, island.getProtectCorner1().getX(), , , 1);
//                        }
//                    }.runTaskTimer(gameManager.getPlugin(), 0L, 5L);
//                } else {
//                    p.sendMessage("现在，你需要去另一个位置");
//                }
                    break;
                case "定点棒2":
                    if (clickedLocation != null) {
                        island.setProtectCorner2(clickedLocation);
                        p.sendMessage("右上位置" + clickedLocation);
                    }
                    break;
                case "设置基地位置1":
                    if (clickedLocation != null) {
                        island.setBaseCorner1(clickedLocation);
                        p.sendMessage("左下位置" + clickedLocation);
                    }
                    break;
                case "设置基地位置2":
                    if (clickedLocation != null) {
                        island.setBaseCorner2(clickedLocation);
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
                    Generator islandGenerator = new Generator(gameManager, currentLocation, GeneratorType.IRON, true);
                    island.addGenerator(islandGenerator);
                }
                {
                    Generator islandGenerator = new Generator(gameManager, currentLocation, GeneratorType.GOLD, true);
                    island.addGenerator(islandGenerator);
                }
                {
                    Generator islandGenerator = new Generator(gameManager, currentLocation, GeneratorType.EMERALD, true);
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
                case "设置等待大厅出生点":
                    world = gameManager.getSetupManager().getWorld(p);
                    world.setWaitingLobbyLocation(currentLocation);
                    p.sendMessage("设置等待大厅出生点成功！" + currentLocation);
                    break;
                case "等待大厅定点棒1":
                    world = gameManager.getSetupManager().getWorld(p);
                    world.setLobbyPos1(currentLocation);
                    p.sendMessage("设置等待大厅位置1成功！" + currentLocation);
                    break;
                case "等待大厅定点棒2":
                    world = gameManager.getSetupManager().getWorld(p);
                    world.setLobbyPos2(currentLocation);
                    p.sendMessage("设置等待大厅位置2成功！" + currentLocation);
                    break;
                case "保存大厅设置":
                    world = gameManager.getSetupManager().getWorld(p);
                    gameManager.getConfigManager().saveWorld(world);
                    p.sendMessage("§a保存成功！");
                    break;
                case "保存":
                    gameManager.getConfigManager().saveIsland(island);
                    gameManager.getSetupManager().worldSetup(p, island.getGameWorld());
                    p.sendMessage("§a保存成功！");
            }
        }

        if (gameManager.getState().equals(GameState.LOBBY) || gameManager.getState().equals(GameState.STARTING)) {
            if (itemName.contains("选择队伍")) {
                TeamPickerGUI gui = new TeamPickerGUI(gameManager, p);
                gameManager.getGuiManager().setGUI(p, gui);
            }
        }


    }
}
