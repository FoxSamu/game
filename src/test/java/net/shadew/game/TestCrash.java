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
