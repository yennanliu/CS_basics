package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/guess-number-higher-or-lower-ii/

/**
 *  375. Guess Number Higher or Lower II
 *  Medium
 *
 *  We are playing the Guessing Game. The game will work as follows:
 *
 *   1. I pick a number between 1 and n.
 *   2. You guess a number.
 *   3. If you guess the right number, you win the game.
 *   4. If you guess the wrong number, then I will tell you whether the number
 *      I picked is higher or lower, and you will continue guessing.
 *   5. Every time you guess a wrong number x, you will pay x dollars. If you
 *      run out of money, you lose the game.
 *
 *  Given a particular n, return the minimum amount of money you need to
 *  guarantee a win regardless of what number I pick.
 *
 *  Example 1:
 *
 *  Input: n = 10
 *  Output: 16
 *
 *  Example 2:
 *
 *  Input: n = 1
 *  Output: 0
 *
 *  Constraints:
 *
 *  1 <= n <= 200
 */
public class GuessNumberHigherOrLowerII {

    // V0
    // IDEA: INTERVAL DP
    //  dp[lo][hi] = min over k in [lo, hi] of ( k + max(dp[lo][k-1], dp[k+1][hi]) )
    /**
     * time = O(n^3)
     * space = O(n^2)
     */
    public int getMoneyAmount(int n) {
        // dp[i][j], 1-indexed, extra row/col to avoid bound checks
        int[][] dp = new int[n + 2][n + 2];

        for (int gap = 1; gap < n; gap++) {
            for (int lo = 1; lo + gap <= n; lo++) {
                int hi = lo + gap;
                int best = Integer.MAX_VALUE;
                for (int k = lo; k <= hi; k++) {
                    int left = (k - 1 >= lo) ? dp[lo][k - 1] : 0;
                    int right = (k + 1 <= hi) ? dp[k + 1][hi] : 0;
                    int cost = k + Math.max(left, right);
                    best = Math.min(best, cost);
                }
                dp[lo][hi] = best;
            }
        }
        return dp[1][n];
    }
}
