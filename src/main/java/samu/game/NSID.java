package samu.game;

import java.util.Comparator;
import java.util.regex.Pattern;

public record NSID(String ns, String id) implements Comparable<NSID> {
    public static final Comparator<NSID> COMPARATOR = Comparator.comparing(NSID::ns).thenComparing(NSID::id);
    private static final Pattern NS_PATTERN = Pattern.compile("[a-z0-9_]+");
    private static final Pattern ID_PATTERN = Pattern.compile("[a-z0-9./_\\-]+");

    public NSID {
        if (!checkNs(ns))
            throw new IllegalArgumentException("Illegal namespace");
        if (!checkId(id))
            throw new IllegalArgumentException("Illegal identifier");
    }

    private NSID(String[] id) {
        this(id[0], id[1]);
    }

    public NSID(String nsid) {
        this(parse(nsid));
    }

    public String format(String fmt) {
        return fmt.formatted(ns, id);
    }

    @Override
    public String toString() {
        return ns + ":" + id;
    }

    public static boolean checkNs(String ns) {
        return NS_PATTERN.asMatchPredicate().test(ns);
    }

    public static boolean checkId(String id) {
        return ID_PATTERN.asMatchPredicate().test(id);
    }

    private static String[] parse(String nsid) {
        int i = nsid.indexOf(':');
        if (i < 0) throw new IllegalArgumentException("NSID has no : symbol");
        return new String[] {nsid.substring(0, i), nsid.substring(i + 1)};
    }

    @Override
    public int compareTo(NSID nsid) {
        return COMPARATOR.compare(this, nsid);
    }
}
