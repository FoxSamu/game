package net.shadew.game;

public class TestGame extends Game {
    private final TestModule module = addModule(new TestModule(new NSID("test:module"), this));

    public TestGame() {
        sync(new ClockSync(500));
    }

    @Override
    public void init() {
        System.out.println("INIT " + System.currentTimeMillis());
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
        new TestGame().run();
    }
}
