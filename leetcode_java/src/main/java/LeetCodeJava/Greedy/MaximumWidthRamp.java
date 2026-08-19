package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-width-ramp/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  962. Maximum Width Ramp
 *  Medium
 *
 *  A ramp in an integer array nums is a pair (i, j) for which i < j and nums[i] <= nums[j].
 *  The width of such a ramp is j - i.
 *
 *  Given an integer array nums, return the maximum width of a ramp in nums.
 *  If there is no ramp in nums, return 0.
 *
 *  Example 1:
 *    Input: nums = [6,0,8,2,1,5]
 *    Output: 4    (i = 1, j = 5: nums[1] = 0 <= nums[5] = 5)
 *
 *  Example 2:
 *    Input: nums = [9,8,1,0,1,9,4,0,4,1]
 *    Output: 7    (i = 2, j = 9)
 *
 *  Constraints:
 *    2 <= nums.length <= 5 * 10^4
 *    0 <= nums[i] <= 5 * 10^4
 */
public class MaximumWidthRamp {

    // V0
    // IDEA: monotonic stack. Only strictly-decreasing prefix values can ever be a useful
    //       left end; push those indices, then scan j from the right and pop every left
    //       end that this j can serve.
    /**
     * time = O(n)
     * space = O(n)
     */
    public int maxWidthRamp(int[] nums) {
        int n = nums.length;
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            if (stack.isEmpty() || nums[stack.peek()] > nums[i]) {
                stack.push(i);
            }
        }

        int res = 0;
        for (int j = n - 1; j >= 0; j--) {
            while (!stack.isEmpty() && nums[stack.peek()] <= nums[j]) {
                res = Math.max(res, j - stack.pop());
            }
        }
        return res;
    }
}
