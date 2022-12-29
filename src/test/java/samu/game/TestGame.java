package samu.game;

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
