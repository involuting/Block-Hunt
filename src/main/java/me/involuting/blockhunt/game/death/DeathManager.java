package me.involuting.blockhunt.game.death;

import me.involuting.blockhunt.game.arena.Arena;
import me.involuting.blockhunt.game.disguise.manager.DisguiseManager;
import me.involuting.blockhunt.game.player.BlockHuntPlayer;
import me.involuting.blockhunt.game.role.Role;
import me.involuting.blockhunt.game.win.WinCondition;
import me.involuting.blockhunt.game.arena.manager.ArenaManager;
import me.involuting.blockhunt.game.player.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DeathManager {

    private final PlayerManager playerManager;
    private final ArenaManager arenaManager;
    private final DisguiseManager disguiseManager;
    private final WinCondition winCondition;

    public DeathManager(PlayerManager playerManager,
                        ArenaManager arenaManager,
                        DisguiseManager disguiseManager,
                        WinCondition winCondition) {
        this.playerManager = playerManager;
        this.arenaManager = arenaManager;
        this.disguiseManager = disguiseManager;
        this.winCondition = winCondition;
    }

    public void handleDeath(Player attacker,
                            Player victim) {

        Arena arena = arenaManager.getArena(victim);

        if (arena == null) {
            return;
        }

        BlockHuntPlayer attackerData =
                playerManager.get(attacker);

        BlockHuntPlayer victimData =
                playerManager.get(victim);

        attackerData.addKill();
        victimData.addDeath();

        disguiseManager.removeDisguise(victim);

        victimData.setRole(Role.HUNTER);

        victim.setHealth(victim.getMaxHealth());
        victim.setFoodLevel(20);
        victim.setFireTicks(0);

        if (arena.getHunterSpawn() != null) {
            victim.teleport(arena.getHunterSpawn());
        }

        for (UUID uuid : arena.getPlayers()) {

            Player player = Bukkit.getPlayer(uuid);

            if (player == null) {
                continue;
            }

            player.sendMessage(
                    "§c☠ §f" + victim.getName()
                            + " §7was found by §f"
                            + attacker.getName()
                            + "§7!"
            );
        }

        attacker.sendMessage("§aYou found an hider ");
        victim.sendMessage("§cYou were found and became a Hunter!");

        winCondition.checkHuntersWin(arena);
    }
}