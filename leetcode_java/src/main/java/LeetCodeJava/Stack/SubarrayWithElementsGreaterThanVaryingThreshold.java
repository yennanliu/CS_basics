package LeetCodeJava.Stack;

// https://leetcode.com/problems/subarray-with-elements-greater-than-varying-threshold/

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 *  2334. Subarray With Elements Greater Than Varying Threshold
 *  Hard
 *
 *  You are given an integer array nums and an integer threshold.
 *
 *  Find any subarray of nums of length k such that every element in the subarray
 *  is greater than threshold / k.
 *
 *  Return the size of any such subarray. If there is no such subarray, return -1.
 *
 *  Example 1:
 *    Input: nums = [1,3,4,3,1], threshold = 6
 *    Output: 3
 *    Explanation: the subarray [3,4,3] has size 3, and every element is greater
 *                 than 6 / 3 = 2.
 *
 *  Example 2:
 *    Input: nums = [6,5,6,5,8], threshold = 7
 *    Output: 1
 *    Explanation: [8] has size 1 and 8 > 7 / 1. (2, 3, 4, 5 are also accepted.)
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    1 <= nums[i], threshold <= 10^9
 */
public class SubarrayWithElementsGreaterThanVaryingThreshold {

    // V0
    // IDEA: "EVERY ELEMENT > threshold / k" IS REALLY "THE MINIMUM > threshold / k"
    //       so it is enough to consider, for each index i, the WIDEST window in
    //       which nums[i] is the minimum - bounded by the previous and next
    //       strictly smaller elements. a monotonic stack finds both in one pass.
    //       with that window of length  width = right[i] - left[i] - 1, the
    //       condition  nums[i] > threshold / width  is checked as
    //       nums[i] * width > threshold  (long math) to stay in exact integers.
    //       testing only these maximal windows is sufficient: any valid subarray
    //       is contained in the maximal window of its own minimum, and shrinking
    //       a window only makes the required bound larger.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int validSubarraySize(int[] nums, int threshold) {
        int n = nums.length;

        int[] left = new int[n];    // previous strictly smaller
        Arrays.fill(left, -1);
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] >= nums[i]) {
                stack.pop();
            }
            left[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(i);
        }

        int[] right = new int[n];   // next strictly smaller
        Arrays.fill(right, n);
        stack.clear();
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && nums[stack.peek()] >= nums[i]) {
                stack.pop();
            }
            right[i] = stack.isEmpty() ? n : stack.peek();
            stack.push(i);
        }

        for (int i = 0; i < n; i++) {
            int width = right[i] - left[i] - 1;
            if ((long) nums[i] * width > threshold) {
                return width;
            }
        }
        return -1;
    }
}
