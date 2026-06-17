package me.involuting.blockhunt.game.taunts.menu;

import me.involuting.blockhunt.game.taunts.Taunt;
import me.involuting.blockhunt.game.taunts.manager.TauntManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class TauntMenu {

    private final TauntManager tauntManager;

    public TauntMenu(
            TauntManager tauntManager
    ) {
        this.tauntManager = tauntManager;
    }

    public void open(
            Player player
    ) {

        Inventory inventory = Bukkit.createInventory(
                null,
                27,
                ChatColor.WHITE + "Taunts"
        );

        int slot = 10;

        for (Taunt taunt : tauntManager.getTaunts()) {

            ItemStack item = new ItemStack(
                    Material.FIREWORK_ROCKET
            );

            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(
                    ChatColor.YELLOW + taunt.getName()
            );

            item.setItemMeta(meta);

            inventory.setItem(
                    slot++,
                    item
            );
        }

        player.openInventory(inventory);
    }
}