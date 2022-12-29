package net.shadew.game;

/**
 * A lifecycle is a {@link Lifetime} that also receives updates. It consists of {@link Init}, {@link Update} and
 * {@link Cleanup}.
 */
public interface Lifecycle extends Lifetime, Update {
    /**
     * {@inheritDoc}
     */
    @Override
    void init();

    /**
     * {@inheritDoc}
     */
    @Override
    void update();

    /**
     * {@inheritDoc}
     */
    @Override
    void cleanup();
}
