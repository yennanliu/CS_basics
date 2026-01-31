package LeetCodeJava.Array;

// https://leetcode.com/problems/concatenation-of-array/description/
/**
 *  1929. Concatenation of Array
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given an integer array nums of length n, you want to create an array ans of length 2n where ans[i] == nums[i] and ans[i + n] == nums[i] for 0 <= i < n (0-indexed).
 *
 * Specifically, ans is the concatenation of two nums arrays.
 *
 * Return the array ans.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,1]
 * Output: [1,2,1,1,2,1]
 * Explanation: The array ans is formed as follows:
 * - ans = [nums[0],nums[1],nums[2],nums[0],nums[1],nums[2]]
 * - ans = [1,2,1,1,2,1]
 * Example 2:
 *
 * Input: nums = [1,3,2,1]
 * Output: [1,3,2,1,1,3,2,1]
 * Explanation: The array ans is formed as follows:
 * - ans = [nums[0],nums[1],nums[2],nums[3],nums[0],nums[1],nums[2],nums[3]]
 * - ans = [1,3,2,1,1,3,2,1]
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 1 <= n <= 1000
 * 1 <= nums[i] <= 1000
 *
 *
 */
public class ConcatenationOfArray {

    // V0
//    public int[] getConcatenation(int[] nums) {
//
//    }

    // V1
    // https://leetcode.com/problems/concatenation-of-array/solutions/3612143/java-simple-solution-runtime-9417-memory-yeuo/
    public int[] getConcatenation_1(int[] nums) {
        int len = nums.length;
        int[] ans = new int[2 * len];
        for (int i = 0; i < len; i++) {
            ans[i] = nums[i];
            ans[i + len] = nums[i];
        }
        return ans;
    }


    // V2
    public int[] getConcatenation_2(int[] nums) {
        int[] ans = new int[2 * nums.length];
        for (int i = 0; i < nums.length; i++) {
            ans[i] = ans[i + nums.length] = nums[i];
        }

        return ans;
    }



}
