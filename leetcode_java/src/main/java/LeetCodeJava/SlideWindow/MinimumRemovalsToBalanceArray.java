package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/minimum-removals-to-balance-array/description/

import java.util.Arrays;

/**
 * 3634. Minimum Removals to Balance Array
 * Medium
 *
 * You are given an integer array nums and an integer k.
 *
 * An array is considered balanced if the value of its maximum element is at most k times the
 * minimum element.
 *
 * You may remove any number of elements from nums without making it empty.
 *
 * Return the minimum number of elements to remove so that the remaining array is balanced.
 *
 * Note: An array of size 1 is considered balanced as its maximum and minimum are equal, and the
 * condition always holds true.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,1,5], k = 2
 * Output: 1
 *
 * Example 2:
 *
 * Input: nums = [1,6,2,9], k = 3
 * Output: 2
 *
 * Example 3:
 *
 * Input: nums = [4,6], k = 2
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 * 1 <= k <= 10^5
 *
 */
public class MinimumRemovalsToBalanceArray {

    // V0
    // IDEA: SORT + SLIDING WINDOW — KEEP THE LONGEST SORTED WINDOW WITH max <= k * min
    //  only the kept min and max matter, and keeping everything between them is free,
    //  so the best kept set is a contiguous block of the sorted array.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int minRemoval(int[] nums, int k) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        int n = sorted.length;

        int longest = 0;
        int left = 0;
        for (int right = 0; right < n; right++) {
            // k * min reaches 10^14, so compare in long
            while (sorted[right] > (long) k * sorted[left]) {
                left++;
            }
            longest = Math.max(longest, right - left + 1);
        }
        return n - longest;
    }
}
