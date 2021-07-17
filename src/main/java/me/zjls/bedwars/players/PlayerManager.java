package me.zjls.bedwars.players;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.tasks.PlayerRespawn;
import me.zjls.bedwars.utils.Bedwars;
import me.zjls.bedwars.utils.Color;
import me.zjls.bedwars.utils.ItemBuilder;
import me.zjls.bedwars.worlds.Island;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class PlayerManager {

    private GameManager gameManager;

    public PlayerManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void setSpectator(Player p) {
        p.getInventory().clear();

        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        p.teleport(gameManager.getGameWorld().getWaitingLobbyLocation());
        p.setGameMode(GameMode.SPECTATOR);

        Island island = gameManager.getGameWorld().getIsland(p);

        if (island.isBedPlaced()) {
            BukkitTask task = Bukkit.getServer().getScheduler().runTaskTimer(gameManager.getPlugin(), new PlayerRespawn(gameManager, p, island), 0, 20);
            Bukkit.getServer().getScheduler().runTaskLater(gameManager.getPlugin(), task::cancel, 20 * 6);
        } else {
            p.sendTitle("§c你死了", "§r您已变成一名旁观者", 20, 40, 20);
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

        Island island = gameManager.getGameWorld().getIsland(p);
        if (island == null || !island.isBedPlaced()) {
            setSpectator(p);
            return;
        }

        p.setGameMode(GameMode.SURVIVAL);
        p.teleport(island.getSpawnLocation());

        giveTeamArmor(p, island);

        ItemStack woodSword = new ItemStack(Material.WOODEN_SWORD);
        ItemMeta woodSwordMeta = woodSword.getItemMeta();
        woodSwordMeta.setUnbreakable(true);

        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "attack_speed", 1000, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "attack_damage", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        woodSwordMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier);
        woodSwordMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier2);
        woodSwordMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        woodSwordMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        woodSword.setItemMeta(woodSwordMeta);

        p.getInventory().addItem(woodSword);

        //todo:升级

    }

    public void giveTeamArmor(Player p, Island island) {
        p.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().toItemStack());
        p.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().toItemStack());
        p.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().toItemStack());
        p.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(island.getColor().getColor()).setUnbreakable().hideUnbreakable().toItemStack());

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
