package me.involuting.blockhunt.game.state;

public enum GameState {

    WAITING,
    STARTING,
    HIDING,
    SEEKING,
    ENDING;

    public boolean isRunning() {
        return this == HIDING || this == SEEKING;
    }

    public boolean canJoin() {
        return this == WAITING || this == STARTING;
    }
}
