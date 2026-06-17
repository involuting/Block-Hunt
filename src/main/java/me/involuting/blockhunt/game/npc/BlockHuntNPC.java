package me.involuting.blockhunt.game.npc;

import me.involuting.blockhunt.game.arena.Arena;
import me.involuting.blockhunt.game.arena.manager.ArenaManager;
import me.involuting.blockhunt.game.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class BlockHuntNPC {

    private static final double TITLE_Y = 3.10;
    private static final double STATUS_Y = 2.75;
    private static final double PLAYERS_Y = 2.40;
    private static final double ACTION_Y = 2.05;

    private final ArenaManager arenaManager;

    private final Villager villager;

    private final TextDisplay titleDisplay;
    private final TextDisplay statusDisplay;
    private final TextDisplay playersDisplay;
    private final TextDisplay actionDisplay;

    private BukkitTask updateTask;

    public BlockHuntNPC(
            JavaPlugin plugin,
            ArenaManager arenaManager,
            Location location
    ) {

        this.arenaManager = arenaManager;

        this.villager = spawnVillager(location);

        this.titleDisplay = createDisplay(
                location.clone().add(0, TITLE_Y, 0),
                "§6§lBLOCK HUNT"
        );

        this.statusDisplay = createDisplay(
                location.clone().add(0, STATUS_Y, 0),
                "§eLoading..."
        );

        this.playersDisplay = createDisplay(
                location.clone().add(0, PLAYERS_Y, 0),
                "§f0 Players"
        );

        this.actionDisplay = createDisplay(
                location.clone().add(0, ACTION_Y, 0),
                "§aClick to Play"
        );

        startUpdater(plugin);
    }

    private void startUpdater(JavaPlugin plugin) {

        updateTask = Bukkit.getScheduler().runTaskTimer(
                plugin,
                this::update,
                0L,
                20L
        );
    }

    private void update() {

        int totalPlayers = 0;
        int availableArenas = 0;

        for (Arena arena : arenaManager.getArenas()) {

            totalPlayers += arena.getPlayers().size();

            if (arena.getState() == GameState.WAITING
                    && arena.getPlayers().size() < arena.getMaxPlayers()) {

                availableArenas++;
            }
        }

        playersDisplay.setText(
                "§f" + totalPlayers + " Players Online"
        );

        if (availableArenas > 0) {

            statusDisplay.setText(
                    "§aAvailable"
            );

            actionDisplay.setText(
                    "§aClick to Join"
            );

        } else {

            statusDisplay.setText(
                    "§cNo Arenas"
            );

            actionDisplay.setText(
                    "§7Waiting for Arena"
            );
        }
    }

    private Villager spawnVillager(Location location) {

        Villager villager = (Villager) location.getWorld()
                .spawnEntity(location, EntityType.VILLAGER);

        villager.setAI(false);
        villager.setGravity(false);
        villager.setInvulnerable(true);
        villager.setCollidable(false);
        villager.setSilent(true);
        villager.setPersistent(true);
        villager.setRemoveWhenFarAway(false);
        villager.setCanPickupItems(false);

        villager.setProfession(Villager.Profession.NONE);
        villager.setVillagerType(Villager.Type.PLAINS);

        return villager;
    }

    private TextDisplay createDisplay(
            Location location,
            String text
    ) {

        TextDisplay display = (TextDisplay) location.getWorld()
                .spawnEntity(location, EntityType.TEXT_DISPLAY);

        display.setText(text);
        display.setBillboard(Display.Billboard.CENTER);
        display.setSeeThrough(true);
        display.setShadowed(true);
        display.setDefaultBackground(false);
        display.setPersistent(true);

        return display;
    }

    public Villager getVillager() {
        return villager;
    }

    public void remove() {

        if (updateTask != null) {
            updateTask.cancel();
        }

        removeEntity(villager);
        removeEntity(titleDisplay);
        removeEntity(statusDisplay);
        removeEntity(playersDisplay);
        removeEntity(actionDisplay);
    }

    private void removeEntity(Entity entity) {

        if (entity == null || !entity.isValid()) {
            return;
        }

        entity.remove();
    }
}