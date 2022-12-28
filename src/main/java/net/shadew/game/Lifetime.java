package net.shadew.game;

public interface Lifetime extends Init, Cleanup {
    @Override
    void init();

    @Override
    void cleanup();
}
