package LeetCodeJava.Array;

// https://leetcode.com/problems/separate-the-digits-in-an-array/description/

import java.util.ArrayList;
import java.util.List;

/**
 *  2553. Separate the Digits in an Array
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given an array of positive integers nums, return an array answer that consists of the digits of each integer in nums after separating them in the same order they appear in nums.
 *
 * To separate the digits of an integer is to get all the digits it has in the same order.
 *
 * For example, for the integer 10921, the separation of its digits is [1,0,9,2,1].
 *
 *
 * Example 1:
 *
 * Input: nums = [13,25,83,77]
 * Output: [1,3,2,5,8,3,7,7]
 * Explanation:
 * - The separation of 13 is [1,3].
 * - The separation of 25 is [2,5].
 * - The separation of 83 is [8,3].
 * - The separation of 77 is [7,7].
 * answer = [1,3,2,5,8,3,7,7]. Note that answer contains the separations in the same order.
 * Example 2:
 *
 * Input: nums = [7,1,3,9]
 * Output: [7,1,3,9]
 * Explanation: The separation of each integer in nums is itself.
 * answer = [7,1,3,9].
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 1 <= nums[i] <= 105
 *
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 */
public class SeparateTheDigitsInAnArray {

    // V0
    public int[] separateDigits(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new int[] {}; // ??
        }
        List<String> cache = splitHelper(nums);
        int[] res = new int[cache.size()];
        for (int i = 0; i < cache.size(); i++) {
            res[i] = Integer.parseInt(cache.get(i));
        }
        //System.out.println(">>> cache = " + cache + " , res = " + res);
        return res;
    }

    private List<String> splitHelper(int[] nums) {
        List<String> res = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return res;
        }

        for (int x : nums) {
            String str = String.valueOf(x);
            for (String y : str.split("")) {
                res.add(y);
            }
        }

        return res;
    }

    // V1
    // https://leetcode.com/problems/separate-the-digits-in-an-array/solutions/7227360/easy-code-for-solve-this-problem-by-4_k4-s6dh/
    public int[] separateDigits_1(int[] nums) {
        List<Integer> list = new ArrayList<>();
        for (int k : nums) {
            String s = String.valueOf(k);
            for (char ch : s.toCharArray()) {
                list.add(Character.getNumericValue(ch));
            }
        }

        int res[] = new int[list.size()];
        int index = 0;
        for (int k : list) {
            res[index++] = k;
        }
        return res;
    }

    // V2


}
