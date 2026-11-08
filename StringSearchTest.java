package com.example.daa;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class StringSearchTest {

    @Test
    public void testLPSReferenceValues() {
        assertArrayEquals(new int[]{0, 1, 2, 0, 1, 2, 3, 3}, StringSearch.lps("AAACAAAA"));
        assertArrayEquals(new int[]{0, 0, 1, 2, 0, 1, 2, 3, 4}, StringSearch.lps("ABABCABAB"));
    }

    @Test
    public void testSearchReferenceValues() {
        assertEquals(List.of(10), StringSearch.searchNaive("ABABDABACDABABCABAB", "ABABCABAB"));
        assertEquals(List.of(10), StringSearch.searchKMP("ABABDABACDABABCABAB", "ABABCABAB"));

        assertEquals(Arrays.asList(0, 1, 2, 3), StringSearch.searchNaive("AAAAA", "AA"));
        assertEquals(Arrays.asList(0, 1, 2, 3), StringSearch.searchKMP("AAAAA", "AA"));

        assertEquals(Arrays.asList(0, 2, 4), StringSearch.searchNaive("ABABABA", "ABA"));
        assertEquals(Arrays.asList(0, 2, 4), StringSearch.searchKMP("ABABABA", "ABA"));
    }

    @Test
    public void testEdgeCases() {
        assertThrows(IllegalArgumentException.class, () -> StringSearch.searchNaive("hello", ""));
        assertThrows(IllegalArgumentException.class, () -> StringSearch.searchKMP("hello", ""));
        assertThrows(IllegalArgumentException.class, () -> StringSearch.lps(""));

        assertTrue(StringSearch.searchNaive("short", "longer_pattern").isEmpty());
        assertTrue(StringSearch.searchKMP("short", "longer_pattern").isEmpty());
    }

    @Test
    public void testRandomTextsEquivalence() {
        Random rand = new Random(54321);
        for (int i = 0; i < 100; i++) {
            String text = MainExperiment.generateRandomString(50, rand);
            String pattern = MainExperiment.generateRandomString(3, rand);

            List<Integer> naiveRes = StringSearch.searchNaive(text, pattern);
            List<Integer> kmpRes = StringSearch.searchKMP(text, pattern);

            assertEquals(naiveRes, kmpRes, "Naive and KMP mismatch for text: " + text + " pattern: " + pattern);
        }
    }
}
