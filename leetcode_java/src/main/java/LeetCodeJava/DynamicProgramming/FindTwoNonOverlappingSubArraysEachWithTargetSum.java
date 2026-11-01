package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/find-two-non-overlapping-sub-arrays-each-with-target-sum/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 1477. Find Two Non-overlapping Sub-arrays Each With Target Sum
 * Medium
 *
 * You are given an array of integers arr and an integer target.
 *
 * You have to find two non-overlapping sub-arrays of arr each with a sum equal target. There can be multiple answers so you have to find an answer where the sum of the lengths of the two sub-arrays is minimum.
 *
 * Return the minimum sum of the lengths of the two required sub-arrays, or return -1 if you cannot find such two sub-arrays.
 *
 *
 * Example 1:
 *
 * Input: arr = [3,2,2,4,3], target = 3
 * Output: 2
 * Explanation: Only two sub-arrays have sum = 3 ([3] and [3]). The sum of their lengths is 2.
 *
 * Example 2:
 *
 * Input: arr = [7,3,4,7], target = 7
 * Output: 2
 * Explanation: Although we have three non-overlapping sub-arrays of sum = 7 ([7], [3,4] and [7]), but we will choose the first and third sub-arrays as the sum of their lengths is 2.
 *
 * Example 3:
 *
 * Input: arr = [4,3,2,6,2,3,4], target = 6
 * Output: -1
 * Explanation: We have only one sub-array of sum = 6.
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 10^5
 * 1 <= arr[i] <= 1000
 * 1 <= target <= 10^8
 *
 */
public class FindTwoNonOverlappingSubArraysEachWithTargetSum {

    private static final int INF = Integer.MAX_VALUE / 2;

    // V0
    // IDEA: PREFIX SUM HASH MAP + BEST-SO-FAR DP
    //       arr[i] >= 1, so prefix sums strictly increase and each appears once:
    //       firstIndex[s] = the count of elements whose prefix sum is s.
    //       at position i, if (sum - target) was seen at j, arr[j..i-1] is a window of length i-j.
    //       best[i] = shortest valid window ending at or before i.
    //       pairing the new window with best[j] cannot overlap, because best[j] only sees arr[:j].
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minSumOfLengths(int[] arr, int target) {
        int n = arr.length;

        // prefix sum -> number of elements that produced it
        Map<Integer, Integer> prefixIndex = new HashMap<>();
        prefixIndex.put(0, 0);

        // best[i] = shortest window with sum target inside arr[0 .. i-1]
        int[] best = new int[n + 1];
        best[0] = INF;

        int answer = INF;
        int prefixSum = 0;
        for (int i = 1; i <= n; i++) {
            prefixSum += arr[i - 1];
            best[i] = best[i - 1];

            Integer start = prefixIndex.get(prefixSum - target);
            if (start != null) {
                // arr[start .. i-1] sums to target
                int length = i - start;
                // pair it with the best window that ends before `start`
                answer = Math.min(answer, best[start] + length);
                best[i] = Math.min(best[i], length);
            }
            prefixIndex.put(prefixSum, i);
        }
        return answer >= INF ? -1 : answer;
    }
}
