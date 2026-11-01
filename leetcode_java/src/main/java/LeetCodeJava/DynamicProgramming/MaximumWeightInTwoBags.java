package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-weight-in-two-bags/description/

import java.util.*;

/**
 * 3647. Maximum Weight in Two Bags
 * Medium
 *
 * You are given an integer array weights and two integers w1 and w2
 * representing the maximum capacities of two bags.
 *
 * Each item may be placed in at most one bag such that:
 *
 * Bag 1 holds at most w1 total weight.
 * Bag 2 holds at most w2 total weight.
 *
 * Return the maximum total weight that can be packed into the two bags.
 *
 *
 * Example 1:
 *
 * Input: weights = [1,4,3,2], w1 = 5, w2 = 4
 * Output: 9
 * Explanation:
 * Bag 1: Place weights[2] = 3 and weights[3] = 2 as 3 + 2 = 5 <= w1
 * Bag 2: Place weights[1] = 4 as 4 <= w2
 * Total weight: 5 + 4 = 9
 *
 * Example 2:
 *
 * Input: weights = [3,6,4,8], w1 = 9, w2 = 7
 * Output: 15
 * Explanation:
 * Bag 1: Place weights[3] = 8 as 8 <= w1
 * Bag 2: Place weights[0] = 3 and weights[2] = 4 as 3 + 4 = 7 <= w2
 * Total weight: 8 + 7 = 15
 *
 * Example 3:
 *
 * Input: weights = [5,7], w1 = 2, w2 = 3
 * Output: 0
 * Explanation:
 * No weight fits in either bag, thus the answer is 0.
 *
 *
 * Constraints:
 *
 * 1 <= weights.length <= 100
 * 1 <= weights[i] <= 100
 * 1 <= w1, w2 <= 300
 *
 */
public class MaximumWeightInTwoBags {

    // V0
    // IDEA: 0/1 KNAPSACK OVER THE PAIR OF LOADS (BAG 1, BAG 2)
    //   the bags compete for the same items, so filling them one after the other is
    //   wrong; the state that captures everything is the pair of current loads.
    //   reachable[a][b] = some items can load bag 1 with exactly a and bag 2 with exactly b
    //   each item w: skip it, put it in bag 1 (a - w -> a) or in bag 2 (b - w -> b).
    //   iterating a and b DOWNWARD reads only pre-item states, so each item is used once.
    /**
     * time = O(N * W1 * W2)
     * space = O(W1 * W2)
     */
    public int maxWeight(int[] weights, int w1, int w2) {
        boolean[][] reachable = new boolean[w1 + 1][w2 + 1];
        // both bags empty
        reachable[0][0] = true;

        for (int weight : weights) {
            for (int a = w1; a >= 0; a--) {
                for (int b = w2; b >= 0; b--) {
                    if (reachable[a][b]) {
                        continue;
                    }
                    boolean intoBagOne = a >= weight && reachable[a - weight][b];
                    boolean intoBagTwo = b >= weight && reachable[a][b - weight];
                    if (intoBagOne || intoBagTwo) {
                        reachable[a][b] = true;
                    }
                }
            }
        }

        int best = 0;
        for (int a = 0; a <= w1; a++) {
            for (int b = 0; b <= w2; b++) {
                if (reachable[a][b]) {
                    best = Math.max(best, a + b);
                }
            }
        }
        return best;
    }
}
