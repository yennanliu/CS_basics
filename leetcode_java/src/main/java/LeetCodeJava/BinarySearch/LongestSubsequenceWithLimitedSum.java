package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/longest-subsequence-with-limited-sum/description/

import java.util.Arrays;

/**
 * 2389. Longest Subsequence With Limited Sum
 * Easy
 *
 * You are given an integer array nums of length n, and an integer array queries of length m.
 *
 * Return an array answer of length m where answer[i] is the maximum size of a subsequence that you can
 * take from nums such that the sum of its elements is less than or equal to queries[i].
 *
 * A subsequence is an array that can be derived from another array by deleting some or no elements
 * without changing the order of the remaining elements.
 *
 *
 * Example 1:
 *
 * Input: nums = [4,5,2,1], queries = [3,10,21]
 * Output: [2,3,4]
 * Explanation: We answer the queries as follows:
 * - The subsequence [2,1] has a sum less than or equal to 3. It can be proven that 2 is the maximum size
 *   of such a subsequence, so answer[0] = 2.
 * - The subsequence [4,5,1] has a sum less than or equal to 10. It can be proven that 3 is the maximum
 *   size of such a subsequence, so answer[1] = 3.
 * - The subsequence [4,5,2,1] has a sum less than or equal to 21. It can be proven that 4 is the maximum
 *   size of such a subsequence, so answer[2] = 4.
 *
 * Example 2:
 *
 * Input: nums = [2,3,4,5], queries = [1]
 * Output: [0]
 * Explanation: The empty subsequence is the only subsequence that has a sum less than or equal to 1, so
 * answer[0] = 0.
 *
 *
 * Constraints:
 *
 * n == nums.length
 * m == queries.length
 * 1 <= n, m <= 1000
 * 1 <= nums[i], queries[i] <= 10^6
 *
 */
public class LongestSubsequenceWithLimitedSum {

    // V0
    // IDEA: ORDER DOES NOT MATTER -> SORT ASCENDING, PREFIX SUM, BINARY SEARCH
    /**
     *  Any subset is a subsequence, so to fit the most elements under a budget take the
     *  smallest ones. After sorting, prefix[s - 1] is the minimum sum of a size-s subsequence.
     *  Prefix sums are increasing, so each query is one upper bound: how many prefix sums
     *  are <= the budget.
     */
    /**
     * time = O(N log N + M log N)
     * space = O(N)
     */
    public int[] answerQueries(int[] nums, int[] queries) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);

        // prefix[i] = sum of the i + 1 smallest values (at most 10^9, fits in a long safely)
        long[] prefix = new long[sorted.length];
        long running = 0;
        for (int i = 0; i < sorted.length; i++) {
            running += sorted[i];
            prefix[i] = running;
        }

        int[] answer = new int[queries.length];
        for (int q = 0; q < queries.length; q++) {
            answer[q] = countAtMost(prefix, queries[q]);
        }
        return answer;
    }

    /** Number of prefix sums that are <= budget (upper bound). */
    private int countAtMost(long[] prefix, long budget) {
        int left = 0;
        int right = prefix.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (prefix[mid] > budget) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
}
