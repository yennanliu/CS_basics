package LeetCodeJava.Math;

// https://leetcode.com/problems/optimal-division/

/**
 *  553. Optimal Division
 *  Medium
 *
 *  You are given an integer array nums. The adjacent integers in nums will perform the
 *  float division.
 *    - For example, for nums = [2,3,4], we will evaluate the expression "2/3/4".
 *
 *  However, you can add any number of parenthesis at any position to change the priority
 *  of operations. You want to add these parentheses such that the value of the expression
 *  after the evaluation is maximum.
 *
 *  Return the corresponding expression that has the maximum value in string format.
 *  Note: your expression should not contain redundant parenthesis.
 *
 *  Example 1:
 *    Input: nums = [1000,100,10,2]
 *    Output: "1000/(100/10/2)"
 *
 *  Example 2:
 *    Input: nums = [2,3,4]
 *    Output: "2/(3/4)"
 *
 *  Example 3:
 *    Input: nums = [2]
 *    Output: "2"
 *
 *  Constraints:
 *    1 <= nums.length <= 10
 *    2 <= nums[i] <= 1000
 *    There is only one optimal division for the given input.
 */
public class OptimalDivision {

    // V0
    // IDEA: math - nums[0] is always in the numerator and nums[1] always in the denominator,
    //       so the best we can do is push every remaining term into the numerator, i.e.
    //       nums[0] / (nums[1] / nums[2] / ... / nums[n-1])
    /**
     * time = O(n)
     * space = O(n)
     */
    public String optimalDivision(int[] nums) {
        if (nums == null || nums.length == 0) {
            return "";
        }
        if (nums.length == 1) {
            return String.valueOf(nums[0]);
        }
        if (nums.length == 2) {
            return nums[0] + "/" + nums[1];
        }
        StringBuilder sb = new StringBuilder();
        sb.append(nums[0]).append("/(").append(nums[1]);
        for (int i = 2; i < nums.length; i++) {
            sb.append("/").append(nums[i]);
        }
        sb.append(")");
        return sb.toString();
    }
}
