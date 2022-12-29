package net.shadew.game;

import java.util.LinkedHashMap;
import java.util.Map;

public class ErrorContext {
    final String name;
    final Map<String, String> properties = new LinkedHashMap<>();
    final StackTraceElement[] trace;

    ErrorContext(String name, StackTraceElement[] trace) {
        this.name = name;
        this.trace = trace;
    }

    public ErrorContext prop(String k, String v) {
        properties.put(k, v);
        return this;
    }

    public ErrorContext prop(String k, Object v) {
        if (v instanceof Reportable r) {
            return prop(k, r.report());
        } else {
            return prop(k, v + "");
        }
    }

    public ErrorContext prop(String k, byte v) {
        return prop(k, "" + v);
    }

    public ErrorContext prop(String k, short v) {
        return prop(k, "" + v);
    }

    public ErrorContext prop(String k, int v) {
        return prop(k, "" + v);
    }

    public ErrorContext prop(String k, long v) {
        return prop(k, "" + v);
    }

    public ErrorContext prop(String k, float v) {
        return prop(k, "" + v);
    }

    public ErrorContext prop(String k, double v) {
        return prop(k, "" + v);
    }

    public ErrorContext prop(String k, char v) {
        return prop(k, "" + v);
    }

    public ErrorContext prop(String k, boolean v) {
        return prop(k, "" + v);
    }

    public ErrorContext prop(String k, boolean v, String yes, String no) {
        return prop(k, v ? yes : no);
    }

    public static ErrorContext context(String name) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        StackTraceElement[] actualTrace = new StackTraceElement[trace.length - 1];
        System.arraycopy(trace, 1, actualTrace, 0, actualTrace.length);

        return new ErrorContext(name, actualTrace);
    }

    public static ErrorContext in(String type, NSID id) {
        return context("in " + type + " " + id);
    }

    public static ErrorContext in(String type, NSID id, LifecyclePhase during) {
        return context("during " + during + " in " + type + " " + id);
    }

    public static ErrorContext in(Module<?> module) {
        return in("module", module.id());
    }

    public static ErrorContext in(Module<?> module, LifecyclePhase during) {
        return in("module", module.id(), during);
    }

    public static ErrorContext in(Service<?> service) {
        return in("service", service.id());
    }

    public static ErrorContext in(Service<?> service, LifecyclePhase during) {
        return in("service", service.id(), during);
    }
}
