package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimum-absolute-sum-difference/description/

import java.util.Arrays;

/**
 * 1818. Minimum Absolute Sum Difference
 * Medium
 *
 * You are given two positive integer arrays nums1 and nums2, both of length n.
 *
 * The absolute sum difference of arrays nums1 and nums2 is defined as the sum of |nums1[i] - nums2[i]|
 * for each 0 <= i < n (0-indexed).
 *
 * You can replace at most one element of nums1 with any other element in nums1 to minimize the absolute
 * sum difference.
 *
 * Return the minimum absolute sum difference after replacing at most one element in the array nums1.
 * Since the answer may be large, return it modulo 10^9 + 7.
 *
 * |x| is defined as:
 *
 * x if x >= 0, or
 * -x if x < 0.
 *
 *
 * Example 1:
 *
 * Input: nums1 = [1,7,5], nums2 = [2,3,5]
 * Output: 3
 * Explanation: There are two possible optimal solutions:
 * - Replace the second element with the first: [1,7,5] => [1,1,5], or
 * - Replace the second element with the third: [1,7,5] => [1,5,5].
 * Both will yield an absolute sum difference of |1-2| + (|1-3| or |5-3|) + |5-5| = 3.
 *
 * Example 2:
 *
 * Input: nums1 = [2,4,6,8,10], nums2 = [2,4,6,8,10]
 * Output: 0
 * Explanation: nums1 is equal to nums2 so no replacement is needed. This will result in an
 * absolute sum difference of 0.
 *
 * Example 3:
 *
 * Input: nums1 = [1,10,4,4,2,7], nums2 = [9,3,5,1,7,4]
 * Output: 20
 * Explanation: Replace the first element with the second: [1,10,4,4,2,7] => [10,10,4,4,2,7].
 * This yields an absolute sum difference of |10-9| + |10-3| + |4-5| + |4-1| + |2-7| + |7-4| = 20
 *
 *
 * Constraints:
 *
 * n == nums1.length
 * n == nums2.length
 * 1 <= n <= 10^5
 * 1 <= nums1[i], nums2[i] <= 10^5
 *
 */
public class MinimumAbsoluteSumDifference {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: SORT + BINARY SEARCH FOR THE BEST SINGLE REPLACEMENT (MAXIMISE THE SAVING)
    /**
     *  base = sum |nums1[i] - nums2[i]|. Replacing nums1[i] by a value v from nums1 saves
     *  |nums1[i] - nums2[i]| - |v - nums2[i]|, so answer = base - the largest saving.
     *
     *  The best v for position i is the nums1 value closest to nums2[i]: sort a copy of nums1
     *  once, binary search nums2[i], and check the neighbour on each side.
     *
     *  Take the modulo only at the end (after subtracting the saving), or the subtraction can
     *  go negative; base fits in a long (10^5 * 10^5).
     */
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int minAbsoluteSumDiff(int[] nums1, int[] nums2) {
        int n = nums1.length;
        int[] sorted = nums1.clone();
        Arrays.sort(sorted);

        long base = 0;
        int bestSaving = 0;
        for (int i = 0; i < n; i++) {
            int oldDiff = Math.abs(nums1[i] - nums2[i]);
            base += oldDiff;

            // closest nums1 value to nums2[i]: the lower bound or the value just before it
            int position = lowerBound(sorted, nums2[i]);
            int newDiff = oldDiff;
            if (position < n) {
                newDiff = Math.min(newDiff, sorted[position] - nums2[i]);
            }
            if (position > 0) {
                newDiff = Math.min(newDiff, nums2[i] - sorted[position - 1]);
            }
            bestSaving = Math.max(bestSaving, oldDiff - newDiff);
        }
        return (int) ((base - bestSaving) % MOD);
    }

    /** First index whose value is >= target, or arr.length if none. */
    private int lowerBound(int[] arr, int target) {
        int left = 0;
        int right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] >= target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
}
