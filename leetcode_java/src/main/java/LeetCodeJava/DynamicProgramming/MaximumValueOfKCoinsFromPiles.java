package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-value-of-k-coins-from-piles/description/

import java.util.*;

/**
 * 2218. Maximum Value of K Coins From Piles
 * Hard
 *
 * There are n piles of coins on a table. Each pile consists of a positive number of coins of assorted denominations.
 *
 * In one move, you can choose any coin on top of any pile, remove it, and add it to your wallet.
 *
 * Given a list piles, where piles[i] is a list of integers denoting the composition of the ith pile from top to bottom, and a positive integer k, return the maximum total value of coins you can have in your wallet if you choose exactly k coins optimally.
 *
 *
 * Example 1:
 *
 * Input: piles = [[1,100,3],[7,8,9]], k = 2
 * Output: 101
 * Explanation:
 * The above diagram shows the different ways we can choose k coins.
 * The maximum total we can obtain is 101.
 *
 * Example 2:
 *
 * Input: piles = [[100],[100],[100],[100],[100],[100],[1,1,1,1,1,1,700]], k = 7
 * Output: 706
 * Explanation:
 * The maximum total can be obtained if we choose all coins from the last pile.
 *
 *
 * Constraints:
 *
 * n == piles.length
 * 1 <= n <= 1000
 * 1 <= piles[i][j] <= 10^5
 * 1 <= k <= sum(piles[i].length) <= 2000
 *
 */
public class MaximumValueOfKCoinsFromPiles {

    // V0
    // IDEA: GROUP KNAPSACK — EACH PILE IS A GROUP, TAKING h COINS FROM IT COSTS h AND GIVES ITS PREFIX SUM
    /**
     * time = O(K * total coins)
     * space = O(K)
     */
    public int maxValueOfCoins(List<List<Integer>> piles, int k) {
        // dp[j] = best value using exactly up to j coins from the piles seen so far
        int[] dp = new int[k + 1];

        for (List<Integer> pile : piles) {
            // prefix[h] = value of the top h coins of this pile (only the first k matter)
            int usable = Math.min(k, pile.size());
            int[] prefix = new int[usable + 1];
            for (int h = 1; h <= usable; h++) {
                prefix[h] = prefix[h - 1] + pile.get(h - 1);
            }

            // iterate capacity downward so this pile is used at most once
            for (int j = k; j >= 1; j--) {
                int best = dp[j];
                // take h coins from the top of this pile
                for (int h = 1; h <= Math.min(j, usable); h++) {
                    best = Math.max(best, dp[j - h] + prefix[h]);
                }
                dp[j] = best;
            }
        }
        return dp[k];
    }
}
