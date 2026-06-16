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

    public boolean isFull() {
        return players.size() >= maxPlayers;
    }

    public int getPlayerCount() {
        return players.size();
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
}