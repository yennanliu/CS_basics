package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/closest-subsequence-sum/description/

import java.util.Arrays;

/**
 * 1755. Closest Subsequence Sum
 * Hard
 *
 * You are given an integer array nums and an integer goal.
 *
 * You want to choose a subsequence of nums such that the sum of its elements is the closest
 * possible to goal. That is, if the sum of the subsequence's elements is sum, then you want to
 * minimize the absolute difference abs(sum - goal).
 *
 * Return the minimum possible value of abs(sum - goal).
 *
 * Note that a subsequence of an array is an array formed by removing some elements (possibly all or
 * none) of the original array.
 *
 *
 * Example 1:
 *
 * Input: nums = [5,-7,3,5], goal = 6
 * Output: 0
 * Explanation: Choose the whole array as a subsequence, with a sum of 6.
 * This is equal to the goal, so the absolute difference is 0.
 *
 * Example 2:
 *
 * Input: nums = [7,-9,15,-2], goal = -5
 * Output: 1
 * Explanation: Choose the subsequence [7,-9,-2], with a sum of -4.
 * The absolute difference is abs(-4 - (-5)) = abs(1) = 1, which is the minimum.
 *
 * Example 3:
 *
 * Input: nums = [1,2,3], goal = -7
 * Output: 7
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 40
 * -10^7 <= nums[i] <= 10^7
 * -10^9 <= goal <= 10^9
 *
 */
public class ClosestSubsequenceSum {

    // V0
    // IDEA: MEET IN THE MIDDLE + BINARY SEARCH
    // 2^40 subsequences is hopeless, but each half has at most 2^20 subset sums. Any subsequence sum
    // is leftSum + rightSum, so for each leftSum binary-search the sorted right sums for the value
    // closest to goal - leftSum (check both neighbours of the insertion point).
    /**
     * time = O(2^(N/2) * N)
     * space = O(2^(N/2))
     */
    public int minAbsDifference(int[] nums, int goal) {
        int half = nums.length / 2;
        long[] leftSums = subsetSums(nums, 0, half);
        long[] rightSums = subsetSums(nums, half, nums.length);
        Arrays.sort(rightSums);

        long best = Math.abs((long) goal);
        for (long leftSum : leftSums) {
            long need = goal - leftSum;
            // first index whose right sum is >= need
            int index = lowerBound(rightSums, need);
            if (index < rightSums.length) {
                best = Math.min(best, Math.abs(rightSums[index] - need));
            }
            if (index > 0) {
                best = Math.min(best, Math.abs(rightSums[index - 1] - need));
            }
            if (best == 0) {
                return 0;
            }
        }
        return (int) best;
    }

    // V1
    // IDEA: MEET IN THE MIDDLE + TWO POINTERS
    // sort both halves; walk left sums ascending and right sums descending. An overshoot can only
    // shrink with a smaller right sum, an undershoot can only grow with a bigger left sum.
    /**
     * time = O(2^(N/2) * N)
     * space = O(2^(N/2))
     */
    public int minAbsDifference_1(int[] nums, int goal) {
        int half = nums.length / 2;
        long[] leftSums = subsetSums(nums, 0, half);
        long[] rightSums = subsetSums(nums, half, nums.length);
        Arrays.sort(leftSums);
        Arrays.sort(rightSums);

        long best = Math.abs((long) goal);
        int left = 0;
        int right = rightSums.length - 1;
        while (left < leftSums.length && right >= 0) {
            long total = leftSums[left] + rightSums[right];
            best = Math.min(best, Math.abs(total - goal));
            if (best == 0) {
                return 0;
            }
            if (total > goal) {
                right--;
            } else {
                left++;
            }
        }
        return (int) best;
    }

    /** Every subset sum of nums[from, to), built by doubling the list once per element. */
    private long[] subsetSums(int[] nums, int from, int to) {
        int count = to - from;
        // 2^count subsets in total
        long[] sums = new long[1 << count];
        int size = 1; // sums[0] = 0 is the empty subset
        for (int i = from; i < to; i++) {
            // every existing subset either skips nums[i] (kept) or takes it (appended)
            for (int j = 0; j < size; j++) {
                sums[size + j] = sums[j] + nums[i];
            }
            size *= 2;
        }
        return sums;
    }

    /** First index in the sorted array whose value is >= target (array length if none). */
    private int lowerBound(long[] sorted, long target) {
        int left = 0;
        int right = sorted.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (sorted[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }
}
