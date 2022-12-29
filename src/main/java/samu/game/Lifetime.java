package samu.game;

/**
 * An object that has to be initialized and cleaned up. It consists of {@link Init} and {@link Cleanup}.
 */
public interface Lifetime extends Init, Cleanup {
    @Override
    void init();

    @Override
    void cleanup();
}
