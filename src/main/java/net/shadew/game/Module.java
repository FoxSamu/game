package net.shadew.game;

public abstract class Module<G extends Game<G>> implements Lifecycle, Signalable, ExceptionHandler, GameContext<G> {
    protected final NSID id;
    protected final G game;

    public Module(NSID id, G game) {
        this.id = id;
        this.game = game;
    }

    public final NSID id() {
        return id;
    }

    @Override
    public final G game() {
        return game;
    }

    @Override
    public final Module<?> module(NSID id) {
        return game.module(id);
    }

    @Override
    public final void emit(Signal signal) {
        game.emit(signal);
    }

    @Override
    public void startService(Service<? extends G> service) {
        game.startService(service);
    }

    @Override
    public boolean stopService(Service<? extends G> service) {
        return game.stopService(service);
    }

    @Override
    public boolean stopService(NSID nsid) {
        return game.stopService(nsid);
    }

    @Override
    public void init() {
    }

    @Override
    public void update() {
    }

    @Override
    public void cleanup() {
    }

    @Override
    public void signal(Signal signal) {
    }

    @Override
    public void onException(Throwable exc, LifecyclePhase phase) {
        game.onException(new ModuleException(this, exc), phase);
    }
}
