package samu.game;

/**
 * An object that receives game signals.
 */
public interface Signalable {
    /**
     * Handle a certain game signal.
     *
     * @param signal The signal
     */
    void signal(Signal signal);
}
