package me.zjls.bedwars.worlds;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import lombok.Getter;
import lombok.Setter;
import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.gui.types.TrapType;
import me.zjls.bedwars.utils.Color;
import me.zjls.bedwars.worlds.generators.Generator;
import me.zjls.bedwars.worlds.generators.GeneratorType;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class Island {

    private GameManager gameManager;
    private GameWorld gameWorld;
    private IslandColor color;

    private List<UUID> players = new ArrayList<>();
    private List<UUID> absolutelyAlive = new ArrayList<>();

    private List<TrapType> trapList = new ArrayList<>();

    //左下前方
    private Location protectCorner1 = null;
    //右后上方
    private Location protectCorner2 = null;

    private Location baseCorner1 = null;
    private Location baseCorner2 = null;

    private Location upgradeLocation = null;
    private Location shopLocation = null;
    private Location bedLocation = null;
    private Location spawnLocation = null;

    private List<Generator> generatorList = new ArrayList<>();

    private NPC itemShop = null;
    private NPC upgradeShop = null;

    private Hologram itemShopHologram = null;
    private Hologram upgradeHologram = null;

    public Island(GameManager gameManager, GameWorld gameWorld, IslandColor color) {
        this.gameManager = gameManager;
        this.gameWorld = gameWorld;
        this.color = color;
    }

    public Location getProtectCorner1() {
        return protectCorner1;
    }

    public void setProtectCorner1(Location protectCorner1) {
        this.protectCorner1 = protectCorner1;
    }

    public Location getProtectCorner2() {
        return protectCorner2;
    }

    public void setProtectCorner2(Location protectCorner2) {
        this.protectCorner2 = protectCorner2;
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public Location getUpgradeLocation() {
        return upgradeLocation;
    }

    public void setUpgradeLocation(Location upgradeLocation) {
        this.upgradeLocation = upgradeLocation;
    }

    public Location getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(Location shopLocation) {
        this.shopLocation = shopLocation;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public Location getBedLocation() {
        return bedLocation;
    }

    public void setBedLocation(Location bedLocation) {
        this.bedLocation = bedLocation;
    }

    public List<Generator> getGenerators() {
        return generatorList;
    }

    public void setGenerators(List<Generator> generators) {
        this.generatorList = generators;

        for (Generator generator : generators) {
            if (generator.getType().equals(GeneratorType.EMERALD) && generator.isInIsland) {
                return;
            }
            generator.setActive(true);
        }
    }

    public void activeEmeraldGenerators() {
        for (Generator generator : getGenerators()) {
            if (generator.getType().equals(GeneratorType.EMERALD)) {
                generator.setActive(true);
            }
        }
    }

    public IslandColor getColor() {
        return color;
    }

    public int alivePlayerCount() {
        if (isBedPlaced()) {
            return players.size();
        }

        List<UUID> alive = players.stream().filter(uuid -> {
            Player p = Bukkit.getPlayer(uuid);
            if (p == null) {
                return false;
            }
            return p.getGameMode().equals(GameMode.SURVIVAL);
        }).collect(Collectors.toList());
        int count = alive.size();

        for (UUID alivePlayer : absolutelyAlive) {
            if (alive.stream().noneMatch(uuid -> uuid.equals(alivePlayer))) {
                if (Bukkit.getOfflinePlayer(alivePlayer).isOnline()) {
                    count += 1;
                } else {
                    absolutelyAlive.remove(alivePlayer);
                }
            }
        }
        return count;
    }

    public List<UUID> getAbsolutelyAlive() {
        return absolutelyAlive;
    }

    public void addGenerator(Generator generator) {
        this.generatorList.add(generator);
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public boolean isMember(Player p) {
        return players.contains(p.getUniqueId());
    }

    public boolean isBedPlaced() {
        return getBedLocation().getBlock().getType().name().contains("BED");
    }

    //if the block in protected zone
    public boolean isProtected(Block block) {
        Location blockLocation = block.getLocation();

        BlockVector3 one = BlockVector3.at(protectCorner1.getX(), protectCorner1.getY(), protectCorner1.getZ());
        BlockVector3 two = BlockVector3.at(protectCorner2.getX(), protectCorner2.getY(), protectCorner2.getZ());

        CuboidRegion region = new CuboidRegion(one, two);

        return region.contains(BlockVector3.at(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ()));
    }

    public boolean isInBase(Player p) {
        Location location = p.getLocation();

        BlockVector3 one = BlockVector3.at(baseCorner1.getX(), baseCorner1.getY(), baseCorner1.getZ());
        BlockVector3 two = BlockVector3.at(baseCorner2.getX(), baseCorner2.getY(), baseCorner2.getZ());

        CuboidRegion region = new CuboidRegion(one, two);

        return region.contains(BlockVector3.at(location.getX(), location.getY(), location.getZ()));
    }

    public void spawnShops() {
        itemShop = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, Color.str("&e右键点击"));
        itemShop.spawn(shopLocation);
        itemShop.getOrAddTrait(SkinTrait.class).setSkinName("zjls6", true);
//        me.zjls.bedwars.utils.NPC.createNPC(shopLocation, "&e右键点击");

        itemShopHologram = HologramsAPI.createHologram(gameManager.getPlugin(), shopLocation.clone().add(0, 2.6, 0));
        itemShopHologram.appendTextLine(Color.str("&b物品商店"));

        upgradeShop = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, Color.str("&e右键点击"));
        upgradeShop.spawn(upgradeLocation);

        upgradeHologram = HologramsAPI.createHologram(gameManager.getPlugin(), upgradeLocation.clone().add(0, 2.6, 0));
        upgradeHologram.appendTextLine(Color.str("&b升级商店"));

    }


    public NPC getItemShop() {
        return itemShop;
    }

    public NPC getUpgradeShop() {
        return upgradeShop;
    }
}
