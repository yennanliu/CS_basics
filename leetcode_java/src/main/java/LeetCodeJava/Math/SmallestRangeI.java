package LeetCodeJava.Math;

// https://leetcode.com/problems/smallest-range-i/

/**
 *  908. Smallest Range I
 *  Easy
 *
 *  You are given an integer array nums and an integer k.
 *
 *  In one operation, you can choose any index i where 0 <= i < nums.length and
 *  change nums[i] to nums[i] + x where x is an integer from the range
 *  [-k, k]. You can apply this operation at most once for each index i.
 *
 *  The score of nums is the difference between the maximum and minimum elements
 *  in nums.
 *
 *  Return the minimum score of nums after applying the mentioned operation at
 *  most once for each index in it.
 *
 *  Example 1:
 *   Input: nums = [1], k = 0
 *   Output: 0
 *
 *  Example 2:
 *   Input: nums = [0,10], k = 2
 *   Output: 6
 *   Explanation: change to [2,8] -> score 6.
 *
 *  Example 3:
 *   Input: nums = [1,3,6], k = 3
 *   Output: 0
 *
 *  Constraints:
 *   - 1 <= nums.length <= 10^4
 *   - 0 <= nums[i] <= 10^4
 *   - 0 <= k <= 10^4
 */
public class SmallestRangeI {

    // V0
    // IDEA: MATH - pull the max down by k and push the min up by k,
    //       so the best possible score is max(0, max - min - 2k).
    /**
     * time = O(n)
     * space = O(1)
     */
    public int smallestRangeI(int[] nums, int k) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int x : nums) {
            min = Math.min(min, x);
            max = Math.max(max, x);
        }
        return Math.max(0, max - min - 2 * k);
    }
}
