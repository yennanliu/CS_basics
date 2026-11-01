package LeetCodeJava.Array;

// https://leetcode.com/problems/longest-strictly-increasing-or-strictly-decreasing-subarray/description/

/**
 * 3105. Longest Strictly Increasing or Strictly Decreasing Subarray
 * Easy
 *
 * You are given an array of integers nums. Return the length of the longest subarray of nums which is either strictly increasing or strictly decreasing.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,4,3,3,2]
 * Output: 2
 * Explanation:
 * The strictly increasing subarrays of nums are [1], [2], [3], [3], [4], and [1,4].
 * The strictly decreasing subarrays of nums are [1], [2], [3], [3], [4], [3,2], and [4,3].
 * Hence, we return 2.
 *
 * Example 2:
 *
 * Input: nums = [3,3,3,3]
 * Output: 1
 * Explanation:
 * The strictly increasing subarrays of nums are [3], [3], [3], and [3].
 * The strictly decreasing subarrays of nums are [3], [3], [3], and [3].
 * Hence, we return 1.
 *
 * Example 3:
 *
 * Input: nums = [3,2,1]
 * Output: 3
 * Explanation:
 * The strictly increasing subarrays of nums are [3], [2], and [1].
 * The strictly decreasing subarrays of nums are [3], [2], [1], [3,2], [2,1], and [3,2,1].
 * Hence, we return 3.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 50
 * 1 <= nums[i] <= 50
 *
 */
public class LongestStrictlyIncreasingOrStrictlyDecreasingSubarray {

    // V0
    // IDEA: TWO RUNNING STREAK COUNTERS, ONE PER DIRECTION
    // increasing / decreasing = length of the strict run ending at i in each
    // direction; equal neighbours reset both, which is why [3,3,3,3] answers 1.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int longestMonotonicSubarray(int[] nums) {
        int increasing = 1;
        int decreasing = 1;
        int longest = 1;

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > nums[i - 1]) {
                increasing++;
            } else {
                increasing = 1;
            }

            if (nums[i] < nums[i - 1]) {
                decreasing++;
            } else {
                decreasing = 1;
            }

            longest = Math.max(longest, Math.max(increasing, decreasing));
        }
        return longest;
    }
}
