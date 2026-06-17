package me.involuting.blockhunt.game.npc;

import me.involuting.blockhunt.game.arena.Arena;
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

    private final Arena arena;

    private final Villager villager;

    private final TextDisplay titleDisplay;
    private final TextDisplay statusDisplay;
    private final TextDisplay playersDisplay;
    private final TextDisplay actionDisplay;

    private BukkitTask updateTask;

    public BlockHuntNPC(
            JavaPlugin plugin,
            Arena arena,
            Location location
    ) {

        this.arena = arena;

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
                "§f0/0 Players"
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

        int players = arena.getPlayerCount();
        int maxPlayers = arena.getMaxPlayers();

        playersDisplay.setText(
                "§f" + players +
                        "§7/§f" + maxPlayers +
                        " Players"
        );

        switch (arena.getState()) {

            case WAITING -> {

                if (arena.isCountingDown()) {

                    statusDisplay.setText(
                            "§eStarting"
                    );

                    actionDisplay.setText(
                            "§6Starting in §f" +
                                    arena.getCountdown() +
                                    "§6s"
                    );

                } else {

                    statusDisplay.setText(
                            "§aWaiting"
                    );

                    int needed =
                            arena.getPlayersNeeded();

                    if (needed > 0) {

                        actionDisplay.setText(
                                "§eNeed " +
                                        needed +
                                        " more player" +
                                        (needed == 1 ? "" : "s")
                        );

                    } else {

                        actionDisplay.setText(
                                "§aReady to Start"
                        );
                    }
                }
            }

            case STARTING -> {

                statusDisplay.setText(
                        "§eStarting"
                );

                actionDisplay.setText(
                        "§6Teleporting..."
                );
            }

            case SEEKING -> {

                statusDisplay.setText(
                        "§cIn Game"
                );

                actionDisplay.setText(
                        "§7Match in Progress"
                );
            }

            case ENDING -> {

                statusDisplay.setText(
                        "§7Ending"
                );

                actionDisplay.setText(
                        "§7Returning to Lobby..."
                );
            }

            default -> {

                statusDisplay.setText(
                        "§8Unknown"
                );

                actionDisplay.setText(
                        "§8N/A"
                );
            }
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

    public Arena getArena() {
        return arena;
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