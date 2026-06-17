package me.involuting.blockhunt.scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import me.involuting.blockhunt.game.arena.Arena;
import me.involuting.blockhunt.game.arena.manager.ArenaManager;
import me.involuting.blockhunt.game.manager.GameManager;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class ScoreboardAdapter {

    private final ArenaManager arenaManager;
    private final GameManager gameManager;

    private final Map<UUID, FastBoard> boards = new HashMap<>();

    public ScoreboardAdapter(
            ArenaManager arenaManager,
            GameManager gameManager
    ) {
        this.arenaManager = arenaManager;
        this.gameManager = gameManager;
    }

    public void createBoard(Player player) {

        FastBoard board = new FastBoard(player);
        board.updateTitle("§6§lBLOCK HUNT");

        boards.put(player.getUniqueId(), board);

        updateBoard(player);
    }

    public void removeBoard(Player player) {

        FastBoard board = boards.remove(player.getUniqueId());

        if (board != null) {
            board.delete();
        }
    }

    public void updateBoard(Player player) {

        FastBoard board = boards.get(player.getUniqueId());

        if (board == null) {
            return;
        }

        board.updateTitle("§6§lBLOCK HUNT");

        Arena arena = arenaManager.getArena(player);

        if (arena == null) {

            board.updateLines(
                    "§7Welcome!",
                    "",
                    "§fStatus",
                    " §8▪ §aIn Lobby",
                    "",
                    "§fPlay",
                    " §8▪ §7Join an match",
                    " §8▪ §7Choose a taunt",
                    " §8▪ §7Choose a disguise",
                    "",
                    "§6example.com"
            );

            return;
        }

        switch (arena.getState()) {

            case WAITING -> board.updateLines(waiting(arena));

            case STARTING -> board.updateLines(starting(arena));

            case HIDING -> board.updateLines(hiding(arena));

            case SEEKING -> board.updateLines(seeking(arena));

            case ENDING -> board.updateLines(ending(arena));
        }
    }

    private List<String> waiting(Arena arena) {

        return List.of(
                "§7Map: §f" + arena.getName(),
                "",
                "§fPlayers: §a" + arena.getPlayers().size(),
                "§fRequired: §a2",
                "",
                "§6example.com"
        );
    }

    private List<String> starting(Arena arena) {

        return List.of(
                "§7Map: §f" + arena.getName(),
                "",
                "§6Starting...",
                "",
                "§fPlayers: §a" + arena.getPlayers().size(),
                "§fCountdown: §e" + arena.getCountdown() + "s",
                "",
                "§6example.com"
        );
    }

    private List<String> hiding(Arena arena) {

        return List.of(
                "§7Map: §f" + arena.getName(),
                "",
                "§aHide!",
                "",
                "§fHiders: §a" + gameManager.getHiderCount(arena),
                "§fHunters: §c" + gameManager.getHunterCount(arena),
                "§fRelease: §e" + gameManager.getRemainingTime(arena) + "s",
                "",
                "§6example.com"
        );
    }

    private List<String> seeking(Arena arena) {

        return List.of(
                "§7Map: §f" + arena.getName(),
                "",
                "§cSeek!",
                "",
                "§fHiders: §a" + gameManager.getHiderCount(arena),
                "§fHunters: §c" + gameManager.getHunterCount(arena),
                "§fTime Left: §e" + gameManager.getRemainingTime(arena) + "s",
                "",
                "§6example.com"
        );
    }

    private List<String> ending(Arena arena) {

        return List.of(
                "§7Map: §f" + arena.getName(),
                "",
                "§6Game Over",
                "",
                "§eReturning to Lobby...",
                "",
                "§6example.com"
        );
    }

    public void updateAll() {

        for (FastBoard board : boards.values()) {

            Player player = board.getPlayer();

            if (player != null && player.isOnline()) {
                updateBoard(player);
            }
        }
    }
}