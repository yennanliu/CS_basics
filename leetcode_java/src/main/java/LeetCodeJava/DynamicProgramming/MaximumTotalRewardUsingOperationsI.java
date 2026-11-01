package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-total-reward-using-operations-i/description/

import java.util.*;

/**
 * 3180. Maximum Total Reward Using Operations I
 * Medium
 *
 * You are given an integer array rewardValues of length n, representing the values of rewards.
 *
 * Initially, your total reward x is 0, and all indices are unmarked. You are allowed to perform the following operation any number of times:
 *
 * Choose an unmarked index i from the range [0, n - 1].
 * If rewardValues[i] is greater than your current total reward x, then add rewardValues[i] to x (i.e., x = x + rewardValues[i]), and mark the index i.
 *
 * Return an integer denoting the maximum total reward you can collect by performing the operations optimally.
 *
 *
 * Example 1:
 *
 * Input: rewardValues = [1,1,3,3]
 * Output: 4
 * Explanation:
 * During the operations, we can choose to mark indices 0 and 2 in order, and the total reward will be 4, which is the maximum.
 *
 * Example 2:
 *
 * Input: rewardValues = [1,6,4,3,2]
 * Output: 11
 * Explanation:
 * Mark the indices 0, 2, 3, and 5 in order. The total reward will then be 11, which is the maximum.
 *
 *
 * Constraints:
 *
 * 1 <= rewardValues.length <= 2000
 * 1 <= rewardValues[i] <= 2000
 *
 */
public class MaximumTotalRewardUsingOperationsI {

    // V0
    // IDEA: SORT + REACHABLE-TOTALS DP (0/1 KNAPSACK ON BOOLEANS)
    //   A reward v can only be added while the total x < v, so totals grow and rewards are
    //   used in increasing order: sorting loses nothing, and duplicates are useless.
    //   reachable[t] = some sequence of operations ends with total t.
    //   For each v, every reachable t < v opens t + v. Sweep t downward so v is used once.
    //   Every final total is < 2 * maxValue, which bounds the array.
    /**
     * time = O(N log N + N * maxValue)
     * space = O(maxValue)
     */
    public int maxTotalReward(int[] rewardValues) {
        int[] values = rewardValues.clone();
        Arrays.sort(values);
        int maxValue = values[values.length - 1];
        int limit = 2 * maxValue;

        boolean[] reachable = new boolean[limit];
        reachable[0] = true;
        for (int i = 0; i < values.length; i++) {
            int v = values[i];
            // a duplicate can never be taken twice (the total already >= v after one copy)
            if (i > 0 && values[i - 1] == v) {
                continue;
            }
            // only totals strictly below v may take reward v
            for (int t = v - 1; t >= 0; t--) {
                if (reachable[t]) {
                    reachable[t + v] = true;
                }
            }
        }

        for (int t = limit - 1; t >= 0; t--) {
            if (reachable[t]) {
                return t;
            }
        }
        return 0;
    }
}
