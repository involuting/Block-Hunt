package me.involuting.blockhunt.game.countdown;



import me.involuting.blockhunt.game.arena.Arena;
import me.involuting.blockhunt.game.manager.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;


import org.bukkit.entity.Player;

import java.util.UUID;

public class GameCountdown extends BukkitRunnable {

    private final Arena arena;
    private final GameManager gameManager;

    private int seconds;

    public GameCountdown(Arena arena, GameManager gameManager, int seconds) {
        this.arena = arena;
        this.gameManager = gameManager;
        this.seconds = seconds;
    }

    @Override
    public void run() {

        if (arena.getPlayers().size() < arena.getMinPlayers()) {

            sendMessage("§cCountdown cancelled. Not enough players.");
            cancel();
            return;
        }

        if (seconds <= 0) {

            sendMessage("§aThe game has started!");

            gameManager.startGame(arena);

            cancel();
            return;
        }

        if (seconds <= 5 || seconds % 10 == 0) {
            sendMessage("§eGame starting in §6" + seconds + " §eseconds!");
        }

        seconds--;
    }

    private void sendMessage(String message) {

        for (UUID uuid : arena.getPlayers()) {

            Player player = Bukkit.getPlayer(uuid);

            if (player != null) {
                player.sendMessage(message);
            }
        }
    }
}