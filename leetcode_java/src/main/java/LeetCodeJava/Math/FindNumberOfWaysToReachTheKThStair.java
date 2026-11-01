package LeetCodeJava.Math;

// https://leetcode.com/problems/find-number-of-ways-to-reach-the-k-th-stair/description/

/**
 * 3154. Find Number of Ways to Reach the K-th Stair
 * Hard
 *
 * You are given a non-negative integer k. There exists a staircase with an infinite number of stairs,
 * with the lowest stair numbered 0.
 *
 * Alice has an integer jump, with an initial value of 0. She starts on stair 1 and wants to reach
 * stair k using any number of operations. If she is on stair i, in one operation she can:
 *
 * Go down to stair i - 1. This operation cannot be used consecutively or on stair 0.
 * Go up to stair i + 2^jump. And then, jump becomes jump + 1.
 *
 * Return the total number of ways Alice can reach stair k.
 *
 * Note that it is possible that Alice reaches the stair k, and performs some operations to reach the
 * stair k again.
 *
 *
 * Example 1:
 *
 * Input: k = 0
 * Output: 2
 *
 * Example 2:
 *
 * Input: k = 1
 * Output: 4
 *
 *
 * Constraints:
 *
 * 0 <= k <= 10^9
 *
 */
public class FindNumberOfWaysToReachTheKThStair {

    // V0
    // IDEA: COMBINATORICS — j UP-MOVES CLIMB EXACTLY 2^j - 1, SO d = 2^j - k DOWN-MOVES ARE NEEDED,
    //   AND NON-CONSECUTIVE DOWN-MOVES FILL d OF THE j + 1 GAPS AROUND THE UP-MOVES: C(j + 1, d)
    /**
     * time = O(log^2 K)
     * space = O(1)
     */
    public int waysToReachStair(int k) {
        long ways = 0;
        // 2^j outgrows k + j + 1 well before j = 62
        for (int j = 0; j < 62; j++) {
            // start at 1, climb 1 + 2 + ... + 2^(j-1) = 2^j - 1, so we stand on 2^j
            long downMoves = (1L << j) - k;
            if (downMoves < 0) {
                continue;
            }
            // only j + 1 gaps exist; from here on downMoves grows faster than the gaps
            if (downMoves > j + 1) {
                break;
            }
            ways += binomial(j + 1, (int) downMoves);
        }
        return (int) ways;
    }

    /** C(n, r) computed incrementally; each partial product is itself a binomial, so it divides exactly. */
    private long binomial(int n, int r) {
        if (r < 0 || r > n) {
            return 0;
        }
        long result = 1;
        for (int t = 0; t < r; t++) {
            result = result * (n - t) / (t + 1);
        }
        return result;
    }
}
