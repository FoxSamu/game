/*
 * Copyright 2022 Shadew
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package net.shadew.game;

public class Loop {
    private final Lifecycle life;
    private SyncService sync;
    private ExceptionHandler exceptionHandler;
    private GameDebugListener gameDebug = GameDebugListener.NOOP;

    private long startTime;
    private long frameStart;
    private long frameTime;
    private float fps;

    private boolean alive = false;
    private boolean stop = false;

    public Loop(Lifecycle life) {
        this.life = life;
    }

    public SyncService sync() {
        return sync;
    }

    public void sync(SyncService sync) {
        this.sync = sync;
    }

    public GameDebugListener gameDebug() {
        return gameDebug;
    }

    public boolean alive() {
        return alive;
    }

    public void stop() {
        stop = true;
    }

    public long frameStart() {
        return frameStart;
    }

    public long frameTime() {
        return frameTime;
    }

    public float dt() {
        return frameTime / 1000F;
    }

    public float fps() {
        return fps;
    }

    public long startTime() {
        return startTime;
    }

    public long uptime() {
        return System.currentTimeMillis() - startTime;
    }

    public long frameUptime() {
        return frameStart - startTime;
    }

    public void exceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public void run() {
        if (alive)
            throw new RuntimeException("Already running");

        alive = true;
        try {
            life.init();
        } catch (Throwable thr) {
            if (exceptionHandler == null) throw thr;
            exceptionHandler.onException(thr, LifecyclePhase.INIT);
        }

        long time = startTime = System.currentTimeMillis();

        while (!stop) {
            frameStart = time;
            try {
                life.update();

                gameDebug.beforeSync();
                sync.sync();
                gameDebug.afterSync();
            } catch (Throwable thr) {
                if (exceptionHandler == null) throw thr;
                exceptionHandler.onException(thr, LifecyclePhase.UPDATE);
            }

            time = System.currentTimeMillis();
            frameTime = time - frameStart;
            fps = 1000F / frameTime;
        }

        try {
            life.cleanup();
        } catch (Throwable thr) {
            if (exceptionHandler == null) throw thr;
            exceptionHandler.onException(thr, LifecyclePhase.CLEANUP);
        }
        alive = false;
    }
}
