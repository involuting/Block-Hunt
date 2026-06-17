package me.involuting.blockhunt.game.arena.manager;

import me.involuting.blockhunt.config.ArenaFile;
import me.involuting.blockhunt.game.arena.Arena;
import me.involuting.blockhunt.game.manager.GameManager;
import me.involuting.blockhunt.game.state.GameState;
import me.involuting.blockhunt.game.win.WinCondition;
import me.involuting.blockhunt.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArenaManager {



    private final Map<String, Arena> arenas = new HashMap<>();
    private final Map<UUID, Arena> playerArenas = new HashMap<>();

    private final ArenaFile arenaFile;

    private final WinCondition winCondition;

    public ArenaManager(ArenaFile arenaFile, WinCondition winCondition) {
        this.arenaFile = arenaFile;
        this.winCondition = winCondition;
    }

    public void registerArena(Arena arena) {
        arenas.put(
                arena.getName().toLowerCase(),
                arena
        );
    }

    public void unregisterArena(String name) {
        arenas.remove(name.toLowerCase());
    }

    public Arena getArena(String name) {
        return arenas.get(name.toLowerCase());
    }

    public Arena getArena(Player player) {
        return playerArenas.get(player.getUniqueId());
    }

    public boolean isInArena(Player player) {
        return playerArenas.containsKey(player.getUniqueId());
    }

    public void addPlayer(Player player, Arena arena) {

        if (player == null || arena == null) {
            return;
        }

        if (isInArena(player)) {
            removePlayer(player);
        }

        arena.addPlayer(player.getUniqueId());

        playerArenas.put(
                player.getUniqueId(),
                arena
        );

        if (arena.getLobbySpawn() != null
                && arena.getLobbySpawn().getWorld() != null) {

            player.teleport(arena.getLobbySpawn());

        } else {

            player.sendMessage(
                    "§cThis map does not have a valid lobby spawn."
            );

            return;
        }

        if (arena.getState() == GameState.ENDING) {
            arena.setState(GameState.WAITING);
        }

        int currentPlayers = arena.getPlayers().size();
        int maxPlayers = arena.getMaxPlayers();

        for (UUID uuid : arena.getPlayers()) {
            Player arenaPlayer = Bukkit.getPlayer(uuid);

            if (arenaPlayer != null) {
                arenaPlayer.sendMessage(
                        "§a" + player.getName()
                                + " joined the game §7("
                                + currentPlayers + "/" + maxPlayers + ")"
                );
            }
        }

        player.sendMessage("");
        player.sendMessage("§6§lBLOCK HUNT");
        player.sendMessage("");
    }

    public void removePlayer(Player player) {

        Arena arena = getArena(player);

        if (arena != null) {
            arena.removePlayer(player.getUniqueId());
        }

        playerArenas.remove(player.getUniqueId());
    }

    public Collection<Arena> getArenas() {
        return arenas.values();
    }

    public void loadArenas() {

        arenas.clear();

        FileConfiguration config = arenaFile.getConfig();

        ConfigurationSection arenasSection =
                config.getConfigurationSection("arenas");

        if (arenasSection == null) {
            return;
        }

        for (String arenaName : arenasSection.getKeys(false)) {

            String path = "arenas." + arenaName;

            Arena arena = new Arena(arenaName, winCondition);

            loadLocations(config, path, arena);

            registerArena(arena);
        }
    }

    private void loadLocations(FileConfiguration config,
                               String path,
                               Arena arena) {

        ConfigurationSection lobbySection =
                config.getConfigurationSection(path + ".lobby");

        if (lobbySection != null) {
            arena.setLobbySpawn(
                    LocationUtil.loadLocation(lobbySection)
            );
        }

        ConfigurationSection hiderSection =
                config.getConfigurationSection(path + ".hider-spawn");

        if (hiderSection != null) {
            arena.setHiderSpawn(
                    LocationUtil.loadLocation(hiderSection)
            );
        }

        ConfigurationSection hunterSection =
                config.getConfigurationSection(path + ".hunter-spawn");

        if (hunterSection != null) {
            arena.setHunterSpawn(
                    LocationUtil.loadLocation(hunterSection)
            );
        }
    }

    public void saveArenas() {

        FileConfiguration config = arenaFile.getConfig();

        config.set("arenas", null);

        for (Arena arena : arenas.values()) {

            String path = "arenas." + arena.getName();

            saveLocation(
                    config,
                    path + ".lobby",
                    arena.getLobbySpawn()
            );

            saveLocation(
                    config,
                    path + ".hider-spawn",
                    arena.getHiderSpawn()
            );

            saveLocation(
                    config,
                    path + ".hunter-spawn",
                    arena.getHunterSpawn()
            );
        }

        arenaFile.save();
    }

    private void saveLocation(FileConfiguration config,
                              String path,
                              org.bukkit.Location location) {

        if (location == null) {
            return;
        }

        LocationUtil.saveLocation(
                config.createSection(path),
                location
        );
    }


    public Arena findAvailableArena() {

        for (Arena arena : getArenas()) {

            if (arena.getState() != GameState.WAITING) {
                continue;
            }

            if (arena.getPlayers().size() >= 16) {
                continue;
            }

            return arena;
        }

        return null;
    }
}