package net.shadew.game;

/**
 * Synchronizes an update loop to some clock.
 */
public interface SyncService {
    /**
     * Wait for the next clock tick.
     */
    void sync();
}
