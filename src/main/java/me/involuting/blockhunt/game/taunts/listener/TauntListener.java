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

public final class TauntListener implements Listener {

    private final ArenaManager arenaManager;
    private final PlayerManager playerManager;
    private final TauntManager tauntManager;

    public TauntListener(
            ArenaManager arenaManager,
            PlayerManager playerManager,
            TauntManager tauntManager
    ) {
        this.arenaManager = arenaManager;
        this.playerManager = playerManager;
        this.tauntManager = tauntManager;
    }

    @EventHandler
    public void onPlayerInteract(
            PlayerInteractEvent event
    ) {

        Action action = event.getAction();

        if (action != Action.RIGHT_CLICK_AIR
                && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() != Material.NOTE_BLOCK) {
            return;
        }

        Arena arena = arenaManager.getArena(player);

        if (arena == null) {
            return;
        }

        if (arena.getState() != GameState.SEEKING) {
            return;
        }

        Taunt taunt = tauntManager.getSelectedTaunt(
                player.getUniqueId()
        );

        if (taunt == null) {
            return;
        }

        taunt.play(player);

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(
            InventoryClickEvent event
    ) {

        if (!ChatColor.stripColor(
                event.getView().getTitle()
        ).equalsIgnoreCase("Taunts")) {
            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (event.getCurrentItem() == null) {
            return;
        }

        String name = ChatColor.stripColor(
                event.getCurrentItem()
                        .getItemMeta()
                        .getDisplayName()
        );

        for (Taunt taunt : tauntManager.getTaunts()) {

            if (!taunt.getName().equalsIgnoreCase(name)) {
                continue;
            }

            tauntManager.setSelectedTaunt(
                    player.getUniqueId(),
                    taunt.getId()
            );

            player.sendMessage(
                    ChatColor.GREEN +
                            "Selected Taunt: " +
                            taunt.getName()
            );

            player.closeInventory();
            break;
        }
    }

    @EventHandler
    public void onTauntInteract(
            PlayerInteractEvent event
    ) {

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

        if (arena.getState() != GameState.SEEKING) {
            return;
        }

        Taunt taunt = tauntManager.getSelectedTaunt(
                player.getUniqueId()
        );

        if (taunt == null) {
            return;
        }

        taunt.play(player);

        event.setCancelled(true);
    }
}