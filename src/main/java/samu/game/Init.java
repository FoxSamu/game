package samu.game;

/**
 * Something that needs to be initialized before its lifetime.
 */
public interface Init {
    /**
     * Initializes this object.
     */
    void init();
}
