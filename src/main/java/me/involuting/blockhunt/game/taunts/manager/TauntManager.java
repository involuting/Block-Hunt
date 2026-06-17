package me.involuting.blockhunt.game.taunts.manager;

import me.involuting.blockhunt.game.taunts.Taunt;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class TauntManager {

    private final Map<String, Taunt> taunts = new HashMap<>();
    private final Map<UUID, String> selectedTaunts = new HashMap<>();

    public void registerTaunt(Taunt taunt) {

        if (taunt == null) {
            return;
        }

        taunts.put(
                taunt.getId().toLowerCase(),
                taunt
        );
    }

    public void unregisterTaunt(String id) {

        if (id == null) {
            return;
        }

        taunts.remove(
                id.toLowerCase()
        );
    }

    public Taunt getTaunt(String id) {

        if (id == null) {
            return null;
        }

        return taunts.get(
                id.toLowerCase()
        );
    }

    public boolean hasTaunt(String id) {

        if (id == null) {
            return false;
        }

        return taunts.containsKey(
                id.toLowerCase()
        );
    }

    public Collection<Taunt> getTaunts() {
        return taunts.values();
    }

    public void setSelectedTaunt(
            UUID uuid,
            String tauntId
    ) {

        if (uuid == null || tauntId == null) {
            return;
        }

        if (!hasTaunt(tauntId)) {
            return;
        }

        selectedTaunts.put(
                uuid,
                tauntId.toLowerCase()
        );
    }

    public Taunt getSelectedTaunt(
            UUID uuid
    ) {

        String id = selectedTaunts.get(uuid);

        if (id == null) {
            return null;
        }

        return getTaunt(id);
    }

    public void removeSelectedTaunt(
            UUID uuid
    ) {
        selectedTaunts.remove(uuid);
    }

    public void clear() {
        taunts.clear();
        selectedTaunts.clear();
    }
}