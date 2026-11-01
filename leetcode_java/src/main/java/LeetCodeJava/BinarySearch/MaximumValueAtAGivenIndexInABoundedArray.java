package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/maximum-value-at-a-given-index-in-a-bounded-array/description/

/**
 * 1802. Maximum Value at a Given Index in a Bounded Array
 * Medium
 *
 * You are given three positive integers: n, index, and maxSum. You want to construct an array nums
 * (0-indexed) that satisfies the following conditions:
 *
 * nums.length == n
 * nums[i] is a positive integer where 0 <= i < n.
 * abs(nums[i] - nums[i+1]) <= 1 where 0 <= i < n-1.
 * The sum of all the elements of nums does not exceed maxSum.
 * nums[index] is maximized.
 *
 * Return nums[index] of the constructed array.
 *
 * Note that abs(x) equals x if x >= 0, and -x otherwise.
 *
 *
 * Example 1:
 *
 * Input: n = 4, index = 2,  maxSum = 6
 * Output: 2
 * Explanation: nums = [1,2,2,1] is one array that satisfies all the conditions.
 * There are no arrays that satisfy all the conditions and have nums[2] == 3, so 2 is the maximum nums[2].
 *
 * Example 2:
 *
 * Input: n = 6, index = 1,  maxSum = 10
 * Output: 3
 *
 *
 * Constraints:
 *
 * 1 <= n <= maxSum <= 10^9
 * 0 <= index < n
 *
 */
public class MaximumValueAtAGivenIndexInABoundedArray {

    // V0
    // IDEA: BINARY SEARCH ON THE ANSWER (FEASIBILITY IS MONOTONE)
    /**
     *  If nums[index] = v is feasible, every smaller value is too -> binary search v in
     *  [1, maxSum].
     *
     *  The cheapest array with nums[index] = v steps down by 1 away from index and then
     *  flattens at 1:  ... 1, 1, 2, 3, v, 3, 2, 1, 1 ...
     *
     *  The left side (index cells, top value v - 1) excludes index; the right side
     *  (n - index cells, top value v) includes it.
     */
    /**
     * time = O(log(maxSum))
     * space = O(1)
     */
    public int maxValue(int n, int index, int maxSum) {
        long left = 1;
        long right = maxSum;
        while (left < right) {
            // upper mid, so "left = mid" always makes progress
            long mid = left + (right - left + 1) / 2;
            long total = sideCost(mid - 1, index) + sideCost(mid, n - index);
            if (total <= maxSum) {
                // mid fits -> the answer is mid or larger
                left = mid;
            } else {
                right = mid - 1;
            }
        }
        return (int) left;
    }

    /** Sum of `cells` values that start at top and step down by 1, floored at 1. */
    private long sideCost(long top, long cells) {
        if (top >= cells) {
            // a full arithmetic run: top, top - 1, ..., top - cells + 1
            long bottom = top - cells + 1;
            return (top + bottom) * cells / 2;
        }
        // run down to 1, then pad the remaining cells with 1s
        return top * (top + 1) / 2 + (cells - top);
    }
}
