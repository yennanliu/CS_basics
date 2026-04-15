package LeetCodeJava.Stack;

// https://leetcode.com/problems/132-pattern/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 *  456. 132 Pattern
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given an array of n integers nums, a 132 pattern is a subsequence of three integers nums[i], nums[j] and nums[k] such that i < j < k and nums[i] < nums[k] < nums[j].
 *
 * Return true if there is a 132 pattern in nums, otherwise, return false.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4]
 * Output: false
 * Explanation: There is no 132 pattern in the sequence.
 * Example 2:
 *
 * Input: nums = [3,1,4,2]
 * Output: true
 * Explanation: There is a 132 pattern in the sequence: [1, 4, 2].
 * Example 3:
 *
 * Input: nums = [-1,3,2,0]
 * Output: true
 * Explanation: There are three 132 patterns in the sequence: [-1, 3, 2], [-1, 3, 0] and [-1, 2, 0].
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 1 <= n <= 2 * 105
 * -109 <= nums[i] <= 109
 *
 *
 */
public class L132Pattern {

    // V0
//    public boolean find132pattern(int[] nums) {
//
//    }

    // V1-1
    // IDEA: BRUTE FORCE (TLE)
    // https://leetcode.com/problems/132-pattern/editorial/
    public boolean find132pattern_1_1(int[] nums) {
        for (int i = 0; i < nums.length - 2; i++) {
            for (int j = i + 1; j < nums.length - 1; j++) {
                for (int k = j + 1; k < nums.length; k++) {
                    if (nums[k] > nums[i] && nums[j] > nums[k])
                        return true;
                }
            }
        }
        return false;
    }


    // V1-2
    // IDEA: Better Brute Force (TLE)
    // https://leetcode.com/problems/132-pattern/editorial/
    public boolean find132pattern_1_2(int[] nums) {
        int min_i = Integer.MAX_VALUE;
        for (int j = 0; j < nums.length - 1; j++) {
            min_i = Math.min(min_i, nums[j]);
            for (int k = j + 1; k < nums.length; k++) {
                if (nums[k] < nums[j] && min_i < nums[k])
                    return true;
            }
        }
        return false;
    }


    // V1-3
    // IDEA: Searching Intervals (TLE)
    // https://leetcode.com/problems/132-pattern/editorial/
    public boolean find132pattern_1_3(int[] nums) {
        List<int[]> intervals = new ArrayList<>();
        int i = 1, s = 0;
        while (i < nums.length) {
            if (nums[i] < nums[i - 1]) {
                if (s < i - 1)
                    intervals.add(
                            new int[] { nums[s], nums[i - 1] });
                s = i;
            }
            for (int[] a : intervals)
                if (nums[i] > a[0] && nums[i] < a[1])
                    return true;
            i++;
        }
        return false;
    }



    // V1-4
    // IDEA: STACK
    // https://leetcode.com/problems/132-pattern/editorial/
    public boolean find132pattern_1_4(int[] nums) {
        if (nums.length < 3)
            return false;
        Stack<Integer> stack = new Stack<>();
        int[] min = new int[nums.length];
        min[0] = nums[0];
        for (int i = 1; i < nums.length; i++)
            min[i] = Math.min(
                    min[i - 1],
                    nums[i]);
        for (int j = nums.length - 1; j >= 0; j--) {
            if (nums[j] > min[j]) {
                while (!stack.isEmpty() && stack.peek() <= min[j])
                    stack.pop();
                if (!stack.isEmpty() && stack.peek() < nums[j])
                    return true;
                stack.push(nums[j]);
            }
        }
        return false;
    }


    // V1-5
    // IDEA: BINARY SEARCH
    // https://leetcode.com/problems/132-pattern/editorial/
    public boolean find132pattern_1_5(int[] nums) {
        if (nums.length < 3)
            return false;
        int[] min = new int[nums.length];
        min[0] = nums[0];
        for (int i = 1; i < nums.length; i++)
            min[i] = Math.min(
                    min[i - 1],
                    nums[i]);
        for (int j = nums.length - 1, k = nums.length; j >= 0; j--) {
            if (nums[j] > min[j]) {
                k = Arrays.binarySearch(nums, k, nums.length, min[j] + 1);
                if (k < 0)
                    k = -1 - k;
                if (k < nums.length && nums[k] < nums[j])
                    return true;
                nums[--k] = nums[j];
            }
        }
        return false;
    }



    // V1-6
    // IDEA: Using Array as a Stack
    // https://leetcode.com/problems/132-pattern/editorial/
    public boolean find132pattern_1_6(int[] nums) {
        if (nums.length < 3)
            return false;
        int[] min = new int[nums.length];
        min[0] = nums[0];
        for (int i = 1; i < nums.length; i++)
            min[i] = Math.min(
                    min[i - 1],
                    nums[i]);
        for (int j = nums.length - 1, k = nums.length; j >= 0; j--) {
            if (nums[j] > min[j]) {
                while (k < nums.length && nums[k] <= min[j])
                    k++;
                if (k < nums.length && nums[k] < nums[j])
                    return true;
                nums[--k] = nums[j];
            }
        }
        return false;
    }





    // V2





}
