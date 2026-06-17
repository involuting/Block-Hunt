package me.involuting.blockhunt.game.npc.listener;

import me.involuting.blockhunt.game.arena.Arena;
import me.involuting.blockhunt.game.arena.manager.ArenaManager;
import me.involuting.blockhunt.game.manager.GameManager;
import me.involuting.blockhunt.game.npc.BlockHuntNPC;
import me.involuting.blockhunt.game.npc.manager.NPCManager;
import me.involuting.blockhunt.game.state.GameState;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public final class NPCListener implements Listener {

    private final NPCManager npcManager;
    private final ArenaManager arenaManager;
    private final GameManager gameManager;

    public NPCListener(
            NPCManager npcManager,
            ArenaManager arenaManager, GameManager gameManager
    ) {
        this.npcManager = npcManager;
        this.arenaManager = arenaManager;
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onNPCClick(PlayerInteractEntityEvent event) {

        if (!(event.getRightClicked() instanceof Villager villager)) {
            return;
        }

        if (!npcManager.isNPC(villager)) {
            return;
        }

        Player player = event.getPlayer();

        Arena arena = arenaManager.findAvailableArena();

        if (arena == null) {

            player.sendMessage(
                    "§cThere are no available maps."
            );

            return;
        }

        arenaManager.addPlayer(
                player,
                arena
        );

        gameManager.startCountdown(arena);
    }



}