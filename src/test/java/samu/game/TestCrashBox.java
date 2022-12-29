package samu.game;

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
