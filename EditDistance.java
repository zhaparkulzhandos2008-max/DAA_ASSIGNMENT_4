package com.example.daa;

/**
 * Interface and implementations for Dynamic Programming Edit Distance.
 */
public class EditDistance {

    // Counter for work done in Edit Distance algorithms
    public static long counter = 0;

    public static void resetCounter() {
        counter = 0;
    }

    /**
     * Recursive Edit Distance: Direct recursion on the last characters, no memory.
     * State: dp(i, j) - min edit distance between prefix a[0..i-1] and b[0..j-1].
     * Transition: if a[i-1] == b[j-1] -> dp(i-1, j-1); else 1 + min(dp(i-1, j), dp(i, j-1), dp(i-1, j-1)).
     * Base Case: dp(0, j) = j (j insertions), dp(i, 0) = i (i deletions).
     */
    public static int distanceRecursive(String a, String b) {
        return distanceRecursiveHelper(a, b, a.length(), b.length());
    }

    private static int distanceRecursiveHelper(String a, String b, int i, int j) {
        counter++;
        if (i == 0) return j;
        if (j == 0) return i;

        if (a.charAt(i - 1) == b.charAt(j - 1)) {
            return distanceRecursiveHelper(a, b, i - 1, j - 1);
        } else {
            int insert = distanceRecursiveHelper(a, b, i, j - 1);
            int delete = distanceRecursiveHelper(a, b, i - 1, j);
            int replace = distanceRecursiveHelper(a, b, i - 1, j - 1);
            return 1 + Math.min(insert, Math.min(delete, replace));
        }
    }

    /**
     * Table Edit Distance: Bottom-up DP table of size (n+1) x (m+1).
     * State: dp[i][j] stores the edit distance between prefix a[0..i-1] and b[0..j-1].
     * Transition: dp[i][j] = dp[i-1][j-1] if equal, else 1 + min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]).
     * Base Case: dp[i][0] = i for all 0<=i<=n, dp[0][j] = j for all 0<=j<=m.
     */
    public static int distanceTable(String a, String b) {
        int n = a.length();
        int m = b.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            counter++;
            dp[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            counter++;
            dp[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                counter++;
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j], Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
                }
            }
        }
        return dp[n][m];
    }

    /**
     * Two Rows Edit Distance: Space-optimized DP keeping only previous and current rows.
     * State: prev[j] and curr[j] store edit distances for prefix of a up to current row index.
     * Transition: curr[j] = prev[j-1] if match, else 1 + min(prev[j], curr[j-1], prev[j-1]).
     * Base Case: prev[j] = j for 0<=j<=m; curr[0] = i at start of row i.
     */
    public static int distanceTwoRows(String a, String b) {
        int n = a.length();
        int m = b.length();

        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        for (int j = 0; j <= m; j++) {
            counter++;
            prev[j] = j;
        }

        for (int i = 1; i <= n; i++) {
            counter++;
            curr[0] = i;
            for (int j = 1; j <= m; j++) {
                counter++;
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    curr[j] = 1 + Math.min(prev[j], Math.min(curr[j - 1], prev[j - 1]));
                }
            }
            System.arraycopy(curr, 0, prev, 0, m + 1);
        }
        return prev[m];
    }
}
