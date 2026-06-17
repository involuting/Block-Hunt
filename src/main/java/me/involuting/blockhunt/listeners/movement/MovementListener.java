package me.involuting.blockhunt.listeners.movement;

import me.involuting.blockhunt.game.arena.Arena;
import me.involuting.blockhunt.game.arena.manager.ArenaManager;
import me.involuting.blockhunt.game.disguise.manager.DisguiseManager;
import me.involuting.blockhunt.game.player.BlockHuntPlayer;
import me.involuting.blockhunt.game.player.manager.PlayerManager;
import me.involuting.blockhunt.game.role.Role;
import me.involuting.blockhunt.game.state.GameState;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementListener implements Listener {

    private final PlayerManager playerManager;
    private final DisguiseManager disguiseManager;
    private final ArenaManager arenaManager;

    public MovementListener(
            PlayerManager playerManager,
            DisguiseManager disguiseManager,
            ArenaManager arenaManager
    ) {
        this.playerManager = playerManager;
        this.disguiseManager = disguiseManager;
        this.arenaManager = arenaManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {

        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null) {
            return;
        }

        Player player = event.getPlayer();

        if (!player.isOnline() || player.isDead()) {
            return;
        }

        BlockHuntPlayer data = playerManager.get(player);

        if (data == null) {
            return;
        }

        Arena arena = arenaManager.getArena(player);

        if (arena == null) {
            return;
        }


        if (arena.getState() == GameState.HIDING
                && data.getRole() == Role.HUNTER) {

            event.setTo(from);
            return;
        }


        if (data.getRole() == Role.SPECTATOR) {
            return;
        }


        boolean moved =
                from.getWorld() != to.getWorld()
                        || from.getBlockX() != to.getBlockX()
                        || from.getBlockY() != to.getBlockY()
                        || from.getBlockZ() != to.getBlockZ();

        if (!moved) {
            return;
        }

        if (data.getRole() != Role.HIDER) {
            return;
        }

        data.setLastMoveTime(System.currentTimeMillis());

        if (data.isSolidified()) {
            disguiseManager.unsolidify(player);
        }
    }
}