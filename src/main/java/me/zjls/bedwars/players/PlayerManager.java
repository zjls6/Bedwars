package me.zjls.bedwars.players;

import lombok.Getter;
import lombok.Setter;
import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.games.GameState;
import me.zjls.bedwars.gui.types.ArmorType;
import me.zjls.bedwars.gui.types.ProtectionTier;
import me.zjls.bedwars.tasks.PlayerRespawn;
import me.zjls.bedwars.utils.Bedwars;
import me.zjls.bedwars.utils.Color;
import me.zjls.bedwars.utils.Good;
import me.zjls.bedwars.utils.ItemBuilder;
import me.zjls.bedwars.worlds.Island;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

@Setter
@Getter
public class PlayerManager {

    private GameManager gameManager;

    private Map<UUID, ArmorType> uuidArmorTypeMap = new HashMap<>();
    private Set<Block> playerBlocks = new HashSet<>();

    private List<UUID> spectatorList = new ArrayList<>();

    private Map<Integer, Integer> favorite = new HashMap<>();
    private Map<UUID, Good> editingQuickBuyPlayersGoodMap = new HashMap<>();

    private Map<UUID, Integer> playerDeathCount = new HashMap<>();
    private Map<UUID, Integer> playerKillCount = new HashMap<>();

    public PlayerManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void setSpectator(Player p) {
        p.getInventory().clear();

        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        p.teleport(gameManager.getGameWorld().getWaitingLobbyLocation());
        p.setGameMode(GameMode.SPECTATOR);

        Island island = gameManager.getGameWorld().getIsland(p);

        if (gameManager.getState().equals(GameState.ACTIVE) && island == null) {
            p.sendMessage(Color.str("&6游戏已经开始，你是一名旁观者！"));
            p.getInventory().clear();
            p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            p.teleport(gameManager.getGameWorld().getWaitingLobbyLocation());
            p.setGameMode(GameMode.SPECTATOR);
            spectatorList.add(p.getUniqueId());
            return;
        }

        if (island.isBedPlaced()) {
            BukkitTask task = Bukkit.getServer().getScheduler().runTaskTimer(gameManager.getPlugin(), new PlayerRespawn(gameManager, p, island), 0, 20);
            Bukkit.getServer().getScheduler().runTaskLater(gameManager.getPlugin(), task::cancel, 20 * 6);
        } else {
            p.sendTitle("§c你死了", "§r您已变成一名旁观者", 20, 40, 20);
            spectatorList.add(p.getUniqueId());
            p.getInventory().clear();
            p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            p.teleport(gameManager.getGameWorld().getWaitingLobbyLocation());
            p.setGameMode(GameMode.SPECTATOR);

            if (!gameManager.getGameWorld().getActiveIslands().contains(island)) {
                Bedwars.bc(Color.str(island.getColor().getName() + " 没了！"));
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1);
            }
        }
        gameManager.endGame();


    }

    public void setPlaying(Player p) {
        p.getInventory().clear();
        p.setNoDamageTicks(20 * 5);

        Island island = gameManager.getGameWorld().getIsland(p);

        if (island == null || !island.isBedPlaced()) {
            setSpectator(p);
            return;
        }

        gameManager.getColorHologramMap().get(island.getColor()).getVisibilityManager().showTo(p);
        gameManager.getColorHologramMap().get(island.getColor()).getVisibilityManager().setVisibleByDefault(false);

        p.setGameMode(GameMode.SURVIVAL);
        p.teleport(island.getSpawnLocation());

        giveTeamArmor(p, island);

        ItemStack woodSword = Bedwars.swordItem(Material.WOODEN_SWORD, 4);

        if (gameManager.getSharpnessTeam().contains(island)) {
            woodSword = new ItemBuilder(woodSword).addEnchant(Enchantment.DAMAGE_ALL, 1).toItemStack();
        }

        p.getInventory().addItem(woodSword);

        //todo:升级

    }

    public void giveTeamArmor(Player p, Island island) {
        if (gameManager.getState().equals(GameState.LOBBY) || gameManager.getState().equals(GameState.STARTING)) {
            p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().toItemStack());
            p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().toItemStack());
            p.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().toItemStack());
            p.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().toItemStack());
            return;
        }
        ArmorType armorType = uuidArmorTypeMap.get(p.getUniqueId());
        ProtectionTier protectionTier = gameManager.getIslandProtectionTierMap().get(island);
        switch (armorType) {
            case LEATHER:
                if (protectionTier == null) {
                    p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().toItemStack());
                    p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().toItemStack());
                    p.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().toItemStack());
                    p.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().toItemStack());
                    return;
                }
                switch (protectionTier) {
                    case ONE:
                        p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                        p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                        p.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                        p.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                        break;
                    case TWO:
                        p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                        p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                        p.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                        p.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                        break;
                    case THREE:
                        p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                        p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                        p.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                        p.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                        break;
                    case FOUR:
                        p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                        p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                        p.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                        p.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                        break;
                }

                break;
            case CHAINMAIL:
                if (protectionTier == null) {
                    p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().toItemStack());
                    p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().toItemStack());
                    p.getInventory().setLeggings(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).setUnbreakable().hideUnbreakable().toItemStack());
                    p.getInventory().setBoots(new ItemBuilder(Material.CHAINMAIL_BOOTS).setUnbreakable().hideUnbreakable().toItemStack());
                    return;
                }
                switch (protectionTier) {
                    case ONE:
                        p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                        p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                        p.getInventory().setLeggings(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                        p.getInventory().setBoots(new ItemBuilder(Material.CHAINMAIL_BOOTS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                        break;
                    case TWO:
                        p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                        p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                        p.getInventory().setLeggings(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                        p.getInventory().setBoots(new ItemBuilder(Material.CHAINMAIL_BOOTS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                        break;
                    case THREE:
                        p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                        p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                        p.getInventory().setLeggings(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                        p.getInventory().setBoots(new ItemBuilder(Material.CHAINMAIL_BOOTS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                        break;
                    case FOUR:
                        p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                        p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                        p.getInventory().setLeggings(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                        p.getInventory().setBoots(new ItemBuilder(Material.CHAINMAIL_BOOTS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                        break;
                }
                break;
            case IRON:
                if (protectionTier == null) {
                    p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().toItemStack());
                    p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().toItemStack());
                    p.getInventory().setLeggings(new ItemBuilder(Material.IRON_LEGGINGS).setUnbreakable().hideUnbreakable().toItemStack());
                    p.getInventory().setBoots(new ItemBuilder(Material.IRON_BOOTS).setUnbreakable().hideUnbreakable().toItemStack());
                    return;
                }
                switch (protectionTier) {
                    case ONE:
                        p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                        p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                        p.getInventory().setLeggings(new ItemBuilder(Material.IRON_LEGGINGS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                        p.getInventory().setBoots(new ItemBuilder(Material.IRON_BOOTS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                        break;
                    case TWO:
                        p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                        p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                        p.getInventory().setLeggings(new ItemBuilder(Material.IRON_LEGGINGS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                        p.getInventory().setBoots(new ItemBuilder(Material.IRON_BOOTS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                        break;
                    case THREE:
                        p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                        p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                        p.getInventory().setLeggings(new ItemBuilder(Material.IRON_LEGGINGS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                        p.getInventory().setBoots(new ItemBuilder(Material.IRON_BOOTS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                        break;
                    case FOUR:
                        p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                        p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                        p.getInventory().setLeggings(new ItemBuilder(Material.IRON_LEGGINGS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                        p.getInventory().setBoots(new ItemBuilder(Material.IRON_BOOTS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                        break;
                }
                break;
            case DIAMOND:
                if (protectionTier == null) {
                    p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().toItemStack());
                    p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().toItemStack());
                    p.getInventory().setLeggings(new ItemBuilder(Material.DIAMOND_LEGGINGS).setUnbreakable().hideUnbreakable().toItemStack());
                    p.getInventory().setBoots(new ItemBuilder(Material.DIAMOND_BOOTS).setUnbreakable().hideUnbreakable().toItemStack());
                    return;
                }
                switch (protectionTier) {
                    case ONE:
                        p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                        p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                        p.getInventory().setLeggings(new ItemBuilder(Material.DIAMOND_LEGGINGS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                        p.getInventory().setBoots(new ItemBuilder(Material.DIAMOND_BOOTS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                        break;
                    case TWO:
                        p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                        p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                        p.getInventory().setLeggings(new ItemBuilder(Material.DIAMOND_LEGGINGS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                        p.getInventory().setBoots(new ItemBuilder(Material.DIAMOND_BOOTS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                        break;
                    case THREE:
                        p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                        p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                        p.getInventory().setLeggings(new ItemBuilder(Material.DIAMOND_LEGGINGS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                        p.getInventory().setBoots(new ItemBuilder(Material.DIAMOND_BOOTS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                        break;
                    case FOUR:
                        p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                        p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                        p.getInventory().setLeggings(new ItemBuilder(Material.DIAMOND_LEGGINGS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                        p.getInventory().setBoots(new ItemBuilder(Material.DIAMOND_BOOTS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                        break;
                }
                break;
            case NETHERITE:
                if (protectionTier == null) {

                    p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().toItemStack());
                    p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().toItemStack());
                    p.getInventory().setLeggings(new ItemBuilder(Material.NETHERITE_LEGGINGS).setUnbreakable().hideUnbreakable().toItemStack());
                    p.getInventory().setBoots(new ItemBuilder(Material.NETHERITE_BOOTS).setUnbreakable().hideUnbreakable().toItemStack());
                    return;
                }
                switch (protectionTier) {
                    case ONE:
                        p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                        p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                        p.getInventory().setLeggings(new ItemBuilder(Material.NETHERITE_LEGGINGS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                        p.getInventory().setBoots(new ItemBuilder(Material.NETHERITE_BOOTS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
                        break;
                    case TWO:
                        p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                        p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                        p.getInventory().setLeggings(new ItemBuilder(Material.NETHERITE_LEGGINGS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                        p.getInventory().setBoots(new ItemBuilder(Material.NETHERITE_BOOTS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
                        break;
                    case THREE:
                        p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                        p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                        p.getInventory().setLeggings(new ItemBuilder(Material.NETHERITE_LEGGINGS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                        p.getInventory().setBoots(new ItemBuilder(Material.NETHERITE_BOOTS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
                        break;
                    case FOUR:
                        p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                        p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                        p.getInventory().setLeggings(new ItemBuilder(Material.NETHERITE_LEGGINGS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                        p.getInventory().setBoots(new ItemBuilder(Material.NETHERITE_BOOTS).setUnbreakable().hideUnbreakable().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).toItemStack());
                        break;
                }
                break;
        }


    }

    public void giveTeamSelector(Player p) {
        p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        p.getInventory().setItemInOffHand(new ItemStack(Material.AIR));

        Island island = gameManager.getGameWorld().getIsland(p);
        Material wool = Material.WHITE_WOOL;
        if (island != null) {
            wool = island.getColor().getTeamWool();
        }

        p.getInventory().addItem(new ItemBuilder(wool).setName(Color.str("&r选择队伍 &7(右键点击)")).toItemStack());

    }


}
