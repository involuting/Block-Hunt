package me.involuting.blockhunt.game.disguise.manager;

import me.involuting.blockhunt.game.disguise.render.DisguiseRenderer;
import me.involuting.blockhunt.game.disguise.type.BlockType;
import me.involuting.blockhunt.game.player.BlockHuntPlayer;
import me.involuting.blockhunt.game.player.manager.PlayerManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DisguiseManager {

    private final PlayerManager playerManager;
    private final DisguiseRenderer disguiseRenderer;

    public DisguiseManager(PlayerManager playerManager,
                           DisguiseRenderer disguiseRenderer) {
        this.playerManager = playerManager;
        this.disguiseRenderer = disguiseRenderer;
    }

    public void disguise(Player player, BlockType blockType) {

        if (player == null
                || !player.isOnline()
                || blockType == null) {
            return;
        }

        BlockHuntPlayer data = playerManager.get(player);

        data.setSelectedBlock(blockType);

        if (data.isSolidified()) {

            disguiseRenderer.updateBlock(
                    player,
                    blockType
            );
        }

        player.sendMessage(
                "§aDisguise selected: §f" +
                        formatBlockName(blockType)
        );
    }

    public void removeDisguise(Player player) {

        if (player == null) {
            return;
        }

        BlockHuntPlayer data = playerManager.get(player);

        disguiseRenderer.remove(player);

        data.setSelectedBlock(null);
        data.setSolidified(false);
        data.setSolidifiedLocation(null);

        player.sendMessage(
                "§cDisguise removed."
        );
    }

    public void solidify(Player player) {

        if (player == null || !player.isOnline()) {
            return;
        }

        BlockHuntPlayer data = playerManager.get(player);

        if (data.isSolidified()) {
            return;
        }

        BlockType blockType = data.getSelectedBlock();

        if (blockType == null) {
            return;
        }

        Location location = player.getLocation()
                .getBlock()
                .getLocation();

        data.setSolidified(true);
        data.setSolidifiedLocation(location);

        disguiseRenderer.render(
                player,
                blockType,
                location
        );

        player.sendMessage("§6§lSOLIDIFIED");
        player.sendMessage("§7Stay still to blend in.");
    }

    public void unsolidify(Player player) {

        if (player == null) {
            return;
        }

        BlockHuntPlayer data = playerManager.get(player);

        if (!data.isSolidified()) {
            return;
        }

        disguiseRenderer.remove(player);

        data.setSolidified(false);
        data.setSolidifiedLocation(null);

        player.sendMessage(
                "§cYou are no longer solidified."
        );
    }

    public void refresh(Player player) {

        if (player == null) {
            return;
        }

        BlockHuntPlayer data = playerManager.get(player);

        if (!data.isSolidified()) {
            return;
        }

        BlockType blockType = data.getSelectedBlock();
        Location location = data.getSolidifiedLocation();

        if (blockType == null || location == null) {
            return;
        }

        if (!disguiseRenderer.isRendered(player)) {

            disguiseRenderer.render(
                    player,
                    blockType,
                    location
            );
            return;
        }

        disguiseRenderer.updateBlock(
                player,
                blockType
        );
    }

    public boolean isDisguised(Player player) {
        return getBlockType(player) != null;
    }

    public boolean isSolidified(Player player) {
        return playerManager.get(player).isSolidified();
    }

    public BlockType getBlockType(Player player) {
        return playerManager.get(player).getSelectedBlock();
    }

    private String formatBlockName(BlockType blockType) {

        StringBuilder builder = new StringBuilder();

        for (String word : blockType.name().split("_")) {

            builder.append(
                    Character.toUpperCase(word.charAt(0))
            );

            if (word.length() > 1) {
                builder.append(
                        word.substring(1).toLowerCase()
                );
            }

            builder.append(" ");
        }

        return builder.toString().trim();
    }
}