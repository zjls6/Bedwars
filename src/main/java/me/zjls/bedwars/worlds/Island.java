package me.zjls.bedwars.worlds;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import me.zjls.bedwars.teams.TeamColor;
import me.zjls.bedwars.worlds.generators.Generator;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class Island {

    private GameWorld gameWorld;

    private TeamColor color;

    //左下前方
    private Location protectCorner1 = null;
    //右后上方
    private Location protectCorner2 = null;

    private Location upgradeLocation = null;
    private Location shopLocation = null;
    private Location bedLocation = null;
    private Location spawnLocation = null;

    private List<Generator> generatorList = new ArrayList<>();

    public Island(GameWorld gameWorld, TeamColor color) {
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

    public TeamColor getColor() {
        return color;
    }

    public void addGenerator(Generator generator) {
        this.generatorList.add(generator);
    }

    //if the block in protected zone
    public boolean isProtected(Block block) {
        Location blockLocation = block.getLocation();

        BlockVector3 one = BlockVector3.at(protectCorner1.getX(), protectCorner1.getY(), protectCorner1.getZ());
        BlockVector3 two = BlockVector3.at(protectCorner2.getX(), protectCorner2.getY(), protectCorner2.getZ());

        CuboidRegion region = new CuboidRegion(one, two);

        return region.contains(BlockVector3.at(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ()));
    }
}
