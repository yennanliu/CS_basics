package LeetCodeJava.Greedy;

// https://leetcode.com/problems/put-marbles-in-bags/description/

import java.util.*;

/**
 * 2551. Put Marbles in Bags
 * Hard
 *
 * You have k bags. You are given a 0-indexed integer array weights where weights[i] is the weight of the ith marble. You are also given the integer k.
 *
 * Divide the marbles into the k bags according to the following rules:
 *
 * - No bag is empty.
 * - If the ith marble and jth marble are in a bag, then all marbles with an index between the ith and jth indices should also be in that same bag.
 * - If a bag consists of all the marbles with an index from i to j inclusively, then the cost of the bag is weights[i] + weights[j].
 *
 * The score after distributing the marbles is the sum of the costs of all the k bags.
 *
 * Return the difference between the maximum and minimum scores among marble distributions.
 *
 *
 * Example 1:
 *
 * Input: weights = [1,3,5,1], k = 2
 * Output: 4
 * Explanation:
 * The distribution [1],[3,5,1] results in the minimal score of (1+1) + (3+1) = 6.
 * The distribution [1,3],[5,1], results in the maximal score of (1+3) + (5+1) = 10.
 * Thus, we return their difference 10 - 6 = 4.
 *
 * Example 2:
 *
 * Input: weights = [1, 3], k = 2
 * Output: 0
 * Explanation: The only distribution possible is [1],[3].
 * Since both the maximal and minimal score are the same, we return 0.
 *
 *
 * Constraints:
 *
 * 1 <= k <= weights.length <= 10^5
 * 1 <= weights[i] <= 10^9
 *
 */
public class PutMarblesInBags {

    // V0
    // IDEA: GREEDY (SPLIT POINTS) + SORTING
    //       k bags = k - 1 cuts. a cut between i and i + 1 adds weights[i] + weights[i + 1]
    //       to the score; weights[0] and weights[n - 1] are always counted and cancel out.
    //       so answer = (sum of the k - 1 largest pair sums) - (sum of the k - 1 smallest).
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long putMarbles(int[] weights, int k) {
        int n = weights.length;
        // one bag means no cut: max and min score are the same
        if (k <= 1 || n <= 1) {
            return 0;
        }

        // pairSums[i] = what a cut between i and i + 1 contributes
        long[] pairSums = new long[n - 1];
        for (int i = 0; i < n - 1; i++) {
            pairSums[i] = (long) weights[i] + weights[i + 1];
        }
        Arrays.sort(pairSums);

        long maxCuts = 0;
        long minCuts = 0;
        int cuts = k - 1;
        for (int i = 0; i < cuts; i++) {
            // the smallest cuts from the front, the largest from the back
            minCuts += pairSums[i];
            maxCuts += pairSums[pairSums.length - 1 - i];
        }
        return maxCuts - minCuts;
    }
}
