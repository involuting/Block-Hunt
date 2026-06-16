package me.involuting.blockhunt.listeners.player;

import me.involuting.blockhunt.game.player.BlockHuntPlayer;
import me.involuting.blockhunt.game.player.manager.PlayerManager;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final PlayerManager playerManager;

    public PlayerListener(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        BlockHuntPlayer data =
                playerManager.get(event.getPlayer());

        event.getPlayer().setGameMode(GameMode.ADVENTURE);
        event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
        event.getPlayer().setFoodLevel(20);
        event.getPlayer().setInvisible(false);

        data.resetGameData();

        event.getPlayer().sendMessage("");
        event.getPlayer().sendMessage("§6§lBlock Hunt");
        event.getPlayer().sendMessage("§7Welcome to Block Hunt Reimagined");
        event.getPlayer().sendMessage("§7Choose a block and survive.");
        event.getPlayer().sendMessage("");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        playerManager.remove(event.getPlayer());
    }
}