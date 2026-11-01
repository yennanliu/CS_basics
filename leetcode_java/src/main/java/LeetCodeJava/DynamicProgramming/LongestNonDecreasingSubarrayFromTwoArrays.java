package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/longest-non-decreasing-subarray-from-two-arrays/description/

/**
 * 2771. Longest Non-decreasing Subarray From Two Arrays
 * Medium
 *
 * You are given two 0-indexed integer arrays nums1 and nums2 of length n.
 *
 * Let's define another 0-indexed integer array, nums3, of length n. For each index i in the range
 * [0, n - 1], you can assign either nums1[i] or nums2[i] to nums3[i].
 *
 * Your task is to maximize the length of the longest non-decreasing subarray in nums3 by choosing
 * its values optimally.
 *
 * Return an integer representing the length of the longest non-decreasing subarray in nums3.
 *
 * Note: A subarray is a contiguous non-empty sequence of elements within an array.
 *
 *
 * Example 1:
 *
 * Input: nums1 = [2,3,1], nums2 = [1,2,1]
 * Output: 2
 * Explanation: One way to construct nums3 is [nums1[0], nums2[1], nums2[2]] => [2,2,1].
 * The subarray [2,2] forms a non-decreasing subarray of length 2.
 *
 * Example 2:
 *
 * Input: nums1 = [1,3,2,1], nums2 = [2,2,3,4]
 * Output: 4
 * Explanation: One way to construct nums3 is [nums1[0], nums2[1], nums2[2], nums2[3]] => [1,2,3,4].
 *
 * Example 3:
 *
 * Input: nums1 = [1,1], nums2 = [2,2]
 * Output: 2
 * Explanation: One way to construct nums3 is [nums1[0], nums1[1]] => [1,1].
 *
 *
 * Constraints:
 *
 * 1 <= nums1.length == nums2.length == n <= 10^5
 * 1 <= nums1[i], nums2[i] <= 10^9
 *
 */
public class LongestNonDecreasingSubarrayFromTwoArrays {

    // V0
    // IDEA: 2-STATE DP - LONGEST RUN ENDING AT i WITH nums1[i] CHOSEN / WITH nums2[i] CHOSEN
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxNonDecreasingLength(int[] nums1, int[] nums2) {
        int n = nums1.length;

        // endWith1 / endWith2 = longest non-decreasing run ending at i-1 that picked nums1 / nums2 there
        int endWith1 = 1;
        int endWith2 = 1;
        int result = 1;

        for (int i = 1; i < n; i++) {
            // a run of length 1 (just this element) is always possible
            int nextWith1 = 1;
            int nextWith2 = 1;

            // pick nums1[i]: extend whichever previous choice it does not drop below
            if (nums1[i] >= nums1[i - 1]) {
                nextWith1 = Math.max(nextWith1, endWith1 + 1);
            }
            if (nums1[i] >= nums2[i - 1]) {
                nextWith1 = Math.max(nextWith1, endWith2 + 1);
            }

            // pick nums2[i]
            if (nums2[i] >= nums1[i - 1]) {
                nextWith2 = Math.max(nextWith2, endWith1 + 1);
            }
            if (nums2[i] >= nums2[i - 1]) {
                nextWith2 = Math.max(nextWith2, endWith2 + 1);
            }

            endWith1 = nextWith1;
            endWith2 = nextWith2;
            result = Math.max(result, Math.max(endWith1, endWith2));
        }
        return result;
    }
}
