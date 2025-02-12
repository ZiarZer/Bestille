package fr.ziarzer;

import fr.ziarzer.commands.DbConnection;
import fr.ziarzer.commands.FriendshipCommand;
import fr.ziarzer.commands.SeeFriendshipAdminCommand;
import fr.ziarzer.domain.FriendshipManager;
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

            this.getCommand("friendship").setExecutor(new FriendshipCommand(friendshipManager));
            this.getCommand("seefriendship").setExecutor(new SeeFriendshipAdminCommand(friendshipManager));

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