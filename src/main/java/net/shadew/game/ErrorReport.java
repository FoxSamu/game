package net.shadew.game;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

public class ErrorReport extends GameException {
    private static final DateTimeFormatter TIME_FORMAT = new DateTimeFormatterBuilder()
                                                             .appendPattern("HH:mm:ss.SSS (z/'UTC'x) - dd MMMM uuuu")
                                                             .toFormatter(Locale.ENGLISH);

    private final ZonedDateTime instant = ZonedDateTime.now();
    private final Throwable initialProblem;
    private final Thread src;
    private final List<ErrorContext> contexts = new ArrayList<>();
    private final Map<String, String> properties = new HashMap<>();

    private ErrorReport(boolean fatal, Throwable initialProblem, Thread src) {
        super(fatal);
        this.initialProblem = initialProblem;
        this.src = src;
    }

    public ErrorReport prop(String k, String v) {
        properties.put(k, v);
        return this;
    }

    public ErrorReport prop(String k, Object v) {
        if (v instanceof Reportable r) {
            return prop(k, r.report());
        } else {
            return prop(k, v + "");
        }
    }

    public ErrorReport prop(String k, byte v) {
        return prop(k, "" + v);
    }

    public ErrorReport prop(String k, short v) {
        return prop(k, "" + v);
    }

    public ErrorReport prop(String k, int v) {
        return prop(k, "" + v);
    }

    public ErrorReport prop(String k, long v) {
        return prop(k, "" + v);
    }

    public ErrorReport prop(String k, float v) {
        return prop(k, "" + v);
    }

    public ErrorReport prop(String k, double v) {
        return prop(k, "" + v);
    }

    public ErrorReport prop(String k, char v) {
        return prop(k, "" + v);
    }

    public ErrorReport prop(String k, boolean v) {
        return prop(k, "" + v);
    }

    public ErrorReport prop(String k, boolean v, String yes, String no) {
        return prop(k, v ? yes : no);
    }

    public ErrorReport addContext(ErrorContext ctx) {
        contexts.add(ctx);
        return this;
    }

    public static ErrorReport of(Throwable problem) {
        if (problem instanceof ErrorReport r) return r;
        return new ErrorReport(isFatalException(problem), problem, Thread.currentThread());
    }

    public static ErrorReport of(Throwable problem, boolean fatal) {
        if (problem instanceof ErrorReport r) {
            r.fatal(fatal || r.fatal());
            return r;
        }
        return new ErrorReport(fatal, problem, Thread.currentThread());
    }

    private static int matchStackTraces(StackTraceElement[] a, StackTraceElement[] b) {
        int al = a.length, bl = b.length;
        int len = Math.min(al, bl);
        for (int i = 1; i <= len; i++) {
            if (!a[al - i].equals(b[bl - i])) {
                return i;
            }
        }
        return len;
    }

    public String writeString(String header) {
        StringBuilder builder = new StringBuilder();
        try {
            write(builder, header);
        } catch (IOException ignored) {
        }
        return builder.toString();
    }

    public void print(String header) {
        try {
            write(System.err, header);
        } catch (IOException ignored) {
        }
    }

    public void write(Appendable out, String header) throws IOException {
        StackTraceElement[] mainTrace = initialProblem.getStackTrace();
        int mainTail = mainTrace.length;

        List<ContextSection> contexts = new ArrayList<>();
        ContextSection lastCtxSec = null;

        for (ErrorContext ctx : this.contexts) {
            ContextSection sec = new ContextSection(ctx, mainTrace);
            int matches = matchStackTraces(mainTrace, ctx.trace);

            int head;
            if (lastCtxSec == null) {
                mainTail -= matches;
                head = mainTail;
            } else {
                lastCtxSec.tail -= matches;
                head = lastCtxSec.tail;
            }

            sec.head = head;
            sec.tail = mainTrace.length;

            contexts.add(sec);
            lastCtxSec = sec;
        }

        List<Section> first = new ArrayList<>();
        List<Section> second = new ArrayList<>();
        List<Section> third = new ArrayList<>();

        first.add(new ExceptionDetailsSection(initialProblem, this.contexts.isEmpty() ? null : this.contexts.get(0)));
        first.add(new HeadSection(initialProblem, mainTrace, mainTail));
        first.addAll(contexts);

        second.add(new FullTraceSection(initialProblem));

        third.add(new SystemInfoSection());

        Box box = new Box();
        box.add(header);
        box.add(TIME_FORMAT.format(instant));
        box.thickLine();

        boolean ln = false;
        for (Section section : first) {
            if (!ln) ln = true;
            else box.line();

            section.add(box);
        }
        box.thickLine();

        ln = false;
        for (Section section : second) {
            if (!ln) ln = true;
            else box.line();

            section.add(box);
        }
        box.thickLine();

        ln = false;
        for (Section section : third) {
            if (!ln) ln = true;
            else box.line();

            section.add(box);
        }

        box.output(out, "", "", 100, System.lineSeparator());
    }

    private interface Section {
        void add(Box box);
    }

    private class SystemInfoSection implements Section {
        @Override
        public void add(Box box) {
            Map<String, String> info = new LinkedHashMap<>();
            info.put("Thread", src.getName() + " [#" + src.getId() + "]");
            info.put("Java Version", System.getProperty("java.version"));
            info.put("OS", System.getProperty("os.name"));
            info.put("OS Version", System.getProperty("os.version"));
            info.put("OS Arch", System.getProperty("os.arch"));

            int w1 = info.keySet().stream().mapToInt(String::length).max().orElse(0) + 1; // +1 for colon
            int w2 = properties.keySet().stream().mapToInt(String::length).max().orElse(0) + 1;
            int w = Math.max(w1, w2);

            box.add("Game information");
            properties.forEach((k, v) -> box.add("  - %-" + w + "s %s", k, v));

            box.add("System information");
            info.forEach((k, v) -> box.add("  - %-" + w + "s %s", k, v));
        }
    }

    private static class FullTraceSection implements Section {
        private final Throwable thr;

        private FullTraceSection(Throwable thr) {
            this.thr = thr;
        }

        @Override
        public void add(Box box) {
            box.add("Full stack trace");
            box.blank();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            thr.printStackTrace(pw);
            pw.flush();
            box.add(sw.toString());
        }
    }

    private static class ExceptionDetailsSection implements Section {
        private final Throwable thr;
        private final ErrorContext first;

        private ExceptionDetailsSection(Throwable thr, ErrorContext first) {
            this.thr = thr;
            this.first = first;
        }

        @Override
        public void add(Box box) {
            box.add(thr.getClass().getName());
            if (thr.getLocalizedMessage() != null)
                box.add(thr.getLocalizedMessage());

            if (first != null) {
                box.blank();
                box.add(first.name);
            }
        }
    }

    private static class HeadSection implements Section {
        private final Throwable thr;
        private final StackTraceElement[] trace;
        private final int tail;

        private HeadSection(Throwable thr, StackTraceElement[] trace, int tail) {
            this.thr = thr;
            this.trace = trace;
            this.tail = tail;
        }

        @Override
        public void add(Box box) {
            box.add("Head of stack trace");
            box.blank();
            box.add(thr.getClass().getName());

            for (int i = 0; i < tail; i++) {
                box.add("    at " + trace[i].toString());
            }
            if (tail != trace.length)
                box.add("    ...");
        }
    }

    private static class ContextSection implements Section {
        private final ErrorContext ctx;
        private final StackTraceElement[] trace;
        private int head;
        private int tail;

        private ContextSection(ErrorContext ctx, StackTraceElement[] trace) {
            this.ctx = ctx;
            this.trace = trace;
        }

        @Override
        public void add(Box box) {
            box.add(ctx.name);
            int w = ctx.properties.keySet().stream().mapToInt(String::length).max().orElse(0) + 1; // +1 for colon
            ctx.properties.forEach((k, v) -> box.add("  - %-" + w + "s %s", k, v));

            box.blank();

            if (head != tail) {
                box.add("    ...");
                for (int i = head; i < tail; i++) {
                    box.add("    at " + trace[i].toString());
                }
                if (tail != trace.length)
                    box.add("    ...");
            }
        }
    }

    private enum BoxElements {
        LINE,
        THICK_LINE,
        BLANK
    }

    static class Box {
        private final List<Object> contents = new ArrayList<>();

        void add(String element) {
            element.lines().forEachOrdered(s -> contents.add(s.replace("\t", "    ")));
        }

        void add(String element, Object... format) {
            add(element.formatted(format));
        }

        void add(Box box) {
            contents.add(box);
        }

        void blank() {
            contents.add(BoxElements.BLANK);
        }

        void line() {
            contents.add(BoxElements.LINE);
        }

        void thickLine() {
            contents.add(BoxElements.THICK_LINE);
        }

        int width() {
            int width = 0;

            for (Object o : contents) {
                if (o instanceof String s) {
                    width = Math.max(width, s.length());
                }
            }

            return width;
        }

        void output(Appendable out, String pre, String post, int minw, String eol) throws IOException {
            int width = minw;

            for (Object o : contents) {
                if (o instanceof String s) {
                    width = Math.max(width, s.length());
                }

                if (o instanceof Box b) {
                    width = Math.max(width, b.width() + 4);
                }
            }

            out.append(pre).append("=".repeat(width + 4)).append(post).append(eol);

            for (Object o : contents) {
                if (o instanceof String s) {
                    out.append(pre)
                       .append("| ")
                       .append(s)
                       .append(" ".repeat(width - s.length()))
                       .append(" |")
                       .append(post)
                       .append(eol);
                } else if (o == BoxElements.LINE) {
                    out.append(pre).append("|").append("-".repeat(width + 2)).append("|").append(post).append(eol);
                } else if (o == BoxElements.THICK_LINE) {
                    out.append(pre).append("=".repeat(width + 4)).append(post).append(eol);
                } else if (o == BoxElements.BLANK) {
                    out.append(pre).append("|").append(" ".repeat(width + 2)).append("|").append(post).append(eol);
                } else if (o instanceof Box b) {
                    b.output(out, pre + "| ", " |" + post, width - 4, eol);
                }
            }

            out.append(pre).append("=".repeat(width + 4)).append(post).append(eol);
        }
    }
}
