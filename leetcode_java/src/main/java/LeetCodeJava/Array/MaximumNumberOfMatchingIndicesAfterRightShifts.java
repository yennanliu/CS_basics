package LeetCodeJava.Array;

// https://leetcode.com/problems/maximum-number-of-matching-indices-after-right-shifts/description/

/**
 * 3400. Maximum Number of Matching Indices After Right Shifts
 * Medium
 *
 * You are given two integer arrays, nums1 and nums2, of the same length.
 *
 * An index i is considered matching if nums1[i] == nums2[i].
 *
 * Return the maximum number of matching indices after performing any number of
 * right shifts on nums1.
 *
 * A right shift is defined as shifting the element at index i to index (i + 1) %
 * n, for all indices.
 *
 * Example 1:
 *
 * Input: nums1 = [3,1,2,3,1,2], nums2 = [1,2,3,1,2,3]
 *
 * Output: 6
 *
 * Explanation:
 *
 * If we right shift nums1 2 times, it becomes [1, 2, 3, 1, 2, 3]. Every index
 * matches, so the output is 6.
 *
 * Example 2:
 *
 * Input: nums1 = [1,4,2,5,3,1], nums2 = [2,3,1,2,4,6]
 *
 * Output: 3
 *
 * Explanation:
 *
 * If we right shift nums1 3 times, it becomes [5, 3, 1, 1, 4, 2]. Indices 1, 2,
 * and 4 match, so the output is 3.
 *
 * Constraints:
 *
 * nums1.length == nums2.length
 * 1 <= nums1.length, nums2.length <= 3000
 * 1 <= nums1[i], nums2[i] <= 10^9
 *
 */
public class MaximumNumberOfMatchingIndicesAfterRightShifts {

    // V0
    // IDEA: TRY EVERY ROTATION (only n distinct right shifts; n <= 3000 so O(n^2) fits)
    /**
     * time = O(N^2)
     * space = O(1)
     */
    public int maximumMatchingIndices(int[] nums1, int[] nums2) {
        int n = nums1.length;
        int best = 0;

        for (int shift = 0; shift < n; shift++) {
            int matches = 0;
            for (int i = 0; i < n; i++) {
                // after `shift` right shifts, index i holds the element that was at (i - shift)
                int source = (i - shift + n) % n;
                if (nums1[source] == nums2[i]) {
                    matches++;
                }
            }
            best = Math.max(best, matches);
        }
        return best;
    }
}
