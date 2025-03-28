package LeetCodeJava.Stack;

// https://leetcode.com/problems/next-greater-element-ii/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * 503. Next Greater Element II
 * Solved
 * Medium
 * Topics
 * Companies
 * Given a circular integer array nums (i.e., the next element of nums[nums.length - 1] is nums[0]), return the next greater number for every element in nums.
 *
 * The next greater number of a number x is the first greater number to its traversing-order next in the array, which means you could search circularly to find its next greater number. If it doesn't exist, return -1 for this number.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,1]
 * Output: [2,-1,2]
 * Explanation: The first 1's next greater number is 2;
 * The number 2 can't find next greater number.
 * The second 1's next greater number needs to search circularly, which is also 2.
 * Example 2:
 *
 * Input: nums = [1,2,3,4,3]
 * Output: [2,3,4,-1,4]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 104
 * -109 <= nums[i] <= 109
 *
 *
 */
public class NextGreaterElement_II {

    // V0
//    public int[] nextGreaterElements(int[] nums) {
//
//    }

    // V0-1
    // IDEA: BRUTE FORCE + DOUBLE STACK
    public int[] nextGreaterElements_0_1(int[] nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }
        // `duplicate` nums
        int k = 0;

        int[] res = new int[nums.length];
        Arrays.fill(res, -1);

        List<Integer> keys = new ArrayList<>();
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < nums.length; i++) {
                int val = nums[i];
                keys.add(val);
            }
        }

//        // System.out.println(">>> nums2 = " + nums2);
//        System.out.println(">>> keys = " + keys);
//        System.out.println(">>> res (before) = " + res);

        Stack<Integer> st = new Stack<>();
        // ???
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < keys.size(); j++) {
                int k_ = keys.get(j);
                if (k_ > nums[i]) {
                    res[i] = k_;
                    break;
                }
            }
        }

        return res;
    }

    // V1-1
    // https://leetcode.com/problems/next-greater-element-ii/editorial/
    // IDEA: BRUTE FORCE
    public int[] nextGreaterElements_1_1(int[] nums) {
        int[] res = new int[nums.length];
        int[] doublenums = new int[nums.length * 2];
        System.arraycopy(nums, 0, doublenums, 0, nums.length);
        System.arraycopy(nums, 0, doublenums, nums.length, nums.length);
        for (int i = 0; i < nums.length; i++) {
            res[i] = -1;
            for (int j = i + 1; j < doublenums.length; j++) {
                if (doublenums[j] > doublenums[i]) {
                    res[i] = doublenums[j];
                    break;
                }
            }
        }
        return res;
    }

    // V1-2
    // https://leetcode.com/problems/next-greater-element-ii/editorial/
    // IDEA: BETTER BRUTE FORCE
    public int[] nextGreaterElements_1_2(int[] nums) {
        int[] res = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            res[i] = -1;
            for (int j = 1; j < nums.length; j++) {
                if (nums[(i + j) % nums.length] > nums[i]) {
                    res[i] = nums[(i + j) % nums.length];
                    break;
                }
            }
        }
        return res;
    }

    // V1-3
    // https://leetcode.com/problems/next-greater-element-ii/editorial/
    // IDEA: STACK
    public int[] nextGreaterElements_1_3(int[] nums) {
        int[] res = new int[nums.length];
        Stack<Integer> stack = new Stack<>();
        for (int i = 2 * nums.length - 1; i >= 0; --i) {
            while (!stack.empty() && nums[stack.peek()] <= nums[i % nums.length]) {
                stack.pop();
            }
            res[i % nums.length] = stack.empty() ? -1 : nums[stack.peek()];
            stack.push(i % nums.length);
        }
        return res;
    }

    // V2

}
