package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/longest-nice-subarray/description/

/**
 * 2401. Longest Nice Subarray
 * Medium
 *
 * You are given an array nums consisting of positive integers.
 *
 * We call a subarray of nums nice if the bitwise AND of every pair of elements that are in different positions in the subarray is equal to 0.
 *
 * Return the length of the longest nice subarray.
 *
 * A subarray is a contiguous part of an array.
 *
 * Note that subarrays of length 1 are always considered nice.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,8,48,10]
 * Output: 3
 * Explanation: The longest nice subarray is [3,8,48]. This subarray satisfies the conditions:
 * - 3 AND 8 = 0.
 * - 3 AND 48 = 0.
 * - 8 AND 48 = 0.
 * It can be proven that no longer nice subarray exists.
 *
 * Example 2:
 *
 * Input: nums = [3,1,5,11,13]
 * Output: 1
 * Explanation: The only possible nice subarrays are of length 1.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 *
 */
public class LongestNiceSubarray {

    // V0
    // IDEA: SLIDING WINDOW + OR MASK ("every pairwise AND is 0" == "no two share a set bit")
    //  so the window state is the OR of its elements: a new x fits iff (mask & x) == 0.
    //  while x collides, drop elements from the left and clear their bits from the mask
    //  (XOR clears them exactly, because the bits inside the window are disjoint).
    //  NOTE: values are < 2^30, so a window never holds more than 30 elements.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int longestNiceSubarray(int[] nums) {
        int best = 0;
        // OR of every element inside nums[left .. right - 1]
        int mask = 0;
        int left = 0;
        for (int right = 0; right < nums.length; right++) {
            int x = nums[right];
            // x shares a bit with something in the window -> shrink from the left
            while ((mask & x) != 0) {
                // remove nums[left]'s bits; they are disjoint from the rest, so XOR clears only them
                mask ^= nums[left];
                left++;
            }
            mask |= x;
            best = Math.max(best, right - left + 1);
        }
        return best;
    }
}
