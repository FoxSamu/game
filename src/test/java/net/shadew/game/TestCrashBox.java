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
import java.io.PrintWriter;
import java.io.StringWriter;

public class TestCrashBox {
    public static void main(String[] args) throws IOException {
        ErrorReport.Box box = new ErrorReport.Box();

        box.add("Hello world, this is a little box");
        box.thickLine();
        box.add("Imagine a crash report being here");
        box.blank();

        StringWriter writer = new StringWriter();
        new Exception().printStackTrace(new PrintWriter(writer));
        box.add(writer.toString());
        box.line();
        box.add("Some footnotes\n    Blah blah blah...");

        ErrorReport.Box subbox = new ErrorReport.Box();
        subbox.add("Hi");
        subbox.line();
        subbox.add("This is an internal box");
        box.add(subbox);

        box.output(System.out, "", "", 0, System.lineSeparator());
    }
}
