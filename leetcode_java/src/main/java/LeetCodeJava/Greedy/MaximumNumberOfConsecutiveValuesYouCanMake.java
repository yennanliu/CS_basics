package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-number-of-consecutive-values-you-can-make/description/

import java.util.*;

/**
 * 1798. Maximum Number of Consecutive Values You Can Make
 * Medium
 *
 * You are given an integer array coins of length n which represents the n coins
 * that you own. The value of the i^th coin is coins[i]. You can make some value x
 * if you can choose some of your n coins such that their values sum up to x.
 *
 * Return the maximum number of consecutive integer values that you can make with
 * your coins starting from and including 0.
 *
 * Note that you may have multiple coins of the same value.
 *
 *
 * Example 1:
 *
 * Input: coins = [1,3]
 * Output: 2
 * Explanation: You can make the following values:
 * - 0: take []
 * - 1: take [1]
 * You can make 2 consecutive integer values starting from 0.
 *
 * Example 2:
 *
 * Input: coins = [1,1,1,4]
 * Output: 8
 * Explanation: You can make every value from 0 to 7.
 * You can make 8 consecutive integer values starting from 0.
 *
 * Example 3:
 *
 * Input: coins = [1,4,10,3,1]
 * Output: 20
 *
 *
 * Constraints:
 *
 * coins.length == n
 * 1 <= n <= 4 * 10^4
 * 1 <= coins[i] <= 4 * 10^4
 *
 */
public class MaximumNumberOfConsecutiveValuesYouCanMake {

    // V0
    // IDEA: GREEDY ON SORTED COINS (keep the reachable prefix [0, reach - 1])
    /**
     * time = O(N log N)
     * space = O(1) (in-place sort)
     */
    public int getMaximumConsecutive(int[] coins) {
        Arrays.sort(coins);

        // invariant: every value in [0, reach - 1] can be built from the coins seen so far
        int reach = 1;
        for (int coin : coins) {
            // a coin bigger than reach leaves a hole at `reach` that no larger coin can fill
            if (coin > reach) {
                break;
            }
            // old range plus the range shifted by `coin` now covers [0, reach + coin - 1]
            reach += coin;
        }
        return reach;
    }
}
