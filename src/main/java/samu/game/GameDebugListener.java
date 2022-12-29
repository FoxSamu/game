package samu.game;

public interface GameDebugListener {
    GameDebugListener NOOP = new GameDebugListener() {
    };

    default void beforeInit() {
    }

    default void afterInit() {
    }

    default void beforeUpdate() {
    }

    default void afterUpdate() {
    }

    default void beforeCleanup() {
    }

    default void afterCleanup() {
    }


    default void moduleBeforeInit(Module<?> module) {
    }

    default void moduleAfterInit(Module<?> module) {
    }

    default void moduleBeforeSignal(Module<?> module, Signal signal) {
    }

    default void moduleAfterSignal(Module<?> module, Signal signal) {
    }

    default void moduleBeforeUpdate(Module<?> module) {
    }

    default void moduleAfterUpdate(Module<?> module) {
    }

    default void moduleBeforeCleanup(Module<?> module) {
    }

    default void moduleAfterCleanup(Module<?> module) {
    }


    default void serviceBeforeInit(Service<?> service) {
    }

    default void serviceAfterInit(Service<?> service) {
    }

    default void serviceBeforeSignal(Service<?> service, Signal signal) {
    }

    default void serviceAfterSignal(Service<?> service, Signal signal) {
    }

    default void serviceBeforeUpdate(Service<?> service) {
    }

    default void serviceAfterUpdate(Service<?> service) {
    }

    default void serviceBeforeCleanup(Service<?> service) {
    }

    default void serviceAfterCleanup(Service<?> service) {
    }

    default void serviceBeforeAwaitFinish(Service<?> service) {
    }

    default void serviceAfterAwaitFinish(Service<?> service) {
    }


    default void gameBeforeInit() {
    }

    default void gameAfterInit() {
    }

    default void gameBeforeSignal(Signal signal) {
    }

    default void gameAfterSignal(Signal signal) {
    }

    default void gameBeforeUpdate() {
    }

    default void gameAfterUpdate() {
    }

    default void gameBeforeCleanup() {
    }

    default void gameAfterCleanup() {
    }


    default void beforeSignal(Signal signal) {
    }

    default void afterSignal(Signal signal) {
    }


    default void beforeSync() {
    }

    default void afterSync() {
    }


    default void serviceStarts(Service<?> service) {
    }

    default void serviceStops(Service<?> service) {
    }


    default void beforeModuleSorting() {
    }

    default void afterModuleSorting() {
    }
}
