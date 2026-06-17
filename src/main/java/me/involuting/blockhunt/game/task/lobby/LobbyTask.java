package me.involuting.blockhunt.game.task.lobby;

import me.involuting.blockhunt.game.arena.Arena;
import me.involuting.blockhunt.game.manager.GameManager;
import me.involuting.blockhunt.game.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class LobbyTask extends BukkitRunnable {

    private static final int START_COUNTDOWN = 10;

    private final Arena arena;
    private final GameManager gameManager;

    private int countdown = START_COUNTDOWN;
    private boolean waitingMessageSent;

    public LobbyTask(
            Arena arena,
            GameManager gameManager
    ) {
        this.arena = arena;
        this.gameManager = gameManager;
    }

    @Override
    public void run() {

        if (arena == null
                || arena.getState() != GameState.WAITING) {

            cancel();
            return;
        }

        int playerCount = getOnlinePlayers();

        if (playerCount < arena.getMinPlayers()) {

            arena.setCountdown(-1);

            if (!waitingMessageSent) {

                broadcast(
                        "§cWaiting for more players..."
                );

                waitingMessageSent = true;
            }

            countdown = START_COUNTDOWN;
            return;
        }

        if (waitingMessageSent) {

            broadcast(
                    "§aEnough players joined. Starting countdown..."
            );

            waitingMessageSent = false;
        }

        arena.setCountdown(countdown);

        if (countdown <= 0) {

            arena.setCountdown(0);

            cancel();

            gameManager.startGame(arena);
            return;
        }

        if (countdown == 10
                || countdown == 5
                || countdown <= 3) {

            broadcast(
                    "§eGame starting in §6"
                            + countdown
                            + "§e second"
                            + (countdown == 1 ? "" : "s")
                            + "."
            );
        }

        countdown--;
    }

    private int getOnlinePlayers() {

        int count = 0;

        for (UUID uuid : arena.getPlayers()) {

            Player player = Bukkit.getPlayer(uuid);

            if (player != null
                    && player.isOnline()) {

                count++;
            }
        }

        return count;
    }

    private void broadcast(String message) {

        for (UUID uuid : arena.getPlayers()) {

            Player player = Bukkit.getPlayer(uuid);

            if (player == null
                    || !player.isOnline()) {
                continue;
            }

            player.sendMessage(message);
        }
    }
}