package com.pflager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
        FileUtils.writeStringToFile(new File(outputFileName), Parser.unescapeEntities(doc.outerHtml(), false), "utf-8");
        return 0;
    }

    private static int inlineAUrl(String inputUriString) throws URISyntaxException, IOException {
        Document doc = Jsoup.connect(inputUriString).get();
        doc.outputSettings().prettyPrint(false);
        URI inputUri = new URI(inputUriString);

        for (Element element : doc.select("script")) {
            String src = element.attr("src");
            if (src != null) {
                URI srcUri = inputUri.resolve(src);
                String srcUriContents = IOUtils.toString(srcUri, "utf-8");
                // delete the src attribute
                element.removeAttr("src");
                // insert srcUrlContents as the contents of the script tag
                element.html(srcUriContents);
            }
        }

        // DPP: 200727024255Z: TBD
        //        int index = inputURL.lastIndexOf('.');
//        String outputFileName = inputURL.substring(0, index) + ".inlined" + inputURL.substring(index);
//        FileUtils.writeStringToFile(new File(outputFileName), Parser.unescapeEntities(doc.outerHtml(), false), "utf-8");
        return 0;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        if (args.length == 0) {
            System.out.println("You must specify at least one filename");
            System.out.println(usage);
            System.exit(1);
        }

        int returnValue = 0;

        for (String arg : args) {
            if (!arg.startsWith("-")) { // it's probably a file or url
                if (arg.startsWith("http")) {
                    returnValue += inlineAUrl(arg);
                } else {
                    returnValue += inlineAFile(arg);
                }
            } else { // it's probably an option
            }
        }

        System.exit(returnValue);
    }
}
