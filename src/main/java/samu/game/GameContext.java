package samu.game;

public interface GameContext<G extends Game<G>> {
    G game();
    Module<?> module(NSID id);
    void emit(Signal signal);

    void startService(Service<? extends G> service);
    boolean stopService(Service<? extends G> service);
    boolean stopService(NSID nsid);
}
