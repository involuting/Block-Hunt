package me.involuting.blockhunt.game.taunts.listener;

import me.involuting.blockhunt.game.arena.Arena;
import me.involuting.blockhunt.game.arena.manager.ArenaManager;
import me.involuting.blockhunt.game.player.manager.PlayerManager;
import me.involuting.blockhunt.game.state.GameState;
import me.involuting.blockhunt.game.taunts.Taunt;
import me.involuting.blockhunt.game.taunts.manager.TauntManager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class TauntListener implements Listener {

    private final ArenaManager arenaManager;
    private final TauntManager tauntManager;

    public TauntListener(
            ArenaManager arenaManager,
            PlayerManager playerManager, TauntManager tauntManager
    ) {
        this.arenaManager = arenaManager;
        this.tauntManager = tauntManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Action action = event.getAction();

        if (action != Action.RIGHT_CLICK_AIR
                && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || item.getType() != Material.NOTE_BLOCK) {
            return;
        }

        Arena arena = arenaManager.getArena(player);

        if (arena == null) {
            return;
        }

        // Change this if your game uses a different state.
        if (arena.getState() != GameState.HIDING) {
            return;
        }

        Taunt taunt = tauntManager.getSelectedTaunt(player.getUniqueId());

        if (taunt == null) {
            player.sendMessage(ChatColor.RED + "You have not selected a taunt.");
            return;
        }

        taunt.play(player);
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!ChatColor.stripColor(event.getView().getTitle())
                .equalsIgnoreCase("Taunts")) {
            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        ItemStack item = event.getCurrentItem();

        if (item == null || item.getType().isAir()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null || !meta.hasDisplayName()) {
            return;
        }

        String name = ChatColor.stripColor(meta.getDisplayName());

        for (Taunt taunt : tauntManager.getTaunts()) {

            if (!taunt.getName().equalsIgnoreCase(name)) {
                continue;
            }

            tauntManager.setSelectedTaunt(
                    player.getUniqueId(),
                    taunt.getId()
            );

            player.sendMessage(ChatColor.GREEN + "Selected Taunt: " + taunt.getName());
            player.closeInventory();
            return;
        }
    }
}