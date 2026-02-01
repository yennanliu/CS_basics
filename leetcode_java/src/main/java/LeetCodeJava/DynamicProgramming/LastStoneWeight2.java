package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/last-stone-weight-ii/
/**
 * 1049. Last Stone Weight II
 * Medium
 * Topics
 * Companies
 * Hint
 * You are given an array of integers stones where stones[i] is the weight of the ith stone.
 *
 * We are playing a game with the stones. On each turn, we choose any two stones and smash them together. Suppose the stones have weights x and y with x <= y. The result of this smash is:
 *
 * If x == y, both stones are destroyed, and
 * If x != y, the stone of weight x is destroyed, and the stone of weight y has new weight y - x.
 * At the end of the game, there is at most one stone left.
 *
 * Return the smallest possible weight of the left stone. If there are no stones left, return 0.
 *
 *
 *
 * Example 1:
 *
 * Input: stones = [2,7,4,1,8,1]
 * Output: 1
 * Explanation:
 * We can combine 2 and 4 to get 2, so the array converts to [2,7,1,8,1] then,
 * we can combine 7 and 8 to get 1, so the array converts to [2,1,1,1] then,
 * we can combine 2 and 1 to get 1, so the array converts to [1,1,1] then,
 * we can combine 1 and 1 to get 0, so the array converts to [1], then that's the optimal value.
 * Example 2:
 *
 * Input: stones = [31,26,33,21,40]
 * Output: 5
 *
 *
 * Constraints:
 *
 * 1 <= stones.length <= 30
 * 1 <= stones[i] <= 100
 *
 */
public class LastStoneWeight2 {

    // V0
//    public int lastStoneWeightII(int[] stones) {
//
//    }

    // V1
    // https://www.youtube.com/watch?v=gdXkkmzvR3c
    // https://github.com/neetcode-gh/leetcode/blob/main/python%2F1049-last-stone-weight-ii.py
    // PYTHON


    // V2-1
    // IDEA: 2D DP
    // https://leetcode.com/problems/last-stone-weight-ii/solutions/6703781/3-ways-1d-and-2d-tabulation-and-recursiv-6wbl/
    /**
     * time = O(N)
     * space = O(N)
     */
    public int lastStoneWeightII_2_1(int[] stones) {
        int sum = 0;
        // Step 1: Calculate total sum
        for (int stone : stones) {
            sum += stone;
        }

        boolean[][] dp = new boolean[stones.length + 1][sum + 1];

        // Step 2: Base case — sum 0 is always possible
        for (int i = 0; i <= stones.length; i++) {
            dp[i][0] = true;
        }

        // Step 3: Fill DP table
        for (int i = 1; i <= stones.length; i++) {
            for (int j = 1; j <= sum / 2; j++) {
                if (stones[i - 1] <= j) {
                    dp[i][j] = dp[i - 1][j - stones[i - 1]] || dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        // Step 4: Find best possible sum close to sum/2
        for (int j = sum / 2; j >= 0; j--) {
            if (dp[stones.length][j]) {
                return sum - 2 * j;
            }
        }

        return 0;
    }

    // V2-2
    // https://leetcode.com/problems/last-stone-weight-ii/solutions/6703781/3-ways-1d-and-2d-tabulation-and-recursiv-6wbl/
    // IDEA:  (Optimized 1D DP Approach)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int lastStoneWeightII_2_2(int[] stones) {
        int n = stones.length, sum = 0;
        for (int w : stones)
            sum += w;
        int half = sum / 2;

        boolean[] dp = new boolean[half + 1];
        dp[0] = true;

        for (int w : stones) {
            for (int j = half; j >= w; j--) {
                dp[j] = dp[j] || dp[j - w];
            }
        }

        for (int j = half; j >= 0; j--) {
            if (dp[j]) {
                return sum - 2 * j;
            }
        }
        return 0;
    }


    // V2-3
    // https://leetcode.com/problems/last-stone-weight-ii/solutions/6703781/3-ways-1d-and-2d-tabulation-and-recursiv-6wbl/
    // IDEA: 2D DP with Explicit Recursive Check
    /**
     * time = O(N)
     * space = O(N)
     */
    public int lastStoneWeightII_2_3(int[] stones) {
        int sum = 0;
        for (int stone : stones) {
            sum += stone;
        }

        boolean[][] memo = new boolean[stones.length + 1][sum / 2 + 1];

        for (int i = 0; i <= stones.length; i++) {
            memo[i][0] = true;
        }

        for (int i = sum / 2; i >= 0; i--) {
            if (canMakeSum(stones, stones.length, i, memo)) {
                return sum - 2 * i;
            }
        }

        return 0;
    }

    private boolean canMakeSum(int[] stones, int i, int j, boolean[][] memo) {
        if (j == 0)
            return true;
        if (i == 0)
            return false;

        if (memo[i][j]) {
            return true;
        }

        if (stones[i - 1] <= j) {
            memo[i][j] = canMakeSum(stones, i - 1, j - stones[i - 1], memo) ||
                    canMakeSum(stones, i - 1, j, memo);
        } else {
            memo[i][j] = canMakeSum(stones, i - 1, j, memo);
        }

        return memo[i][j];
    }

    // V3
    // https://leetcode.com/problems/last-stone-weight-ii/solutions/1272897/simple-java-implementation-knapsack-by-p-yuov/
    // IDEA: DP
    /**
     * time = O(N)
     * space = O(N)
     */
    public int lastStoneWeightII_3(int[] stones) {
        int sumStWt = 0;
        for (int stone : stones) {
            sumStWt += stone;
        }
        Integer[][] dp = new Integer[stones.length][sumStWt];
        return helper(stones, 0, 0, 0, dp);
    }

    private int helper(int[] stones, int index, int sumL, int sumR, Integer[][] dp) {
        if (index == stones.length) {
            return Math.abs(sumL - sumR);
        }

        if (dp[index][sumL] != null) {
            return dp[index][sumL];
        }

        dp[index][sumL] = Math.min(helper(stones, index + 1, sumL + stones[index], sumR, dp),
                helper(stones, index + 1, sumL, sumR + stones[index], dp));
        return dp[index][sumL];
    }

    // V4
    // IDEA: DP (gpt)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int lastStoneWeightII_4(int[] stones) {
        int total = 0;
        for (int stone : stones) {
            total += stone;
        }

        int target = total / 2;
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;

        for (int stone : stones) {
            for (int j = target; j >= stone; j--) {
                dp[j] = dp[j] || dp[j - stone];
            }
        }

        for (int i = target; i >= 0; i--) {
            if (dp[i]) {
                return total - 2 * i;
            }
        }

        return 0; // fallback
    }

}
