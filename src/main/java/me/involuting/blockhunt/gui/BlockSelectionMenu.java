package me.involuting.blockhunt.gui;

import me.involuting.blockhunt.game.disguise.manager.DisguiseManager;
import me.involuting.blockhunt.game.disguise.type.BlockType;
import me.involuting.blockhunt.game.player.manager.PlayerManager;
import me.involuting.blockhunt.gui.button.BlockSelectionButton;
import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import org.bukkit.entity.Player;

public class BlockSelectionMenu extends Menu {

    private final DisguiseManager disguiseManager;

    public BlockSelectionMenu(Player player,
                              PlayerManager playerManager, DisguiseManager disguiseManager) {

        super("§8Select Disguise", MenuSize.FOUR, player);

        this.disguiseManager = disguiseManager;
    }

    @Override
    public void setup(BackgroundLayer background,
                      ForegroundLayer foreground) {

        int[] slots = {
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25
        };

        int slot = 10;

        for (BlockType blockType : BlockType.values()) {

            foreground.set(
                    slot,
                    new BlockSelectionButton(
                            blockType,
                            disguiseManager
                    )
            );

            slot++;

            if (slot == 17) {
                slot = 19;
            }
        }
    }
}