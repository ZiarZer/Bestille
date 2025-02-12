package fr.ziarzer;

import fr.ziarzer.commands.DbConnection;
import fr.ziarzer.commands.FriendshipCommand;
import fr.ziarzer.commands.FriendsCommand;
import fr.ziarzer.commands.SeeFriendshipAdminCommand;
import fr.ziarzer.domain.FriendshipManager;
import fr.ziarzer.domain.PlayerService;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

public class BestillePlugin extends JavaPlugin {
    private Connection dbConnection;
    private FriendshipManager friendshipManager;
    public static final long TASK_TIMER_INTERVAL = 200L; // 10 seconds

    @Override
    public void onEnable() {
        try {
            dbConnection = DbConnection.getInstance();
            friendshipManager = new FriendshipManager(getLogger());
            PlayerService playerService = new PlayerService();

            this.getCommand("friends").setExecutor(new FriendsCommand(friendshipManager, playerService));
            this.getCommand("friendship").setExecutor(new FriendshipCommand(friendshipManager, playerService));
            this.getCommand("seefriendship").setExecutor(new SeeFriendshipAdminCommand(friendshipManager, playerService));

            getLogger().info("Bestille plugin enabled");

            getServer()
                    .getScheduler()
                    .runTaskTimer(this, () -> friendshipManager.updateAlivePlayersFriendshipLevels(), 0, TASK_TIMER_INTERVAL);
        } catch (SQLException e) {
            getLogger().warning(e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        try {
            getLogger().info("Disconnecting database");
            if (dbConnection != null && !dbConnection.isClosed()) {
                dbConnection.close();
                getLogger().info("Closed database connection");
            }
        } catch (SQLException e) {
            getLogger().warning(e.getMessage());
        }
    }
}