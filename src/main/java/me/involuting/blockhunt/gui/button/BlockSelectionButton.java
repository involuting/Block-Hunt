package me.involuting.blockhunt.gui.button;

import me.involuting.blockhunt.game.disguise.manager.DisguiseManager;
import me.involuting.blockhunt.game.disguise.type.BlockType;
import me.involuting.blockhunt.util.ItemBuilder;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BlockSelectionButton extends Button {

    private final BlockType blockType;
    private final DisguiseManager disguiseManager;

    public BlockSelectionButton(BlockType blockType,
                                DisguiseManager disguiseManager) {

        this.blockType = blockType;
        this.disguiseManager = disguiseManager;
    }

    @Override
    public ItemStack getIcon() {

        return new ItemBuilder(blockType.getMaterial())
                .name("§a" + formatName())
                .lore(
                        "§7Click to disguise as",
                        "§f" + formatName(),
                        "",
                        "§eClick to select"
                )
                .build();
    }

    @Override
    public void onClick(ButtonClick click) {

        Player player = click.getMenu().getPlayer();

        disguiseManager.disguise(
                player,
                blockType
        );

        player.closeInventory();
    }

    private String formatName() {

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