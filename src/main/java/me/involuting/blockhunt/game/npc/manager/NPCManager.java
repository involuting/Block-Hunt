package me.involuting.blockhunt.game.npc.manager;

import me.involuting.blockhunt.game.arena.Arena;
import me.involuting.blockhunt.game.npc.BlockHuntNPC;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class NPCManager {

    private final JavaPlugin plugin;

    private final Map<UUID, BlockHuntNPC> npcs =
            new HashMap<>();

    public NPCManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public BlockHuntNPC create(
            Arena arena,
            Location location
    ) {

        BlockHuntNPC npc = new BlockHuntNPC(
                plugin,
                arena,
                location
        );

        npcs.put(
                npc.getVillager().getUniqueId(),
                npc
        );

        return npc;
    }

    public boolean isNPC(Entity entity) {

        return entity != null
                && npcs.containsKey(
                entity.getUniqueId()
        );
    }

    public BlockHuntNPC get(Entity entity) {

        if (entity == null) {
            return null;
        }

        return npcs.get(
                entity.getUniqueId()
        );
    }

    public BlockHuntNPC get(UUID uuid) {

        return npcs.get(uuid);
    }

    public void remove(Entity entity) {

        if (entity == null) {
            return;
        }

        remove(entity.getUniqueId());
    }

    public void remove(UUID uuid) {

        BlockHuntNPC npc =
                npcs.remove(uuid);

        if (npc == null) {
            return;
        }

        npc.remove();
    }

    public Collection<BlockHuntNPC> getNPCs() {

        return Collections.unmodifiableCollection(
                npcs.values()
        );
    }

    public int size() {

        return npcs.size();
    }

    public void shutdown() {

        for (BlockHuntNPC npc : npcs.values()) {
            npc.remove();
        }

        npcs.clear();
    }
}