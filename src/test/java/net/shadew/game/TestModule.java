package net.shadew.game;

public class TestModule extends Module<TestGame> {
    public TestModule(NSID id, TestGame game) {
        super(id, game);
    }

    @Override
    public void init() {
        System.out.println("INIT " + id);
    }

    @Override
    public void cleanup() {
        System.out.println("CLEANUP " + id);
    }

    @Override
    public void update() {
        System.out.println("UPDATE " + id);
    }
}
