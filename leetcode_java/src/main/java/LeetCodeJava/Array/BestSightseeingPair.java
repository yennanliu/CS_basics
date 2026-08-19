package LeetCodeJava.Array;

// https://leetcode.com/problems/best-sightseeing-pair/

/**
 *  1014. Best Sightseeing Pair
 *  Medium
 *
 *  You are given an integer array values where values[i] represents the value of
 *  the ith sightseeing spot. Two sightseeing spots i and j have a distance j - i
 *  between them.
 *
 *  The score of a pair (i < j) of sightseeing spots is
 *  values[i] + values[j] + i - j: the sum of the values of the sightseeing spots,
 *  minus the distance between them.
 *
 *  Return the maximum score of a pair of sightseeing spots.
 *
 *
 *  Example 1:
 *
 *  Input: values = [8,1,5,2,6]
 *  Output: 11
 *  Explanation: i = 0, j = 2, values[0] + values[2] + 0 - 2 = 8 + 5 + 0 - 2 = 11
 *
 *  Example 2:
 *
 *  Input: values = [1,2]
 *  Output: 2
 *
 *
 *  Constraints:
 *
 *  2 <= values.length <= 5 * 10^4
 *  1 <= values[i] <= 1000
 */
public class BestSightseeingPair {

    // V0
    // IDEA: GREEDY 1-PASS — keep the best (values[i] + i) seen so far, pair it with (values[j] - j)
    /**
     * time = O(n)
     * space = O(1)
     */
    public int maxScoreSightseeingPair(int[] values) {
        int best = values[0] + 0; // max of values[i] + i for i < j
        int res = Integer.MIN_VALUE;
        for (int j = 1; j < values.length; j++) {
            res = Math.max(res, best + values[j] - j);
            best = Math.max(best, values[j] + j);
        }
        return res;
    }
}
