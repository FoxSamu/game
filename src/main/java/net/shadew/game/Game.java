package net.shadew.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Game implements Lifecycle {
    private final Loop loop = new Loop(new GameLife());

    private final List<Module<?>> modules = new ArrayList<>();
    private final Map<NSID, Module<?>> modulesById = new HashMap<>();

    protected final <M extends Module<?>> M addModule(M module) {
        modules.add(module);
        modulesById.put(module.id(), module);
        return module;
    }

    public final Module<?> module(NSID id) {
        return modulesById.get(id);
    }

    public final void run() {
        loop.run();
    }

    public final void stop() {
        loop.stop();
    }

    public final boolean alive() {
        return loop.alive();
    }

    public final float fps() {
        return loop.fps();
    }

    public final long frameTime() {
        return loop.frameTime();
    }

    public final long frameStart() {
        return loop.frameStart();
    }

    public final long startTime() {
        return loop.startTime();
    }

    public final long uptime() {
        return loop.uptime();
    }

    public final long frameUptime() {
        return loop.frameUptime();
    }

    protected final void sync(SyncService service) {
        loop.sync(service);
    }

    protected final void exceptionHandler(ExceptionHandler<LifecyclePhase> exceptionHandler) {
        loop.exceptionHandler(exceptionHandler);
    }

    private class GameLife implements Lifecycle {
        @Override
        public void init() {
            Game.this.init();

            for (Module<?> m : modules) {
                m.init();
            }
        }

        @Override
        public void cleanup() {
            Game.this.cleanup();

            for (int i = modules.size() - 1; i >= 0; i --) {
                modules.get(i).cleanup();
            }
        }

        @Override
        public void update() {
            Game.this.update();

            for (Module<?> m : modules) {
                m.update();
            }
        }
    }
}
