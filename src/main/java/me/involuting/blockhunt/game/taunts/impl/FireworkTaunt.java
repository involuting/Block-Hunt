package me.involuting.blockhunt.game.taunts.impl;

import me.involuting.blockhunt.game.taunts.Taunt;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public final class FireworkTaunt extends Taunt {

    public FireworkTaunt() {
        super(
                "fireworks",
                "Firework Taunt"
        );
    }

    @Override
    public void play(Player player) {

        Firework firework = player.getWorld().spawn(
                player.getLocation(),
                Firework.class
        );

        FireworkMeta meta = firework.getFireworkMeta();

        meta.addEffect(
                FireworkEffect.builder()
                        .withColor(Color.RED)
                        .withColor(Color.YELLOW)
                        .trail(true)
                        .flicker(true)
                        .build()
        );

        firework.setFireworkMeta(meta);

        firework.detonate();
    }
}