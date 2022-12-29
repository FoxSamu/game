package net.shadew.game;

public enum NoSync implements SyncService {
    INSTANCE;

    @Override
    public void sync() {
    }
}
