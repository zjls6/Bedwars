package me.zjls.bedwars.gui.types;

import me.zjls.bedwars.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ShopType {

    MAIN,
    BLOCK,
    SWORD,
    ARMOR,
    TOOLS,
    BOW,
    POTIONS,
    UTILITY,
    GLASS;

    public String getName() {
        switch (this) {
            case MAIN:
                return "快捷购买";
            case BLOCK:
                return "方块";
            case SWORD:
                return "近战武器";
            case ARMOR:
                return "盔甲";
            case TOOLS:
                return "工具";
            case BOW:
                return "远程武器";
            case POTIONS:
                return "药水";
            case UTILITY:
                return "实用道具";
        }
        return "";
    }

    public ItemStack getItemStack() {
        switch (this) {
            case MAIN:
                return new ItemBuilder(Material.NETHER_STAR).setName("&a" + getName()).setLore("&e点击查看！").hideAttributes().toItemStack();
            case BLOCK:
                return new ItemBuilder(Material.TERRACOTTA).setName("&a" + getName()).setLore("&e点击查看！").hideAttributes().toItemStack();
            case SWORD:
                return new ItemBuilder(Material.GOLDEN_SWORD).setName("&a" + getName()).setLore("&e点击查看！").hideAttributes().toItemStack();
            case ARMOR:
                return new ItemBuilder(Material.CHAINMAIL_BOOTS).setName("&a" + getName()).setLore("&e点击查看！").hideAttributes().toItemStack();
            case TOOLS:
                return new ItemBuilder(Material.STONE_PICKAXE).setName("&a" + getName()).setLore("&e点击查看！").hideAttributes().toItemStack();
            case BOW:
                return new ItemBuilder(Material.BOW).setName("&a" + getName()).setLore("&e点击查看！").hideAttributes().toItemStack();
            case POTIONS:
                return new ItemBuilder(Material.POTION).setName("&a" + getName()).setLore("&e点击查看！").hideAttributes().hidePotionEffects().toItemStack();
            case UTILITY:
                return new ItemBuilder(Material.TNT).setName("&a" + getName()).setLore("&e点击查看！").hideAttributes().toItemStack();
        }
        return null;
    }

}
