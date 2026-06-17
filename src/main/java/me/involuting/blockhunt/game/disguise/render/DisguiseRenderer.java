package me.involuting.blockhunt.game.disguise.render;

import me.involuting.blockhunt.game.disguise.type.BlockType;
import org.bukkit.Location;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DisguiseRenderer {

    private static final double Y_OFFSET = 0.0D;
    private static final float VIEW_RANGE = 64F;

    private final Map<UUID, BlockDisplay> displays = new HashMap<>();

    public void render(Player player,
                       BlockType blockType,
                       Location location) {

        if (player == null
                || !player.isOnline()
                || blockType == null
                || location == null
                || location.getWorld() == null) {
            return;
        }

        remove(player);

        BlockDisplay display = location.getWorld().spawn(
                location.clone().add(0, Y_OFFSET, 0),
                BlockDisplay.class
        );

        display.setBlock(
                blockType.getMaterial().createBlockData()
        );

        display.setBillboard(Display.Billboard.FIXED);

        display.setTransformation(
                new Transformation(
                        new Vector3f(0F, 0F, 0F),
                        new AxisAngle4f(),
                        new Vector3f(1F, 1F, 1F),
                        new AxisAngle4f()
                )
        );

        display.setViewRange(VIEW_RANGE);

        display.setInterpolationDuration(0);
        display.setInterpolationDelay(0);

        display.setGravity(false);
        display.setPersistent(false);
        display.setInvulnerable(true);

        displays.put(
                player.getUniqueId(),
                display
        );

        player.setInvisible(true);
    }


    public void updateBlock(Player player,
                            BlockType blockType) {

        BlockDisplay display = getDisplay(player);

        if (display == null
                || !display.isValid()
                || blockType == null) {
            return;
        }

        display.setBlock(
                blockType.getMaterial().createBlockData()
        );
    }

    public void remove(Player player) {

        if (player == null) {
            return;
        }

        BlockDisplay display =
                displays.remove(player.getUniqueId());

        if (display != null && display.isValid()) {
            display.remove();
        }

        player.setInvisible(false);
    }

    public void remove(UUID uuid) {

        BlockDisplay display =
                displays.remove(uuid);

        if (display != null && display.isValid()) {
            display.remove();
        }
    }

    public void removeAll() {

        displays.values().forEach(display -> {

            if (display != null && display.isValid()) {
                display.remove();
            }
        });

        displays.clear();
    }

    public BlockDisplay getDisplay(Player player) {

        if (player == null) {
            return null;
        }

        return displays.get(
                player.getUniqueId()
        );
    }

    public boolean isRendered(Player player) {

        return player != null
                && getDisplay(player) != null;
    }

    public int getRenderedCount() {
        return displays.size();
    }


}