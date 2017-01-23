package com.haier.adp.common.boot;

import org.springframework.boot.Banner;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

/**
 * Desc: haier banner
 * Date: 16/2/15 21:38
 * Author: Grancy
 * Mail: guork@terminus.io
 */
public class HaierBanner implements Banner {

    private static final String[] BANNER = { "",
        "                                                                \n" +
        "   ██    ██      ██      ██  ██ █████  ██ ████                  \n" +
        "   ██    ██    ██  ██    ██  ██        ██    ██                 \n" +
        "   ██ ██ ██   ██    ██   ██  ██ █████  ████ ██                  \n" +
        "   ██    ██  ██ ████ ██  ██  ██        ██  ██                   \n" +
        "   ██    ██ ██        ██ ██  ████████  ██    ██                 \n" +
        "                                                                \n"
    };

    private static final String SPRING_BOOT = " :: Powered by Terminus.inc :: ";

    private static final int STRAP_LINE_SIZE = 42;

    /**
     * Print the banner to the specified print stream.
     *
     * @param environment the spring environment
     * @param sourceClass the source class for the application
     * @param printStream the output print stream
     */
    @Override
    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream printStream) {
        for (String line : BANNER) {
            printStream.println(line);
        }
//        String version = "1.0.0";
//        version = version == null ? "" : " (v" + version + ")";
        String version = "(v1.0.0)";
        String padding = "";
        while (padding.length() < STRAP_LINE_SIZE
                - (version.length() + SPRING_BOOT.length())) {
            padding += " ";
        }

        printStream.println(AnsiOutput.toString(AnsiColor.GREEN, SPRING_BOOT, AnsiColor.DEFAULT, padding, AnsiStyle.FAINT, version));
        printStream.println();
    }
}
