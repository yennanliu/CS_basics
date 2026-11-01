package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/minimum-value-to-get-positive-step-by-step-sum/description/

/**
 * 1413. Minimum Value to Get Positive Step by Step Sum
 * Easy
 *
 * Given an array of integers nums, you start with an initial positive value
 * startValue.
 *
 * In each iteration, you calculate the step by step sum of startValue plus
 * elements in nums (from left to right).
 *
 * Return the minimum positive value of startValue such that the step by step sum
 * is never less than 1.
 *
 *
 * Example 1:
 *
 * Input: nums = [-3,2,-3,4,2]
 * Output: 5
 * Explanation: If you choose startValue = 4, in the third iteration your step by
 * step sum is less than 1.
 *
 * Example 2:
 *
 * Input: nums = [1,2]
 * Output: 1
 * Explanation: Minimum start value should be positive.
 *
 * Example 3:
 *
 * Input: nums = [1,-2,-3]
 * Output: 5
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * -100 <= nums[i] <= 100
 *
 */
public class MinimumValueToGetPositiveStepByStepSum {

    // V0
    // IDEA: PREFIX SUM — startValue + prefix[i] >= 1 for every i
    //       -> startValue >= 1 - min(prefix), and it must stay positive
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minStartValue(int[] nums) {
        int prefixSum = 0;
        // lowest prefix seen so far; start at 0 so the answer is never below 1
        int lowestPrefix = 0;

        for (int num : nums) {
            prefixSum += num;
            lowestPrefix = Math.min(lowestPrefix, prefixSum);
        }

        // the smallest start that lifts the lowest prefix up to exactly 1
        return 1 - lowestPrefix;
    }
}
