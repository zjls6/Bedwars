package me.zjls.bedwars.gui.types;

import me.zjls.bedwars.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum TrapType {

    TRAP, ATTACK, WARN, MINING;

    public ItemStack getItem(int i) {
        switch (this) {
            case TRAP:
                return new ItemBuilder(Material.TRIPWIRE_HOOK).setName("&c陷阱 &6#" + i + "&7：&a这是个陷阱！")
                        .setLore(Arrays.asList("&7对来基地的敌人造成", "&7失明与缓慢效果", "&7持续 &68 &7秒", "", "&7第" + format(i) + "个进入我方基地", "&7的敌人会触发此陷阱！")).toItemStack();
            case ATTACK:
                return new ItemBuilder(Material.TRIPWIRE_HOOK).setName("&c陷阱 &6#" + i + "&7：&a反击陷阱")
                        .setLore(Arrays.asList("&7给予基地附近的队友", "&7速度 &6I &7与跳跃提升 &6II", "&7效果持续 &610 &7秒", "", "&7第" + format(i) + "个进入我方基地", "&7的敌人会触发此陷阱！")).toItemStack();
            case WARN:
                return new ItemBuilder(Material.TRIPWIRE_HOOK).setName("&c陷阱 &6#" + i + "&7：&a报警陷阱")
                        .setLore(Arrays.asList("&7显示隐身的玩家", "&7及其名称和队伍", "", "&7第" + format(i) + "个进入我方基地", "&7的敌人会触发此陷阱！")).toItemStack();
            case MINING:
                return new ItemBuilder(Material.TRIPWIRE_HOOK).setName("&c陷阱 &6#" + i + "&7：&a挖掘疲劳陷阱")
                        .setLore(Arrays.asList("&7对来基地的敌人造成", "&7挖掘疲劳效果", "&7持续 &610 &7秒", "", "&7第" + format(i) + "个进入我方基地", "&7的敌人会触发此陷阱！")).toItemStack();
        }
        return null;
    }

    public String format(int i) {
        switch (i) {
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            default:
                return "";
        }
    }
}
