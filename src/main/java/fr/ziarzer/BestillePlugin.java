package fr.ziarzer;

import fr.ziarzer.domain.FriendshipManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BestillePlugin extends JavaPlugin {
    private FriendshipManager friendshipManager;
    public static final int MAX_DISTANCE = 100;
    public static final int MAX_SQUARE_DISTANCE = MAX_DISTANCE * MAX_DISTANCE;
    public static final long TASK_TIMER_INTERVAL = 200L; // 10 seconds

    private static double computeSquareDistance(Location a, Location b) {
        return Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2) + Math.pow(a.getZ() - b.getZ(), 2);
    }

    private static Stream <? extends Player> getNearbyPlayers(Player player, Collection <? extends Player> onlinePlayers) {
        return onlinePlayers.stream().filter(otherPlayer -> computeSquareDistance(player.getLocation(), otherPlayer.getLocation()) <= MAX_SQUARE_DISTANCE);
    }

    @Override
    public void onEnable() {
        friendshipManager = new FriendshipManager(getLogger());
        getLogger().info("Bestille plugin enabled");
        getServer().getScheduler().runTaskTimer(this, () -> {
                Collection <? extends Player> alivePlayers = Bukkit.getOnlinePlayers().stream().filter(player -> player.getHealth() > 0).collect(Collectors.toList());
                for (Player player: alivePlayers) {
                    getNearbyPlayers(player, alivePlayers).filter(otherPlayer -> otherPlayer.getUniqueId().compareTo(player.getUniqueId()) > 0).forEach(otherPlayer -> {
                        friendshipManager.incrementFriendship(player, otherPlayer);
                    });
                }
        }, 0, TASK_TIMER_INTERVAL);
    }

    @Override
    public void onDisable() {
        try {
            friendshipManager.db.disconnect();
        } catch (SQLException e) {
            getLogger().log(Level.WARNING, e.getMessage());
        }
    }
}