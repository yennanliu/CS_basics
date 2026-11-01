package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/minimum-absolute-difference-queries/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 1906. Minimum Absolute Difference Queries
 * Medium
 *
 * The minimum absolute difference of an array a is defined as the minimum value of |a[i] - a[j]|,
 * where 0 <= i < j < a.length and a[i] != a[j]. If all elements of a are the same, the minimum
 * absolute difference is -1.
 *
 * For example, the minimum absolute difference of the array [5,2,3,7,2] is |2 - 3| = 1. Note that
 * it is not 0 because a[i] and a[j] must be different.
 *
 * You are given an integer array nums and the array queries where queries[i] = [li, ri]. For each
 * query i, compute the minimum absolute difference of the subarray nums[li...ri] containing the
 * elements of nums between the 0-based indices li and ri (inclusive).
 *
 * Return an array ans where ans[i] is the answer to the ith query.
 *
 * A subarray is a contiguous sequence of elements in an array.
 *
 * The value of |x| is defined as:
 * x if x >= 0.
 * -x if x < 0.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,4,8], queries = [[0,1],[1,2],[2,3],[0,3]]
 * Output: [2,1,4,1]
 *
 * Example 2:
 *
 * Input: nums = [4,5,2,2,7,10], queries = [[2,3],[0,2],[0,5],[3,5]]
 * Output: [-1,1,1,3]
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 10^5
 * 1 <= nums[i] <= 100
 * 1 <= queries.length <= 2 * 10^4
 * 0 <= li < ri < nums.length
 *
 */
public class MinimumAbsoluteDifferenceQueries {

    private static final int MAX_VALUE = 100;

    // V0
    // IDEA: PREFIX COUNT PER VALUE (the value range is tiny: 1..100)
    // prefixCount[i][v] = how many times v occurs in nums[0 .. i-1], so "is v inside nums[l..r]" is
    // prefixCount[r + 1][v] - prefixCount[l][v] > 0. The minimum difference of a range is the smallest
    // gap between consecutive PRESENT values, found by scanning v = 1..100 in order.
    /**
     * time = O((N + Q) * V), V = 100
     * space = O(N * V)
     */
    public int[] minDifference(int[] nums, int[][] queries) {
        int n = nums.length;
        int[][] prefixCount = new int[n + 1][MAX_VALUE + 1];
        for (int i = 0; i < n; i++) {
            for (int v = 1; v <= MAX_VALUE; v++) {
                prefixCount[i + 1][v] = prefixCount[i][v];
            }
            prefixCount[i + 1][nums[i]]++;
        }

        int[] answer = new int[queries.length];
        for (int q = 0; q < queries.length; q++) {
            int left = queries[q][0];
            int right = queries[q][1];
            int previousPresent = -1;
            int best = Integer.MAX_VALUE;
            for (int v = 1; v <= MAX_VALUE; v++) {
                boolean present = prefixCount[right + 1][v] - prefixCount[left][v] > 0;
                if (!present) {
                    continue;
                }
                if (previousPresent != -1) {
                    best = Math.min(best, v - previousPresent);
                }
                previousPresent = v;
            }
            // fewer than two distinct values in the range
            answer[q] = (best == Integer.MAX_VALUE) ? -1 : best;
        }
        return answer;
    }

    // V1
    // IDEA: POSITION LISTS PER VALUE + BINARY SEARCH
    // keep the sorted indices where each value occurs; v is present in [l, r] iff the first index
    // >= l exists and is <= r. Uses O(N) memory instead of O(N * V).
    /**
     * time = O(N + Q * V * log N)
     * space = O(N)
     */
    public int[] minDifference_1(int[] nums, int[][] queries) {
        List<List<Integer>> positions = new ArrayList<>();
        for (int v = 0; v <= MAX_VALUE; v++) {
            positions.add(new ArrayList<>());
        }
        for (int i = 0; i < nums.length; i++) {
            positions.get(nums[i]).add(i);
        }

        int[] answer = new int[queries.length];
        for (int q = 0; q < queries.length; q++) {
            int left = queries[q][0];
            int right = queries[q][1];
            int previousPresent = -1;
            int best = Integer.MAX_VALUE;
            for (int v = 1; v <= MAX_VALUE; v++) {
                List<Integer> indices = positions.get(v);
                int first = lowerBound(indices, left);
                if (first == indices.size() || indices.get(first) > right) {
                    continue;
                }
                if (previousPresent != -1) {
                    best = Math.min(best, v - previousPresent);
                }
                previousPresent = v;
            }
            answer[q] = (best == Integer.MAX_VALUE) ? -1 : best;
        }
        return answer;
    }

    /** First index in the sorted list whose value is >= target (list size if none). */
    private int lowerBound(List<Integer> sorted, int target) {
        int low = 0;
        int high = sorted.size();
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (sorted.get(mid) < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }
}
