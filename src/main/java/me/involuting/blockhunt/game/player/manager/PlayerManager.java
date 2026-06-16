package me.involuting.blockhunt.game.player.manager;

import me.involuting.blockhunt.game.player.BlockHuntPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private final Map<UUID, BlockHuntPlayer> players = new HashMap<>();

    public BlockHuntPlayer get(Player player) {
        return players.computeIfAbsent(
                player.getUniqueId(),
                BlockHuntPlayer::new
        );
    }

    public void remove(Player player) {
        players.remove(player.getUniqueId());
    }

    public boolean exists(Player player) {
        return players.containsKey(player.getUniqueId());
    }

}