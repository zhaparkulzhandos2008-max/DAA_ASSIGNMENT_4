package com.example.daa;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class MainExperiment {

    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateRandomString(int length, Random rand) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(rand.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    public static String generateRepeatedChar(char c, int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String csvPath = "results/results.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvPath))) {
            writer.println("algorithm,input_type,n,time_ms,counter");

            Random rand = new Random(42); // fixed seed for reproducibility

            // 1. Edit Distance Experiments
            int[] editSizesSmall = {4, 8, 12, 16};
            int[] editSizesLarge = {100, 500, 1000, 2000};

            // Recursive on Small Sizes
            for (int n : editSizesSmall) {
                String s1 = generateRandomString(n, rand);
                String s2 = generateRandomString(n, rand);
                
                long totalTime = 0;
                long lastCounter = 0;
                for (int run = 0; run < 5; run++) {
                    EditDistance.resetCounter();
                    long start = System.nanoTime();
                    EditDistance.distanceRecursive(s1, s2);
                    long end = System.nanoTime();
                    totalTime += (end - start);
                    lastCounter = EditDistance.counter;
                }
                double avgMs = (totalTime / 5.0) / 1e6;
                writer.printf("EditDistance_Recursive,Random,%d,%.4f,%d%n", n, avgMs, lastCounter);
            }

            // Table & TwoRows on Large Sizes
            String[] editInputTypes = {"Random", "WorstCase_Different"};
            for (String type : editInputTypes) {
                for (int n : editSizesLarge) {
                    String s1, s2;
                    if (type.equals("Random")) {
                        s1 = generateRandomString(n, rand);
                        s2 = generateRandomString(n, rand);
                    } else {
                        s1 = generateRepeatedChar('A', n);
                        s2 = generateRepeatedChar('B', n);
                    }

                    // Table
                    long totalTime = 0;
                    long lastCounter = 0;
                    for (int run = 0; run < 5; run++) {
                        EditDistance.resetCounter();
                        long start = System.nanoTime();
                        EditDistance.distanceTable(s1, s2);
                        long end = System.nanoTime();
                        totalTime += (end - start);
                        lastCounter = EditDistance.counter;
                    }
                    double avgMsTable = (totalTime / 5.0) / 1e6;
                    writer.printf("EditDistance_Table,%s,%d,%.4f,%d%n", type, n, avgMsTable, lastCounter);

                    // TwoRows
                    totalTime = 0;
                    for (int run = 0; run < 5; run++) {
                        EditDistance.resetCounter();
                        long start = System.nanoTime();
                        EditDistance.distanceTwoRows(s1, s2);
                        long end = System.nanoTime();
                        totalTime += (end - start);
                        lastCounter = EditDistance.counter;
                    }
                    double avgMsTwoRows = (totalTime / 5.0) / 1e6;
                    writer.printf("EditDistance_TwoRows,%s,%d,%.4f,%d%n", type, n, avgMsTwoRows, lastCounter);
                }
            }

            // 2. String Search Experiments
            int[] searchSizes = {1000, 5000, 20000, 100000};
            String[] searchInputTypes = {"Random", "WorstCase_Repetitive"};

            for (String type : searchInputTypes) {
                for (int n : searchSizes) {
                    String text, pattern;
                    int m = Math.max(10, n / 100);
                    if (type.equals("Random")) {
                        text = generateRandomString(n, rand);
                        pattern = generateRandomString(m, rand);
                    } else {
                        text = generateRepeatedChar('A', n);
                        pattern = generateRepeatedChar('A', m - 1) + "B";
                    }

                    // Naive
                    long totalTime = 0;
                    long lastCounter = 0;
                    for (int run = 0; run < 5; run++) {
                        StringSearch.resetCounter();
                        long start = System.nanoTime();
                        StringSearch.searchNaive(text, pattern);
                        long end = System.nanoTime();
                        totalTime += (end - start);
                        lastCounter = StringSearch.counter;
                    }
                    double avgMsNaive = (totalTime / 5.0) / 1e6;
                    writer.printf("StringSearch_Naive,%s,%d,%.4f,%d%n", type, n, avgMsNaive, lastCounter);

                    // KMP
                    totalTime = 0;
                    for (int run = 0; run < 5; run++) {
                        StringSearch.resetCounter();
                        long start = System.nanoTime();
                        StringSearch.searchKMP(text, pattern);
                        long end = System.nanoTime();
                        totalTime += (end - start);
                        lastCounter = StringSearch.counter;
                    }
                    double avgMsKMP = (totalTime / 5.0) / 1e6;
                    writer.printf("StringSearch_KMP,%s,%d,%.4f,%d%n", type, n, avgMsKMP, lastCounter);
                }
            }

            System.out.println("Experiment complete. Results saved to " + csvPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
