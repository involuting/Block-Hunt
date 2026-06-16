package me.involuting.blockhunt.game.win;

import me.involuting.blockhunt.game.arena.Arena;
import me.involuting.blockhunt.game.player.BlockHuntPlayer;
import me.involuting.blockhunt.game.role.Role;
import me.involuting.blockhunt.game.manager.GameManager;
import me.involuting.blockhunt.game.player.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class WinCondition {

    private final PlayerManager playerManager;
    private final GameManager gameManager;

    public WinCondition(PlayerManager playerManager,
                        GameManager gameManager) {
        this.playerManager = playerManager;
        this.gameManager = gameManager;
    }

    public void checkHuntersWin(Arena arena) {

        int hiders = 0;

        for (UUID uuid : arena.getPlayers()) {

            Player player = Bukkit.getPlayer(uuid);

            if (player == null) {
                continue;
            }

            BlockHuntPlayer data =
                    playerManager.get(player);

            if (data.getRole() == Role.HIDER) {
                hiders++;
            }
        }

        if (hiders == 0) {
            huntersWin(arena);
        }
    }

    public void huntersWin(Arena arena) {

        broadcast(arena, "");
        broadcast(arena, "§c§lHUNTERS WIN!");
        broadcast(arena, "§7All hiders have been found.");
        broadcast(arena, "");

        gameManager.endGame(arena);
    }

    public void hidersWin(Arena arena) {

        broadcast(arena, "");
        broadcast(arena, "§a§lHIDERS WIN!");
        broadcast(arena, "§7The hunters ran out of time.");
        broadcast(arena, "");

        gameManager.endGame(arena);
    }

    private void broadcast(Arena arena, String message) {

        for (UUID uuid : arena.getPlayers()) {

            Player player = Bukkit.getPlayer(uuid);

            if (player != null) {
                player.sendMessage(message);
            }
        }
    }

    public int getHiderCount(Arena arena) {

        int count = 0;

        for (UUID uuid : arena.getPlayers()) {

            Player player = Bukkit.getPlayer(uuid);

            if (player == null) {
                continue;
            }

            if (playerManager.get(player).getRole() == Role.HIDER) {
                count++;
            }
        }

        return count;
    }

    public int getHunterCount(Arena arena) {

        int count = 0;

        for (UUID uuid : arena.getPlayers()) {

            Player player = Bukkit.getPlayer(uuid);

            if (player == null) {
                continue;
            }

            if (playerManager.get(player).getRole() == Role.HUNTER) {
                count++;
            }
        }

        return count;
    }
}