package me.involuting.blockhunt.game.task.game;

import lombok.Getter;
import me.involuting.blockhunt.game.arena.Arena;
import me.involuting.blockhunt.game.state.GameState;
import me.involuting.blockhunt.game.win.WinCondition;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@Getter
public class GameTask extends BukkitRunnable {

    private final Arena arena;
    private final WinCondition winCondition;

    private int hideTime;
    private int gameTime;


    public GameTask(Arena arena,
                    WinCondition winCondition,
                    int hideTime,
                    int gameTime) {

        this.arena = arena;
        this.winCondition = winCondition;
        this.hideTime = hideTime;
        this.gameTime = gameTime;
    }

    @Override
    public void run() {

        try {

            if (!isArenaValid()) {
                stopTask();
                return;
            }

            switch (arena.getState()) {

                case HIDING -> tickHiding();

                case SEEKING -> tickSeeking();

                case WAITING, ENDING -> stopTask();

                default -> {
                }
            }

        } catch (Exception exception) {

            exception.printStackTrace();
            stopTask();
        }
    }

    private boolean isArenaValid() {

        return arena != null
                && arena.getPlayers() != null
                && !arena.getPlayers().isEmpty();
    }

    private void tickHiding() {

        if (hideTime <= 0) {
            startSeekingPhase();
            return;
        }

        if (hideTime <= 5 || hideTime % 10 == 0) {

            broadcast(
                    "§eHunters released in §6"
                            + hideTime
                            + "§e second"
                            + (hideTime == 1 ? "" : "s")
                            + "."
            );
        }

        hideTime--;
    }

    private void tickSeeking() {

        winCondition.checkHuntersWin(arena);

        if (arena.getState() == GameState.ENDING) {
            stopTask();
            return;
        }

        if (gameTime <= 0) {

            winCondition.hidersWin(arena);
            stopTask();
            return;
        }

        if (gameTime <= 10 || gameTime % 60 == 0) {

            broadcast(
                    "§eTime Remaining: §6"
                            + formatTime(gameTime)
            );
        }

        gameTime--;
    }

    private void startSeekingPhase() {

        arena.setState(GameState.SEEKING);

        broadcast("");
        broadcast("§c§lHUNTERS RELEASED!");
        broadcast("§7Find all remaining hiders.");
        broadcast("§eTime Remaining: §f" + formatTime(gameTime));
        broadcast("");
    }

    private void stopTask() {

        cancel();
    }

    private void broadcast(String message) {

        for (UUID uuid : arena.getPlayers()) {

            Player player = Bukkit.getPlayer(uuid);

            if (player == null || !player.isOnline()) {
                continue;
            }

            player.sendMessage(message);
        }
    }

    private String formatTime(int seconds) {

        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;

        return String.format(
                "%d:%02d",
                minutes,
                remainingSeconds
        );
    }

    public int getRemainingTime() {
        return gameTime;
    }

    public boolean isHiding() {
        return arena.getState() == GameState.HIDING;
    }

    public boolean isSeeking() {
        return arena.getState() == GameState.SEEKING;
    }
}