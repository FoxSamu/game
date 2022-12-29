package net.shadew.game;

public class Signal {
    protected final NSID id;

    public Signal(NSID id) {
        this.id = id;
    }

    public final NSID id() {
        return id;
    }
}
