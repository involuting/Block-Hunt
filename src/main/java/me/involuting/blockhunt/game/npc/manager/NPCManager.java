package me.involuting.blockhunt.game.npc.manager;



import me.involuting.blockhunt.game.npc.BlockHuntNPC;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NPCManager {

    private final Map<UUID, BlockHuntNPC> npcs = new HashMap<>();

    public BlockHuntNPC create(Location location) {

        BlockHuntNPC npc = new BlockHuntNPC(location);

        npcs.put(
                npc.getVillager().getUniqueId(),
                npc
        );

        return npc;
    }

    public boolean isNPC(Entity entity) {

        return npcs.containsKey(
                entity.getUniqueId()
        );
    }

    public BlockHuntNPC get(Villager villager) {

        return npcs.get(
                villager.getUniqueId()
        );
    }
}
