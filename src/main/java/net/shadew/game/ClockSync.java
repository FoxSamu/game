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
