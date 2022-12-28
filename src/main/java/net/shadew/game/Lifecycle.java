package net.shadew.game;

public interface Lifecycle extends Lifetime, Update {
    @Override
    void init();

    @Override
    void update();

    @Override
    void cleanup();
}
