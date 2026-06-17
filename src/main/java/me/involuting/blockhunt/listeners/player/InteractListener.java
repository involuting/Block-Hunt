package me.involuting.blockhunt.listeners.player;

import me.involuting.blockhunt.game.disguise.manager.DisguiseManager;
import me.involuting.blockhunt.game.player.BlockHuntPlayer;
import me.involuting.blockhunt.game.role.Role;
import me.involuting.blockhunt.game.taunts.manager.TauntManager;
import me.involuting.blockhunt.game.taunts.menu.TauntMenu;
import me.involuting.blockhunt.game.disguise.gui.BlockSelectionMenu;
import me.involuting.blockhunt.game.player.manager.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InteractListener implements Listener {

    private final PlayerManager playerManager;
    private final DisguiseManager disguiseManager;
    private final TauntManager tauntManager;

    public InteractListener(PlayerManager playerManager, DisguiseManager disguiseManager, TauntManager tauntManager) {
        this.playerManager = playerManager;
        this.disguiseManager = disguiseManager;
        this.tauntManager = tauntManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_AIR
                && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null || !meta.hasDisplayName()) {
            return;
        }

        BlockHuntPlayer data =
                playerManager.get(event.getPlayer());

        if (data.getRole() != Role.HIDER) {
            return;
        }

        String name = meta.getDisplayName();

        if (name.equals("§aBlock Selector")) {

            event.setCancelled(true);

            new BlockSelectionMenu(
                    event.getPlayer(),
                    playerManager, disguiseManager
            ).open();

            return;
        }

        if (name.equals("§aTaunt Selector")) {

            event.setCancelled(true);

            new TauntMenu(tauntManager).open(event.getPlayer());


        }
    }
}