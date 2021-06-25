package me.zjls.bedwars.worlds;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import me.zjls.bedwars.worlds.generators.Generator;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.List;

public class Island {

    private GameWorld gameWorld;
    //左下前方
    private Location protectCorner1;
    //右后上方
    private Location protectCorner2;

    private Location upgradeLocation;
    private Location shopLocation;
    private Location BedLocation;
    private Location spawnLocation;

    private List<Generator> generatorList;

    public Location getProtectCorner1() {
        return protectCorner1;
    }

    public Location getProtectCorner2() {
        return protectCorner2;
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public Location getUpgradeLocation() {
        return upgradeLocation;
    }

    public Location getShopLocation() {
        return shopLocation;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Location getBedLocation() {
        return BedLocation;
    }

    public List<Generator> getGenerators() {
        return generatorList;
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
