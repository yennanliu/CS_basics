package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/longest-subarray-of-1s-after-deleting-one-element/description/

/**
 * 1493. Longest Subarray of 1's After Deleting One Element
 * Medium
 *
 * Given a binary array nums, you should delete one element from it.
 *
 * Return the size of the longest non-empty subarray containing only 1's in the resulting array. Return 0 if there is no such subarray.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,0,1]
 * Output: 3
 * Explanation: After deleting the number in position 2, [1,1,1] contains 3 numbers with value of 1's.
 *
 * Example 2:
 *
 * Input: nums = [0,1,1,1,0,1,1,0,1]
 * Output: 5
 * Explanation: After deleting the number in position 4, [0,1,1,1,1,1,0,1] longest subarray with value of 1's is [1,1,1,1,1].
 *
 * Example 3:
 *
 * Input: nums = [1,1,1]
 * Output: 2
 * Explanation: You must delete one element.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * nums[i] is either 0 or 1.
 *
 */
public class LongestSubarrayOf1sAfterDeletingOneElement {

    // V0
    // IDEA: SLIDING WINDOW (longest window holding at most one 0)
    //       deleting one element from a window of 1's plus at most one 0 leaves
    //       (window length - 1) ones. so answer = longest such window - 1.
    //       recording `right - left` (not + 1) already subtracts the deleted
    //       element, so the all-ones case correctly gives n - 1.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int longestSubarray(int[] nums) {
        int best = 0;
        int zeros = 0;
        int left = 0;
        for (int right = 0; right < nums.length; right++) {
            if (nums[right] == 0) {
                zeros++;
            }
            // at most one 0 may stay in the window
            while (zeros > 1) {
                if (nums[left] == 0) {
                    zeros--;
                }
                left++;
            }
            // window length minus the one deleted element
            best = Math.max(best, right - left);
        }
        return best;
    }
}
