package me.zjls.bedwars.mysql;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
public class MySQL {

    private String host = "localhost";
    private String port = "3306";
    private String database = "minecraft";
    private String username = "root";
    private String password = "Whitewook666";
    private boolean useSSL = false;


    private Connection connection;

    public MySQL() {
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void connect() throws SQLException {
        if (!isConnected()) {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true" + "&useSSL="
                    + useSSL, username, password);
        }
    }

    public void disConnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
