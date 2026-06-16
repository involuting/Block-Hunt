package me.involuting.blockhunt.listeners.combat;

import me.involuting.blockhunt.BlockHunt;
import me.involuting.blockhunt.game.arena.Arena;
import me.involuting.blockhunt.game.disguise.manager.DisguiseManager;
import me.involuting.blockhunt.game.player.BlockHuntPlayer;
import me.involuting.blockhunt.game.role.Role;
import me.involuting.blockhunt.game.state.GameState;
import me.involuting.blockhunt.game.win.WinCondition;
import me.involuting.blockhunt.game.arena.manager.ArenaManager;
import me.involuting.blockhunt.game.player.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class CombatListener implements Listener {

    private final PlayerManager playerManager;
    private final ArenaManager arenaManager;
    private final DisguiseManager disguiseManager;
    private final WinCondition winCondition;

    public CombatListener(PlayerManager playerManager,
                          ArenaManager arenaManager,
                          DisguiseManager disguiseManager,
                          WinCondition winCondition) {

        this.playerManager = playerManager;
        this.arenaManager = arenaManager;
        this.disguiseManager = disguiseManager;
        this.winCondition = winCondition;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player attacker)) {
            return;
        }

        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }

        Arena arena = arenaManager.getArena(attacker);

        if (arena == null) {
            return;
        }

        if (arena != arenaManager.getArena(victim)) {
            return;
        }

        event.setCancelled(true);

        if (victim.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        if (arena.getState() != GameState.SEEKING) {
            return;
        }

        BlockHuntPlayer attackerData = playerManager.get(attacker);
        BlockHuntPlayer victimData = playerManager.get(victim);

        if (attackerData.getRole() != Role.HUNTER) {
            return;
        }

        if (victimData.getRole() != Role.HIDER) {
            return;
        }

        attackerData.addKill();

        eliminateHider(victim);

        broadcast(
                arena,
                "§e" + victim.getName()
                        + " §7was found by §c"
                        + attacker.getName()
        );

        winCondition.checkHuntersWin(arena);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getPlayer();

        event.setDeathMessage(null);

        event.setShowDeathMessages(false);
    }

    private void eliminateHider(Player player) {

        BlockHuntPlayer data = playerManager.get(player);

        disguiseManager.removeDisguise(player);

        Bukkit.getScheduler().runTask(
                BlockHunt.getInstance(),
                () -> {

                    data.addDeath();
                    data.setRole(Role.SPECTATOR);

                    player.setGameMode(GameMode.SPECTATOR);

                    player.teleport(
                            player.getLocation().clone().add(0, 1, 0)
                    );
                }
        );
    }

    private void broadcast(Arena arena, String message) {

        for (UUID uuid : arena.getPlayers()) {

            Player player = Bukkit.getPlayer(uuid);

            if (player != null) {
                player.sendMessage(message);
            }
        }
    }
}