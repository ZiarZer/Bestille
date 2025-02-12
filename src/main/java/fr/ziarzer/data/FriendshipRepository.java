package fr.ziarzer.data;

import fr.ziarzer.commands.DbConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FriendshipRepository {
    private final Connection dbConnection;

    public FriendshipRepository() throws SQLException {
        this.dbConnection = DbConnection.getInstance();
    }

    public void createTable() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS friendship_levels(player_1 varchar(160) NOT NULL, player_2 varchar(160), friendship_level int DEFAULT 0, PRIMARY KEY (player_1, player_2));";
            dbConnection.prepareStatement(sql).executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getCurrentFriendship(UUID uuid_1, UUID uuid_2) throws SQLException {
        String sql = "SELECT friendship_level FROM friendship_levels WHERE player_1 = ? AND player_2 = ?;";
        PreparedStatement stmt = dbConnection.prepareStatement(sql);
        stmt.setString(1, uuid_1.toString());
        stmt.setString(2, uuid_2.toString());
        ResultSet results = stmt.executeQuery();
        if (!results.next()) { return 0; }
        return results.getInt(1);
    }

    public Map<UUID, Integer> getAllFriendshipLevels(UUID uuid) throws SQLException {
        String sql = "SELECT * FROM friendship_levels WHERE player_1 = ? OR player_2 = ?;";
        PreparedStatement stmt = dbConnection.prepareStatement(sql);
        stmt.setString(1, uuid.toString());
        stmt.setString(2, uuid.toString());
        ResultSet results = stmt.executeQuery();

        HashMap<UUID, Integer> friendshipLevels = new HashMap<>();
        while (results.next()) {
            UUID otherUuid = UUID.fromString(results.getString(1));
            if (uuid == otherUuid) {
                otherUuid = UUID.fromString(results.getString(2));
            }
            friendshipLevels.put(otherUuid, results.getInt(3));
        }
        return friendshipLevels;
    }

    public void incrementFriendship(UUID uuid_1, UUID uuid_2) throws SQLException {
        String sql = "INSERT INTO friendship_levels (player_1, player_2, friendship_level) VALUES (?, ?, 1) ON CONFLICT (player_1, player_2) DO UPDATE SET friendship_level = friendship_level + 1;";
        PreparedStatement stmt = dbConnection.prepareStatement(sql);
        stmt.setString(1, uuid_1.toString());
        stmt.setString(2, uuid_2.toString());
        stmt.executeUpdate();
    }
}
