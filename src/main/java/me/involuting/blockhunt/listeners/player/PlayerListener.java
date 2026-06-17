package me.involuting.blockhunt.listeners.player;

import me.involuting.blockhunt.game.player.BlockHuntPlayer;
import me.involuting.blockhunt.game.player.manager.PlayerManager;
import me.involuting.blockhunt.scoreboard.ScoreboardAdapter;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerListener implements Listener {

    private final PlayerManager playerManager;
    private final ScoreboardAdapter scoreboardAdapter;

    public PlayerListener(
            PlayerManager playerManager,
            ScoreboardAdapter scoreboardAdapter
    ) {
        this.playerManager = playerManager;
        this.scoreboardAdapter = scoreboardAdapter;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        BlockHuntPlayer data = playerManager.get(player);

        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setFireTicks(0);

        player.setAllowFlight(false);
        player.setFlying(false);
        player.setInvisible(false);

        data.resetGameData();

        scoreboardAdapter.createBoard(player);

        player.sendMessage("");
        player.sendMessage("§6§lBLOCK HUNT");
        player.sendMessage("§7Welcome to Block Hunt!");
        player.sendMessage("§7Choose a disguise and survive.");
        player.sendMessage("");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        scoreboardAdapter.removeBoard(player);
        playerManager.remove(player);
    }
}