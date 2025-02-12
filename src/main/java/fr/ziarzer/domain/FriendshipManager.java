package fr.ziarzer.domain;

import fr.ziarzer.data.FriendshipRepository;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.logging.Logger;

import static fr.ziarzer.utils.StringUtils.colorText;

public class FriendshipManager {
    public static final int MAX_FRIENDSHIP_LEVEL = 1200;
    public FriendshipRepository repository;
    private final Logger logger;

    public FriendshipManager(Logger logger) throws SQLException {
        this.repository = new FriendshipRepository();
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
}
