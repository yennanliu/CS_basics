package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimum-operations-to-make-all-array-elements-equal/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 2602. Minimum Operations to Make All Array Elements Equal
 * Medium
 *
 * You are given an array nums consisting of positive integers.
 *
 * You are also given an integer array queries of size m. For the ith query, you want to make all of the elements of nums equal to queries[i]. You can perform the following operation on the array any number of times:
 *
 * - Increase or decrease an element of the array by 1.
 *
 * Return an array answer of size m where answer[i] is the minimum number of operations to make all elements of nums equal to queries[i].
 *
 * Note that after each query the array is reset to its original state.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,1,6,8], queries = [1,5]
 * Output: [14,10]
 * Explanation: For the first query we can do the following operations:
 * - Decrease nums[0] 2 times, so that nums = [1,1,6,8].
 * - Decrease nums[2] 5 times, so that nums = [1,1,1,8].
 * - Decrease nums[3] 7 times, so that nums = [1,1,1,1].
 * So the total number of operations for the first query is 2 + 5 + 7 = 14.
 * For the second query we can do the following operations:
 * - Increase nums[0] 2 times, so that nums = [5,1,6,8].
 * - Increase nums[1] 4 times, so that nums = [5,5,6,8].
 * - Decrease nums[2] 1 time, so that nums = [5,5,5,8].
 * - Decrease nums[3] 3 times, so that nums = [5,5,5,5].
 * So the total number of operations for the second query is 2 + 4 + 1 + 3 = 10.
 *
 * Example 2:
 *
 * Input: nums = [2,9,6,3], queries = [10]
 * Output: [20]
 * Explanation: We can increase each value in the array to 10. The total number of operations will be 8 + 1 + 4 + 7 = 20.
 *
 *
 * Constraints:
 *
 * n == nums.length
 * m == queries.length
 * 1 <= n, m <= 10^5
 * 1 <= nums[i], queries[i] <= 10^9
 *
 */
public class MinimumOperationsToMakeAllArrayElementsEqual {

    // V0
    // IDEA: SORT + PREFIX SUM + BINARY SEARCH PER QUERY
    // the cost for query x is sum(|nums[i] - x|). After sorting, split around x:
    //   below = # of elements < x   -> raising them costs  x * below - prefix[below]
    //   upTo  = # of elements <= x  -> lowering the rest costs (prefix[n] - prefix[upTo]) - (n - upTo) * x
    // elements equal to x cost 0 and fall in neither bucket.
    /**
     * time = O((N + M) log N)
     * space = O(N)
     */
    public List<Long> minOperations(int[] nums, int[] queries) {
        int n = nums.length;
        int[] sorted = nums.clone();
        Arrays.sort(sorted);

        // prefix[i] = sum of the i smallest elements
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + sorted[i];
        }

        List<Long> result = new ArrayList<>();
        for (int query : queries) {
            long x = query;
            int below = firstIndexAtLeast(sorted, query);       // sorted[0..below) < x
            int upTo = firstIndexAtLeast(sorted, query + 1);    // sorted[upTo..n) > x
            long raiseCost = x * below - prefix[below];
            long lowerCost = (prefix[n] - prefix[upTo]) - (n - upTo) * x;
            result.add(raiseCost + lowerCost);
        }
        return result;
    }

    /** First index whose value is >= target (n if none). */
    private int firstIndexAtLeast(int[] sorted, int target) {
        int left = 0;
        int right = sorted.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (sorted[mid] >= target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
}
