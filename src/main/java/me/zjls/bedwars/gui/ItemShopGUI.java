package me.zjls.bedwars.gui;

import lombok.Getter;
import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.gui.types.ShopType;
import me.zjls.bedwars.utils.Bedwars;
import me.zjls.bedwars.utils.Color;
import me.zjls.bedwars.utils.Good;
import me.zjls.bedwars.utils.ItemBuilder;
import me.zjls.bedwars.worlds.IslandColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

@Getter
public class ItemShopGUI {

    private GameManager gameManager;
    private Inventory inventory;
    private Player p;

    private Map<Integer, Good> products = new HashMap<>();
    private List<Good> allProducts = new ArrayList<>();

    public ItemShopGUI(GameManager gameManager, Player p) {
        this.gameManager = gameManager;
        this.p = p;
        openInventory(ShopType.MAIN);
    }

    public Map<Integer, Good> setupProducts(ShopType type) {
        products = new HashMap<>();
        if (type.equals(ShopType.MAIN)) {
            allProducts = new ArrayList<>();
            for (ShopType value : ShopType.values()) {
                if (value.equals(ShopType.MAIN)) {
                    continue;
                }
                for (Map.Entry<Integer, Good> entry : setupProducts(value).entrySet()) {
                    allProducts.add(entry.getValue());
                }
            }
//            p.sendMessage(String.valueOf(allProducts.size()));
        }

        if (type.equals(ShopType.BLOCK)) {
            IslandColor color = gameManager.getGameWorld().getIsland(p).getColor();
            Material teamWool = color.getTeamWool();
            products.put(19, new Good(new ItemStack(teamWool, 16)
                    , color.getChatColor() + color.getName() + "色羊毛", Arrays.asList("&7可用于搭桥穿越岛屿", "&7羊毛的颜色对应你的队伍颜色")
                    , 4, 0, 0, 0, 101, p));
            Material teamTerracotta = color.getTeamTerracotta();
            products.put(20, new Good(new ItemStack(teamTerracotta, 16)
                    , color.getChatColor() + color.getName() + "色陶瓦", Arrays.asList("&7用于保卫床的基础方块", "&7很容易被破坏")
                    , 16, 0, 0, 0, 102, p));
            Material teamConcrete = color.getTeamConcrete();
            products.put(21, new Good(new ItemStack(teamConcrete, 8)
                    , color.getChatColor() + color.getName() + "色混凝土", Arrays.asList("&7用于保卫床的进阶方块", "&7较容易被破坏")
                    , 16, 0, 0, 0, 103, p));
            Material teamGlass = color.getTeamGlass();
            products.put(22, new Good(new ItemStack(teamGlass, 4)
                    , color.getChatColor() + color.getName() + "色防爆玻璃", Arrays.asList("&7免疫爆炸", "&7不会被TNT和火球炸毁")
                    , 12, 0, 0, 0, 104, p));
            products.put(23, new Good(new ItemStack(Material.OAK_PLANKS, 16), "&f橡木木板"
                    , Arrays.asList("&7用于保卫床的优质的方块", "&7可有效抵御镐子的破坏")
                    , 0, 4, 0, 0, 105, p));
            products.put(24, new Good(new ItemStack(Material.LADDER, 16), "&f梯子"
                    , Arrays.asList("&7可用于救助卡在树上的猫", "&7开个玩笑")
                    , 4, 0, 0, 0, 106, p));
            products.put(25, new Good(new ItemStack(Material.END_STONE, 12), "&f末地石"
                    , Arrays.asList("&7用于保卫床的坚固方块", "&7不易被破坏")
                    , 24, 0, 0, 0, 107, p));
            products.put(28, new Good(new ItemStack(Material.OBSIDIAN, 4), "&f黑曜石"
                    , Arrays.asList("&7百分百保护你的床", "&7很难被破坏")
                    , 0, 0, 4, 0, 108, p));
            return products;
        }

        if (type.equals(ShopType.SWORD)) {
            ItemStack stoneSword = Bedwars.swordItem(Material.STONE_SWORD, 5);
            products.put(19, new Good(stoneSword, "&f石剑", Arrays.asList("&a打人利器", "&666")
                    , 10, 0, 0, 0, 201, p));
            ItemStack ironSword = Bedwars.swordItem(Material.IRON_SWORD, 6);
            products.put(20, new Good(ironSword, "&f铁剑", Arrays.asList("&a打人利器", "&666")
                    , 0, 7, 0, 0, 202, p));
            ItemStack diamondSword = Bedwars.swordItem(Material.DIAMOND_SWORD, 7);
            products.put(21, new Good(diamondSword, "&f钻石剑", Arrays.asList("&a打人利器", "&666")
                    , 10, 0, 6, 0, 203, p));
            ItemStack neitherSword = Bedwars.swordItem(Material.NETHERITE_SWORD, 8);
            products.put(22, new Good(neitherSword, "&c下界合金剑", Arrays.asList("&a打人利器", "&666")
                    , 0, 6, 6, 0, 204, p));
            return products;
        }

        if (type.equals(ShopType.ARMOR)) {
            products.put(19, new Good(new ItemStack(Material.CHAINMAIL_BOOTS)
                    , "&f永久的 锁链护甲", Arrays.asList("&7每次重生时会获得", "&7锁链护腿和锁链靴子")
                    , 40, 0, 0, 0, 301, p));
            products.put(20, new Good(new ItemStack(Material.IRON_BOOTS)
                    , "&f永久的 铁护甲", Arrays.asList("&7每次重生时会获得", "&7铁护腿和铁靴子")
                    , 0, 12, 0, 0, 302, p));
            products.put(21, new Good(new ItemStack(Material.DIAMOND_BOOTS)
                    , "&f永久的 钻石护甲", Arrays.asList("&7每次重生时会获得", "&7钻石护腿和钻石靴子")
                    , 40, 0, 4, 0, 303, p));
            products.put(22, new Good(new ItemStack(Material.NETHERITE_BOOTS)
                    , "&f永久的 下界合金护甲", Arrays.asList("&7每次重生时会获得", "&7下界合金护腿和下界合金靴子")
                    , 40, 4, 6, 0, 304, p));
            return products;
        }
        if (type.equals(ShopType.TOOLS)) {
            products.put(19, new Good(new ItemStack(Material.SHEARS), "&f剪刀", Arrays.asList("&7可以快速破坏羊毛", "&7剪断敌人的羊毛桥吧")
                    , 20, 0, 0, 0, 401, p));
            products.put(20, new Good(new ItemBuilder(Material.WOODEN_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 1)
                    .hideAttributes().setUnbreakable().hideUnbreakable().toItemStack(), "&f木镐", Arrays.asList("&a好用的工具", "&666")
                    , 10, 0, 0, 0, 402, p));
            products.put(21, new Good(new ItemBuilder(Material.WOODEN_AXE).addEnchant(Enchantment.DIG_SPEED, 1)
                    .hideAttributes().setUnbreakable().hideUnbreakable().toItemStack(), "&f木斧", Arrays.asList("&a好用的工具", "&666")
                    , 10, 0, 0, 0, 403, p));
            products.put(22, new Good(new ItemStack(Material.STONE_PICKAXE), "&f石镐", Arrays.asList("&a好用的工具", "&666")
                    , 20, 0, 0, 0, 404, p));
            products.put(23, new Good(new ItemStack(Material.STONE_AXE), "&f石斧", Arrays.asList("&a好用的工具", "&666")
                    , 20, 0, 0, 0, 405, p));
            products.put(24, new Good(new ItemStack(Material.IRON_PICKAXE), "&f铁镐", Arrays.asList("&a好用的工具", "&666")
                    , 0, 4, 0, 0, 406, p));
            products.put(25, new Good(new ItemStack(Material.IRON_AXE), "&f铁斧", Arrays.asList("&a好用的工具", "&666")
                    , 0, 4, 0, 0, 407, p));
            products.put(28, new Good(new ItemStack(Material.DIAMOND_PICKAXE), "&f钻石镐", Arrays.asList("&a好用的工具", "&666")
                    , 0, 0, 3, 0, 408, p));
            products.put(29, new Good(new ItemStack(Material.DIAMOND_AXE), "&f钻石斧", Arrays.asList("&a好用的工具", "&666")
                    , 0, 0, 3, 0, 409, p));
            return products;
        }
        if (type.equals(ShopType.BOW)) {
            products.put(19, new Good(new ItemStack(Material.ARROW, 8)
                    , "&f箭", Arrays.asList("&a远程武器", "&666")
                    , 0, 2, 0, 0, 501, p));
            products.put(20, new Good(new ItemStack(Material.BOW)
                    , "&f弓", Arrays.asList("&a远程武器", "&666")
                    , 0, 12, 0, 0, 502, p));
            products.put(21, new Good(new ItemStack(Material.BOW)
                    , "&f弓(力量I)", Arrays.asList("&a远程武器", "&666")
                    , 0, 24, 0, 0, 503, p));
            products.put(22, new Good(new ItemStack(Material.BOW)
                    , "&f弓(力量I, 冲击I)", Arrays.asList("&a远程武器", "&666")
                    , 0, 0, 6, 0, 504, p));
            return products;
        }
        if (type.equals(ShopType.POTIONS)) {
            products.put(19, new Good(new ItemBuilder(Material.POTION)
                    .setPotionEffect(PotionEffectType.SPEED, 45 * 20, 2
                            , false, true, true).toItemStack()
                    , "&f速度II药水(45秒)", Arrays.asList("&a药水", "&666")
                    , 0, 0, 1, 0, 601, p));
            products.put(20, new Good(new ItemBuilder(Material.POTION)
                    .setPotionEffect(PotionEffectType.JUMP, 45 * 20, 5
                            , false, true, true).toItemStack()
                    , "&f跳跃提升V药水(45秒)", Arrays.asList("&a药水", "&666")
                    , 0, 0, 1, 0, 602, p));
            products.put(21, new Good(new ItemBuilder(Material.POTION)
                    .setPotionEffect(PotionEffectType.INVISIBILITY, 30 * 20, 5
                            , true, false, false).toItemStack()
                    , "&f隐身药水 (30 秒)", Arrays.asList("&a药水", "&666")
                    , 0, 0, 2, 0, 603, p));
            return products;
        }

        if (type.equals(ShopType.UTILITY)) {
            products.put(19, new Good(new ItemStack(Material.GOLDEN_APPLE)
                    , "&6金苹果", Arrays.asList("&7全面治愈", "&7rush必备")
                    , 0, 3, 0, 0, 701, p));
            products.put(20, new Good(new ItemStack(Material.SNOWBALL)
                    , "&f床虱", Arrays.asList("&7在雪球着陆的地方生成蠹虫", "&7用于分散敌人注意力，持续15秒")
                    , 40, 0, 0, 0, 702, p));
            products.put(21, new Good(new ItemStack(Material.GHAST_SPAWN_EGG)
                    , "&f梦境守护者", Arrays.asList("&7铁傀儡帮你守卫基地", "&7持续四分钟")
                    , 120, 0, 0, 0, 703, p));
            products.put(22, new Good(new ItemStack(Material.FIRE_CHARGE), "&c火球"
                    , Arrays.asList("&7右键发射", "&7击飞行走的敌人")
                    , 40, 0, 0, 0, 704, p));
            products.put(23, new Good(new ItemStack(Material.TNT), "&cTNT"
                    , Arrays.asList("&7瞬间点燃", "&7适用于炸床等")
                    , 0, 4, 0, 0, 705, p));
            products.put(24, new Good(new ItemStack(Material.ENDER_PEARL)
                    , "&2末影珍珠", Arrays.asList("&7入侵敌人基地的最快方法", "&7说不定还能自救")
                    , 0, 0, 4, 0, 706, p));
            products.put(25, new Good(new ItemStack(Material.WATER_BUCKET)
                    , "&1水桶", Arrays.asList("&7能很好的降低来犯敌人的速度", "&7也可以抵御来自爆炸造成的伤害")
                    , 0, 3, 0, 0, 707, p));
            products.put(28, new Good(new ItemStack(Material.EGG), "&f搭桥蛋"
                    , Arrays.asList("&7扔出搭桥蛋后", "&7会在其飞行轨迹上", "&7生成羊毛桥")
                    , 0, 0, 2, 0, 708, p));
            products.put(29, new Good(new ItemStack(Material.MILK_BUCKET)
                    , "&f魔法牛奶", Arrays.asList("&7使用后可以清除", "&7你身上所有的负面效果 ")
                    , 0, 4, 0, 0, 709, p));
            products.put(30, new Good(new ItemStack(Material.SPONGE, 4)
                    , "&e海绵", Arrays.asList("&7用于吸收水分", "&7一次可以吸好多")
                    , 0, 3, 0, 0, 710, p));
            products.put(31, new Good(new ItemStack(Material.TRAPPED_CHEST)
                    , "&f紧凑型速建防御塔", Arrays.asList("&7建造一个速建防御塔", "&7在拆床时也许有用吧")
                    , 24, 0, 0, 0, 711, p));
            return products;
        }
        if (type.equals(ShopType.GLASS)) {
            if (gameManager.getPlayerManager().getEditingQuickBuyPlayersGoodMap().containsKey(p.getUniqueId())) {
                products.put(19, new Good(new ItemStack(Material.RED_STAINED_GLASS_PANE), "&r空闲槽位"
                        , Arrays.asList("&7这是一个快速购买槽位", "&b左键点击", "&7将物品添加至此")
                        , 0, 0, 0, 0, 0, p));
            } else {
                products.put(19, new Good(new ItemStack(Material.RED_STAINED_GLASS_PANE), "&r空闲槽位"
                        , Arrays.asList("&7这是一个快速购买槽位", "&bShift+左键点击", "&7将任意物品添加至此")
                        , 0, 0, 0, 0, 0, p));
            }

            return products;
        }
        return products;
    }

    public void clear(Inventory inventory) {
        for (int i = 18; i < 45; ++i) {
            inventory.setItem(i, null);
        }
    }

    public void openInventory(ShopType type) {
        inventory = Bukkit.createInventory(null, 54, Color.str("&r" + type.getName() + " &7- &r物品商店"));

        for (int i = 9; i < 18; i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("&8⬆ &7类别").setLore("&8⬇ &7物品").toItemStack());
        }

        int j = 0;
        for (ShopType shopType : ShopType.values()) {
            inventory.setItem(j, shopType.getItemStack());
            if (j == 7) {
                break;
            }
            j++;
        }

        if (type.equals(ShopType.MAIN)) {
            inventory.setItem(0, new ItemBuilder(type.getItemStack()).clearLore().toItemStack());
            inventory.setItem(9, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("&8⬆ &7类别").setLore("&8⬇ &7物品").toItemStack());

            setupProducts(ShopType.MAIN);
            Map<Integer, Integer> favorite = gameManager.getPlugin().data.getFavorite(p.getUniqueId());

            for (int k = 0; k < 21; ++k) {
                int id = favorite.get(k);

                Good good = getGood(id);
//                System.out.println(map);
//                System.out.println(map.get(index).getDisplayName());

                inventory.setItem(getFavoriteSlot(k), good.getItem());
//
//                System.out.println("array[0]"+array[0]);
//                System.out.println("array[1]"+array[1]);
//                System.out.println("array[2]"+array[2]);

            }


//            for (int i = 19; i < 45; i++) {
//                if (i == 26 || i == 27 || i == 35 || i == 36 || i == 44) {
//                    continue;
//                }
//                inventory.setItem(i, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("&c未开发").toItemStack());
//            }
        }

        if (type.equals(ShopType.BLOCK)) {
            this.clear(inventory);
            inventory.setItem(1, new ItemBuilder(type.getItemStack()).clearLore().toItemStack());
            inventory.setItem(10, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("&8⬆ &7类别").setLore("&8⬇ &7物品").toItemStack());
            setupProducts(ShopType.BLOCK);
            addProductsToShop();
        }
        if (type.equals(ShopType.SWORD)) {
            this.clear(inventory);
            inventory.setItem(2, new ItemBuilder(type.getItemStack()).clearLore().toItemStack());
            inventory.setItem(11, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("&8⬆ &7类别").setLore("&8⬇ &7物品").toItemStack());
            setupProducts(ShopType.SWORD);
            addProductsToShop();
        }
        if (type.equals(ShopType.ARMOR)) {
            this.clear(inventory);
            inventory.setItem(3, new ItemBuilder(type.getItemStack()).clearLore().toItemStack());
            inventory.setItem(12, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("&8⬆ &7类别").setLore("&8⬇ &7物品").toItemStack());
            setupProducts(ShopType.ARMOR);
            addProductsToShop();

        }
        if (type.equals(ShopType.TOOLS)) {
            this.clear(inventory);
            inventory.setItem(4, new ItemBuilder(type.getItemStack()).clearLore().toItemStack());
            inventory.setItem(13, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("&8⬆ &7类别").setLore("&8⬇ &7物品").toItemStack());
            setupProducts(ShopType.TOOLS);
            addProductsToShop();
        }
        if (type.equals(ShopType.BOW)) {
            this.clear(inventory);
            inventory.setItem(5, new ItemBuilder(type.getItemStack()).clearLore().toItemStack());
            inventory.setItem(14, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("&8⬆ &7类别").setLore("&8⬇ &7物品").toItemStack());
            setupProducts(ShopType.BOW);
            addProductsToShop();
        }
        if (type.equals(ShopType.POTIONS)) {
            this.clear(inventory);
            inventory.setItem(6, new ItemBuilder(type.getItemStack()).clearLore().toItemStack());
            inventory.setItem(15, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("&8⬆ &7类别").setLore("&8⬇ &7物品").toItemStack());
            setupProducts(ShopType.POTIONS);
            addProductsToShop();
        }
        if (type.equals(ShopType.UTILITY)) {
            this.clear(inventory);
            inventory.setItem(7, new ItemBuilder(type.getItemStack()).clearLore().toItemStack());
            inventory.setItem(16, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("&8⬆ &7类别").setLore("&8⬇ &7物品").toItemStack());
            setupProducts(ShopType.UTILITY);
            addProductsToShop();
        }

        p.openInventory(inventory);

    }

    public void addProductsToShop() {
        for (int i = 18; i < 45; i++) {
            if (products.containsKey(i)) {
                ItemStack item = products.get(i).getItem();
                inventory.setItem(i, item);
            }
        }
    }

    public int getFavoriteNumber(int n) {
        if (n == 19) {
            return 0;
        }
        if (n == 20) {
            return 1;
        }
        if (n == 21) {
            return 2;
        }
        if (n == 22) {
            return 3;
        }
        if (n == 23) {
            return 4;
        }
        if (n == 24) {
            return 5;
        }
        if (n == 25) {
            return 6;
        }
        if (n == 28) {
            return 7;
        }
        if (n == 29) {
            return 8;
        }
        if (n == 30) {
            return 9;
        }
        if (n == 31) {
            return 10;
        }
        if (n == 32) {
            return 11;
        }
        if (n == 33) {
            return 12;
        }
        if (n == 34) {
            return 13;
        }
        if (n == 37) {
            return 14;
        }
        if (n == 38) {
            return 15;
        }
        if (n == 39) {
            return 16;
        }
        if (n == 40) {
            return 17;
        }
        if (n == 41) {
            return 18;
        }
        if (n == 42) {
            return 19;
        }
        if (n == 43) {
            return 20;
        }
        return -1;
    }

    public int getFavoriteSlot(int n) {
        if (n == 0) {
            return 19;
        }
        if (n == 1) {
            return 20;
        }
        if (n == 2) {
            return 21;
        }
        if (n == 3) {
            return 22;
        }
        if (n == 4) {
            return 23;
        }
        if (n == 5) {
            return 24;
        }
        if (n == 6) {
            return 25;
        }
        if (n == 7) {
            return 28;
        }
        if (n == 8) {
            return 29;
        }
        if (n == 9) {
            return 30;
        }
        if (n == 10) {
            return 31;
        }
        if (n == 11) {
            return 32;
        }
        if (n == 12) {
            return 33;
        }
        if (n == 13) {
            return 34;
        }
        if (n == 14) {
            return 37;
        }
        if (n == 15) {
            return 38;
        }
        if (n == 16) {
            return 39;
        }
        if (n == 17) {
            return 40;
        }
        if (n == 18) {
            return 41;
        }
        if (n == 19) {
            return 42;
        }
        if (n == 20) {
            return 43;
        }
        return -1;
    }

    public Good getGood(int id) {
        for (Good good : allProducts) {
            if (good.getId() == id) {
                return good;
            }
        }
        return null;
    }


}
