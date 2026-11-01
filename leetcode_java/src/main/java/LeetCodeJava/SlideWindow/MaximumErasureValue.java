package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/maximum-erasure-value/description/

import java.util.HashSet;
import java.util.Set;

/**
 * 1695. Maximum Erasure Value
 * Medium
 *
 * You are given an array of positive integers nums and want to erase a subarray containing unique
 * elements. The score you get by erasing the subarray is equal to the sum of its elements.
 *
 * Return the maximum score you can get by erasing exactly one subarray.
 *
 * An array b is called to be a subarray of a if it forms a contiguous subsequence of a, that is, if it
 * is equal to a[l],a[l+1],...,a[r] for some (l,r).
 *
 *
 * Example 1:
 *
 * Input: nums = [4,2,4,5,6]
 * Output: 17
 * Explanation: The optimal subarray here is [2,4,5,6].
 *
 * Example 2:
 *
 * Input: nums = [5,2,1,2,5,2,1,2,5]
 * Output: 8
 * Explanation: The optimal subarray here is [5,2,1] or [1,2,5].
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^4
 *
 */
public class MaximumErasureValue {

    // V0
    // IDEA: SLIDING WINDOW / TWO POINTERS (longest-unique-window, scored by sum)
    //       "longest substring without repeating characters", but with sum
    //       instead of length. keep [left, right] all distinct: before adding
    //       nums[right], shrink from the left until it is no longer inside.
    //       values are positive, so the max is reached on a maximal unique
    //       window, which the sweep visits.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maximumUniqueSubarray(int[] nums) {
        Set<Integer> inWindow = new HashSet<>();
        int left = 0;
        int windowSum = 0;
        int best = 0;
        for (int value : nums) {
            // evict from the left until the duplicate of `value` is gone
            while (inWindow.contains(value)) {
                inWindow.remove(nums[left]);
                windowSum -= nums[left];
                left++;
            }
            inWindow.add(value);
            windowSum += value;
            best = Math.max(best, windowSum);
        }
        return best;
    }
}
