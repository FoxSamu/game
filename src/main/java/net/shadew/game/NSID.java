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

import java.util.Comparator;
import java.util.regex.Pattern;

public record NSID(String ns, String id) implements Comparable<NSID> {
    public static final Comparator<NSID> COMPARATOR = Comparator.comparing(NSID::ns).thenComparing(NSID::id);
    private static final String DEFNS = DefaultNamespace.defNs;
    private static final Pattern NS_PATTERN = Pattern.compile("[a-z0-9_]+");
    private static final Pattern ID_PATTERN = Pattern.compile("[a-z0-9./_\\-]+");

    static {
        if (!checkNs(DEFNS)) {
            throw new RuntimeException("Default namespace is not valid");
        }
    }

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
        if (i < 0) {
            if (DEFNS == null)
                throw new IllegalArgumentException("NSID has no : symbol");
            return new String[] {DEFNS, nsid};
        }
        return new String[] {nsid.substring(0, i), nsid.substring(i + 1)};
    }

    @Override
    public int compareTo(NSID nsid) {
        return COMPARATOR.compare(this, nsid);
    }
}
