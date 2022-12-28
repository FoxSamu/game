package net.shadew.game;

public abstract class Module<G extends Game> implements Lifecycle {
    protected final NSID id;
    protected final G game;

    public Module(NSID id, G game) {
        this.id = id;
        this.game = game;
    }

    public final NSID id() {
        return id;
    }

    public final G game() {
        return game;
    }
}
