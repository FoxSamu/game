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

public class GameException extends RuntimeException {
    private boolean fatal;

    public GameException() {
        this(false, null, null);
    }

    public GameException(String message) {
        this(false, message, null);
    }

    public GameException(Throwable cause) {
        this(cause instanceof Error || cause instanceof GameException e && e.fatal(), null, cause);
    }

    public GameException(String message, Throwable cause) {
        this(cause instanceof Error || cause instanceof GameException e && e.fatal(), message, cause);
    }

    public GameException(boolean fatal) {
        this(fatal, null, null);
    }

    public GameException(boolean fatal, String message) {
        this(fatal, message, null);
    }

    public GameException(boolean fatal, Throwable cause) {
        this(fatal, null, cause);
    }

    public GameException(boolean fatal, String message, Throwable cause) {
        super(message, cause);

        this.fatal = fatal;
    }

    /**
     * Check if the given exception is fatal, which usually means that the game should stop.
     *
     * @param exc The exception
     * @return True if it is fatal
     */
    public static boolean isFatalException(Throwable exc) {
        return exc instanceof Error || exc instanceof GameException e && e.fatal();
    }

    public boolean fatal() {
        return fatal;
    }

    public void fatal(boolean fatal) {
        this.fatal = fatal;
    }
}
