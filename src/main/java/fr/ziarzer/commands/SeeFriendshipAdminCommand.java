package fr.ziarzer.commands;

import fr.ziarzer.domain.FriendshipManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SeeFriendshipAdminCommand implements CommandExecutor {
    private final FriendshipManager friendshipManager;

    public SeeFriendshipAdminCommand(FriendshipManager friendshipManager) {
        super();
        this.friendshipManager = friendshipManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player senderPlayer = (Player) sender;
        if (args.length == 0) {
            return false;
        }
        Player firstPlayer = Bukkit.getPlayer(args[0]);
        if (firstPlayer == null) {
            senderPlayer.sendMessage(ChatColor.RED + "Player " + args[0] + " not found");
            return true;
        }

        if (args.length >= 2) {
            Player secondPlayer = Bukkit.getPlayer(args[1]);
            if (secondPlayer == null) {
                senderPlayer.sendMessage(ChatColor.RED + "Player " + args[1] + " not found");
                return true;
            }
            Integer friendshipLevel = friendshipManager.getFriendshipLevel(firstPlayer, secondPlayer);
            senderPlayer.sendMessage("Friendship between " + args[0]+ " and " + args[1] + ": " + friendshipLevel.toString());
            return true;
        }

        return true;
    }
}
