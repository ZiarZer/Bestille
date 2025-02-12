package fr.ziarzer.commands;

import fr.ziarzer.domain.FriendshipManager;
import fr.ziarzer.domain.PlayerService;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class FriendsCommand implements CommandExecutor {
    private final FriendshipManager friendshipManager;
    private final PlayerService playerService;

    public FriendsCommand(FriendshipManager friendshipManager, PlayerService playerService) {
        super();
        this.friendshipManager = friendshipManager;
        this.playerService = playerService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        UUID uuid = ((Player) sender).getUniqueId();
        sender.sendMessage(ChatColor.BOLD + "Your friends:");
        Map<UUID, Integer> friendshipLevels = friendshipManager.getAllFriendshipLevels(uuid);
        friendshipLevels.forEach(((otherUuid, friendshipLevel) -> sender.sendMessage(
                playerService.getNicknameByUUID(otherUuid) + ChatColor.BOLD + " " + ChatColor.RED + "❤".repeat(friendshipLevel / 240))
        ));
        return true;
    }
}
