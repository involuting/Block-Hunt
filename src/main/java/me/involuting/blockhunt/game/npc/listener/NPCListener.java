package me.involuting.blockhunt.game.npc.listener;



import me.involuting.blockhunt.game.arena.Arena;
import me.involuting.blockhunt.game.npc.BlockHuntNPC;
import me.involuting.blockhunt.game.npc.manager.NPCManager;
import me.involuting.blockhunt.game.arena.manager.ArenaManager;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class NPCListener implements Listener {

    private final NPCManager npcManager;
    private final ArenaManager arenaManager;

    public NPCListener(NPCManager npcManager,
                       ArenaManager arenaManager) {

        this.npcManager = npcManager;
        this.arenaManager = arenaManager;
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

        Arena arena = npc.getArena();

        if (arena == null) {
            return;
        }

        arenaManager.addPlayer(
                event.getPlayer(),
                arena
        );
    }
}
