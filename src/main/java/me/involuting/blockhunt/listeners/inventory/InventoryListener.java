package me.involuting.blockhunt.listeners.inventory;



import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getView().getTitle() == null) {
            return;
        }

        if (!event.getView().getTitle().equals("Block Selection")) {
            return;
        }

        event.setCancelled(true);

        if (event.getCurrentItem() == null) {
            return;
        }

        // Handle block selection here later
    }
}
