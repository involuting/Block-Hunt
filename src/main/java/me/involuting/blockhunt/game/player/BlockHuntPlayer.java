package me.involuting.blockhunt.game.player;

import lombok.Getter;
import lombok.Setter;
import me.involuting.blockhunt.game.disguise.type.BlockType;
import me.involuting.blockhunt.game.role.Role;
import org.bukkit.Location;

import java.util.UUID;
@Getter @Setter
public class BlockHuntPlayer {

    private final UUID uuid;

    private Role role = Role.SPECTATOR;

    private BlockType selectedBlock;

    private boolean solidified;
    private Location solidifiedLocation;

    private long lastMoveTime;

    private boolean alive = true;

    private int kills;
    private int deaths;

    private int gamesPlayed;
    private int gamesWon;

    public BlockHuntPlayer(UUID uuid) {
        this.uuid = uuid;
        this.lastMoveTime = System.currentTimeMillis();
    }

    public UUID getUuid() {
        return uuid;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public BlockType getSelectedBlock() {
        return selectedBlock;
    }

    public void setSelectedBlock(BlockType selectedBlock) {
        this.selectedBlock = selectedBlock;
    }

    public boolean isSolidified() {
        return solidified;
    }

    public void setSolidified(boolean solidified) {
        this.solidified = solidified;
    }

    public Location getSolidifiedLocation() {
        return solidifiedLocation;
    }

    public void setSolidifiedLocation(Location solidifiedLocation) {
        this.solidifiedLocation = solidifiedLocation;
    }

    public long getLastMoveTime() {
        return lastMoveTime;
    }

    public void setLastMoveTime(long lastMoveTime) {
        this.lastMoveTime = lastMoveTime;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isHunter() {
        return role == Role.HUNTER;
    }

    public boolean isHider() {
        return role == Role.HIDER;
    }

    public boolean isSpectator() {
        return role == Role.SPECTATOR;
    }

    public int getKills() {
        return kills;
    }

    public void addKill() {
        kills++;
    }

    public int getDeaths() {
        return deaths;
    }

    public void addDeath() {
        deaths++;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void addGamePlayed() {
        gamesPlayed++;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void addGameWon() {
        gamesWon++;
    }

    /**
     * Resets data for a new match.
     */
    public void resetGameData() {

        role = Role.SPECTATOR;

        selectedBlock = null;

        solidified = false;
        solidifiedLocation = null;

        alive = true;

        lastMoveTime = System.currentTimeMillis();

        kills = 0;
        deaths = 0;
    }

    /**
     * Called when a hider is eliminated.
     */
    public void eliminate() {

        alive = false;

        solidified = false;
        solidifiedLocation = null;

        role = Role.SPECTATOR;

        addDeath();
    }

    /**
     * Lifetime stats should NOT reset every game.
     */
    public void resetLifetimeStats() {

        kills = 0;
        deaths = 0;

        gamesPlayed = 0;
        gamesWon = 0;
    }


}