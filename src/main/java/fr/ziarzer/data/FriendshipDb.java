package fr.ziarzer.data;

import java.sql.*;
import java.util.UUID;
import java.util.logging.Logger;

public class FriendshipDb {
    private final Logger logger;
    Connection connection = null;

    public FriendshipDb(Logger logger) {
        this.logger = logger;
    }

    public void initConnection() {
        this.logger.info("Initializing database");
        var url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/bestille-plugin.db";
        try {
            connection = DriverManager.getConnection(url);
            String sql = "CREATE TABLE IF NOT EXISTS friendship_levels(player_1 varchar(160) NOT NULL, player_2 varchar(160), friendship_level int DEFAULT 0, PRIMARY KEY (player_1, player_2));";
            connection.prepareStatement(sql).executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getCurrentFriendship(UUID uuid_1, UUID uuid_2) throws SQLException {
        String sql = "SELECT friendship_level FROM friendship_levels WHERE player_1 = ? AND player_2 = ?;";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, uuid_1.toString());
        stmt.setString(2, uuid_2.toString());
        ResultSet results = stmt.executeQuery();
        if (!results.next()) { return 0; }
        return results.getInt(1);
    }

    public void incrementFriendship(UUID uuid_1, UUID uuid_2) throws SQLException {
        String sql = "INSERT INTO friendship_levels (player_1, player_2, friendship_level) VALUES (?, ?, 1) ON CONFLICT (player_1, player_2) DO UPDATE SET friendship_level = friendship_level + 1;";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, uuid_1.toString());
        stmt.setString(2, uuid_2.toString());
        stmt.executeUpdate();
    }

    public void disconnect() throws SQLException {
        this.logger.info("Disconnecting database");
        if (connection != null && !connection.isClosed()) {
            connection.close();
            this.logger.info("Closed database connection");
        }
    }
}
