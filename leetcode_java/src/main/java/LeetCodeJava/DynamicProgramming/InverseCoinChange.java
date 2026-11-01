package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/inverse-coin-change/description/

import java.util.*;

/**
 * 3592. Inverse Coin Change
 * Medium
 *
 * You are given a 1-indexed integer array numWays, where numWays[i] represents the number of ways to select a total amount i using an infinite supply of some fixed coin denominations. Each denomination is a positive integer with value at most numWays.length.
 *
 * However, the exact coin denominations have been lost. Your task is to recover the set of denominations that could have resulted in the given numWays array.
 *
 * Return a sorted array containing unique integers which represents this set of denominations.
 *
 * If no such set exists, return an empty array.
 *
 *
 * Example 1:
 *
 * Input: numWays = [0,1,0,2,0,3,0,4,0,5]
 * Output: [2,4,6]
 * Explanation:
 *
 * Amount | Ways | Explanation
 * 1      | 0    | There is no way to select coins with total value 1.
 * 2      | 1    | The only way is [2].
 * 3      | 0    | There is no way to select coins with total value 3.
 * 4      | 2    | The ways are [2, 2] and [4].
 * 5      | 0    | There is no way to select coins with total value 5.
 * 6      | 3    | The ways are [2, 2, 2], [2, 4], and [6].
 * 7      | 0    | There is no way to select coins with total value 7.
 * 8      | 4    | The ways are [2, 2, 2, 2], [2, 2, 4], [2, 6], and [4, 4].
 * 9      | 0    | There is no way to select coins with total value 9.
 * 10     | 5    | The ways are [2, 2, 2, 2, 2], [2, 2, 2, 4], [2, 4, 4],
 *        |      | [2, 2, 6], and [4, 6].
 *
 * Example 2:
 *
 * Input: numWays = [1,2,2,3,4]
 * Output: [1,2,5]
 * Explanation:
 *
 * Amount | Ways | Explanation
 * 1      | 1    | The only way is [1].
 * 2      | 2    | The ways are [1, 1] and [2].
 * 3      | 2    | The ways are [1, 1, 1] and [1, 2].
 * 4      | 3    | The ways are [1, 1, 1, 1], [1, 1, 2], and [2, 2].
 * 5      | 4    | The ways are [1, 1, 1, 1, 1], [1, 1, 1, 2], [1, 2, 2], and [5].
 *
 * Example 3:
 *
 * Input: numWays = [1,2,3,4,15]
 * Output: []
 * Explanation: No set of denomination satisfies this array.
 *
 *
 * Constraints:
 *
 * 1 <= numWays.length <= 100
 * 0 <= numWays[i] <= 2 * 10^8
 *
 */
public class InverseCoinChange {

    // V0
    // IDEA: REBUILD THE COIN CHANGE (LC 518) TABLE LEFT TO RIGHT, READ OFF THE MISSING COINS
    //   ways[a] = number of ways to make amount a with the coins found so far.
    //   When the sweep reaches amount i, every smaller coin is already folded in,
    //   so ways[i] is final except for a coin worth exactly i, which adds 1 way:
    //     ways[i] == numWays[i]      -> no coin i
    //     ways[i] + 1 == numWays[i]  -> coin i exists: record it, fold it in
    //     otherwise                  -> no coin set fits, return []
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public List<Integer> findCoins(int[] numWays) {
        int n = numWays.length;
        long[] ways = new long[n + 1];
        // one way to make 0: use nothing
        ways[0] = 1;
        List<Integer> coins = new ArrayList<>();

        for (int amount = 1; amount <= n; amount++) {
            long wanted = numWays[amount - 1];
            if (ways[amount] == wanted) {
                continue;
            }
            if (ways[amount] + 1 != wanted) {
                return new ArrayList<>();
            }
            coins.add(amount);
            // unbounded knapsack update for the new coin
            for (int total = amount; total <= n; total++) {
                ways[total] += ways[total - amount];
            }
        }
        return coins;
    }
}
