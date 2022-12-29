package net.shadew.game;

public class GameException extends RuntimeException {
    private final boolean fatal;

    public GameException() {
        this(false, null, null);
    }

    public GameException(String message) {
        this(false, message, null);
    }

    public GameException(Throwable cause) {
        this(cause instanceof Error || cause instanceof GameException e && e.fatal(), null, cause);
    }

    public GameException(String message, Throwable cause) {
        this(cause instanceof Error || cause instanceof GameException e && e.fatal(), message, cause);
    }

    public GameException(boolean fatal) {
        this(fatal, null, null);
    }

    public GameException(boolean fatal, String message) {
        this(fatal, message, null);
    }

    public GameException(boolean fatal, Throwable cause) {
        this(fatal, null, cause);
    }

    public GameException(boolean fatal, String message, Throwable cause) {
        super(message, cause);

        this.fatal = fatal;
    }

    public boolean fatal() {
        return fatal;
    }
}
