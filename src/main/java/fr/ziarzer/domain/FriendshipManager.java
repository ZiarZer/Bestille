package fr.ziarzer.domain;

import fr.ziarzer.data.FriendshipRepository;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fr.ziarzer.utils.StringUtils.colorText;

public class FriendshipManager {
    public static final int MAX_FRIENDSHIP_LEVEL = 1200;
    public static final int MAX_DISTANCE = 100;
    public static final int MAX_SQUARE_DISTANCE = MAX_DISTANCE * MAX_DISTANCE;
    public FriendshipRepository repository;
    private final Logger logger;

    public FriendshipManager(Logger logger) throws SQLException {
        this.repository = new FriendshipRepository();
        this.repository.createTable();
        this.logger = logger;
    }

    public Integer getFriendshipLevel(Player player, Player otherPlayer) {
        try {
            return repository.getCurrentFriendship(player.getUniqueId(), otherPlayer.getUniqueId());
        } catch (SQLException e) {
            this.logger.info(e.getMessage());
            return null;
        }
    }

    public void incrementFriendship(Player player, Player otherPlayer) {
        try {
            repository.incrementFriendship(player.getUniqueId(), otherPlayer.getUniqueId());
            int newFriendshipLevel = repository.getCurrentFriendship(player.getUniqueId(), otherPlayer.getUniqueId());
            if (newFriendshipLevel == MAX_FRIENDSHIP_LEVEL) {
                sendNewBestFriendMessage(player, otherPlayer);
                sendNewBestFriendMessage(otherPlayer, player);
            }
        } catch (SQLException e) {
            this.logger.info(e.getMessage());
        }
    }

    public void sendNewBestFriendMessage(Player player, Player otherPlayer) {
        final String youString = colorText("You", ChatColor.LIGHT_PURPLE, true);
        final String newBestFriendString = colorText(otherPlayer.getDisplayName(), ChatColor.LIGHT_PURPLE, true);
        player.sendMessage(youString +
                colorText(" and ", ChatColor.LIGHT_PURPLE, false) +
                newBestFriendString +
                colorText(" are now ", ChatColor.LIGHT_PURPLE, false) +
                colorText("best friends", ChatColor.LIGHT_PURPLE, true)
        );
    }

    private static double computeSquareDistance(Location a, Location b) {
        return Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2) + Math.pow(a.getZ() - b.getZ(), 2);
    }

    private static Stream<? extends Player> getNearbyPlayers(Player player, Collection <? extends Player> onlinePlayers) {
        return onlinePlayers.stream().filter(otherPlayer -> computeSquareDistance(player.getLocation(), otherPlayer.getLocation()) <= MAX_SQUARE_DISTANCE);
    }

    public void updateAlivePlayersFriendshipLevels() {
        Collection<? extends Player> alivePlayers = Bukkit.getOnlinePlayers().stream().filter(player -> player.getHealth() >= 0).collect(Collectors.toList());
        for (Player player : alivePlayers) {
            getNearbyPlayers(player, alivePlayers).filter(otherPlayer -> otherPlayer.getUniqueId().compareTo(player.getUniqueId()) > 0).forEach(otherPlayer -> incrementFriendship(player, otherPlayer));
        }
    }
}
