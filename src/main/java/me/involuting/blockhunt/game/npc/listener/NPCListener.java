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
    public void onInteract(PlayerInteractEntityEvent event) {

        if (!(event.getRightClicked() instanceof Villager villager)) {
            return;
        }

        BlockHuntNPC npc = npcManager.get(villager);

        if (npc == null) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();

        if (arenaManager.isInArena(player)) {

            player.sendMessage(
                    "§cYou are already in a Block Hunt game."
            );

            return;
        }

        Arena arena = findBestArena();

        if (arena == null) {

            player.sendMessage(
                    "§cNo available maps found."
            );

            return;
        }

        arenaManager.addPlayer(player, arena);

        gameManager.startCountdown(arena);

        player.sendMessage("");
        player.sendMessage("§6§lBLOCK HUNT");
        player.sendMessage(
                "§aJoined Map §e" + arena.getName()
        );
        player.sendMessage(
                "§7Players: §f" + arena.getPlayers().size()
        );
        player.sendMessage("");
    }

    private Arena findBestArena() {

        Arena bestArena = null;

        for (Arena arena : arenaManager.getArenas()) {

            if (arena.getState() == GameState.ENDING) {
                continue;
            }

            if (arena.isFull()) {
                continue;
            }

            if (bestArena == null) {
                bestArena = arena;
                continue;
            }

            if (arena.getPlayers().size()
                    > bestArena.getPlayers().size()) {

                bestArena = arena;
            }
        }

        return bestArena;
    }
}