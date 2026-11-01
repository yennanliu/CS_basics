package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-elements-to-add-to-form-a-given-sum/description/

/**
 * 1785. Minimum Elements to Add to Form a Given Sum
 * Medium
 *
 * You are given an integer array nums and two integers limit and goal. The array
 * nums has an interesting property that abs(nums[i]) <= limit.
 *
 * Return the minimum number of elements you need to add to make the sum of the
 * array equal to goal. The array must maintain its property that
 * abs(nums[i]) <= limit.
 *
 * Note that abs(x) equals x if x >= 0, and -x otherwise.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,-1,1], limit = 3, goal = -4
 * Output: 2
 * Explanation: You can add -2 and -3, then the sum of the array will be 1 - 1 + 1 - 2 - 3 = -4.
 *
 * Example 2:
 *
 * Input: nums = [1,-10,9,1], limit = 100, goal = 0
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= limit <= 10^6
 * -limit <= nums[i] <= limit
 * -10^9 <= goal <= 10^9
 *
 */
public class MinimumElementsToAddToFormAGivenSum {

    // V0
    // IDEA: GREEDY (every added element moves the sum by at most `limit` -> ceil(gap / limit))
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minElements(int[] nums, int limit, int goal) {
        // the sum can reach 10^5 * 10^6 = 10^11, so it needs a long
        long sum = 0;
        for (int v : nums) {
            sum += v;
        }

        long gap = Math.abs(sum - goal);
        // each new element closes up to `limit` of the gap; the last one covers the remainder
        return (int) ((gap + limit - 1) / limit);
    }
}
