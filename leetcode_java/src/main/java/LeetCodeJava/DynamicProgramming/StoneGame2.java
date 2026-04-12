package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/stone-game-ii/description/
/**
 *  1140. Stone Game II
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Alice and Bob continue their games with piles of stones. There are a number of piles arranged in a row, and each pile has a positive integer number of stones piles[i]. The objective of the game is to end with the most stones.
 *
 * Alice and Bob take turns, with Alice starting first.
 *
 * On each player's turn, that player can take all the stones in the first X remaining piles, where 1 <= X <= 2M. Then, we set M = max(M, X). Initially, M = 1.
 *
 * The game continues until all the stones have been taken.
 *
 * Assuming Alice and Bob play optimally, return the maximum number of stones Alice can get.
 *
 *
 *
 * Example 1:
 *
 * Input: piles = [2,7,9,4,4]
 *
 * Output: 10
 *
 * Explanation:
 *
 * If Alice takes one pile at the beginning, Bob takes two piles, then Alice takes 2 piles again. Alice can get 2 + 4 + 4 = 10 stones in total.
 * If Alice takes two piles at the beginning, then Bob can take all three piles left. In this case, Alice get 2 + 7 = 9 stones in total.
 * So we return 10 since it's larger.
 *
 * Example 2:
 *
 * Input: piles = [1,2,3,4,5,100]
 *
 * Output: 104
 *
 *
 *
 * Constraints:
 *
 * 1 <= piles.length <= 100
 * 1 <= piles[i] <= 104
 *
 *
 */
public class StoneGame2 {

    // V0
//    public int stoneGameII(int[] piles) {
//
//    }

    // V1
    // IDEA: DP + DFS ?? (gpt)
    public int stoneGameII_1(int[] piles) {
        int n = piles.length;

        // suffix sum: total[i] = sum from i to end
        int[] total = new int[n];
        total[n - 1] = piles[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            total[i] = total[i + 1] + piles[i];
        }

        int[][] memo = new int[n][n + 1];

        return dfs(0, 1, piles, total, memo);
    }

    private int dfs(int i, int M, int[] piles, int[] total, int[][] memo) {
        int n = piles.length;

        if (i >= n)
            return 0;

        if (memo[i][M] != 0)
            return memo[i][M];

        int max = 0;

        // try all X
        for (int X = 1; X <= 2 * M && i + X <= n; X++) {
            max = Math.max(
                    max,
                    total[i] - dfs(i + X, Math.max(M, X), piles, total, memo));
        }

        memo[i][M] = max;
        return max;
    }

    // V2
    // IDEA: TOP DOWN DP (gemini)
    private int[][] memo;
    private int[] suffixSum;
    private int n;

    public int stoneGameII_2(int[] piles) {
        this.n = piles.length;
        // memo[i][m] stores max stones starting from index i with parameter m
        // Max possible M is n, so we use n + 1
        this.memo = new int[n][n + 1];
        this.suffixSum = new int[n];

        // 1. Precompute suffix sums for O(1) stone calculation
        suffixSum[n - 1] = piles[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            suffixSum[i] = suffixSum[i + 1] + piles[i];
        }

        return dfs(0, 1);
    }

    private int dfs(int i, int m) {
        // Base case: no more piles
        if (i >= n)
            return 0;

        // If we can take all remaining piles, do it
        if (i + 2 * m >= n)
            return suffixSum[i];

        // Check memo
        if (memo[i][m] != 0)
            return memo[i][m];

        int maxStones = 0;
        // Try taking X piles where 1 <= X <= 2M
        for (int x = 1; x <= 2 * m; x++) {
            // New M is max(old M, X)
            int nextM = Math.max(m, x);

            // Current player's stones = (Total remaining) - (What the other player gets)
            int currentTake = suffixSum[i] - dfs(i + x, nextM);
            maxStones = Math.max(maxStones, currentTake);
        }

        return memo[i][m] = maxStones;
    }

    

    // V3
    // IDEA: 2D DP
    // https://leetcode.com/problems/stone-game-ii/solutions/5662713/9855easy-solutionwith-explanation-by-mra-dtbz/
    public int stoneGameII_3(int[] piles) {
        int n = piles.length;

        int[][] dp = new int[n][n + 1];
        int[] suffixSum = new int[n];
        suffixSum[n - 1] = piles[n - 1];

        for (int i = n - 2; i >= 0; i--) {
            suffixSum[i] = suffixSum[i + 1] + piles[i];
        }

        for (int i = n - 1; i >= 0; i--) {
            for (int m = 1; m <= n; m++) {
                if (i + 2 * m >= n) {
                    dp[i][m] = suffixSum[i];
                } else {
                    for (int x = 1; x <= 2 * m; x++) {
                        dp[i][m] = Math.max(dp[i][m], suffixSum[i] - dp[i + x][Math.max(m, x)]);
                    }
                }
            }
        }

        return dp[0][1];
    }


    // V4
    // IDEA: 2D DP
    // https://leetcode.com/problems/stone-game-ii/solutions/5662924/dynamic-programming-with-on3-tc-multiple-ip1p/
    public int stoneGameII_4(int[] piles) {
        int totalPiles = piles.length;
        int[] suffixSums = new int[totalPiles + 1];
        for (int i = totalPiles - 1; i >= 0; i--) {
            suffixSums[i] = suffixSums[i + 1] + piles[i];
        }
        return maxStonesAliceCanGet(suffixSums, 1, 0, new int[totalPiles][totalPiles + 1]);
    }

    private int maxStonesAliceCanGet(int[] suffixSums, int m, int currentPile, int[][] memo) {
        int totalPiles = suffixSums.length - 1;

        if (currentPile >= totalPiles)
            return 0;

        if (currentPile + 2 * m >= totalPiles) {
            return suffixSums[currentPile];
        }

        if (memo[currentPile][m] != 0)
            return memo[currentPile][m];

        int maxStones = 0;

        for (int x = 1; x <= 2 * m; x++) {
            int currentStones = suffixSums[currentPile]
                    - maxStonesAliceCanGet(suffixSums, Math.max(m, x), currentPile + x, memo);
            maxStones = Math.max(maxStones, currentStones);
        }

        memo[currentPile][m] = maxStones;
        return maxStones;
    }




}
