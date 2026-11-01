package LeetCodeJava.Array;

// https://leetcode.com/problems/smallest-index-with-equal-value/description/

/**
 * 2057. Smallest Index With Equal Value
 * Easy
 *
 * Given a 0-indexed integer array nums, return the smallest index i of nums such that i mod 10 == nums[i], or -1 if such index does not exist.
 *
 * x mod y denotes the remainder when x is divided by y.
 *
 *
 * Example 1:
 *
 * Input: nums = [0,1,2]
 * Output: 0
 * Explanation:
 * i=0: 0 mod 10 = 0 == nums[0].
 * i=1: 1 mod 10 = 1 == nums[1].
 * i=2: 2 mod 10 = 2 == nums[2].
 * All indices have i mod 10 == nums[i], so we return the smallest index 0.
 *
 * Example 2:
 *
 * Input: nums = [4,3,2,1]
 * Output: 2
 * Explanation:
 * i=0: 0 mod 10 = 0 != nums[0].
 * i=1: 1 mod 10 = 1 != nums[1].
 * i=2: 2 mod 10 = 2 == nums[2].
 * i=3: 3 mod 10 = 3 != nums[3].
 * 2 is the only index which has i mod 10 == nums[i].
 *
 * Example 3:
 *
 * Input: nums = [1,2,3,4,5,6,7,8,9,0]
 * Output: -1
 * Explanation: No index satisfies i mod 10 == nums[i].
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * 0 <= nums[i] <= 9
 *
 */
public class SmallestIndexWithEqualValue {

    // V0
    // IDEA: LINEAR SCAN -- THE FIRST HIT IS THE SMALLEST INDEX
    /**
     * time = O(N)
     * space = O(1)
     */
    public int smallestEqual(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            if (i % 10 == nums[i]) {
                return i;
            }
        }
        return -1;
    }
}
