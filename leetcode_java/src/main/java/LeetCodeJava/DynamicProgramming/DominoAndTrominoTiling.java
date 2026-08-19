package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/domino-and-tromino-tiling/

/**
 *  790. Domino and Tromino Tiling
 *  Medium
 *
 *  You have two types of tiles: a 2 x 1 domino shape and a tromino shape.
 *  You may rotate these shapes.
 *
 *  Given an integer n, return the number of ways to tile an 2 x n board.
 *  Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *  In a tiling, every square must be covered by a tile. Two tilings are
 *  different if and only if there are two 4-directionally adjacent cells on
 *  the board such that exactly one of the tilings has both squares occupied
 *  by a tile.
 *
 *  Example 1:
 *    Input: n = 3
 *    Output: 5
 *    Explanation: The five different ways are shown above.
 *
 *  Example 2:
 *    Input: n = 1
 *    Output: 1
 *
 *  Constraints:
 *    - 1 <= n <= 1000
 */
public class DominoAndTrominoTiling {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: closed recurrence  f(n) = 2 * f(n-1) + f(n-3)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int numTilings(int n) {
        if (n <= 2) {
            return n;
        }
        long[] f = new long[n + 1];
        f[0] = 1;
        f[1] = 1;
        f[2] = 2;
        for (int i = 3; i <= n; i++) {
            f[i] = (2 * f[i - 1] + f[i - 3]) % MOD;
        }
        return (int) f[n];
    }

    // V1
    // IDEA: explicit state DP - full[i] = fully covered up to col i, partial[i] = one cell sticking out
    /**
     * time = O(N)
     * space = O(N)
     */
    public int numTilings_1(int n) {
        if (n <= 2) {
            return n;
        }
        // full[i]    : columns 1..i completely covered
        // partial[i] : columns 1..i covered except ONE cell in column i
        //              (only one of the 2 symmetric shapes is counted here)
        long[] full = new long[n + 1];
        long[] partial = new long[n + 1];
        full[1] = 1;
        full[2] = 2;
        partial[1] = 0;
        partial[2] = 1;

        for (int i = 3; i <= n; i++) {
            full[i] = (full[i - 1] + full[i - 2] + 2 * partial[i - 1]) % MOD;
            partial[i] = (full[i - 2] + partial[i - 1]) % MOD;
        }
        return (int) full[n];
    }
}
