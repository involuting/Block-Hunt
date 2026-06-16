package me.involuting.blockhunt.game.disguise.task;

import me.involuting.blockhunt.game.player.BlockHuntPlayer;
import me.involuting.blockhunt.game.role.Role;
import me.involuting.blockhunt.game.disguise.manager.DisguiseManager;
import me.involuting.blockhunt.game.player.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SolidifyTask extends BukkitRunnable {

    private static final long SOLIDIFY_TIME = 3000L;

    private final PlayerManager playerManager;
    private final DisguiseManager disguiseManager;

    public SolidifyTask(PlayerManager playerManager,
                        DisguiseManager disguiseManager) {
        this.playerManager = playerManager;
        this.disguiseManager = disguiseManager;
    }

    @Override
    public void run() {

        long now = System.currentTimeMillis();

        for (Player player : Bukkit.getOnlinePlayers()) {

            BlockHuntPlayer data = playerManager.get(player);

            // Only hiders can solidify
            if (data.getRole() != Role.HIDER) {
                continue;
            }

            // Must have selected a disguise
            if (data.getSelectedBlock() == null) {
                continue;
            }

            // Already solidified
            if (data.isSolidified()) {
                continue;
            }

            long idleTime = now - data.getLastMoveTime();

            if (idleTime >= SOLIDIFY_TIME) {
                disguiseManager.solidify(player);
            }
        }
    }
}