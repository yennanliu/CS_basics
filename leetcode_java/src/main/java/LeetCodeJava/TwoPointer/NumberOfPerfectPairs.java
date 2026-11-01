package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/number-of-perfect-pairs/description/

import java.util.Arrays;

/**
 * 3649. Number of Perfect Pairs
 * Medium
 *
 * You are given an integer array nums.
 *
 * A pair of indices (i, j) is called perfect if the following conditions are satisfied:
 *
 * i < j
 *
 * Let a = nums[i], b = nums[j]. Then:
 *
 * min(|a - b|, |a + b|) <= min(|a|, |b|)
 *
 * max(|a - b|, |a + b|) >= max(|a|, |b|)
 *
 * Return the number of distinct perfect pairs.
 *
 * Note: The absolute value |x| refers to the non-negative value of x.
 *
 *
 * Example 1:
 *
 * Input: nums = [0,1,2,3]
 * Output: 2
 *
 * Example 2:
 *
 * Input: nums = [-3,2,-1,4]
 * Output: 4
 *
 * Example 3:
 *
 * Input: nums = [1,10,100,1000]
 * Output: 0
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 10^5
 * -10^9 <= nums[i] <= 10^9
 *
 */
public class NumberOfPerfectPairs {

    // V0
    // IDEA: THE SIGNS CANCEL, SO THE CONDITION IS max(|a|,|b|) <= 2 * min(|a|,|b|) -> SORT + TWO POINTERS
    //  with x = min(|a|,|b|), y = max(|a|,|b|), {|a - b|, |a + b|} is always {y - x, y + x};
    //  y - x <= x gives y <= 2x, and y + x >= y always holds.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long perfectPairs(int[] nums) {
        int n = nums.length;
        // |nums[i]| reaches 10^9 and 2 * |x| would overflow int, so work in long
        long[] magnitudes = new long[n];
        for (int i = 0; i < n; i++) {
            magnitudes[i] = Math.abs((long) nums[i]);
        }
        Arrays.sort(magnitudes);

        long count = 0;
        int left = 0;
        for (int right = 0; right < n; right++) {
            // smallest left whose magnitude is at least half of magnitudes[right]
            while (magnitudes[right] > 2 * magnitudes[left]) {
                left++;
            }
            // right pairs with every index in [left, right)
            count += right - left;
        }
        return count;
    }
}
