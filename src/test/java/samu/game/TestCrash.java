package samu.game;

import java.io.IOException;
import java.time.Instant;

public class TestCrash {
    public static void method1() {
        throw new RuntimeException("WAAAAAAAAA");
    }

    public static void method2() {
        method1();
    }

    public static void method3() {
        method2();
    }

    public static void method4() {
        try {
            method3();
        } catch (Throwable thr) {
            throw ErrorReport.of(thr).addContext(
                ErrorContext.context("Method 4")
                            .prop("time", Instant.now())
            );
        }
    }

    public static void method5() {
        method4();
    }

    public static void method6() {
        try {
            method5();
        } catch (Throwable thr) {
            throw ErrorReport.of(thr).addContext(
                ErrorContext.context("Method 6")
                            .prop("time", Instant.now())
            );
        }
    }

    public static void method7() {
        method6();
    }

    public static void method8() {
        method7();
    }

    public static void method9() {
        method8();
    }

    public static void main(String[] args) throws IOException {
        try {
            method9();
        } catch (ErrorReport err) {
            err.write(System.out, "Crash report");
        }
    }
}
