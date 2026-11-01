package LeetCodeJava.Math;

// https://leetcode.com/problems/number-of-ways-to-reach-a-position-after-exactly-k-steps/description/

/**
 * 2400. Number of Ways to Reach a Position After Exactly k Steps
 * Medium
 *
 * You are given two positive integers startPos and endPos. Initially, you are standing at position startPos on an infinite number line. With one step, you can move either one position to the left, or one position to the right.
 *
 * Given a positive integer k, return the number of different ways to reach the position endPos starting from startPos, such that you perform exactly k steps. Since the answer may be very large, return it modulo 10^9 + 7.
 *
 * Two ways are considered different if the order of the steps made is not exactly the same.
 *
 * Note that the number line includes negative integers.
 *
 *
 * Example 1:
 *
 * Input: startPos = 1, endPos = 2, k = 3
 * Output: 3
 * Explanation: We can reach position 2 from 1 in exactly 3 steps in three ways:
 * - 1 -> 2 -> 3 -> 2.
 * - 1 -> 2 -> 1 -> 2.
 * - 1 -> 0 -> 1 -> 2.
 * It can be proven that no other way is possible, so we return 3.
 *
 * Example 2:
 *
 * Input: startPos = 2, endPos = 5, k = 10
 * Output: 0
 * Explanation: It is impossible to reach position 5 from position 2 in exactly 10 steps.
 *
 *
 * Constraints:
 *
 * 1 <= startPos, endPos, k <= 1000
 *
 */
public class NumberOfWaysToReachAPositionAfterExactlyKSteps {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: COMBINATORICS - CHOOSE WHICH STEPS GO RIGHT
    //   with d = |endPos - startPos|: right + left = k, right - left = d -> right = (k + d) / 2.
    //   possible only when d <= k and k - d is even; the answer is C(k, right),
    //   built with Pascal's triangle so it stays exact under the modulus.
    /**
     * time = O(k^2)
     * space = O(k)
     */
    public int numberOfWays(int startPos, int endPos, int k) {
        int distance = Math.abs(endPos - startPos);
        // too far, or the parity of k cannot match the displacement
        if (distance > k || (k - distance) % 2 != 0) {
            return 0;
        }
        int rightSteps = (k + distance) / 2;

        // row[j] = C(i, j) after processing row i; update right to left to reuse one array
        long[] row = new long[k + 1];
        row[0] = 1;
        for (int i = 1; i <= k; i++) {
            for (int j = i; j >= 1; j--) {
                row[j] = (row[j] + row[j - 1]) % MOD;
            }
        }
        return (int) row[rightSteps];
    }

    // V1
    // IDEA: DP OVER STEPS - ways[pos] AFTER EACH STEP, POSITIONS SHIFTED BY k TO STAY NON-NEGATIVE
    //   ways_next[pos] = ways[pos - 1] + ways[pos + 1]
    /**
     * time = O(k^2)
     * space = O(k)
     */
    public int numberOfWays_1(int startPos, int endPos, int k) {
        int distance = Math.abs(endPos - startPos);
        if (distance > k) {
            return 0;
        }

        // only the displacement matters: start at offset 0, stored at index k
        int size = 2 * k + 1;
        long[] ways = new long[size];
        ways[k] = 1;
        for (int step = 0; step < k; step++) {
            long[] next = new long[size];
            for (int pos = 0; pos < size; pos++) {
                if (ways[pos] == 0) {
                    continue;
                }
                // one step left or one step right
                if (pos - 1 >= 0) {
                    next[pos - 1] = (next[pos - 1] + ways[pos]) % MOD;
                }
                if (pos + 1 < size) {
                    next[pos + 1] = (next[pos + 1] + ways[pos]) % MOD;
                }
            }
            ways = next;
        }
        return (int) ways[k + distance];
    }
}
