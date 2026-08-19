package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-cost-to-merge-stones/


/**
 *  1000. Minimum Cost to Merge Stones
 *  Hard
 *
 *  There are n piles of stones arranged in a row. The ith pile has stones[i] stones.
 *
 *  A move consists of merging exactly k consecutive piles into one pile, and the
 *  cost of this move is equal to the total number of stones in these k piles.
 *
 *  Return the minimum cost to merge all piles of stones into one pile.
 *  If it is impossible, return -1.
 *
 *  Example 1:
 *    Input: stones = [3,2,4,1], k = 2
 *    Output: 20
 *
 *  Example 2:
 *    Input: stones = [3,2,4,1], k = 3
 *    Output: -1
 *
 *  Example 3:
 *    Input: stones = [3,5,1,2,6], k = 3
 *    Output: 25
 *
 *  Constraints:
 *    n == stones.length
 *    1 <= n <= 30
 *    1 <= stones[i] <= 100
 *    2 <= k <= 30
 */
public class MinimumCostToMergeStones {

    // V0
    // IDEA: interval DP. dp[i][j] = min cost to merge stones[i..j] into the fewest
    //       possible piles (which is 1 + (j-i) % (k-1)); split points step by (k-1).
    /**
     * time = O(n^3 / k)
     * space = O(n^2)
     */
    public int mergeStones(int[] stones, int k) {
        int n = stones.length;
        if ((n - 1) % (k - 1) != 0) {
            return -1;
        }

        // prefix[i] = sum of stones[0..i-1]
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + stones[i];
        }

        int[][] dp = new int[n][n];
        final int INF = Integer.MAX_VALUE / 2;

        for (int len = k; len <= n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;
                dp[i][j] = INF;
                for (int mid = i; mid < j; mid += (k - 1)) {
                    dp[i][j] = Math.min(dp[i][j], dp[i][mid] + dp[mid + 1][j]);
                }
                // only when [i..j] can collapse into exactly 1 pile do we pay the merge cost
                if ((j - i) % (k - 1) == 0) {
                    dp[i][j] += prefix[j + 1] - prefix[i];
                }
            }
        }

        return dp[0][n - 1];
    }

    // V1
    // IDEA: top-down memoization over (i, j, piles) - merge stones[i..j] into `piles` piles.
    /**
     * time = O(n^3 * k)
     * space = O(n^2 * k)
     */
    public int mergeStones_1(int[] stones, int k) {
        int n = stones.length;
        if ((n - 1) % (k - 1) != 0) {
            return -1;
        }
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + stones[i];
        }
        Integer[][][] memo = new Integer[n][n][k + 1];
        return helper(0, n - 1, 1, k, prefix, memo);
    }

    private int helper(int i, int j, int piles, int k, int[] prefix, Integer[][][] memo) {
        int len = j - i + 1;
        if ((len - piles) % (k - 1) != 0) {
            return Integer.MAX_VALUE / 2;
        }
        if (i == j) {
            return piles == 1 ? 0 : Integer.MAX_VALUE / 2;
        }
        if (memo[i][j][piles] != null) {
            return memo[i][j][piles];
        }

        int res;
        if (piles == 1) {
            res = helper(i, j, k, k, prefix, memo) + prefix[j + 1] - prefix[i];
        } else {
            res = Integer.MAX_VALUE / 2;
            for (int mid = i; mid < j; mid += (k - 1)) {
                int left = helper(i, mid, 1, k, prefix, memo);
                int right = helper(mid + 1, j, piles - 1, k, prefix, memo);
                res = Math.min(res, left + right);
            }
        }
        memo[i][j][piles] = res;
        return res;
    }
}
