package me.zjls.bedwars.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Good {

    private ItemStack item;

    private List<String> itemLore = new ArrayList<>();

    private String displayName;

    private int ironAmount;
    private int goldAmount;
    private int emeraldAmount;
    private int diamondAmount;

    private int id;
    private Player p;
    private boolean hasEnoughResource = false;

//    public Good(ItemStack item, int ironAmount) {
//        this(item, ironAmount, 0, 0, 0);
//    }

    public Good(ItemStack item, String displayName, int ironAmount, int goldAmount, int emeraldAmount, int diamondAmount) {
        this.item = item;
        this.displayName = displayName;
        this.ironAmount = ironAmount;
        this.goldAmount = goldAmount;
        this.emeraldAmount = emeraldAmount;
        this.diamondAmount = diamondAmount;
    }

    public Good(ItemStack item, String displayName, List<String> itemLore, int ironAmount, int goldAmount, int emeraldAmount, int diamondAmount, int id, Player p) {
        this.item = item;
        this.displayName = displayName;
        this.itemLore = itemLore;
        this.ironAmount = ironAmount;
        this.goldAmount = goldAmount;
        this.emeraldAmount = emeraldAmount;
        this.diamondAmount = diamondAmount;
        this.id = id;
        this.p = p;
    }

    public List<String> getCostLore() {
        List<String> cost = new ArrayList<>();
        cost.add(Color.str("&7花费："));
        if (ironAmount != 0) {
            if (p.getInventory().containsAtLeast(new ItemStack(Material.IRON_INGOT), ironAmount)) {
                hasEnoughResource = true;
            }
            cost.add(Color.str("&f" + ironAmount + " 铁锭"));
        }
        if (goldAmount != 0) {
            if (p.getInventory().containsAtLeast(new ItemStack(Material.GOLD_INGOT), goldAmount)) {
                hasEnoughResource = true;
            }
            cost.add(Color.str("&6" + goldAmount + " 金锭"));
        }
        if (emeraldAmount != 0) {
            if (p.getInventory().containsAtLeast(new ItemStack(Material.EMERALD), emeraldAmount)) {
                hasEnoughResource = true;
            }
            cost.add(Color.str("&a" + emeraldAmount + " 绿宝石"));
        }
        if (diamondAmount != 0) {
            if (p.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND), diamondAmount)) {
                hasEnoughResource = true;
            }
            cost.add(Color.str("&b" + diamondAmount + " 钻石"));
        }
        return cost;
    }

    public List<String> getLore() {
        List<String> lores = new ArrayList<>(Color.str(itemLore));
        if (item.getType().equals(Material.RED_STAINED_GLASS_PANE)) {
            List<String> finalLore = new ArrayList<>(lores);
            return finalLore;
        }
        List<String> finalLore = new ArrayList<>(getCostLore());
        finalLore.add("");
        finalLore.addAll(lores);
        finalLore.add("");
        finalLore.add(Color.str("&bShift+左键添加至快速购买"));
        if (!hasEnoughResource) {
            finalLore.add("");
            finalLore.add(Color.str("&c你没有足够的资源！"));
        }
        return finalLore;
    }

    public ItemStack getItem() {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Color.str(displayName));
        List<String> lore = getLore();
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }


}
