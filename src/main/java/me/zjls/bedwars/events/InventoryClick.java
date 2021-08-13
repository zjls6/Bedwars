package me.zjls.bedwars.events;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.games.GameState;
import me.zjls.bedwars.gui.GUI;
import me.zjls.bedwars.gui.ItemShopGUI;
import me.zjls.bedwars.gui.UpgradeShopGUI;
import me.zjls.bedwars.gui.types.ArmorType;
import me.zjls.bedwars.gui.types.ProtectionTier;
import me.zjls.bedwars.gui.types.ShopType;
import me.zjls.bedwars.gui.types.TrapType;
import me.zjls.bedwars.utils.Color;
import me.zjls.bedwars.utils.Good;
import me.zjls.bedwars.utils.ItemBuilder;
import me.zjls.bedwars.worlds.Island;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class InventoryClick implements Listener {

    private GameManager gameManager;

    public InventoryClick(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        String title = ChatColor.stripColor(e.getView().getTitle());
        if (title.contains("物品商店") || title.equals("升级与陷阱商店") || title.equals("选择陷阱")) {
            return;
        }

        if (gameManager.getState().equals(GameState.LOBBY) || gameManager.getState().equals(GameState.STARTING)) {
            if (p.getGameMode() != GameMode.CREATIVE) {
                e.setCancelled(true);
            }
        }

        ItemStack item = e.getCurrentItem();
        if (item == null) return;
//        p.sendMessage("1");
        if (item.getType().equals(Material.WOODEN_SWORD)) {
            if (e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                e.setCancelled(true);
                return;
            }
        }
        if (!item.hasItemMeta()) return;
//        p.sendMessage("2");

        if (e.getInventory().getType().equals(InventoryType.CRAFTING)) {
            if (e.getSlotType().equals(InventoryType.SlotType.ARMOR)) {
                e.setCancelled(true);
            }
        }

        if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).contains("队")) {
            e.setCancelled(true);
        }
        GUI gui = gameManager.getGuiManager().getOpenGUI(p);

        if (gui == null) {
            return;
        }

        e.setCancelled(true);

        GUI newGUI = gui.handleClick(p, item, e.getView());


        e.getView().close();

        gameManager.getGuiManager().setGUI(p, newGUI);

    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
//        p.sendMessage("11");
        if (e.getInventory().getType().equals(InventoryType.CHEST)) {
            gameManager.getGuiManager().clear(p);
//            p.sendMessage("chest");
        }
//        if (e.getView().getTitle().contains("物品商店")) {
//            gameManager.getPlayerManager().getEditingQuickBuyPlayersGoodMap().remove(p.getUniqueId());
//            p.sendMessage("11");
//        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent e) {
        Player p = (Player) e.getPlayer();
//        if (e.getView().getTitle().contains("物品商店")) {
//            gameManager.getPlayerManager().getEditingQuickBuyPlayersGoodMap().remove(p.getUniqueId());
//            p.sendMessage("11");
//        }
    }

//    @EventHandler
//    public void onMove(InventoryMoveItemEvent e) {
//
//        if (gameManager.getState().equals(GameState.LOBBY) || gameManager.getState().equals(GameState.STARTING)) {
//            ItemStack item = e.getItem();
//            if (item.hasItemMeta()) {
//                if (item.getItemMeta().hasDisplayName()) {
//                    e.setCancelled(true);
//                }
//            }
//        }
//        if (gameManager.getState().equals(GameState.ACTIVE)) {
//            if (e.getDestination().getType() != InventoryType.CRAFTING && e.getItem().getType().equals(Material.WOODEN_SWORD)) {
//                e.setCancelled(true);
//            }
//            if (Objects.equals(e.getDestination().getItem(0), ShopType.MAIN.getItemStack())) {
//                e.setCancelled(true);
//            }
//        }

    //    }
    @EventHandler
    public void onDrag(InventoryDragEvent e) {
//        e.getWhoClicked().sendMessage(ChatColor.stripColor(e.getView().getTitle()));
        String title = ChatColor.stripColor(e.getView().getTitle());
        if (title.contains("物品商店") || title.equals("升级与陷阱商店") || title.equals("选择陷阱")) {
            e.setCancelled(true);
            return;
        }

//        if (e.getInventory().getType().equals(InventoryType.CRAFTING)) {
        System.out.println(e.getInventory().getType().name() + "       item:" + Objects.requireNonNull(e.getCursor()).getType().name());
        if (e.getCursor() != null && e.getCursor().getType().equals(Material.WOODEN_SWORD)) {
            e.setCancelled(true);
        }
//        }
    }

    @EventHandler
    public void onItemShopClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        String title = ChatColor.stripColor(e.getView().getTitle());
        if (title.contains("物品商店")) {
            if (e.getAction().equals(InventoryAction.DROP_ONE_SLOT) || e.getAction().equals(InventoryAction.DROP_ONE_CURSOR)) {
                e.setCancelled(true);
                return;
            }
//            if ((e.getClickedInventory().getType().equals(InventoryType.CHEST) && e.getCursor() != null) || e.isShiftClick()) { }
            InventoryType.SlotType slotType = e.getSlotType();

            if (slotType.equals(InventoryType.SlotType.QUICKBAR) || slotType.equals(InventoryType.SlotType.OUTSIDE) || slotType.equals(InventoryType.SlotType.CRAFTING)) {
                e.setCancelled(true);
                p.sendMessage("2");
                return;
            }
            if (e.isShiftClick() && e.isRightClick()) {
                e.setCancelled(true);
                p.sendMessage("3");
                return;
            }

            if (item == null) {
                p.sendMessage("4");
                return;
            }
            if (!gameManager.getPlayerInGame().contains(p.getUniqueId())) {
                p.sendMessage("5");
                return;
            }

            int slot = e.getSlot();
            ItemShopGUI itemShopGUI = NPCClick.getItemShopGUI();
            switch (slot) {
                case 0:
                    itemShopGUI.openInventory(ShopType.MAIN);
                    break;
                case 1:
                    itemShopGUI.openInventory(ShopType.BLOCK);
                    break;
                case 2:
                    itemShopGUI.openInventory(ShopType.SWORD);
                    break;
                case 3:
                    itemShopGUI.openInventory(ShopType.ARMOR);
                    break;
                case 4:
                    itemShopGUI.openInventory(ShopType.TOOLS);
                    break;
                case 5:
                    itemShopGUI.openInventory(ShopType.BOW);
                    break;
                case 6:
                    itemShopGUI.openInventory(ShopType.POTIONS);
                    break;
                case 7:
                    itemShopGUI.openInventory(ShopType.UTILITY);
                    break;
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 26:
                case 27:
                case 36:
                case 44:
                case 45:
                case 53:
                    p.sendMessage("6");
                    e.setCancelled(true);
                    break;
                default:
                    p.sendMessage("7");
                    Map<UUID, Good> playersGoodMap = gameManager.getPlayerManager().getEditingQuickBuyPlayersGoodMap();
                    Map<Integer, Integer> favoriteMap = gameManager.getPlugin().data.getFavorite(p.getUniqueId());

                    if (playersGoodMap.containsKey(p.getUniqueId()) && title.contains("快捷购买") && e.isLeftClick()) {
                        p.sendMessage(String.valueOf(slot));
                        p.sendMessage(String.valueOf(itemShopGUI.getFavoriteNumber(slot)));
                        p.sendMessage(String.valueOf(playersGoodMap.get(p.getUniqueId()).getId()));

                        favoriteMap.replace(itemShopGUI.getFavoriteNumber(slot), playersGoodMap.get(p.getUniqueId()).getId());
                        playersGoodMap.remove(p.getUniqueId());
                        gameManager.getPlugin().data.setFavorite(p.getUniqueId(), favoriteMap);
                        itemShopGUI.openInventory(ShopType.MAIN);
                        return;
                    }
                    if (item.getType().equals(Material.RED_STAINED_GLASS_PANE)) {
                        if (!playersGoodMap.containsKey(p.getUniqueId())) {
                            e.setCancelled(true);
                            return;
                        }
                    }

                    Good good = null;

//                        if (!itemShopGUI.getProducts().containsKey(i)) {
//                            continue;
//                        }
//                        Good good = itemShopGUI.getProducts().get(i);
//                        if (!good.getItem().equals(item)) {
//                            continue;
//                        }

                    for (Good product : itemShopGUI.getAllProducts()) {
                        if (!product.getItem().equals(item)) {
                            continue;
                        }
                        good = product;
                    }

                    Map<Integer, Good> intGoodMap = itemShopGUI.setupProducts(ShopType.SWORD);
                    for (Map.Entry<Integer, Good> entry : intGoodMap.entrySet()) {
                        ItemStack sword = entry.getValue().getItem();
                        p.sendMessage(String.valueOf(entry.getValue().getId()));
                        if (sword.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                            good = entry.getValue();
                        }
                    }
                    if (good == null) {
                        p.sendMessage("8");
                        return;
                    }
//                        System.out.println(e.isShiftClick());
//                        System.out.println(e.isLeftClick());


                    if (e.isShiftClick() && e.isLeftClick()) {
//                        if (gameManager.getPlayerManager().getEditingQuickBuyPlayers().contains(p) && title.contains("快捷购买")) {
//                            favoriteMap.replace(itemShopGUI.getFavoriteNumber(slot), good.getId());
//                            gameManager.getPlayerManager().getEditingQuickBuyPlayers().remove(p);
//                            gameManager.getPlugin().data.setFavorite(p.getUniqueId(), favoriteMap);
//                            itemShopGUI.openInventory(ShopType.MAIN);
//                            return;
//                        } else {
                        if (title.contains("快捷购买")) {
                            favoriteMap.replace(itemShopGUI.getFavoriteNumber(slot), 0);
//                                p.sendMessage(String.valueOf(replace));
                            gameManager.getPlugin().data.setFavorite(p.getUniqueId(), favoriteMap);
                            p.sendMessage("9");
                        } else {
                            if (playersGoodMap.containsKey(p.getUniqueId())) {
                                playersGoodMap.replace(p.getUniqueId(), good);
                            } else {
                                playersGoodMap.put(p.getUniqueId(), good);
                            }
                            p.sendMessage(String.valueOf(good.getId()));
                            p.sendMessage("10");
                        }
                        itemShopGUI.openInventory(ShopType.MAIN);
                        return;
                    }

//                    if (e.isShiftClick() && e.isLeftClick()) {
//                        Map<Integer, Integer> favoriteMap = gameManager.getPlugin().data.getFavorite(p.getUniqueId());
//                        if (gameManager.getPlayerManager().getEditingQuickBuyPlayers().contains(p) && title.contains("快捷购买")) {
//                            favoriteMap.replace(itemShopGUI.getFavoriteNumber(slot), good.getId());
//                            gameManager.getPlayerManager().getEditingQuickBuyPlayers().remove(p);
//                            gameManager.getPlugin().data.setFavorite(p.getUniqueId(), favoriteMap);
//                            itemShopGUI.openInventory(ShopType.MAIN);
//                            return;
//                        }
//                        if (title.contains("快捷购买")) {
//                            favoriteMap.replace(itemShopGUI.getFavoriteNumber(slot), 0);
//                            gameManager.getPlugin().data.setFavorite(p.getUniqueId(), favoriteMap);
//                            itemShopGUI.openInventory(ShopType.MAIN);
//                        } else {
//                            favoriteMap.replace(itemShopGUI.getFavoriteNumber(slot), good.getId());
//
//                        }
//                        return;
//                    }

                    int needIronAmount = good.getIronAmount();
                    int needGoldAmount = good.getGoldAmount();
                    int needEmeraldAmount = good.getEmeraldAmount();
                    int needDiamondAmount = good.getDiamondAmount();

                    if (needIronAmount != 0) {
                        if (p.getInventory().containsAtLeast(new ItemStack(Material.IRON_INGOT), needIronAmount)) {
                            if (isInventoryFull(p)) {
                                p.sendMessage(Color.str("&c格子不够"));
                                p.closeInventory();
                                return;
                            }

                        } else {
                            p.sendMessage(Color.str("&c你没有足够的 &f铁锭"));
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                            p.closeInventory();
                            return;
                        }
                    }
                    if (needGoldAmount != 0) {
                        if (p.getInventory().containsAtLeast(new ItemStack(Material.GOLD_INGOT), needGoldAmount)) {
                            if (isInventoryFull(p)) {
                                p.sendMessage(Color.str("&c格子不够"));
                                p.closeInventory();
                                return;
                            }
                        } else {
                            p.sendMessage(Color.str("&c你没有足够的 &6金锭"));
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                            p.closeInventory();
                            return;
                        }
                    }
                    if (needEmeraldAmount != 0) {
                        if (p.getInventory().containsAtLeast(new ItemStack(Material.EMERALD), needEmeraldAmount)) {
                            if (isInventoryFull(p)) {
                                p.sendMessage(Color.str("&c格子不够"));
                                p.closeInventory();
                                return;
                            }
                        } else {
                            p.sendMessage(Color.str("&c你没有足够的 &a绿宝石"));
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                            p.closeInventory();
                            return;
                        }
                    }
                    if (needDiamondAmount != 0) {
                        if (p.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND), needDiamondAmount)) {
                            if (isInventoryFull(p)) {
                                p.sendMessage(Color.str("&c格子不够"));
                                p.closeInventory();
                                return;
                            }
                        } else {
                            p.sendMessage(Color.str("&c你没有足够的 &b钻石"));
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                            p.closeInventory();
                            return;
                        }
                    }
                    p.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, needIronAmount));
                    p.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, needGoldAmount));
                    p.getInventory().removeItem(new ItemStack(Material.EMERALD, needEmeraldAmount));
                    p.getInventory().removeItem(new ItemStack(Material.DIAMOND, needDiamondAmount));

                    String name = good.getItem().getItemMeta().getDisplayName();
                    Island island = gameManager.getGameWorld().getIsland(p);
                    switch (ChatColor.stripColor(name)) {
                        case "永久的 锁链护甲":
                            gameManager.getPlayerManager().getUuidArmorTypeMap().replace(p.getUniqueId(), ArmorType.CHAINMAIL);
                            gameManager.getPlayerManager().giveTeamArmor(p, island);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                            p.sendMessage(Color.str("&a你购买了 &6" + name));
                            p.updateInventory();
                            break;
                        case "永久的 铁护甲":
                            gameManager.getPlayerManager().getUuidArmorTypeMap().replace(p.getUniqueId(), ArmorType.IRON);
                            gameManager.getPlayerManager().giveTeamArmor(p, island);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                            p.sendMessage(Color.str("&a你购买了 &6" + name));
                            p.updateInventory();
                            break;
                        case "永久的 钻石护甲":
                            gameManager.getPlayerManager().getUuidArmorTypeMap().replace(p.getUniqueId(), ArmorType.DIAMOND);
                            gameManager.getPlayerManager().giveTeamArmor(p, island);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                            p.sendMessage(Color.str("&a你购买了 &6" + name));
                            p.updateInventory();
                            break;
                        case "永久的 下界合金护甲":
                            gameManager.getPlayerManager().getUuidArmorTypeMap().replace(p.getUniqueId(), ArmorType.NETHERITE);
                            gameManager.getPlayerManager().giveTeamArmor(p, island);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                            p.sendMessage(Color.str("&a你购买了 &6" + name));
                            p.updateInventory();
                            break;
                        default:
                            if (name.contains("剑")) {
                                p.getInventory().remove(Material.WOODEN_SWORD);
                                if (buySharp(p, good, name, island)) return;
                            }
                            if (name.contains("斧")) {
                                if (buySharp(p, good, name, island)) return;
                            }
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                            p.sendMessage(Color.str("&a你购买了 &6" + name));
                            if (good.getItem().getType().isBlock()) {
                                p.getInventory().addItem(new ItemBuilder(good.getItem()).clearName().clearLore().toItemStack());
                            } else {
                                p.getInventory().addItem(new ItemBuilder(good.getItem()).clearLore().toItemStack());
                            }

                            p.updateInventory();
                    }


            }
            e.setCancelled(true);
        }

    }


    private boolean buySharp(Player p, Good good, String name, Island island) {
        if (gameManager.getSharpnessTeam().contains(island)) {
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
            p.sendMessage(Color.str("&a你购买了 &6" + name));
            p.getInventory().addItem(new ItemBuilder(good.getItem()).clearName().clearLore().addEnchant(Enchantment.DAMAGE_ALL, 1).toItemStack());
            p.updateInventory();
            return true;
        }
        return false;
    }

    public boolean isInventoryFull(Player p) {
        return p.getInventory().firstEmpty() == -1;
    }

//    public boolean isInventoryFull(Player p) {
//
//        for (int i = 0; i < p.getInventory().getContents().length; i++) {
//            boolean b1 = p.getInventory().getItem(i) != null;
//            boolean b2 = i == p.getInventory().getContents().length - 1;
//            p.sendMessage(String.valueOf(b1));
//            p.sendMessage(String.valueOf(b2));
//            if (b1 && b2) {
//                return false;
//            }
//        }
//        return true;
//    }

    @EventHandler
    public void onUpgradeShopClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        String title = ChatColor.stripColor(e.getView().getTitle());

        if (title.equals("升级与陷阱商店") || title.equals("选择陷阱")) {
            if (e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) || e.getAction().equals(InventoryAction.DROP_ONE_SLOT)) {
                e.setCancelled(true);
                return;
            }
            InventoryType.SlotType slotType = e.getSlotType();
            if (slotType.equals(InventoryType.SlotType.QUICKBAR) || slotType.equals(InventoryType.SlotType.OUTSIDE) || slotType.equals(InventoryType.SlotType.CRAFTING)) {
                e.setCancelled(true);
                return;
            }

            if (item == null) {
                return;
            }

            int slot = e.getSlot();

            UpgradeShopGUI upgradeShopGUI = NPCClick.getUpgradeShopGUI();
            if (!gameManager.getPlayerInGame().contains(p.getUniqueId())) {
                return;
            }
            Island island = gameManager.getGameWorld().getIsland(p);
            if (island == null) {
                return;
            }
            if (title.equals("升级与陷阱商店")) {
                switch (slot) {
                    case 10:
                        List<Island> sharpnessTeam = gameManager.getSharpnessTeam();
                        if (sharpnessTeam.contains(island)) {
                            p.sendMessage(Color.str("&a你已经购买了 &6锋利附魔"));
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                            p.closeInventory();
                            return;
                        }
                        if (p.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND), 4)) {
                            sharpnessTeam.add(island);
                            for (ItemStack content : p.getInventory().getContents()) {
                                if (content == null) {
                                    continue;
                                }
                                if (content.getType().name().contains("SWORD") || content.getType().name().contains("AXE")) {
                                    ItemMeta itemMeta = content.getItemMeta();
                                    itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
                                    content.setItemMeta(itemMeta);
                                }
                            }
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                            p.getInventory().removeItem(new ItemStack(Material.DIAMOND, 4));
                            p.sendMessage(Color.str("&a你购买了 &6锋利附魔"));
                        } else {
                            p.sendMessage(Color.str("&c你没有足够的 &b钻石"));
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                            p.closeInventory();
                            return;
                        }
                        break;
                    case 11:
                        Map<Island, ProtectionTier> protectionTierMap = gameManager.getIslandProtectionTierMap();
                        ProtectionTier protectionTier = protectionTierMap.getOrDefault(island, ProtectionTier.Default);
                        switch (protectionTier) {
                            case Default:
                                if (p.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND), 2)) {
                                    protectionTierMap.put(island, ProtectionTier.ONE);
                                    p.getInventory().setHelmet(new ItemBuilder(p.getInventory().getHelmet()).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                                    p.getInventory().setChestplate(new ItemBuilder(p.getInventory().getChestplate()).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                                    p.getInventory().setLeggings(new ItemBuilder(p.getInventory().getLeggings()).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                                    p.getInventory().setBoots(new ItemBuilder(p.getInventory().getBoots()).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                                    p.getInventory().removeItem(new ItemStack(Material.DIAMOND, 2));
                                    protectionTier = protectionTierMap.get(island);
                                    upgradeShopGUI.setUpgradeInventory(island);
                                    p.sendMessage(Color.str("&a你购买了 &6装备强化&c" + protectionTier.getName()));
                                } else {
                                    p.sendMessage(Color.str("&c你没有足够的 &b钻石"));
                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                                    p.closeInventory();
                                    return;
                                }
                                break;
                            case ONE:
                                if (p.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND), 4)) {
                                    protectionTierMap.put(island, ProtectionTier.TWO);
                                    p.getInventory().setHelmet(new ItemBuilder(p.getInventory().getHelmet())
                                            .removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                                    p.getInventory().setChestplate(new ItemBuilder(p.getInventory().getChestplate())
                                            .removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                                    p.getInventory().setLeggings(new ItemBuilder(p.getInventory().getLeggings())
                                            .removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                                    p.getInventory().setBoots(new ItemBuilder(p.getInventory().getBoots())
                                            .removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                                    p.getInventory().removeItem(new ItemStack(Material.DIAMOND, 4));
                                    protectionTier = protectionTierMap.get(island);
                                    upgradeShopGUI.setUpgradeInventory(island);
                                    p.sendMessage(Color.str("&a你购买了 &6装备强化&c" + protectionTier.getName()));
                                } else {
                                    p.sendMessage(Color.str("&c你没有足够的 &b钻石"));
                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                                    p.closeInventory();
                                    return;
                                }
                                break;
                            case TWO:
                                if (p.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND), 8)) {
                                    protectionTierMap.put(island, ProtectionTier.THREE);
                                    p.getInventory().setHelmet(new ItemBuilder(p.getInventory().getHelmet())
                                            .removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                                    p.getInventory().setChestplate(new ItemBuilder(p.getInventory().getChestplate())
                                            .removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                                    p.getInventory().setLeggings(new ItemBuilder(p.getInventory().getLeggings())
                                            .removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                                    p.getInventory().setBoots(new ItemBuilder(p.getInventory().getBoots())
                                            .removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                                    p.getInventory().removeItem(new ItemStack(Material.DIAMOND, 8));
                                    protectionTier = protectionTierMap.get(island);
                                    upgradeShopGUI.setUpgradeInventory(island);
                                    p.sendMessage(Color.str("&a你购买了 &6装备强化&c" + protectionTier.getName()));
                                } else {
                                    p.sendMessage(Color.str("&c你没有足够的 &b钻石"));
                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                                    p.closeInventory();
                                    return;
                                }
                                break;
                            case THREE:
                                if (p.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND), 16)) {
                                    protectionTierMap.put(island, ProtectionTier.FOUR);
                                    p.getInventory().setHelmet(new ItemBuilder(p.getInventory().getHelmet())
                                            .removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                                    p.getInventory().setChestplate(new ItemBuilder(p.getInventory().getChestplate())
                                            .removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                                    p.getInventory().setLeggings(new ItemBuilder(p.getInventory().getLeggings())
                                            .removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                                    p.getInventory().setBoots(new ItemBuilder(p.getInventory().getBoots())
                                            .removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                                            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                                    p.getInventory().removeItem(new ItemStack(Material.DIAMOND, 16));
                                    protectionTier = protectionTierMap.get(island);
                                    upgradeShopGUI.setUpgradeInventory(island);
                                    p.sendMessage(Color.str("&a你购买了 &6装备强化&c" + protectionTier.getName()));
                                } else {
                                    p.sendMessage(Color.str("&c你没有足够的 &b钻石"));
                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                                    p.closeInventory();
                                    return;
                                }
                                break;
                            case FOUR:
                                p.sendMessage(Color.str("&6你已经升到最高级了！"));
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                                p.closeInventory();
                                break;
                        }
                        break;
                    case 12:
                        if (p.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND), 2)) {
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                            p.getInventory().removeItem(new ItemStack(Material.DIAMOND, 2));
                            for (UUID uuid : island.getPlayers()) {
                                Player player = Bukkit.getPlayer(uuid);
                                if (player != null) {
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100000, 0, true, false));
                                }
                            }
                            p.sendMessage(Color.str("&a你购买了 &6疯狂矿工"));
                        } else {
                            p.sendMessage(Color.str("&c你没有足够的 &b钻石"));
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                            p.closeInventory();
                            return;
                        }
                        break;
                    case 13:
                        break;
                    case 16:
                        upgradeShopGUI.openTrapGUI(island);
                        break;
                    default:
                        break;
                }
            }

            if (title.equals("选择陷阱")) {
                List<TrapType> trapList = island.getTrapList();
                int needDiamond = 0;
                switch (trapList.size()) {
                    case 0:
                        needDiamond = 1;
                        break;
                    case 1:
                        needDiamond = 2;
                        break;
                    case 2:
                        needDiamond = 4;
                        break;
                    default:
                        break;
                }
                switch (slot) {
                    case 26:
                        upgradeShopGUI.openMainGUI();
                        break;
                    case 10:

                        if (p.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND), needDiamond)) {
                            if (trapList.size() < 3) {
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                                p.getInventory().removeItem(new ItemStack(Material.DIAMOND, needDiamond));
                                trapList.add(TrapType.TRAP);
                                upgradeShopGUI.openTrapGUI(island);
                                p.sendMessage(Color.str("&a你购买了 &6这是个陷阱！"));
                            } else {
                                p.sendMessage(Color.str("&c陷阱队列已满！"));
                                p.closeInventory();
                            }
                        } else {
                            p.sendMessage(Color.str("&c你没有足够的 &b钻石"));
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                            p.closeInventory();
                        }
                        break;
                    case 11:
                        if (p.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND), needDiamond)) {
                            if (trapList.size() < 3) {
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                                p.getInventory().removeItem(new ItemStack(Material.DIAMOND, needDiamond));
                                trapList.add(TrapType.ATTACK);
                                upgradeShopGUI.openTrapGUI(island);
                                p.sendMessage(Color.str("&a你购买了 &6反击陷阱"));
                            } else {
                                p.sendMessage(Color.str("&c陷阱队列已满！"));
                                p.closeInventory();
                            }
                        } else {
                            p.sendMessage(Color.str("&c你没有足够的 &b钻石"));
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                            p.closeInventory();
                        }
                        break;
                    case 12:
                        if (p.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND), needDiamond)) {
                            if (trapList.size() < 3) {
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                                p.getInventory().removeItem(new ItemStack(Material.DIAMOND, needDiamond));
                                trapList.add(TrapType.WARN);
                                upgradeShopGUI.openTrapGUI(island);
                                p.sendMessage(Color.str("&a你购买了 &6报警陷阱"));
                            } else {
                                p.sendMessage(Color.str("&c陷阱队列已满！"));
                                p.closeInventory();
                            }
                        } else {
                            p.sendMessage(Color.str("&c你没有足够的 &b钻石"));
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                            p.closeInventory();
                        }
                        break;
                    case 13:
                        if (p.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND), needDiamond)) {
                            if (trapList.size() < 3) {
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                                p.getInventory().removeItem(new ItemStack(Material.DIAMOND, needDiamond));
                                trapList.add(TrapType.MINING);
                                upgradeShopGUI.openTrapGUI(island);
                                p.sendMessage(Color.str("&a你购买了 &6挖掘疲劳陷阱"));
                            } else {
                                p.sendMessage(Color.str("&c陷阱队列已满！"));
                                p.closeInventory();
                            }
                        } else {
                            p.sendMessage(Color.str("&c你没有足够的 &b钻石"));
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                            p.closeInventory();
                        }
                        break;
                    default:
                        break;
                }
            }
            e.setCancelled(true);
        }
    }
}
