package me.involuting.blockhunt.game.npc;

import me.involuting.blockhunt.game.arena.Arena;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

public class BlockHuntNPC {

    private final Villager villager;
    private Arena arena;

    public BlockHuntNPC(Location location) {

        this.villager = (Villager) location.getWorld().spawnEntity(
                location,
                EntityType.VILLAGER
        );

        villager.setAI(false);
        villager.setInvulnerable(true);
        villager.setCollidable(false);
        villager.setSilent(true);
        villager.setRemoveWhenFarAway(false);

        updateName();
    }

    public void setArena(Arena arena) {
        this.arena = arena;
        updateName();
    }

    public void updateName() {

        if (arena == null) {
            villager.setCustomName(
                    "§6§lBLOCK HUNT\n§7Click to Join"
            );
            return;
        }

        villager.setCustomName(
                "§6§lBLOCK HUNT\n" +
                        "§e" + arena.getName() + "\n" +
                        "§f" + arena.getPlayers().size() + "/8 Players"
        );

        villager.setCustomNameVisible(true);
    }

    public Villager getVillager() {
        return villager;
    }

    public Arena getArena() {
        return arena;
    }
}