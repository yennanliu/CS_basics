package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-sum-of-values-by-dividing-array/description/

import java.util.*;

/**
 * 3117. Minimum Sum of Values by Dividing Array
 * Hard
 *
 * You are given two arrays nums and andValues of length n and m respectively.
 *
 * The value of an array is equal to the last element of that array.
 *
 * You have to divide nums into m disjoint contiguous subarrays such that for the ith subarray [li, ri], the bitwise AND of the subarray elements is equal to andValues[i], in other words, nums[li] & nums[li + 1] & ... & nums[ri] == andValues[i] for all 1 <= i <= m, where & represents the bitwise AND operator.
 *
 * Return the minimum possible sum of the values of the m subarrays nums is divided into. If it is not possible to divide nums into m subarrays satisfying these conditions, return -1.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,4,3,3,2], andValues = [0,3,3,2]
 * Output: 12
 * Explanation:
 * The only possible way to divide nums is:
 * [1,4] as 1 & 4 == 0.
 * [3] as the bitwise AND of a single element subarray is that element itself.
 * [3] as the bitwise AND of a single element subarray is that element itself.
 * [2] as the bitwise AND of a single element subarray is that element itself.
 * The sum of the values for these subarrays is 4 + 3 + 3 + 2 = 12.
 *
 * Example 2:
 *
 * Input: nums = [2,3,5,7,7,7,5], andValues = [0,7,5]
 * Output: 17
 * Explanation:
 * There are three ways to divide nums:
 * [[2,3,5],[7,7,7],[5]] with the sum of the values 5 + 7 + 5 == 17.
 * [[2,3,5,7],[7,7],[5]] with the sum of the values 7 + 7 + 5 == 19.
 * [[2,3,5,7,7],[7],[5]] with the sum of the values 7 + 7 + 5 == 19.
 * The minimum possible sum of the values is 17.
 *
 * Example 3:
 *
 * Input: nums = [1,2,3,4], andValues = [2]
 * Output: -1
 * Explanation:
 * The bitwise AND of the entire array nums is 0. As there is no possible way to divide nums into a single subarray to have the bitwise AND of elements 2, return -1.
 *
 *
 * Constraints:
 *
 * 1 <= n == nums.length <= 10^4
 * 1 <= m == andValues.length <= 10
 * 1 <= nums[i] < 10^5
 * 0 <= andValues[j] < 10^5
 *
 */
public class MinimumSumOfValuesByDividingArray {

    // V0
    // IDEA: DP BY SEGMENT INDEX, CARRYING THE FEW DISTINCT RUNNING-AND VALUES
    //   dp[j][i] = min total value after splitting the first i elements into j subarrays.
    //   For subarrays ending at i, the AND over [start, i] only loses bits as start moves
    //   left, so it takes at most ~17 distinct values. Keep a map
    //     andToBest[a] = min dp[j-1][start] over starts whose AND(start..i) == a
    //   Moving i forward maps every key a to a & nums[i] (keeping the smaller cost) and adds
    //   the new single-element start. If andValues[j-1] is a key, segment j may close at i,
    //   costing nums[i] (a subarray's value is its last element).
    /**
     * time = O(N * M * log(maxValue))
     * space = O(N)
     */
    public int minimumValueSum(int[] nums, int[] andValues) {
        int n = nums.length;
        int m = andValues.length;
        final int INF = Integer.MAX_VALUE;

        // prev[i] = dp[j-1][i]; with 0 segments only the empty prefix is valid
        int[] prev = new int[n + 1];
        Arrays.fill(prev, INF);
        prev[0] = 0;

        for (int j = 1; j <= m; j++) {
            int target = andValues[j - 1];
            int[] cur = new int[n + 1];
            Arrays.fill(cur, INF);
            Map<Integer, Integer> andToBest = new HashMap<>();

            for (int i = 0; i < n; i++) {
                Map<Integer, Integer> next = new HashMap<>();
                // extend every open segment by nums[i]
                for (Map.Entry<Integer, Integer> entry : andToBest.entrySet()) {
                    int newAnd = entry.getKey() & nums[i];
                    int cost = entry.getValue();
                    if (cost < next.getOrDefault(newAnd, INF)) {
                        next.put(newAnd, cost);
                    }
                }
                // a new segment may start at i if the first i elements split into j-1 segments
                if (prev[i] != INF && prev[i] < next.getOrDefault(nums[i], INF)) {
                    next.put(nums[i], prev[i]);
                }
                andToBest = next;

                // close segment j at index i if its AND matches
                Integer best = andToBest.get(target);
                if (best != null) {
                    cur[i + 1] = best + nums[i];
                }
            }
            prev = cur;
        }
        return prev[n] == INF ? -1 : prev[n];
    }
}
