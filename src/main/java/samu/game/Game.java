package samu.game;

import java.util.*;

/**
 * A game with a game loop.
 */
public abstract class Game<G extends Game<G>> implements Lifecycle, Signalable, ExceptionHandler, GameContext<G> {
    private final Loop loop = new Loop(new GameLife());

    private final Map<NSID, Module<? extends G>> modulesById = new HashMap<>();
    private final ModuleSorter<G> moduleSorter = new ModuleSorter<>(modulesById);
    private final List<Module<? extends G>> modules = Collections.unmodifiableList(moduleSorter.modules());
    private final Set<NSID> circularlyDependentModules = Collections.unmodifiableSet(moduleSorter.circularDependencies());
    private final Set<NSID> missingModules = Collections.unmodifiableSet(moduleSorter.missing());
    private final Set<NSID> optMissingModules = Collections.unmodifiableSet(moduleSorter.optMissing());
    private final Set<NSID> loadedModules = Collections.unmodifiableSet(moduleSorter.loaded());

    private final Map<NSID, Service<? extends G>> services = new HashMap<>();
    private final Set<Service<? extends G>> newServices = new HashSet<>();
    private final Set<Service<? extends G>> oldServices = new HashSet<>();

    private GameDebugListener debug = GameDebugListener.NOOP;

    public Game() {
        loop.exceptionHandler(this);
    }

    /**
     * Add a module to the game.
     *
     * @param module The module, must not be null
     * @return The module, for ease when assigning it to a field
     */
    protected final <M extends Module<? extends G>> M addModule(M module) {
        if (module == null) throw new NullPointerException("Null module");

        if (modulesById.containsKey(module.id()))
            throw new IllegalArgumentException("Module " + module.id() + " already added");

        modulesById.put(module.id(), module);
        return module;
    }

    @Override
    @SuppressWarnings("unchecked")
    public G game() {
        return (G) this;
    }

    @Override
    public final Module<?> module(NSID id) {
        return modulesById.get(id);
    }

    /**
     * Starts this game.
     */
    public final void run() {
        loop.run();
    }

    /**
     * Stops this game.
     */
    public final void stop() {
        loop.stop();
    }

    /**
     * Check whether the game is running.
     *
     * @return True if it is running
     */
    public final boolean alive() {
        return loop.alive();
    }

    /**
     * Returns the FPS of the game, i.e. the amount of updates (frames) per second.
     *
     * @return The FPS of the game.
     */
    public final float fps() {
        return loop.fps();
    }

    /**
     * Returns the time (in milliseconds) between the start of the previous frame and the this frame.
     *
     * @return The frame time of the last frame
     */
    public final long frameTime() {
        return loop.frameTime();
    }

    /**
     * Returns the time delta, in seconds.
     *
     * @return The time delta
     */
    public final float dt() {
        return loop.dt();
    }

    /**
     * Returns a timestamp of the current frame, as measured by {@link System#currentTimeMillis()}.
     *
     * @return The timestamp of the current frame
     */
    public final long frameStart() {
        return loop.frameStart();
    }

    /**
     * Returns the timestamp of when the game started, as measured by {@link System#currentTimeMillis()}.
     *
     * @return The timestamp of when the game started
     */
    public final long startTime() {
        return loop.startTime();
    }

    /**
     * Returns the amount of milliseconds the game has been running.
     *
     * @return The amount of milliseconds the game has been running
     */
    public final long uptime() {
        return loop.uptime();
    }

    /**
     * Returns the amount of milliseconds between the start of the game and the start of the current frame.
     *
     * @return The time between the start of the game and the start of the current frame
     */
    public final long frameUptime() {
        return loop.frameUptime();
    }

    /**
     * Sets the {@link SyncService} of the game loop.
     *
     * @param service The sync service, not null
     */
    protected final void sync(SyncService service) {
        if (service == null) throw new NullPointerException("Null SyncService");

        loop.sync(service);
    }

    protected void debug(GameDebugListener debug) {
        this.debug = debug == null ? GameDebugListener.NOOP : debug;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void emit(Signal signal) {
        if (signal == null) throw new NullPointerException("Null Signal");

        debug.beforeSignal(signal);
        debug.gameBeforeSignal(signal);
        signal(signal);
        debug.gameAfterSignal(signal);

        for (Module<?> m : modules) {
            debug.moduleBeforeSignal(m, signal);
            m.signal(signal);
            debug.moduleAfterSignal(m, signal);
        }

        for (Service<? extends G> s : services.values()) {
            if (!newServices.contains(s) && !oldServices.contains(s)) {
                debug.serviceBeforeSignal(s, signal);
                s.signal(signal);
                debug.serviceAfterSignal(s, signal);
            }
        }
        debug.afterSignal(signal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startService(Service<? extends G> service) {
        debug.serviceStarts(service);

        NSID id = service.id();
        if (services.containsKey(id)) {
            throw new RuntimeException("Service " + id + " is already running");
        }

        services.put(id, service);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stopService(Service<? extends G> service) {
        return stopService(service.id());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stopService(NSID id) {
        if (!services.containsKey(id)) {
            return false;
        }
        debug.serviceStops(services.get(id));

        return oldServices.add(services.remove(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cleanup() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void signal(Signal signal) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onException(Throwable exc, LifecyclePhase phase) {
        if (exc instanceof ErrorReport er) {
            handleReport(er);
        } else {
            handleReport(ErrorReport.of(exc));
        }

        if (GameException.isFatalException(exc)) {
            stop();
        }
    }

    /**
     * Handle an error report.
     *
     * @param report The report
     */
    protected void handleReport(ErrorReport report) {
        report.print("Error report");
    }

    /**
     * Get a set of  module IDs that were in some way dependent on themselves, which is updated on initialization. If
     * this is not empty, the game should likely stop and it will throw a fatal {@link GameException} before calling any
     * initialization method. The set is sorted by the natural ordering of {@link NSID}s.
     *
     * @return The set of circularly dependent module IDs
     */
    public Set<NSID> circularlyDependentModules() {
        return circularlyDependentModules;
    }

    /**
     * Get a set of missing module IDs that were required by other modules, which is updated on initialization. If this
     * is not empty, the game should likely stop and it will throw a fatal {@link GameException} before calling any
     * initialization method. The set is sorted by the natural ordering of {@link NSID}s.
     *
     * @return The set of missing module IDs
     */
    protected final Set<NSID> missingModules() {
        return missingModules;
    }

    /**
     * Get a set of missing module IDs that were optionally needed by other modules, which is updated on initialization.
     * The set is sorted by the natural ordering of {@link NSID}s.
     *
     * @return The set of missing module IDs
     */
    protected final Set<NSID> optMissingModules() {
        return optMissingModules;
    }

    /**
     * Get a set of module IDs that were loaded, which is updated on initialization. The set is sorted by the natural
     * ordering of {@link NSID}s.
     *
     * @return The set of loaded module IDs
     */
    public Set<NSID> loadedModules() {
        return loadedModules;
    }

    /**
     * Get a list of modules in order of dependency.
     *
     * @return The list of modules
     */
    public List<Module<? extends G>> modules() {
        return modules;
    }

    /**
     * Called before initialization when required dependencies of some modules were not found. Default action throws a
     * {@link GameException} with the {@link GameException#fatal()} flag set.
     *
     * @param missing The set of missing modules, as returned by {@link #missingModules()}.
     */
    protected void handleMissingModules(Set<NSID> missing) {
        throw new GameException(true, "The following modules were required by other modules, but could not be found: " + moduleSorter.missing());
    }

    /**
     * Called before initialization when circular dependencies were found. Default action throws a {@link GameException}
     * with the {@link GameException#fatal()} flag set.
     *
     * @param cdms The set of Circularly Dependent Modules (CDMs), as returned by
     *             {@link #circularlyDependentModules()}.
     */
    protected void handleCircularDependencies(Set<NSID> cdms) {
        throw new GameException(true, "The following modules were dependent on themselves: " + moduleSorter.missing());
    }

    private class GameLife implements Lifecycle {
        @Override
        public void init() {
            debug.beforeModuleSorting();
            moduleSorter.sort();
            if (!missingModules.isEmpty()) {
                handleMissingModules(missingModules);
            }
            if (!circularlyDependentModules.isEmpty()) {
                handleCircularDependencies(circularlyDependentModules);
            }
            debug.afterModuleSorting();

            debug.beforeInit();
            debug.gameBeforeInit();
            Game.this.init();
            debug.gameAfterInit();

            for (Module<? extends G> m : modules) {
                try {
                    debug.moduleBeforeInit(m);
                    m.init();
                    debug.moduleAfterInit(m);
                } catch (Throwable thr) {
                    m.onException(thr, LifecyclePhase.INIT);
                }
            }
            debug.afterInit();
        }

        @Override
        public void cleanup() {
            debug.beforeCleanup();
            for (Service<? extends G> s : services.values()) {
                try {
                    debug.serviceBeforeCleanup(s);
                    s.cleanup();
                    debug.serviceAfterCleanup(s);
                } catch (Throwable thr) {
                    s.onException(thr, LifecyclePhase.CLEANUP);
                }
            }

            for (Service<? extends G> s : services.values()) {
                try {
                    debug.serviceBeforeAwaitFinish(s);
                    s.awaitFinish();
                    debug.serviceAfterAwaitFinish(s);
                } catch (Throwable thr) {
                    s.onException(thr, LifecyclePhase.CLEANUP);
                }
            }

            for (int i = modules.size() - 1; i >= 0; i--) {
                Module<? extends G> m = modules.get(i);
                try {
                    debug.moduleBeforeCleanup(m);
                    m.cleanup();
                    debug.moduleAfterCleanup(m);
                } catch (Throwable thr) {
                    m.onException(thr, LifecyclePhase.CLEANUP);
                }
            }

            debug.gameBeforeCleanup();
            Game.this.cleanup();
            debug.gameAfterCleanup();
            debug.afterCleanup();
        }

        @Override
        public void update() {
            debug.beforeUpdate();
            debug.gameBeforeUpdate();
            Game.this.update();
            debug.gameAfterUpdate();

            for (Module<? extends G> m : modules) {
                try {
                    debug.moduleBeforeUpdate(m);
                    m.update();
                    debug.moduleAfterUpdate(m);
                } catch (Throwable thr) {
                    m.onException(thr, LifecyclePhase.UPDATE);
                }
            }

            for (Service<? extends G> s : services.values()) {
                try {
                    debug.serviceBeforeUpdate(s);
                    s.update();
                    debug.serviceAfterUpdate(s);
                } catch (Throwable thr) {
                    s.onException(thr, LifecyclePhase.UPDATE);
                }
            }

            for (Service<? extends G> s : newServices) {
                try {
                    debug.serviceBeforeInit(s);
                    s.init();
                    debug.serviceAfterInit(s);
                } catch (Throwable thr) {
                    s.onException(thr, LifecyclePhase.INIT);
                }
            }
            newServices.clear();

            for (Service<? extends G> s : oldServices) {
                try {
                    debug.serviceBeforeCleanup(s);
                    s.cleanup();
                    debug.serviceAfterCleanup(s);
                } catch (Throwable thr) {
                    s.onException(thr, LifecyclePhase.CLEANUP);
                }
            }
            oldServices.clear();
            debug.afterUpdate();
        }
    }
}
