package com.hp.readfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * MRProcessor. Reads lines of data and counts matches. Based on MapReduce model.
 */
public class MRProcessor {
    public static final int SCALE = 2;
    private AtomicInteger counter = new AtomicInteger(0);

    // Entry point - Procesor file_to_process
    public static void main(String[] args) {
        MRProcessor processor = new MRProcessor();
        try {
            long start = System.currentTimeMillis();
            int result = processor.process(args[0]);
            long time = System.currentTimeMillis() - start;

            System.out.println("[MRProcessor]Found " + result + " ocurrences in " + time + " milisecs");

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
        int maxThreads = getMaxThreads();
        System.out.println("Process file " + file + " with " + maxThreads + " threads");

        Path path = FileSystems.getDefault().getPath(file);
        BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.ISO_8859_1);

        ExecutorService executor = Executors.newFixedThreadPool(maxThreads);

        // Start a new task per line
        String line;
        while ((line = reader.readLine()) != null) {
            final String buf = line;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    counter.addAndGet(new Task().perform(buf));
                }
            });
        }

        // Wait for all threads to complete
        shutdownAndAwaitTermination(executor);

        return counter.get();
    }

    private void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

    private int getMaxThreads() {
        int cpus = Runtime.getRuntime().availableProcessors();
        int maxThreads = cpus * SCALE;
        return (maxThreads > 0 ? maxThreads : 1);
    }

    static public class Task {
        /**
         * Given a line, looks for matches in facility names (token number 10) and state (token number 14).
         *
         * @param line line of data
         * @return 1 if matches (facility names starts with R or ends with S, and state is ID), otherwise returns 0
         * @throws java.io.IOException
         */
        public int perform(final String line) {
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
