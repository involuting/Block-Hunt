package me.involuting.blockhunt.command;

import me.involuting.blockhunt.game.arena.Arena;
import me.involuting.blockhunt.game.disguise.manager.DisguiseManager;
import me.involuting.blockhunt.game.disguise.type.BlockType;
import me.involuting.blockhunt.game.npc.BlockHuntNPC;
import me.involuting.blockhunt.game.npc.manager.NPCManager;
import me.involuting.blockhunt.game.player.BlockHuntPlayer;
import me.involuting.blockhunt.game.role.Role;
import me.involuting.blockhunt.game.arena.manager.ArenaManager;
import me.involuting.blockhunt.game.manager.GameManager;
import me.involuting.blockhunt.game.player.manager.PlayerManager;
import me.involuting.blockhunt.game.state.GameState;
import me.involuting.blockhunt.game.win.WinCondition;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class BlockHuntCommand implements CommandExecutor {

    private final ArenaManager arenaManager;
    private final GameManager gameManager;
    private final PlayerManager playerManager;
    private final DisguiseManager disguiseManager;
    private final NPCManager npcManager;
    private final WinCondition winCondition;

    public BlockHuntCommand(ArenaManager arenaManager,
                            GameManager gameManager,
                            PlayerManager playerManager,
                            DisguiseManager disguiseManager, NPCManager npcManager, WinCondition winCondition) {

        this.arenaManager = arenaManager;
        this.gameManager = gameManager;
        this.playerManager = playerManager;
        this.disguiseManager = disguiseManager;
        this.npcManager = npcManager;
        this.winCondition = winCondition;
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "create" -> createArena(player, args);

            case "join" -> joinArena(player, args);

            case "leave" -> leaveArena(player);

            case "start" -> startArena(player, args);

            case "disguise" -> disguise(player, args);

            case "solidify" -> {
                disguiseManager.solidify(player);
                player.sendMessage("§aForced solidification.");
            }

            case "unsolidify" -> {
                disguiseManager.unsolidify(player);
                player.sendMessage("§cForced unsolidification.");
            }

            case "delete" -> deleteArena(player, args);

            case "list" -> listArenas(player);

            case "setlobby" -> setLobby(player, args);

            case "sethiderspawn" -> setHiderSpawn(player, args);

            case "role" -> setRole(player, args);

            case "sethunterspawn" -> setHunterSpawn(player, args);

            case "undisguise" -> {
                disguiseManager.removeDisguise(player);
            }

            case "stats" -> showStats(player);

            case "spawnnpc" -> {

                npcManager.create(
                        player.getLocation()
                );

                player.sendMessage(
                        "§aSuccessfully spawned a Quick Join NPC."
                );

                return true;
            }

            case "removenpc" -> removeNpc(player);

            case "info" -> arenaInfo(player, args);

            default -> sendHelp(player);




        }

        return true;
    }

    private void arenaInfo(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage("§cUsage: /blockhunt info <arena>");
            return;
        }

        Arena arena = arenaManager.getArena(args[1]);

        if (arena == null) {
            player.sendMessage("§cArena not found.");
            return;
        }

        player.sendMessage("");
        player.sendMessage("§6§lArena Info");
        player.sendMessage("§eName: §f" + arena.getName());
        player.sendMessage("§eState: §f" + arena.getState());
        player.sendMessage("§ePlayers: §f" + arena.getPlayers().size());

        player.sendMessage(
                "§eLobby: §f" +
                        (arena.getLobbySpawn() != null)
        );

        player.sendMessage(
                "§eHunter Spawn: §f" +
                        (arena.getHunterSpawn() != null)
        );

        player.sendMessage(
                "§eHider Spawn: §f" +
                        (arena.getHiderSpawn() != null)
        );
    }

    private void removeNpc(Player player) {

        for (Entity entity : player.getNearbyEntities(
                5,
                5,
                5
        )) {

            if (!npcManager.isNPC(entity)) {
                continue;
            }

            entity.remove();

            player.sendMessage(
                    "§cRemoved NPC."
            );

            return;
        }

        player.sendMessage(
                "§cNo NPC found nearby."
        );
    }

    private void setRole(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage(
                    "§cUsage: /blockhunt role <hunter|hider|spectator>"
            );
            return;
        }

        try {

            Role role =
                    Role.valueOf(args[1].toUpperCase());

            playerManager.get(player)
                    .setRole(role);

            player.sendMessage(
                    "§aRole set to §f" + role.name()
            );

        } catch (IllegalArgumentException ex) {

            player.sendMessage(
                    "§cInvalid role."
            );
        }
    }

    private void setHunterSpawn(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage(
                    "§cUsage: /blockhunt sethunterspawn <arena>"
            );
            return;
        }

        Arena arena = arenaManager.getArena(args[1]);

        if (arena == null) {
            player.sendMessage("§cArena not found.");
            return;
        }

        arena.setHunterSpawn(player.getLocation());

        player.sendMessage(
                "§aHunter spawn updated."
        );
    }

    private void setHiderSpawn(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage(
                    "§cUsage: /blockhunt sethiderspawn <arena>"
            );
            return;
        }

        Arena arena = arenaManager.getArena(args[1]);

        if (arena == null) {
            player.sendMessage("§cArena not found.");
            return;
        }

        arena.setHiderSpawn(player.getLocation());

        player.sendMessage(
                "§aHider spawn updated."
        );
    }



    private void setLobby(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage(
                    "§cUsage: /blockhunt setlobby <arena>"
            );
            return;
        }

        Arena arena = arenaManager.getArena(args[1]);

        if (arena == null) {
            player.sendMessage("§cArena not found.");
            return;
        }

        arena.setLobbySpawn(player.getLocation());

        player.sendMessage(
                "§aLobby spawn updated."
        );
    }



    private void listArenas(Player player) {

        player.sendMessage("");
        player.sendMessage("§6§lARENAS");

        for (Arena arena : arenaManager.getArenas()) {

            player.sendMessage(
                    "§e" + arena.getName()
                            + " §7("
                            + arena.getPlayers().size()
                            + " players)"
            );
        }

        player.sendMessage("");
    }

    private void createArena(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage("§cUsage: /blockhunt create <name>");
            return;
        }

        String name = args[1];

        if (arenaManager.getArena(name) != null) {
            player.sendMessage("§cArena already exists.");
            return;
        }

        Arena arena = new Arena(name, winCondition);
        arena.setLobbySpawn(player.getLocation());

        arenaManager.registerArena(arena);

        player.sendMessage(
                "§aCreated arena §f" + name
        );
    }

    private void joinArena(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage("§cUsage: /blockhunt join <arena>");
            return;
        }

        if (arenaManager.isInArena(player)) {
            player.sendMessage("§cYou are already in an arena.");
            return;
        }

        Arena arena = arenaManager.getArena(args[1]);

        if (arena == null) {
            player.sendMessage("§cArena not found.");
            return;
        }

        if (arena.getState() != GameState.WAITING) {
            player.sendMessage("§cThis arena is already in progress.");
            return;
        }

        if (arena.getPlayers().size() >= 16) {
            player.sendMessage("§cThis arena is full.");
            return;
        }

        arenaManager.addPlayer(player, arena);

        BlockHuntPlayer data = playerManager.get(player);
        data.resetGameData();

        player.getInventory().clear();
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setFireTicks(0);

        if (arena.getLobbySpawn() != null) {
            player.teleport(arena.getLobbySpawn());
        }

        gameManager.broadcast(
                arena,
                "§a" + player.getName()
                        + " §7joined the arena §8("
                        + arena.getPlayers().size()
                        + "/16§8)"
        );

        player.sendMessage("");
        player.sendMessage("§6§lBLOCK HUNT");
        player.sendMessage("§eArena: §f" + arena.getName());
        player.sendMessage("§ePlayers: §f" + arena.getPlayers().size() + "/16");
        player.sendMessage("");

        gameManager.startCountdown(arena);
    }

    private void leaveArena(Player player) {

        Arena arena = arenaManager.getArena(player);

        if (arena == null) {
            player.sendMessage("§cYou are not in an arena.");
            return;
        }

        arenaManager.removePlayer(player);

        player.getInventory().clear();

        BlockHuntPlayer data = playerManager.get(player);

        data.resetGameData();

        player.sendMessage(
                "§cYou left the arena."
        );
    }

    private void startArena(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage("§cUsage: /blockhunt start <arena>");
            return;
        }

        Arena arena = arenaManager.getArena(args[1]);

        if (arena == null) {
            player.sendMessage("§cArena not found.");
            return;
        }

        if (!gameManager.startGame(arena)) {

            player.sendMessage(
                    "§cUnable to start arena §f" + arena.getName()
            );

            return;
        }

        player.sendMessage(
                "§aSuccessfully started arena §f"
                        + arena.getName()
        );
    }

    private void disguise(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage(
                    "§cUsage: /blockhunt disguise <block>"
            );
            return;
        }

        try {

            BlockType blockType =
                    BlockType.valueOf(
                            args[1].toUpperCase()
                    );

            BlockHuntPlayer data =
                    playerManager.get(player);

            data.setRole(Role.HIDER);

            disguiseManager.disguise(
                    player,
                    blockType
            );



        } catch (IllegalArgumentException exception) {

            player.sendMessage(
                    "§cUnknown block type."
            );
        }
    }

    private void deleteArena(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage("§cUsage: /blockhunt delete <arena>");
            return;
        }

        Arena arena = arenaManager.getArena(args[1]);

        if (arena == null) {
            player.sendMessage("§cArena not found.");
            return;
        }

        arenaManager.unregisterArena(arena.getName());

        player.sendMessage(
                "§cDeleted arena §f" + arena.getName()
        );
    }

    private void sendHelp(Player player) {

        player.sendMessage("§8§m--------------------------------");
        player.sendMessage("§6§lBLOCK HUNT §7Commands");
        player.sendMessage("");

        player.sendMessage("§eArena Commands");
        player.sendMessage(" §7• §f/blockhunt create <name>");
        player.sendMessage(" §7• §f/blockhunt delete <arena>");
        player.sendMessage(" §7• §f/blockhunt list");
        player.sendMessage(" §7• §f/blockhunt join <arena>");
        player.sendMessage(" §7• §f/blockhunt leave");
        player.sendMessage(" §7• §f/blockhunt start <arena>");
        player.sendMessage("");

        player.sendMessage("§eArena Setup");
        player.sendMessage(" §7• §f/blockhunt setlobby <arena>");
        player.sendMessage(" §7• §f/blockhunt sethiderspawn <arena>");
        player.sendMessage(" §7• §f/blockhunt sethunterspawn <arena>");
        player.sendMessage("");

        player.sendMessage("§eDisguise Testing");
        player.sendMessage(" §7• §f/blockhunt disguise <block>");
        player.sendMessage(" §7• §f/blockhunt undisguise");
        player.sendMessage(" §7• §f/blockhunt solidify");
        player.sendMessage(" §7• §f/blockhunt unsolidify");
        player.sendMessage("");

        player.sendMessage("§eDebug Commands");
        player.sendMessage(" §7• §f/blockhunt role <hunter|hider|spectator>");
        player.sendMessage(" §7• §f/blockhunt stats");
        player.sendMessage("");

        player.sendMessage("§8§m--------------------------------");
    }

    private void showStats(Player player) {

        BlockHuntPlayer data =
                playerManager.get(player);

        player.sendMessage("");
        player.sendMessage("§6§lBLOCK HUNT STATS");
        player.sendMessage("§eRole: §f" + data.getRole());
        player.sendMessage("§eKills: §f" + data.getKills());
        player.sendMessage("§eDeaths: §f" + data.getDeaths());
        player.sendMessage("§eGames Played: §f" + data.getGamesPlayed());
        player.sendMessage("§eGames Won: §f" + data.getGamesWon());
        player.sendMessage("");
    }
}