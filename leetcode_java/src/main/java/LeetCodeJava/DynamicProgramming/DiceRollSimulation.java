package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/dice-roll-simulation/description/

/**
 * 1223. Dice Roll Simulation
 * Hard
 *
 * A die simulator generates a random number from 1 to 6 for each roll. You introduced a
 * constraint to the generator such that it cannot roll the number i more than rollMax[i]
 * (1-indexed) consecutive times.
 *
 * Given an array of integers rollMax and an integer n, return the number of distinct
 * sequences that can be obtained with exact n rolls. Since the answer may be too large,
 * return it modulo 10^9 + 7.
 *
 * Two sequences are considered different if at least one element differs from each other.
 *
 *
 * Example 1:
 *
 * Input: n = 2, rollMax = [1,1,2,2,2,3]
 * Output: 34
 * Explanation: There will be 2 rolls of die, if there are no constraints on the die, there
 * are 6 * 6 = 36 possible combinations. In this case, looking at rollMax array, the numbers
 * 1 and 2 appear at most once consecutively, therefore sequences (1,1) and (2,2) cannot
 * occur, so the final answer is 36-2 = 34.
 *
 * Example 2:
 *
 * Input: n = 2, rollMax = [1,1,1,1,1,1]
 * Output: 30
 *
 * Example 3:
 *
 * Input: n = 3, rollMax = [1,1,1,2,2,3]
 * Output: 181
 *
 *
 * Constraints:
 *
 * 1 <= n <= 5000
 * rollMax.length == 6
 * 1 <= rollMax[i] <= 15
 *
 */
public class DiceRollSimulation {

    private static final int MOD = 1_000_000_007;
    private static final int FACES = 6;

    // V0
    // IDEA: 2D DP (LAST FACE x CURRENT RUN LENGTH)
    /**
     * dp[face][run] = number of sequences so far that END with `face` repeated exactly
     *                 run + 1 consecutive times (0 <= run < rollMax[face])
     *
     * rolling one more die:
     *   next[face][0]   = (all sequences) - (sequences ending in face)   // previous roll differed
     *   next[face][run] = dp[face][run - 1]                              // extend the run
     *
     * base: after one roll, dp[face][0] = 1 for every face
     * answer: sum of every dp[face][run] after n rolls
     */
    /**
     * time = O(N * 6 * M), M = max(rollMax)
     * space = O(6 * M)
     */
    public int dieSimulator(int n, int[] rollMax) {
        long[][] dp = new long[FACES][];
        for (int face = 0; face < FACES; face++) {
            dp[face] = new long[rollMax[face]];
            dp[face][0] = 1;
        }

        for (int roll = 2; roll <= n; roll++) {
            // count of sequences per ending face, and over all faces
            long[] endingWith = new long[FACES];
            long total = 0;
            for (int face = 0; face < FACES; face++) {
                for (long count : dp[face]) {
                    endingWith[face] = (endingWith[face] + count) % MOD;
                }
                total = (total + endingWith[face]) % MOD;
            }

            long[][] next = new long[FACES][];
            for (int face = 0; face < FACES; face++) {
                next[face] = new long[rollMax[face]];
                // previous roll was another face: a new run of length 1 starts
                // (+ MOD keeps the difference non-negative)
                next[face][0] = (total - endingWith[face] + MOD) % MOD;
                // previous roll was this face: the run grows, if still allowed
                for (int run = 1; run < rollMax[face]; run++) {
                    next[face][run] = dp[face][run - 1];
                }
            }
            dp = next;
        }

        long answer = 0;
        for (int face = 0; face < FACES; face++) {
            for (long count : dp[face]) {
                answer = (answer + count) % MOD;
            }
        }
        return (int) answer;
    }
}
