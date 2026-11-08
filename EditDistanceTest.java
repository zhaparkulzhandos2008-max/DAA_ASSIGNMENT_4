package com.example.daa;

import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class EditDistanceTest {

    @Test
    public void testReferenceValues() {
        assertEquals(3, EditDistance.distanceTable("kitten", "sitting"));
        assertEquals(2, EditDistance.distanceTable("flaw", "lawn"));
        assertEquals(5, EditDistance.distanceTable("intention", "execution"));
        assertEquals(3, EditDistance.distanceTable("", "abc"));
        assertEquals(0, EditDistance.distanceTable("abc", "abc"));

        // Check two rows matching table
        assertEquals(3, EditDistance.distanceTwoRows("kitten", "sitting"));
        assertEquals(2, EditDistance.distanceTwoRows("flaw", "lawn"));
        assertEquals(5, EditDistance.distanceTwoRows("intention", "execution"));
        assertEquals(3, EditDistance.distanceTwoRows("", "abc"));
        assertEquals(0, EditDistance.distanceTwoRows("abc", "abc"));

        // Check recursive matching table
        assertEquals(3, EditDistance.distanceRecursive("kitten", "sitting"));
        assertEquals(2, EditDistance.distanceRecursive("flaw", "lawn"));
        assertEquals(5, EditDistance.distanceRecursive("intention", "execution"));
        assertEquals(3, EditDistance.distanceRecursive("", "abc"));
        assertEquals(0, EditDistance.distanceRecursive("abc", "abc"));
    }

    @Test
    public void testRandomStringsEquivalence() {
        Random rand = new Random(12345);
        for (int i = 0; i < 100; i++) {
            int len1 = rand.nextInt(9); // short strings for recursion
            int len2 = rand.nextInt(9);
            String s1 = MainExperiment.generateRandomString(len1, rand);
            String s2 = MainExperiment.generateRandomString(len2, rand);

            int resRec = EditDistance.distanceRecursive(s1, s2);
            int resTab = EditDistance.distanceTable(s1, s2);
            int resTwo = EditDistance.distanceTwoRows(s1, s2);

            assertEquals(resRec, resTab, "Recursive and Table mismatch for: " + s1 + ", " + s2);
            assertEquals(resTab, resTwo, "Table and TwoRows mismatch for: " + s1 + ", " + s2);
        }
    }
}
