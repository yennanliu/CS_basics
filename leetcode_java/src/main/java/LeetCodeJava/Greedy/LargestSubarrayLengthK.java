package LeetCodeJava.Greedy;

// https://leetcode.com/problems/largest-subarray-length-k/description/

import java.util.Arrays;

/**
 * 1708. Largest Subarray Length K
 * Easy
 *
 * An array A is larger than some array B if for the first index i where
 * A[i] != B[i], A[i] > B[i].
 *
 * For example, consider 0-indexing:
 *
 * [1,3,2,4] > [1,2,2,4], since at index 1, 3 > 2.
 * [1,4,4,4] < [2,1,1,1], since at index 0, 1 < 2.
 *
 * A subarray is a contiguous subsequence of the array.
 *
 * Given an integer array nums of distinct integers, return the largest subarray of
 * nums of length k.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,4,5,2,3], k = 3
 * Output: [5,2,3]
 *
 * Example 2:
 *
 * Input: nums = [1,4,5,2,3], k = 4
 * Output: [4,5,2,3]
 *
 * Example 3:
 *
 * Input: nums = [1,4,5,2,3], k = 1
 * Output: [5]
 *
 *
 * Constraints:
 *
 * 1 <= k <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 * All the integers of nums are unique.
 *
 * Follow up: What if the integers in nums are not distinct?
 *
 */
public class LargestSubarrayLengthK {

    // V0
    // IDEA: GREEDY (VALUES ARE DISTINCT -> THE FIRST ELEMENT DECIDES EVERYTHING)
    //       a length-k subarray starts in [0, n - k]; the one with the largest
    //       first element wins, and distinct values mean there is never a tie.
    /**
     * time = O(N)
     * space = O(1) excluding the output
     */
    public int[] largestSubarray(int[] nums, int k) {
        int n = nums.length;
        int bestStart = 0;
        for (int i = 1; i <= n - k; i++) {
            if (nums[i] > nums[bestStart]) {
                bestStart = i;
            }
        }
        return Arrays.copyOfRange(nums, bestStart, bestStart + k);
    }
}
