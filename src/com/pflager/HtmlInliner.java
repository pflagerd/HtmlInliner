package com.pflager;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;

public class HtmlInliner {

    private static String usage =
            "HtmlInliner <file> ...\n" +
            "  e.g. HtmlInliner somefile.html\n" +
            "    which will produce somefile.inlined.html as output, if successful."
            ;

    private static int inlineAFile(String inputFileName) throws IOException {
        File file = new File(inputFileName);
        File parentDirectory = file.getParentFile();
        Document doc = Jsoup.parse(file, "UTF-8");
        doc.outputSettings().prettyPrint(false);

        for (Element element : doc.select("script")) {
            String src = element.attr("src");
            if (src != null) {
                String srcFileName = parentDirectory.getCanonicalPath() + "/" + src;
                String srcFileContents = FileUtils.readFileToString(new File(srcFileName), "utf-8");
                // delete the src attribute
                element.removeAttr("src");
                // insert srcFileContents as the contents of the script tag
                element.html(srcFileContents);
            }
        }

        int index = inputFileName.lastIndexOf('.');
        String outputFileName = inputFileName.substring(0, index) + ".inlined" + inputFileName.substring(index);
        FileUtils.writeStringToFile(new File(outputFileName), doc.outerHtml(), "utf-8");
        return 0;
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
