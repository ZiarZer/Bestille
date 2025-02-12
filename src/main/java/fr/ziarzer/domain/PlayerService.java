package fr.ziarzer.domain;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerService {
    public String getNicknameByUUID(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        return player != null ? player.getDisplayName() : null;
    }

    public UUID getUUIDByNickname(String nickname) {
        Player player = Bukkit.getPlayer(nickname);
        return player != null ? player.getUniqueId() : null;
    }
}
