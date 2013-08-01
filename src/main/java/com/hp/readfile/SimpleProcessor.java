package com.hp.readfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringTokenizer;


/**
 * SimpleProcessor. Processes lines of data and counts matches.
 */
public class SimpleProcessor {

    private int counter = 0;

    // Entry point - Processor file_to_process
    public static void main(String[] args) {
        SimpleProcessor processor = new SimpleProcessor();
        try {
            long start = System.currentTimeMillis();
            int result = processor.process(args[0]);
            long time = System.currentTimeMillis() - start;

            System.out.println("[Simple Processor]Found " + result + " ocurrences in " + time + " milisecs");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Process a file. Reads lines and counts matches.
     *
     * @param file path to the file
     * @return number of matches
     * @throws java.io.IOException
     */
    public int process(String file) throws IOException {

        Path path = FileSystems.getDefault().getPath(file);
        BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.ISO_8859_1);

        // Start a new task per line
        String line;
        while ((line = reader.readLine()) != null) {
            counter = counter + (new Task().perform(line));
        }

        return counter;
    }


    static public class Task {

        /**
         * Given a line, looks for matches in facility names (token number 10) and state (token number 14).
         *
         * @param line line of data
         * @return 1 if matches (facility names starts with R or ends with S, and state is ID), otherwise returns 0
         * @throws java.io.IOException
         */
        public int perform(String line) {
            if (line == null || line.isEmpty()) return 0;

            StringTokenizer tokenizer = new StringTokenizer(line, "\t");
            int tokenCounter = 0;
            String facility = null;
            String state = null;
            while ((tokenizer.hasMoreTokens() && tokenCounter < 14)) {
                if (tokenCounter == 9) {
                    facility = tokenizer.nextToken();
                } else if (tokenCounter == 13) {
                    state = tokenizer.nextToken();
                } else {
                    tokenizer.nextToken();
                }
                tokenCounter++;
            }

            if (facility == null || state == null) {
                //error reading line
                return 0;
            } else if ((facility.startsWith("R") || facility.endsWith("S")) && state.equalsIgnoreCase("ID")) {
                //found
                System.out.println("Found facility: " + facility + " in state " + state);
                return 1;
            } else {
                // not found
                return 0;
            }
        }
    }
}
