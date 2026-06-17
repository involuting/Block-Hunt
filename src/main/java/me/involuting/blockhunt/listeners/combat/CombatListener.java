package me.involuting.blockhunt.listeners.combat;

import me.involuting.blockhunt.game.arena.Arena;
import me.involuting.blockhunt.game.arena.manager.ArenaManager;
import me.involuting.blockhunt.game.disguise.manager.DisguiseManager;
import me.involuting.blockhunt.game.player.BlockHuntPlayer;
import me.involuting.blockhunt.game.player.manager.PlayerManager;
import me.involuting.blockhunt.game.role.Role;
import me.involuting.blockhunt.game.state.GameState;
import me.involuting.blockhunt.game.win.WinCondition;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public class CombatListener implements Listener {

    private final PlayerManager playerManager;
    private final ArenaManager arenaManager;
    private final DisguiseManager disguiseManager;
    private final WinCondition winCondition;

    public CombatListener(
            PlayerManager playerManager,
            ArenaManager arenaManager,
            DisguiseManager disguiseManager,
            WinCondition winCondition
    ) {
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

        Arena attackerArena = arenaManager.getArena(attacker);
        Arena victimArena = arenaManager.getArena(victim);

        if (attackerArena == null || victimArena == null) {
            return;
        }

        if (!attackerArena.equals(victimArena)) {
            return;
        }

        // No PvP damage should ever occur in Block Hunt
        event.setCancelled(true);

        if (attackerArena.getState() != GameState.SEEKING) {
            return;
        }

        if (attacker.getGameMode() == GameMode.SPECTATOR
                || victim.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        BlockHuntPlayer attackerData = playerManager.get(attacker);
        BlockHuntPlayer victimData = playerManager.get(victim);

        if (attackerData == null || victimData == null) {
            return;
        }

        // Only hunters can eliminate hiders
        if (attackerData.getRole() != Role.HUNTER) {
            return;
        }

        if (victimData.getRole() != Role.HIDER) {
            return;
        }

        eliminateHider(victim, victimData);

        attackerData.addKill();

        broadcast(
                attackerArena,
                "§c✖ §e" + victim.getName()
                        + " §7was found by §c"
                        + attacker.getName()
        );

        winCondition.checkHuntersWin(attackerArena);
    }

    private void eliminateHider(Player player, BlockHuntPlayer data) {

        Arena arena = arenaManager.getArena(player);

        if (arena == null) {
            return;
        }

        if (arena.getState() != GameState.SEEKING) {
            return;
        }

        if (data.getRole() != Role.HIDER) {
            return;
        }

        disguiseManager.removeDisguise(player);

        data.addDeath();
        data.setRole(Role.SPECTATOR);

        player.setGameMode(GameMode.SPECTATOR);

        player.teleport(
                player.getLocation().clone().add(0.0, 1.0, 0.0)
        );

        player.sendMessage("§cYou have been found!");
    }

    private void broadcast(Arena arena, String message) {

        for (UUID uuid : arena.getPlayers()) {

            Player player = Bukkit.getPlayer(uuid);

            if (player == null) {
                continue;
            }

            player.sendMessage(message);
        }
    }
}