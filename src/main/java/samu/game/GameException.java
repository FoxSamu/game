package samu.game;

public class GameException extends RuntimeException {
    private boolean fatal;

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

    /**
     * Check if the given exception is fatal, which usually means that the game should stop.
     *
     * @param exc The exception
     * @return True if it is fatal
     */
    public static boolean isFatalException(Throwable exc) {
        return exc instanceof Error || exc instanceof GameException e && e.fatal();
    }

    public boolean fatal() {
        return fatal;
    }

    public void fatal(boolean fatal) {
        this.fatal = fatal;
    }
}
