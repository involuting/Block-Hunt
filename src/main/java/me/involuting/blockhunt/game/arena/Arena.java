package me.involuting.blockhunt.game.arena;

import me.involuting.blockhunt.game.state.GameState;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Arena {

    private final String name;

    private Location lobbySpawn;
    private Location hunterSpawn;
    private Location hiderSpawn;

    private final Set<UUID> players = new HashSet<>();

    private GameState state = GameState.WAITING;

    private int minPlayers = 2;
    private int maxPlayers = 8;

    /**
     * -1 = waiting for players
     * >0 = countdown running
     * 0 = game starting/started
     */
    private int countdown = -1;

    public Arena(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public void setLobbySpawn(Location lobbySpawn) {
        this.lobbySpawn = lobbySpawn;
    }

    public Location getHunterSpawn() {
        return hunterSpawn;
    }

    public void setHunterSpawn(Location hunterSpawn) {
        this.hunterSpawn = hunterSpawn;
    }

    public Location getHiderSpawn() {
        return hiderSpawn;
    }

    public void setHiderSpawn(Location hiderSpawn) {
        this.hiderSpawn = hiderSpawn;
    }

    public Set<UUID> getPlayers() {
        return players;
    }

    public void addPlayer(UUID uuid) {
        players.add(uuid);
    }

    public void removePlayer(UUID uuid) {
        players.remove(uuid);
    }

    public int getPlayerCount() {
        return players.size();
    }

    public boolean isFull() {
        return getPlayerCount() >= maxPlayers;
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public int getPlayersNeeded() {
        return Math.max(0, minPlayers - getPlayerCount());
    }

    public boolean canStart() {
        return getPlayerCount() >= minPlayers;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public boolean isCountingDown() {
        return countdown > 0;
    }

    public void reset() {

        players.clear();

        state = GameState.WAITING;

        countdown = -1;
    }
}