package me.zjls.bedwars.utils;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.games.GameState;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Bedwars {

    private GameManager gameManager;

    public Bedwars(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public static void bc(String msg) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(msg);
        }
    }

    public static ItemStack swordItem(Material swordType, double damage) {
        ItemStack woodSword = new ItemStack(swordType);
        ItemMeta woodSwordMeta = woodSword.getItemMeta();
        woodSwordMeta.setUnbreakable(true);

        AttributeModifier attackSpeed = new AttributeModifier(UUID.randomUUID(), "attack_speed", 1000, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        AttributeModifier attackDamage = new AttributeModifier(UUID.randomUUID(), "attack_damage", damage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        woodSwordMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeed);
        woodSwordMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, attackDamage);
        woodSwordMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        woodSwordMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        woodSword.setItemMeta(woodSwordMeta);
        return woodSword;
    }

    public static Location getLocation(Location location, int x, int y, int z) {
        Location loc = location.getBlock().getLocation();
        loc.add(x, y, z);
        return loc;
    }

    public static void takeItem(Player player, ItemStack item) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType() == item.getType()) {
            itemInHand.setAmount(itemInHand.getAmount() - 1);
            player.getInventory().setItemInMainHand(itemInHand);
            return;
        } else {
            ItemStack itemInOffHand = player.getInventory().getItemInOffHand();
            if (itemInOffHand.getType() == item.getType()) {
                itemInOffHand.setAmount(itemInOffHand.getAmount() - 1);
                player.getInventory().setItemInOffHand(itemInOffHand);
                return;
            }
        }
        ItemMeta meta = item.getItemMeta();
        ItemStack itemStack = new ItemStack(item.getType(), 1);
        itemStack.setItemMeta(meta);
        player.getInventory().removeItem(itemStack);
    }

    public String getDeathsMessages(Player player, EntityDamageEvent.DamageCause damageCause) {
        if (!gameManager.getPlayerInGame().contains(player.getUniqueId()) || !gameManager.getState().equals(GameState.ACTIVE)) {
            return null;
        }
        try {
            if (player.getKiller() != null) {
                Player killer = player.getKiller();
                if (damageCause == EntityDamageEvent.DamageCause.VOID) {
                    List<String> deathMessages = new ArrayList<>();
                    deathMessages.add(Color.str(player.getName() + " 被 " + killer.getName() + " 打下了虚空"));
                    deathMessages.add(Color.str(player.getName() + " 被 " + killer.getName() + " 打入了虚空，真是太绝望了"));
                    deathMessages.add(Color.str(player.getName() + " 被 " + killer.getName() + " 打入了虚空，真是太绝望了！！！！！"));
                    int randomIndex = ThreadLocalRandom.current().nextInt(deathMessages.size()) % deathMessages.size();


                    return deathMessages.get(randomIndex);
                }
                if (damageCause == EntityDamageEvent.DamageCause.PROJECTILE) {
                    List<String> deathMessages = new ArrayList<>();
                    deathMessages.add(Color.str(player.getName() + " 被 " + killer.getName() + " 的火球炸死了"));
                    deathMessages.add(Color.str(player.getName() + " 被 " + killer.getName() + " 的火球炸死了，真是太绝望了"));
                    deathMessages.add(Color.str(player.getName() + " 被 " + killer.getName() + " 的火球炸死了，真是太绝望了！！！！！"));
                    int randomIndex = ThreadLocalRandom.current().nextInt(deathMessages.size()) % deathMessages.size();

                    return deathMessages.get(randomIndex);
                }
                if (damageCause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    List<String> deathMessages = new ArrayList<>();
                    deathMessages.add(Color.str(player.getName() + " 被 " + killer.getName() + " 打死了"));
                    deathMessages.add(Color.str(player.getName() + " 被 " + killer.getName() + " 虐杀，真是太绝望了"));
                    deathMessages.add(Color.str(player.getName() + " 被 " + killer.getName() + " 的骚操作秀倒了，真是太绝望了！！！！！"));
                    int randomIndex = ThreadLocalRandom.current().nextInt(deathMessages.size()) % deathMessages.size();
                    player.getKiller().playSound(player.getKiller().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    return deathMessages.get(randomIndex);
                }
            }
            if (damageCause == EntityDamageEvent.DamageCause.VOID) {
                List<String> deathMessages = new ArrayList<>();
                deathMessages.add(Color.str(player.getName() + " 掉入了虚空"));
                deathMessages.add(Color.str(player.getName() + " 滑入了虚空！"));
                deathMessages.add(Color.str(player.getName() + " 脚底抹了油，掉入了虚空"));
                int randomIndex = ThreadLocalRandom.current().nextInt(deathMessages.size()) % deathMessages.size();

                return deathMessages.get(randomIndex);
            }
            List<String> deathMessages = new ArrayList<>();
            deathMessages.add(Color.str(player.getName() + " 死了"));
            deathMessages.add(Color.str(player.getName() + " 不知道怎么就死了！"));
            deathMessages.add(Color.str(player.getName() + " 人没了"));
            int randomIndex = ThreadLocalRandom.current().nextInt(deathMessages.size()) % deathMessages.size();

            return deathMessages.get(randomIndex);
        } catch (Exception ex) {
            List<String> deathMessages = new ArrayList<>();
            deathMessages.add(Color.str(player.getName() + " 死了"));
            deathMessages.add(Color.str(player.getName() + " 不知道怎么就死了！"));
            deathMessages.add(Color.str(player.getName() + " 人没了"));
            int randomIndex = ThreadLocalRandom.current().nextInt(deathMessages.size()) % deathMessages.size();

            return deathMessages.get(randomIndex);
        }
    }

    public String getProgressBar(int current, int max, int totalBars) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return StringUtils.repeat(Color.str("&b|"), progressBars)
                + StringUtils.repeat(Color.str("&7|"), totalBars - progressBars);
    }

}
