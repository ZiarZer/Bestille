package fr.ziarzer.commands;

import fr.ziarzer.domain.FriendshipManager;
import fr.ziarzer.domain.PlayerService;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SeeFriendshipAdminCommand implements CommandExecutor {
    private final FriendshipManager friendshipManager;
    private final PlayerService playerService;

    public SeeFriendshipAdminCommand(FriendshipManager friendshipManager, PlayerService playerService) {
        super();
        this.friendshipManager = friendshipManager;
        this.playerService = playerService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            return false;
        }
        UUID firstUuid = playerService.getUUIDByNickname(args[0]);
        if (firstUuid == null) {
            sender.sendMessage(ChatColor.RED + "Player " + args[0] + " not found");
            return true;
        }

        if (args.length >= 2) {
            UUID secondUuid = playerService.getUUIDByNickname(args[1]);
            if (secondUuid == null) {
                sender.sendMessage(ChatColor.RED + "Player " + args[1] + " not found");
                return true;
            }
            Integer friendshipLevel = friendshipManager.getFriendshipLevel(firstUuid, secondUuid);
            sender.sendMessage("Friendship between " + args[0]+ " and " + args[1] + ": " + friendshipLevel.toString());
        }

        return true;
    }
}
