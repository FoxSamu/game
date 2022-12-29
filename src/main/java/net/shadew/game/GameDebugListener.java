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
