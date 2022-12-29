package net.shadew.game;

public final class DefaultNamespace {
    static String defNs = System.getProperty("net.shadew.defns");

    private DefaultNamespace() {
        throw new Error("No DefaultNamespace instances for you");
    }

    public static void set(String defns) {
        defNs = defns;
    }
}
