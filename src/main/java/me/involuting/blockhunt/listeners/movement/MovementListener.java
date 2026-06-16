package me.involuting.blockhunt.listeners.movement;

import me.involuting.blockhunt.game.arena.Arena;
import me.involuting.blockhunt.game.arena.manager.ArenaManager;
import me.involuting.blockhunt.game.disguise.manager.DisguiseManager;
import me.involuting.blockhunt.game.player.BlockHuntPlayer;
import me.involuting.blockhunt.game.player.manager.PlayerManager;
import me.involuting.blockhunt.game.role.Role;
import me.involuting.blockhunt.game.state.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementListener implements Listener {

    private final PlayerManager playerManager;
    private final DisguiseManager disguiseManager;
    private final ArenaManager arenaManager;

    public MovementListener(PlayerManager playerManager,
                            DisguiseManager disguiseManager,
                            ArenaManager arenaManager) {

        this.playerManager = playerManager;
        this.disguiseManager = disguiseManager;
        this.arenaManager = arenaManager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {

        if (event.getTo() == null) {
            return;
        }

        Player player = event.getPlayer();

        if (!player.isOnline() || player.isDead()) {
            return;
        }

        // Ignore head rotation
        if (event.getFrom().getBlockX() == event.getTo().getBlockX()
                && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        BlockHuntPlayer data = playerManager.get(player);

        if (data == null) {
            return;
        }

        Arena arena = arenaManager.getArena(player);

        // Freeze hunters during hiding phase
        if (arena != null
                && arena.getState() == GameState.HIDING
                && data.getRole() == Role.HUNTER) {

            event.setTo(event.getFrom());
            return;
        }

        // Hider movement handling
        if (data.getRole() != Role.HIDER) {
            return;
        }

        data.setLastMoveTime(System.currentTimeMillis());

        if (data.isSolidified()) {
            disguiseManager.unsolidify(player);
        }
    }
}