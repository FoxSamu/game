package samu.game;

import java.util.HashMap;
import java.util.Map;

public abstract class Module<G extends Game<G>> implements Lifecycle, Signalable, ExceptionHandler, GameContext<G> {
    protected final NSID id;
    protected final G game;

    final Map<NSID, Relation> before = new HashMap<>();
    final Map<NSID, Relation> after = new HashMap<>();

    public Module(NSID id, G game) {
        this.id = id;
        this.game = game;
    }

    public Module(String id, G game) {
        this.id = new NSID(id);
        this.game = game;
    }

    public final void dependsOn(NSID module) {
        this.after.put(module, Relation.REQUIRED);
    }

    public final void dependsOn(NSID module, Relation r) {
        this.after.put(module, r);
    }

    public final void dependsOn(String module) {
        dependsOn(new NSID(module));
    }

    public final void dependsOn(String module, Relation r) {
        dependsOn(new NSID(module), r);
    }

    public final void finalizedBy(NSID module) {
        this.before.put(module, Relation.REQUIRED);
    }

    public final void finalizedBy(NSID module, Relation r) {
        this.before.put(module, r);
    }

    public final void finalizedBy(String module) {
        finalizedBy(new NSID(module));
    }

    public final void finalizedBy(String module, Relation r) {
        finalizedBy(new NSID(module), r);
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
        ErrorReport report = ErrorReport.of(exc).addContext(ErrorContext.in(this, phase));
        game.onException(report, phase);
    }
}
