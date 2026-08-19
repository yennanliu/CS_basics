package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximize-score-after-n-operations/

/**
 *  1799. Maximize Score After N Operations
 *  Hard
 *
 *  You are given nums, an array of positive integers of size 2 * n.
 *  You must perform n operations on this array.
 *
 *  In the ith operation (1-indexed), you will:
 *    - Choose two elements, x and y.
 *    - Receive a score of i * gcd(x, y).
 *    - Remove x and y from nums.
 *
 *  Return the maximum score you can receive after performing n operations.
 *
 *  Example 1:
 *    Input: nums = [1,2]
 *    Output: 1     (1 * gcd(1,2) = 1)
 *
 *  Example 2:
 *    Input: nums = [3,4,6,8]
 *    Output: 11    (1*gcd(3,6) + 2*gcd(4,8) = 3 + 8 = 11)
 *
 *  Constraints:
 *    1 <= n <= 7
 *    nums.length == 2 * n
 *    1 <= nums[i] <= 10^6
 */
public class MaximizeScoreAfterNOperations {

    // V0
    // IDEA: bitmask DP over which elements are already used; popcount/2 + 1 = operation index.
    /**
     * time = O(2^m * m^2)   where m = nums.length (<= 14)
     * space = O(2^m)
     */
    public int maxScore(int[] nums) {
        int m = nums.length;
        int full = 1 << m;

        // pre-compute pairwise gcd
        int[][] g = new int[m][m];
        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < m; j++) {
                g[i][j] = gcd(nums[i], nums[j]);
            }
        }

        int[] dp = new int[full];
        for (int mask = 0; mask < full; mask++) {
            int cnt = Integer.bitCount(mask);
            if (cnt % 2 != 0) {
                continue;
            }
            int op = cnt / 2 + 1;
            for (int i = 0; i < m; i++) {
                if ((mask & (1 << i)) != 0) {
                    continue;
                }
                for (int j = i + 1; j < m; j++) {
                    if ((mask & (1 << j)) != 0) {
                        continue;
                    }
                    int next = mask | (1 << i) | (1 << j);
                    dp[next] = Math.max(dp[next], dp[mask] + op * g[i][j]);
                }
            }
        }
        return dp[full - 1];
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return a;
    }
}
