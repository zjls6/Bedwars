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
                case "?????????????????????":
                    Generator diamondGenerator = new Generator(gameManager, currentLocation, GeneratorType.DIAMOND, false);
                    gameManager.getConfigManager().saveWorldGenerator(p.getWorld().getName(), diamondGenerator);
                    p.sendMessage("??????????????????????????????" + diamondGenerator.getLocation());
                    break;
                case "????????????????????????":
                    Generator emeraldGenerator = new Generator(gameManager, currentLocation, GeneratorType.EMERALD, false);
                    gameManager.getConfigManager().saveWorldGenerator(p.getWorld().getName(), emeraldGenerator);
                    p.sendMessage("?????????????????????????????????" + emeraldGenerator.getLocation());
                    break;
                case "????????????":
                    SetupIslandGUI gui = new SetupIslandGUI(gameManager);
                    gameManager.getGuiManager().setGUI(p, gui);
                    break;
                case "?????????1":
                    if (clickedLocation != null) {
                        island.setProtectCorner1(clickedLocation);
                        p.sendMessage("????????????" + clickedLocation);
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
//                    p.sendMessage("????????????????????????????????????");
//                }
                    break;
                case "?????????2":
                    if (clickedLocation != null) {
                        island.setProtectCorner2(clickedLocation);
                        p.sendMessage("????????????" + clickedLocation);
                    }
                    break;
                case "??????????????????1":
                    if (clickedLocation != null) {
                        island.setBaseCorner1(clickedLocation);
                        p.sendMessage("????????????" + clickedLocation);
                    }
                    break;
                case "??????????????????2":
                    if (clickedLocation != null) {
                        island.setBaseCorner2(clickedLocation);
                        p.sendMessage("????????????" + clickedLocation);
                    }
                    break;
                case "????????????????????????":
                    island.setShopLocation(currentLocation);
                    p.sendMessage("??????????????????????????????" + currentLocation);
                    break;
                case "????????????????????????":
                    island.setUpgradeLocation(currentLocation);
                    p.sendMessage("??????????????????????????????" + currentLocation);
                    break;
                case "?????????????????????": {
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

                p.sendMessage("??????????????????????????????" + currentLocation);
                break;
                case "?????????":
                    if (clickedLocation != null) {
                        island.setBedLocation(clickedLocation);
                        p.sendMessage("????????????????????????" + clickedLocation);
                    }
                    break;
                case "?????????????????????":
                    island.setSpawnLocation(currentLocation);
                    p.sendMessage("??????????????????????????????" + currentLocation);
                    break;
                case "???????????????????????????":
                    world = gameManager.getSetupManager().getWorld(p);
                    world.setWaitingLobbyLocation(currentLocation);
                    p.sendMessage("????????????????????????????????????" + currentLocation);
                    break;
                case "?????????????????????1":
                    world = gameManager.getSetupManager().getWorld(p);
                    world.setLobbyPos1(currentLocation);
                    p.sendMessage("????????????????????????1?????????" + currentLocation);
                    break;
                case "?????????????????????2":
                    world = gameManager.getSetupManager().getWorld(p);
                    world.setLobbyPos2(currentLocation);
                    p.sendMessage("????????????????????????2?????????" + currentLocation);
                    break;
                case "??????????????????":
                    world = gameManager.getSetupManager().getWorld(p);
                    gameManager.getConfigManager().saveWorld(world);
                    p.sendMessage("??a???????????????");
                    break;
                case "??????":
                    gameManager.getConfigManager().saveIsland(island);
                    gameManager.getSetupManager().worldSetup(p, island.getGameWorld());
                    p.sendMessage("??a???????????????");
            }
        }

        if (gameManager.getState().equals(GameState.LOBBY) || gameManager.getState().equals(GameState.STARTING)) {
            if (itemName.contains("????????????")) {
                TeamPickerGUI gui = new TeamPickerGUI(gameManager, p);
                gameManager.getGuiManager().setGUI(p, gui);
            }
        }


    }
}
