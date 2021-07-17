package me.zjls.bedwars.worlds.generators;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.utils.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Generator {

    public GeneratorTier currentTier = GeneratorTier.ONE;
    public boolean isInIsland;
    private GameManager gameManager;
    private Location location;
    private GeneratorType type;
    private Hologram hologram = null;

    private int secSinceActive = 0;
    //资源是否开始生成
    private boolean active = false;

    public Generator(GameManager gameManager, Location location, GeneratorType type, boolean isInIsland) {
        this.gameManager = gameManager;
        this.location = location;
        this.type = type;
        this.isInIsland = isInIsland;
    }

    public void spawn() {
        if (type.equals(GeneratorType.DIAMOND) && isInIsland) {
            return;//baned
        }
        if (!active) {
            if (hologram != null) {
                hologram.getVisibilityManager().setVisibleByDefault(false);
            }
            return;
        }

        secSinceActive += 1;

        if (!isInIsland) {
            updateHologram();
        }

        if (secSinceActive != getActiveTime()) {
            return;
        }

        secSinceActive = 0;

        switch (type) {
            case IRON:
                Item iron = location.getWorld().dropItem(location.clone().add(0, 1, 0), new ItemStack(Material.IRON_INGOT));
                iron.setVelocity(new Vector(0, 0, 0));
                break;
            case GOLD:
                Item gold = location.getWorld().dropItem(location.clone().add(0, 1, 0), new ItemStack(Material.GOLD_INGOT));
                gold.setVelocity(new Vector(0, 0, 0));
                break;
            case DIAMOND:
                Item diamond = location.getWorld().dropItem(location.clone().add(0, 1, 0), new ItemStack(Material.DIAMOND));
                diamond.setVelocity(new Vector(0, 0, 0));
                break;
            case EMERALD:
                Item emerald = location.getWorld().dropItem(location.clone().add(0, 1, 0), new ItemStack(Material.EMERALD));
                emerald.setVelocity(new Vector(0, 0, 0));
                break;
            default:

        }
    }

    public void setActive(boolean active) {
//        hologram.getVisibilityManager().setVisibleByDefault(true);
        if (active == this.active) {
            return;
        }
        this.active = active;

        if (isInIsland) {
            return;
        }

        if (!active && hologram != null) {
            hologram.delete();
            return;
        }

        hologram = HologramsAPI.createHologram(gameManager.getPlugin(), location.clone().add(0, 4, 0));
        hologram.appendTextLine(Color.str("&e等级&c" + currentTier.getName())); //0 可变
        hologram.appendTextLine(Color.str(type.getChatColor() + "&l" + type.getName())); //1 不变
        int timeLeft = (getActiveTime() - secSinceActive);
        if (timeLeft == 0) {
            timeLeft = 30;
        }
        hologram.appendTextLine(Color.str("&e将在 &c" + timeLeft + " &e秒后产出")); //2 可变
        hologram.appendItemLine(type.getItem()); //3 不变

    }

    private void updateHologram() {
        hologram.removeLine(0);
        hologram.insertTextLine(0, Color.str("&e等级 &c" + currentTier.getName()));
        hologram.removeLine(2);
        int timeLeft = (getActiveTime() - secSinceActive);
        if (timeLeft == 0) {
            timeLeft = getActiveTime();
        }
        hologram.insertTextLine(2, Color.str("&e将在 &c" + timeLeft + " &e秒后产出"));
    }


    private int getActiveTime() {
        switch (type) {
            case IRON:
                if (currentTier.equals(GeneratorTier.ONE)) {
                    return 4; //s
                } else if (currentTier.equals(GeneratorTier.TWO)) {
                    return 2;
                } else {
                    return 1;
                }
            case GOLD:
                if (currentTier.equals(GeneratorTier.ONE)) {
                    return 16;
                } else if (currentTier.equals(GeneratorTier.TWO)) {
                    return 8;
                } else {
                    return 4;
                }
            case DIAMOND:
                if (currentTier.equals(GeneratorTier.ONE)) {
                    return 30;
                } else if (currentTier.equals(GeneratorTier.TWO)) {
                    return 20;
                } else {
                    return 15;
                }
            case EMERALD:
                if (isInIsland) {
                    if (currentTier.equals(GeneratorTier.ONE)) {
                        return 60;
                    } else if (currentTier.equals(GeneratorTier.TWO)) {
                        return 40;
                    }
                } else {
                    if (currentTier.equals(GeneratorTier.ONE)) {
                        return 65;
                    } else if (currentTier.equals(GeneratorTier.TWO)) {
                        return 45;
                    } else {
                        return 30;
                    }
                }
        }
        return 20;
    }


    public Hologram getHologram() {
        return hologram;
    }

    public Location getLocation() {
        return location;
    }

    public GeneratorType getType() {
        return type;
    }

    public void setTier(GeneratorTier tier) {
        if (currentTier != tier) {
            this.currentTier = tier;
            setSecSinceActive(0);
        }
    }

    public void setSecSinceActive(int secSinceActive) {
        this.secSinceActive = secSinceActive;
    }
}
