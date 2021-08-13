package me.zjls.bedwars.events;

import lombok.Getter;
import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.gui.ItemShopGUI;
import me.zjls.bedwars.gui.UpgradeShopGUI;
import me.zjls.bedwars.utils.Color;
import me.zjls.bedwars.utils.Good;
import me.zjls.bedwars.worlds.Island;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.UUID;

@Getter
public class NPCClick implements Listener {

    @Getter
    private static ItemShopGUI itemShopGUI;
    @Getter
    private static UpgradeShopGUI upgradeShopGUI;
    private GameManager gameManager;

    public NPCClick(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onClick(NPCRightClickEvent e) {
        Player p = e.getClicker();
        if (!gameManager.getPlayerInGame().contains(p.getUniqueId())) {
            return;
        }

        for (Island island : gameManager.getGameWorld().getIslands()) {
            if (e.getNPC().equals(island.getItemShop())) {
                Map<UUID, Good> playersGoodMap = gameManager.getPlayerManager().getEditingQuickBuyPlayersGoodMap();
                if (playersGoodMap.containsKey(p.getUniqueId())) {
                    playersGoodMap.remove(p.getUniqueId());

                }
                itemShopGUI = new ItemShopGUI(gameManager, p);
                p.sendMessage(Color.str("&6你打开了物品商店"));
                return;
            }
            if (e.getNPC().equals(island.getUpgradeShop())) {
                upgradeShopGUI = new UpgradeShopGUI(gameManager, p);
                p.sendMessage(Color.str("&6你打开了升级商店"));
                return;
            }
        }

    }
}
