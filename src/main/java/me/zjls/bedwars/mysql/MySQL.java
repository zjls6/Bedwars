package me.zjls.bedwars.mysql;

import cn.beecp.BeeDataSource;
import cn.beecp.BeeDataSourceConfig;
import lombok.Getter;

import javax.sql.DataSource;
import java.sql.Connection;
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
            BeeDataSourceConfig config = new BeeDataSourceConfig();
            config.setDriverClassName("com.mysql.jdbc.Driver");
            config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaxActive(10);
            config.setInitialSize(0);
            config.setMaxWait(8000);//ms

            DataSource ds = new BeeDataSource(config);

            connection = ds.getConnection();
        }

//        if (!isConnected()) {
//            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL="
//                    + useSSL, username, password);
//        }
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
