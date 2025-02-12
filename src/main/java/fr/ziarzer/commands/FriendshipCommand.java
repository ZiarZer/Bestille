package fr.ziarzer.commands;

import fr.ziarzer.domain.FriendshipManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FriendshipCommand implements CommandExecutor {
    enum FriendshipMessage {
        STRANGERS("You don't know each other."),
        BARELY("You barely know each other."),
        VAGUELY("You vaguely know each other."),
        FRIENDS("You are friends!"),
        GOOD_FRIENDS("You are good friends!"),
        BEST_FRIENDS("You are best friends!");

        private final String message;

        FriendshipMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }

    private final FriendshipManager friendshipManager;

    public FriendshipCommand(FriendshipManager friendshipManager) {
        super();
        this.friendshipManager = friendshipManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player senderPlayer = (Player) sender;
        if (args.length == 0) {
            return false;
        }

        Player otherPlayer = Bukkit.getPlayer(args[0]);
        if (otherPlayer == null) {
            senderPlayer.sendMessage(ChatColor.RED + "Player " + args[0] + " not found");
            return true;
        }

        Integer friendshipLevel = friendshipManager.getFriendshipLevel(senderPlayer, otherPlayer);
        FriendshipMessage message;
        if (friendshipLevel == null || friendshipLevel == 0) {
            message = FriendshipMessage.STRANGERS;
        } else if (friendshipLevel >= FriendshipManager.MAX_FRIENDSHIP_LEVEL) {
            message = FriendshipMessage.BEST_FRIENDS;
        } else if (friendshipLevel >= FriendshipManager.MAX_FRIENDSHIP_LEVEL * 0.6) {
            message = FriendshipMessage.GOOD_FRIENDS;
        } else if (friendshipLevel >= FriendshipManager.MAX_FRIENDSHIP_LEVEL * 0.4) {
            message = FriendshipMessage.FRIENDS;
        } else if (friendshipLevel >= FriendshipManager.MAX_FRIENDSHIP_LEVEL * 0.2) {
            message = FriendshipMessage.VAGUELY;
        } else {
            message = FriendshipMessage.BARELY;
        }
        senderPlayer.sendMessage(ChatColor.BOLD + "Friendship with " + args[0]+ "\n" + ChatColor.RESET + message);

        return true;
    }
}
