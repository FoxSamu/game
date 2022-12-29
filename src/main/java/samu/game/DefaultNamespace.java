package samu.game;

public final class DefaultNamespace {
    static String defNs = System.getProperty("defns");

    private DefaultNamespace() {
        throw new Error("No DefaultNamespace instances for you");
    }

    public static void set(String defns) {
        defNs = defns;
    }
}
