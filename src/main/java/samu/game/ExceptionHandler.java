package samu.game;

/**
 * A handler for exceptions that occur during a lifecycle.
 */
public interface ExceptionHandler {
    /**
     * Handles an exception.
     *
     * @param exc   The exception
     * @param phase The lifecycle phase
     */
    void onException(Throwable exc, LifecyclePhase phase);
}
