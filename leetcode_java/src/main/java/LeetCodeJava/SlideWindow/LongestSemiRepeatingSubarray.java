package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/longest-semi-repeating-subarray/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 3641. Longest Semi-Repeating Subarray
 * Medium
 *
 * You are given an integer array nums of length n and an integer k.
 *
 * A semi-repeating subarray is a contiguous subarray in which at most k elements repeat (i.e.,
 * appear more than once).
 *
 * Return the length of the longest semi-repeating subarray in nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,1,2,3,4], k = 2
 * Output: 6
 *
 * Example 2:
 *
 * Input: nums = [1,1,1,1,1], k = 4
 * Output: 5
 *
 * Example 3:
 *
 * Input: nums = [1,1,1,1,1], k = 0
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^5
 * 0 <= k <= nums.length
 *
 */
public class LongestSemiRepeatingSubarray {

    // V0
    // IDEA: SLIDING WINDOW ON THE NUMBER OF DISTINCT VALUES THAT OCCUR 2+ TIMES
    //  it counts distinct repeating values, not surplus copies ([1,1,1] has one); growing the
    //  window can only raise that number and shrinking can only lower it, so two pointers work.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int longestSubarray(int[] nums, int k) {
        // value -> occurrences in the window
        Map<Integer, Integer> window = new HashMap<>();
        int repeating = 0;
        int left = 0;
        int best = 0;

        for (int right = 0; right < nums.length; right++) {
            int count = window.getOrDefault(nums[right], 0) + 1;
            window.put(nums[right], count);
            // this value has just started repeating
            if (count == 2) {
                repeating++;
            }
            while (repeating > k) {
                int leaving = nums[left];
                int leavingCount = window.get(leaving);
                // this value stops repeating once it drops back to one copy
                if (leavingCount == 2) {
                    repeating--;
                }
                window.put(leaving, leavingCount - 1);
                left++;
            }
            best = Math.max(best, right - left + 1);
        }
        return best;
    }
}
