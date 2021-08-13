package me.zjls.bedwars.gui;

import dev.jcsoftware.jscoreboards.JScoreboardTeam;
import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.utils.Color;
import me.zjls.bedwars.utils.ItemBuilder;
import me.zjls.bedwars.worlds.Island;
import me.zjls.bedwars.worlds.IslandColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TeamPickerGUI implements GUI {
    private final GameManager gameManager;
    private final Inventory inventory;

    public TeamPickerGUI(GameManager gameManager, Player p) {
        this.gameManager = gameManager;
        inventory = Bukkit.createInventory(null, 27, getName());

        for (Island island : gameManager.getGameWorld().getIslands()) {
            ItemStack teamWool = new ItemStack(island.getColor().getTeamWool());
            ItemMeta teamWoolMeta = teamWool.getItemMeta();
            List<String> lore = new ArrayList<>();
            teamWoolMeta.setDisplayName(island.getColor().getChatColor() + island.getColor().getName() + "队");

            if (island.isMember(p)) {
                teamWoolMeta.addEnchant(Enchantment.THORNS, 1, false);
                teamWoolMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);//隐藏附魔
                lore.add(Color.str("&a已选择"));
                teamWoolMeta.setLore(lore);
            }

            List<UUID> players = island.getPlayers();

            if (!players.isEmpty()) {
                lore.add(Color.str("&f队伍玩家列表： &7(&6" + players.size() + "&7/&a" + gameManager.getGameWorld().getMaxTeamSize() + "&7)"));
                for (UUID uuid : players) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null) {
                        lore.add(Color.of(island.getColor().getColor()) + player.getDisplayName());
                    } else {
                        lore.add(Color.of(island.getColor().getColor()) + "unknown");
                    }
                }
                teamWoolMeta.setLore(lore);
            }

            teamWool.setItemMeta(teamWoolMeta);

            inventory.addItem(teamWool);
        }


        inventory.setItem(26, new ItemBuilder(Material.BARRIER, 1).setName("&c关闭").toItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public String getName() {
        return "队伍选择器";
    }

    @Override
    public GUI handleClick(Player p, ItemStack item, InventoryView inventoryView) {

        IslandColor clickColor = null;
        String itemName = ChatColor.stripColor(item.getItemMeta().getDisplayName());

        if (ChatColor.stripColor(itemName).contains("关闭")) {
            inventoryView.close();
            p.closeInventory();
            return null;
        }
        for (IslandColor color : IslandColor.values()) {
            if (itemName.contains(ChatColor.stripColor(color.getName()))) {
                clickColor = color;
                break;
            }
        }

        Optional<Island> playerIsland = gameManager.getGameWorld().getIslands().stream().filter(island -> island.isMember(p)).findFirst();
        playerIsland.ifPresent(island -> island.getPlayers().remove(p.getUniqueId()));
        for (JScoreboardTeam team : gameManager.getTeams()) {
            if (team.isOnTeam(p.getUniqueId())) {
                team.removePlayer(p);
            }
        }

        gameManager.getScoreboard().updateScoreboard();

        IslandColor finalClickColor = clickColor;
        Optional<Island> selectedIsland = gameManager.getGameWorld().getIslands().stream().filter(island -> island.getColor() == finalClickColor).findFirst();

        if (selectedIsland.isPresent()) {
            Island island = selectedIsland.get();
            if (island.getPlayers().size() != gameManager.getGameWorld().getMaxTeamSize()) {
                island.getPlayers().add(p.getUniqueId());
                JScoreboardTeam team = gameManager.getColorTeamMap().get(island.getColor());
                team.addPlayer(p);
                gameManager.getScoreboard().updateScoreboard();
                gameManager.getPlayerManager().giveTeamArmor(p, island);
            } else {
                p.sendMessage(Color.str(island.getColor().getChatColor() + island.getColor().getName() + "队 &r已经满人了！"));
            }
        }

        gameManager.getPlayerManager().giveTeamSelector(p);

        return null;
    }

    @Override
    public boolean isInventory(InventoryView view) {
        return view.getTitle().equals(getName());
    }
}
