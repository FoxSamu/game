package samu.game;

public enum NoSync implements SyncService {
    INSTANCE;

    @Override
    public void sync() {
    }
}
