package me.zjls.bedwars.worlds.generators;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class Generator {

    private Location location;
    private GeneratorType type;

    private boolean activate = false; //资源是否开始生成

    public Location getLocation() {
        return location;
    }

    public GeneratorType getType() {
        return type;
    }

    public void spawn(){
        if (!activate) return;
        Item item = (Item) location.getWorld().spawnEntity(location, EntityType.DROPPED_ITEM);
        switch (type){
            case IRON:
                item.setItemStack(new ItemStack(Material.IRON_INGOT));
                break;
            case GOLD:
                item.setItemStack(new ItemStack(Material.GOLD_INGOT));
                break;
            case DIAMOND:
                item.setItemStack(new ItemStack(Material.DIAMOND));
                break;
            case EMERALD:
                item.setItemStack(new ItemStack(Material.EMERALD));
                break;
        }
    }
}
