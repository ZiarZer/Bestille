package fr.ziarzer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.stream.Stream;

public class BestillePlugin extends JavaPlugin {
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
        getServer().getScheduler().runTaskTimer(this, () -> {
                Collection <? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
                for (Player player: onlinePlayers) {
                    getNearbyPlayers(player, onlinePlayers).filter(otherPlayer -> otherPlayer.getUniqueId().compareTo(player.getUniqueId()) > 0).forEach(otherPlayer -> {
                        player.sendMessage(otherPlayer.getName());
                        otherPlayer.sendMessage(player.getName());
                    });
                }
        }, 0, TASK_TIMER_INTERVAL);
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled");
    }
}