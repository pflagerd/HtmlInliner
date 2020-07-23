package com.pflager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

public class HtmlInliner {

    private static String usage =
            "HtmlInliner <file> ...\n" +
            "  e.g. HtmlInliner somefile.html\n" +
            "    which will produce somefile.inlined.html as output, if successful."
            ;

    private static int inlineAFile(String fileName) throws IOException {
        Document doc = Jsoup.parse(new File(fileName), "UTF-8");

        System.out.println(doc.outerHtml()); // DPP: 200723013715Z: For testing only.
        return 1;
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("You must specify at least one filename");
            System.out.println(usage);
            System.exit(1);
        }

        int returnValue = 0;

        for (String arg : args) {
            if (!arg.startsWith("-")) { // it's probably a file
                returnValue += inlineAFile(arg);
            } else { // it's probably an option
            }
        }

        System.exit(returnValue);
    }
}
