package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/single-number/

/**
 *  136. Single Number
 *  Easy
 *
 *  Given a non-empty array of integers nums, every element appears twice except
 *  for one. Find that single one.
 *
 *  You must implement a solution with a linear runtime complexity and use only
 *  constant extra space.
 *
 *  Example 1:
 *   Input: nums = [2,2,1]
 *   Output: 1
 *
 *  Example 2:
 *   Input: nums = [4,1,2,1,2]
 *   Output: 4
 *
 *  Example 3:
 *   Input: nums = [1]
 *   Output: 1
 *
 *  Constraints:
 *   1 <= nums.length <= 3 * 10^4
 *   -3 * 10^4 <= nums[i] <= 3 * 10^4
 *   Each element appears twice except for one element which appears only once.
 */
public class SingleNumber {

    // V0
    // IDEA: XOR. x ^ x == 0 and x ^ 0 == x, so XOR-ing everything cancels the
    //       pairs and leaves the unique value.
    /**
     * time = O(n)
     * space = O(1)
     */
    public int singleNumber(int[] nums) {
        int res = 0;
        for (int num : nums) {
            res ^= num;
        }
        return res;
    }
}
