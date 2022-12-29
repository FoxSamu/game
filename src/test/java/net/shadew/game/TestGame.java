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

public class TestGame extends Game<TestGame> {
    private final TestModule module1 = addModule(new TestModule(new NSID("module1"), this));
    private final TestModule module2 = addModule(new TestModule(new NSID("module2"), this));
    private final TestModule module3 = addModule(new TestModule(new NSID("module3"), this));

    public TestGame() {
        sync(new ClockSync(2000));

        module1.dependsOn(new NSID("test:module2"));
        module3.finalizedBy(new NSID("test:module2"));
    }

    @Override
    public void init() {
        System.out.println("INIT " + System.currentTimeMillis());
        System.out.println("Module order: " + modules().stream().map(Module::id).toList());
    }

    @Override
    public void cleanup() {
        System.out.println("CLEANUP " + System.currentTimeMillis());
    }

    @Override
    public void update() {
        System.out.println("UPDATE " + frameUptime() + " / " + fps() + " FPS");
    }

    public static void main(String[] args) {
        DefaultNamespace.set("test");
        new TestGame().run();
    }
}
