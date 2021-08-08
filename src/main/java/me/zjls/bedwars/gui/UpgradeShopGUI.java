package me.zjls.bedwars.gui;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.gui.types.ProtectionTier;
import me.zjls.bedwars.utils.Color;
import me.zjls.bedwars.utils.ItemBuilder;
import me.zjls.bedwars.worlds.Island;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class UpgradeShopGUI {

    private GameManager gameManager;
    private Inventory upgradeInventory;
    private Inventory trapInventory;
    private Player p;

    public UpgradeShopGUI(GameManager gameManager, Player p) {
        this.gameManager = gameManager;
        this.p = p;

        openMainGUI();
    }

    public void openMainGUI() {
        Island island = gameManager.getGameWorld().getIsland(p);
        if (island == null) {
            return;
        }

        upgradeInventory = Bukkit.createInventory(null, 45, Color.str(Color.str("&r升级与陷阱商店")));

        setUpgradeInventory(island);
        for (int i = 18; i < 27; i++) {
            upgradeInventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("&8⬆ &7队伍升级").setLore("&8⬇ &7陷阱队列").toItemStack());
        }

        p.openInventory(upgradeInventory);
    }

    public void setUpgradeInventory(Island island) {

        if (gameManager.getSharpnessTeam().size() == 0) {
            upgradeInventory.setItem(10, new ItemBuilder(Material.IRON_SWORD).hideAttributes().setName("&c锋利附魔")
                    .setLore(Arrays.asList("&7我方队员的剑和斧", "&7将获得永久的 &6锋利I &7附魔", "", "&7花费：&b4 钻石")).toItemStack());
        } else {
            for (Island sharpIsland : gameManager.getSharpnessTeam()) {
                if (island.equals(sharpIsland)) {
                    upgradeInventory.setItem(10, new ItemBuilder(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1)
                            .hideEnchants().hideAttributes().setName("&c锋利附魔")
                            .setLore(Arrays.asList("&7我方队员的剑和斧", "&7将获得永久的 &6锋利I &7附魔", "", "&a已解锁")).toItemStack());
                } else {
                    upgradeInventory.setItem(10, new ItemBuilder(Material.IRON_SWORD).hideAttributes().setName("&c锋利附魔")
                            .setLore(Arrays.asList("&7我方队员的剑和斧", "&7将获得永久的 &6锋利I &7附魔", "", "&7花费：&b4 钻石")).toItemStack());
                }
            }
        }

        ProtectionTier protectionTier = gameManager.getIslandProtectionTierMap().get(island);
        if (protectionTier == null) {
            upgradeInventory.setItem(11, new ItemBuilder(Material.IRON_CHESTPLATE).hideAttributes().setName("&f装备强化&cI")
                    .setLore(Arrays.asList("&7我方队员的盔甲", "&7将获得永久的 &6保护 &7附魔", "", "&7Lv.1：&6保护I &7- &b2 钻石"
                            , "&7Lv.2：&6保护II &7- &b4 钻石", "&7Lv.3：&6保护III &7- &b8 钻石", "&7Lv.4：&6保护IV &7- &b16 钻石")).toItemStack());
        } else {
            switch (protectionTier) {
                default:
                    upgradeInventory.setItem(11, new ItemBuilder(Material.IRON_CHESTPLATE).hideAttributes().setName("&f装备强化&cI")
                            .setLore(Arrays.asList("&7我方队员的盔甲", "&7将获得永久的 &6保护 &7附魔", "", "&7Lv.1：&6保护I &7- &b2 钻石"
                                    , "&7Lv.2：&6保护II &7- &b4 钻石", "&7Lv.3：&6保护III &7- &b8 钻石", "&7Lv.4：&6保护IV &7- &b16 钻石")).toItemStack());
                case ONE:
                    upgradeInventory.setItem(11, new ItemBuilder(Material.IRON_CHESTPLATE).hideAttributes().setName("&f装备强化&cII")
                            .setLore(Arrays.asList("&7我方队员的盔甲", "&7将获得永久的 &6保护 &7附魔", "", "&aLv.1：保护I"
                                    , "&7Lv.2：&6保护II &7- &b4 钻石", "&7Lv.3：&6保护III &7- &b8 钻石", "&7Lv.4：&6保护IV &7- &b16 钻石")).toItemStack());
                    break;
                case TWO:
                    upgradeInventory.setItem(11, new ItemBuilder(Material.IRON_CHESTPLATE).hideAttributes().setName("&f装备强化&cIII")
                            .setLore(Arrays.asList("&7我方队员的盔甲", "&7将获得永久的 &6保护 &7附魔", "", "&aLv.1：保护I"
                                    , "&aLv.2：保护II", "&7Lv.3：&6保护III &7- &b8 钻石", "&7Lv.4：&6保护IV &7- &b16 钻石")).toItemStack());
                    break;
                case THREE:
                    upgradeInventory.setItem(11, new ItemBuilder(Material.IRON_CHESTPLATE).hideAttributes().setName("&f装备强化&cIV")
                            .setLore(Arrays.asList("&7我方队员的盔甲", "&7将获得永久的 &6保护 &7附魔", "", "&aLv.1：保护I"
                                    , "&aLv.2：保护II", "&aLv.3：保护III", "&7Lv.4：&6保护IV &7- &b16 钻石")).toItemStack());
                    break;
                case FOUR:
                    upgradeInventory.setItem(11, new ItemBuilder(Material.IRON_CHESTPLATE).hideAttributes().setName("&f装备强化&a(已全部解锁)")
                            .setLore(Arrays.asList("&7我方队员的盔甲", "&7将获得永久的 &6保护 &7附魔", "", "&aLv.1：保护I"
                                    , "&aLv.2：保护II", "&aLv.3：保护III", "&aLv.4：保护IV", "", "&a已全部解锁！")).toItemStack());
                    break;
            }
        }

        upgradeInventory.setItem(12, new ItemBuilder(Material.GOLDEN_PICKAXE).setName("&f疯狂矿工")
                .setLore(Arrays.asList("&7我方队员永久获得急迫效果", "", "&7Lv.1：&6急迫I &7- &b2 钻石", "&7Lv.2：&6急迫II &7- &b4 钻石")).toItemStack());

        upgradeInventory.setItem(16, new ItemBuilder(Material.LEATHER).setName("&a购买一个陷阱")
                .setLore(Arrays.asList("&7已购买的陷阱", "&7将加入下面的队列中", "", "&e点击查看！")).toItemStack());

        upgradeInventory.setItem(30, new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS, 1).setName("&c陷阱 &6#1&7：&c无")
                .setLore(Arrays.asList("&7敌人进入我方基地时", "&7会触发该陷阱", "", "&7购买的陷阱会在此排列", "&7陷阱费用取决于排列的数量")).toItemStack());
        upgradeInventory.setItem(31, new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS, 2).setName("&c陷阱 &6#2&7：&c无")
                .setLore(Arrays.asList("&7敌人进入我方基地时", "&7会触发该陷阱", "", "&7购买的陷阱会在此排列", "&7陷阱费用取决于排列的数量")).toItemStack());
        upgradeInventory.setItem(32, new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS, 3).setName("&c陷阱 &6#3&7：&c无")
                .setLore(Arrays.asList("&7敌人进入我方基地时", "&7会触发该陷阱", "", "&7购买的陷阱会在此排列", "&7陷阱费用取决于排列的数量")).toItemStack());

    }

    public void openTrapGUI() {

        trapInventory = Bukkit.createInventory(null, 27, Color.str("&r选择陷阱"));

        trapInventory.setItem(10, new ItemBuilder(Material.TRIPWIRE_HOOK).setName("&c这是个陷阱！")
                .setLore(Arrays.asList("&7对来基地的敌人造成", "&7失明与缓慢效果", "&7持续 &68 &7秒", "", "&7花费：&b1 钻石")).toItemStack());

        trapInventory.setItem(11, new ItemBuilder(Material.FEATHER).setName("&c反击陷阱")
                .setLore(Arrays.asList("&7给予基地附近的队友", "&7速度 &6I &7与跳跃提升 &6II", "&7效果持续 &610 &7秒", "", "&7花费：&b1 钻石")).toItemStack());
        trapInventory.setItem(12, new ItemBuilder(Material.REDSTONE_TORCH).setName("&c报警陷阱")
                .setLore(Arrays.asList("&7显示隐身的玩家", "&7及其名称和队伍", "", "&7花费：&b1 钻石")).toItemStack());
        trapInventory.setItem(13, new ItemBuilder(Material.IRON_PICKAXE).setName("&c挖掘疲劳陷阱")
                .setLore(Arrays.asList("&7对来基地的敌人造成", "&7挖掘疲劳效果", "&7持续 &610 &7秒", "", "&7花费：&b1 钻石")).toItemStack());

        trapInventory.setItem(26, new ItemBuilder(Material.ARROW).setName("&a返回").setLore("&7至升级与陷阱").toItemStack());

        p.openInventory(trapInventory);
    }

}