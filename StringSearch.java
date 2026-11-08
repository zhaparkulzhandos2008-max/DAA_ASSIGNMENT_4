package com.example.daa;

import java.util.ArrayList;
import java.util.List;

public class StringSearch {

    public static long counter = 0;

    public static void resetCounter() {
        counter = 0;
    }

    /**
     * Naive String Search algorithm finding all starting indices (including overlapping).
     */
    public static List<Integer> searchNaive(String text, String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern cannot be empty.");
        }
        List<Integer> matches = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        if (m > n) {
            return matches;
        }

        for (int i = 0; i <= n - m; i++) {
            int j = 0;
            while (j < m) {
                counter++;
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
                j++;
            }
            if (j == m) {
                matches.add(i);
            }
        }
        return matches;
    }

    /**
     * Computes the Longest Prefix Suffix (LPS) array for KMP algorithm.
     */
    public static int[] lps(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern cannot be empty.");
        }
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;
        lps[0] = 0;

        while (i < m) {
            counter++;
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    /**
     * KMP String Search algorithm finding all starting indices (including overlapping).
     */
    public static List<Integer> searchKMP(String text, String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern cannot be empty.");
        }
        List<Integer> matches = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        if (m > n) {
            return matches;
        }

        int[] lpsArray = lps(pattern);
        int i = 0; // index for text
        int j = 0; // index for pattern

        while (i < n) {
            counter++;
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }
            if (j == m) {
                matches.add(i - j);
                j = lpsArray[j - 1];
            } else if (i < n && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j = lpsArray[j - 1];
                } else {
                    i++;
                }
            }
        }
        return matches;
    }
}
