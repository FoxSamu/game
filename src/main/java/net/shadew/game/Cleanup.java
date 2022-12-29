package net.shadew.game;

/**
 * Something that needs to be cleaned up after its lifetime.
 */
public interface Cleanup {
    /**
     * Cleans the resources used by this object.
     */
    void cleanup();
}
