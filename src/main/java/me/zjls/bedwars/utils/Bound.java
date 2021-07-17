package me.zjls.bedwars.utils;

import org.bukkit.Location;

public class Bound {

    private Location min,max;

    public Bound() {
    }

    public Bound(Location min, Location max) {
        this.min = min;
        this.max = max;
    }

    public Location getMin() {
        return min;
    }

    public void setMin(Location min) {
        this.min = min;
    }

    public Location getMax() {
        return max;
    }

    public void setMax(Location max) {
        this.max = max;
    }
}
