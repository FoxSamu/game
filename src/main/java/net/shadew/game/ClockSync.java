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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ClockSync implements SyncService {
    private final int msPerFrame;
    private final long timeOrigin;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition c = lock.newCondition();

    public ClockSync(int msPerFrame, long timeOrigin) {
        this.msPerFrame = msPerFrame;
        this.timeOrigin = timeOrigin;
    }

    public ClockSync(int msPerFrame) {
        this(msPerFrame, System.currentTimeMillis());
    }

    @Override
    public void sync() {
        try {
            lock.lock();

            long t = System.currentTimeMillis() - timeOrigin;
            long f = t / msPerFrame;
            long nextT = (f + 1) * msPerFrame;

            t = System.currentTimeMillis() - timeOrigin;
            while (t < nextT) {
                try {
                    c.await(nextT - t, TimeUnit.MILLISECONDS);
                } catch (InterruptedException ignored) {
                }
                t = System.currentTimeMillis() - timeOrigin;
            }
        } finally {
            lock.unlock();
        }
    }
}
