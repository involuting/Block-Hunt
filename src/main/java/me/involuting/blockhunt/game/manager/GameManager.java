package me.involuting.blockhunt.game.manager;

import me.involuting.blockhunt.BlockHunt;
import me.involuting.blockhunt.game.arena.Arena;
import me.involuting.blockhunt.game.arena.manager.ArenaManager;
import me.involuting.blockhunt.game.disguise.manager.DisguiseManager;
import me.involuting.blockhunt.game.player.BlockHuntPlayer;
import me.involuting.blockhunt.game.player.manager.PlayerManager;
import me.involuting.blockhunt.game.role.Role;
import me.involuting.blockhunt.game.state.GameState;
import me.involuting.blockhunt.game.task.game.GameTask;
import me.involuting.blockhunt.game.task.lobby.LobbyTask;
import me.involuting.blockhunt.game.win.WinCondition;
import me.involuting.blockhunt.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GameManager {

    private static final int MIN_PLAYERS = 2;
    private static final int HIDE_TIME = 30;
    private static final int GAME_TIME = 300;

    private final ArenaManager arenaManager;

    private final PlayerManager playerManager;

    private  final DisguiseManager disguiseManager;

    private final Map<String, GameTask> activeGames = new HashMap<>();
    private final Map<String, LobbyTask> countdowns = new HashMap<>();

    public GameManager(ArenaManager arenaManager, PlayerManager playerManager, DisguiseManager disguiseManager) {
        this.arenaManager = arenaManager;
        this.playerManager = playerManager;
        this.disguiseManager = disguiseManager;
    }

    public boolean startGame(Arena arena) {

        if (arena == null) {
            return false;
        }

        stopCountdown(arena);

        if (arena.getState() != GameState.WAITING) {
            return false;
        }

        if (isRunning(arena)) {
            return false;
        }

        if (arena.getLobbySpawn() == null) {
            broadcast(arena, "§cLobby spawn is not set.");
            return false;
        }

        if (arena.getHunterSpawn() == null) {
            broadcast(arena, "§cHunter spawn is not set.");
            return false;
        }

        if (arena.getHiderSpawn() == null) {
            broadcast(arena, "§cHider spawn is not set.");
            return false;
        }

        List<Player> players = getPlayers(arena);

        if (players.size() < MIN_PLAYERS) {

            broadcast(
                    arena,
                    "§cAt least §f" + MIN_PLAYERS + "§c players are required."
            );

            return false;
        }

        arena.setState(GameState.HIDING);

        Player hunter = players.get(
                ThreadLocalRandom.current().nextInt(players.size())
        );

        for (Player player : players) {

            preparePlayer(player);

            BlockHuntPlayer data = playerManager.get(player);

            data.resetGameData();
            data.addGamePlayed();

            if (player.equals(hunter)) {
                setupHunter(player, arena, data);
            } else {
                setupHider(player, arena, data);
            }
        }

        broadcast(arena, "");
        broadcast(arena, "§6§lBLOCK HUNT");
        broadcast(arena, "§7Arena: §f" + arena.getName());
        broadcast(arena, "§7Players: §f" + players.size());
        broadcast(arena, "§eHunters release in §6" + HIDE_TIME + "§e seconds.");
        broadcast(arena, "");

        GameTask task = new GameTask(
                arena,
                new WinCondition(playerManager, this),
                HIDE_TIME,
                GAME_TIME
        );

        task.runTaskTimer(
                BlockHunt.getInstance(),
                20L,
                20L
        );

        activeGames.put(
                arena.getName().toLowerCase(),
                task
        );

        return true;
    }

    private void preparePlayer(Player player) {

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setFireTicks(0);

        player.setExp(0F);
        player.setLevel(0);

        player.setAllowFlight(false);
        player.setFlying(false);
        player.setInvisible(false);
    }

    private void setupHunter(Player player,
                             Arena arena,
                             BlockHuntPlayer data) {

        data.setRole(Role.HUNTER);

        player.teleport(arena.getHunterSpawn());

        player.sendMessage("§c§lHUNTER");
        player.sendMessage("§7Find all hiders.");
    }

    private void setupHider(Player player,
                            Arena arena,
                            BlockHuntPlayer data) {

        data.setRole(Role.HIDER);

        player.teleport(arena.getHiderSpawn());

        player.getInventory().setItem(
                0,
                new ItemBuilder(Material.CHEST)
                        .name("§aBlock Selector")
                        .lore("§7Choose your disguise.")
                        .build()
        );

        player.getInventory()
                .setItem(1,
                        new ItemBuilder(Material.ITEM_FRAME)
                                .name("§aTaunt Selector")
                                .build());

        player.getInventory()
                .setItem(1,
                        new ItemBuilder(Material.NOTE_BLOCK)
                                .name("§aUse Equipped Taunt")
                                .build());

        player.sendMessage("§a§lHIDER");
        player.sendMessage("§7Hide before hunters are released.");
    }

    public void endGame(Arena arena) {

        if (arena == null) {
            return;
        }

        stopCountdown(arena);

        GameTask task = activeGames.remove(
                arena.getName().toLowerCase()
        );

        if (task != null) {
            task.cancel();
        }

        arena.setState(GameState.ENDING);

        for (UUID uuid : new HashSet<>(arena.getPlayers())) {

            Player player = Bukkit.getPlayer(uuid);

            if (player == null) {
                continue;
            }

            BlockHuntPlayer data = playerManager.get(player);

            if (data != null) {
                data.resetGameData();
            }

            disguiseManager.removeDisguise(player);

            player.getInventory().clear();

            player.setGameMode(GameMode.SURVIVAL);
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setWalkSpeed(0.2F);

            player.setHealth(20.0);
            player.setFoodLevel(20);

            if (arena.getLobbySpawn() != null) {
                player.teleport(arena.getLobbySpawn());
            }

            player.sendMessage("");
            player.sendMessage("§6§lBLOCK HUNT");
            player.sendMessage("§7The game has ended.");
            player.sendMessage("");

            arenaManager.removePlayer(player);
        }

        arena.setCountdown(-1);
        arena.setState(GameState.WAITING);
    }

    public void startCountdown(Arena arena) {

        if (arena == null) {
            return;
        }

        String key = arena.getName().toLowerCase();

        if (countdowns.containsKey(key)) {
            return;
        }

        arena.setCountdown(-1);

        LobbyTask task = new LobbyTask(
                arena,
                this
        );

        task.runTaskTimer(
                BlockHunt.getInstance(),
                20L,
                20L
        );

        countdowns.put(key, task);
    }

    public void stopCountdown(Arena arena) {

        if (arena == null) {
            return;
        }

        arena.setCountdown(-1);

        LobbyTask task = countdowns.remove(
                arena.getName().toLowerCase()
        );

        if (task != null) {
            task.cancel();
        }
    }

    public boolean isRunning(Arena arena) {

        return activeGames.containsKey(
                arena.getName().toLowerCase()
        );
    }

    public GameTask getGame(Arena arena) {

        return activeGames.get(
                arena.getName().toLowerCase()
        );
    }

    public void broadcast(Arena arena, String message) {

        for (UUID uuid : arena.getPlayers()) {

            Player player = Bukkit.getPlayer(uuid);

            if (player != null && player.isOnline()) {
                player.sendMessage(message);
            }
        }
    }

    private List<Player> getPlayers(Arena arena) {

        List<Player> players = new ArrayList<>();

        for (UUID uuid : arena.getPlayers()) {

            Player player = Bukkit.getPlayer(uuid);

            if (player != null && player.isOnline()) {
                players.add(player);
            }
        }

        return players;
    }

    public boolean isCountdownRunning(Arena arena) {

        if (arena == null) {
            return false;
        }

        return countdowns.containsKey(
                arena.getName().toLowerCase()
        );
    }

    public LobbyTask getCountdown(Arena arena) {

        if (arena == null) {
            return null;
        }

        return countdowns.get(
                arena.getName().toLowerCase()
        );
    }
}