package me.zjls.bedwars.mysql;

import me.zjls.bedwars.Main;
import me.zjls.bedwars.games.GameManager;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class SQLGetter {
    private GameManager gameManager;
    private Main plugin;
    private String table = "bedwars";
    //    private String favorite = "Block#19,Sword#19,Armor#19,Utility#19,Bow#20,Potions#19,Utility#23,Block#24,Sword#20,Armor#20,Utility#22,Bow#19,Potions#20,Utility#25,Glass#19,Glass#19,Glass#19,Glass#19,Glass#19,Glass#19,Glass#19";
    private String favorite = "101,201,301,701,502,601,705,107,202,302,704,501,602,707,0,0,0,0,0,0,0";

    public SQLGetter(Main plugin) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
    }

    public void createTable() {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("create table if not exists " + table
                    + " (id int unsigned auto_increment primary key ,UUID varchar(255), Name varchar(255), Kills int, FinalKills int, Deaths int, Wins int, BedDestroy int, Kits varchar(255),Favorite varchar(2555) character set utf8 collate utf8_unicode_ci NULL DEFAULT NULL,Coins int, Experience int, Level int)");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        if (!isPlayerExists(uuid)) {
            PreparedStatement ps;
            try {
                ps = plugin.sql.getConnection().prepareStatement("insert ignore into " + table + " (UUID,Name,Kills,FinalKills,Deaths,Wins,BedDestroy,Kits,Favorite,Coins,Experience,Level) values (?,?,0,0,0,0,0,null,?,0,0,1)");
                ps.setString(1, uuid.toString());
                ps.setString(2, player.getName());
                ps.setString(3, favorite);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isPlayerExists(UUID uuid) {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("select * from " + table + " where UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet results = ps.executeQuery();
            return results.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Map<Integer, Integer> getFavorite(UUID uuid) {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("select Favorite from " + table + " where UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                String favorite = resultSet.getString("Favorite");
                Map<Integer, Integer> favoriteMap = gameManager.getPlayerManager().getFavorite();
                for (int i = 0; i < 21; ++i) {
                    favoriteMap.put(i, Integer.valueOf(favorite.split(",")[i]));
                }
                return favoriteMap;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setFavorite(UUID uuid, Map<Integer, Integer> favoriteMap) {
        PreparedStatement ps;
        try {
            ps = plugin.sql.getConnection().prepareStatement("update " + table + " set Favorite=? where UUID=?");
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (Map.Entry<Integer, Integer> entry : favoriteMap.entrySet()) {
                if (i == 20) {
                    sb.append(entry.getValue());

                } else {
                    sb.append(entry.getValue()).append(",");
                    i++;
                }
            }
            ps.setString(1, String.valueOf(sb));
            ps.setString(2, uuid.toString());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
