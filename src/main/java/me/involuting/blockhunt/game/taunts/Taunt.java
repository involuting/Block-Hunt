package me.involuting.blockhunt.game.taunts;

import org.bukkit.entity.Player;

public abstract class Taunt {

    private final String id;
    private final String name;

    protected Taunt(
            String id,
            String name
    ) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract void play(Player player);
}