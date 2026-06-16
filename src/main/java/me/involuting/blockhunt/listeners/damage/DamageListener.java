package me.involuting.blockhunt.listeners.damage;



import me.involuting.blockhunt.game.player.BlockHuntPlayer;
import me.involuting.blockhunt.game.role.Role;
import me.involuting.blockhunt.game.player.manager.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener {

    private final PlayerManager playerManager;

    public DamageListener(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }

        if (!(event.getDamager() instanceof Player attacker)) {
            return;
        }

        BlockHuntPlayer victimData =
                playerManager.get(victim);

        BlockHuntPlayer attackerData =
                playerManager.get(attacker);

        if (attackerData.getRole() != Role.HUNTER) {
            event.setCancelled(true);
            return;
        }

        if (victimData.getRole() != Role.HIDER) {
            event.setCancelled(true);
            return;
        }

        victim.setHealth(0.0);

        attackerData.addKill();
        victimData.addDeath();
    }
}
