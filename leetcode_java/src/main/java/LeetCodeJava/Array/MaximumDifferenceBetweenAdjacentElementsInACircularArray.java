package LeetCodeJava.Array;

// https://leetcode.com/problems/maximum-difference-between-adjacent-elements-in-a-circular-array/description/

/**
 * 3423. Maximum Difference Between Adjacent Elements in a Circular Array
 * Easy
 *
 * Given a circular array nums, find the maximum absolute difference between
 * adjacent elements.
 *
 * Note: In a circular array, the first and last elements are adjacent.
 *
 * Example 1:
 *
 * Input: nums = [1,2,4]
 *
 * Output: 3
 *
 * Explanation:
 *
 * Because nums is circular, nums[0] and nums[2] are adjacent. They have the
 * maximum absolute difference of |4 - 1| = 3.
 *
 * Example 2:
 *
 * Input: nums = [-5,-10,-5]
 *
 * Output: 5
 *
 * Explanation:
 *
 * The adjacent elements nums[0] and nums[1] have the maximum absolute difference
 * of |-5 - (-10)| = 5.
 *
 * Constraints:
 *
 * 2 <= nums.length <= 100
 * -100 <= nums[i] <= 100
 *
 */
public class MaximumDifferenceBetweenAdjacentElementsInACircularArray {

    // V0
    // IDEA: SCAN ALL n ADJACENT PAIRS, WRAPPING WITH (i + 1) % n
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxAdjacentDistance(int[] nums) {
        int n = nums.length;
        int best = 0;
        for (int i = 0; i < n; i++) {
            // (i + 1) % n makes the last element's neighbour the first one
            int next = nums[(i + 1) % n];
            best = Math.max(best, Math.abs(nums[i] - next));
        }
        return best;
    }
}
