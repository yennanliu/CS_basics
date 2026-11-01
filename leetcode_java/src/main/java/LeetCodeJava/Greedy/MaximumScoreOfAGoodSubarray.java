package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-score-of-a-good-subarray/description/

import java.util.*;

/**
 * 1793. Maximum Score of a Good Subarray
 * Hard
 *
 * You are given an array of integers nums (0-indexed) and an integer k.
 *
 * The score of a subarray (i, j) is defined as
 * min(nums[i], nums[i+1], ..., nums[j]) * (j - i + 1). A good subarray is a
 * subarray where i <= k <= j.
 *
 * Return the maximum possible score of a good subarray.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,4,3,7,4,5], k = 3
 * Output: 15
 * Explanation: The optimal subarray is (1, 5) with a score of min(4,3,7,4,5) * (5-1+1) = 3 * 5 = 15.
 *
 * Example 2:
 *
 * Input: nums = [5,5,4,5,4,1,1,1], k = 0
 * Output: 20
 * Explanation: The optimal subarray is (0, 4) with a score of min(5,5,4,5,4) * (4-0+1) = 4 * 5 = 20.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 2 * 10^4
 * 0 <= k < nums.length
 *
 */
public class MaximumScoreOfAGoodSubarray {

    // V0
    // IDEA: TWO POINTERS EXPANDING OUT OF k (greedily swallow the larger neighbour)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maximumScore(int[] nums, int k) {
        int n = nums.length;
        int left = k;
        int right = k;
        int windowMin = nums[k];
        int best = windowMin;

        // every good subarray contains k: grow the window [left, right] one step at a time
        while (left > 0 || right < n - 1) {
            if (left == 0) {
                right++;
            } else if (right == n - 1) {
                left--;
            } else if (nums[left - 1] > nums[right + 1]) {
                // taking the larger neighbour keeps the window minimum as high as possible
                left--;
            } else {
                right++;
            }

            windowMin = Math.min(windowMin, Math.min(nums[left], nums[right]));
            best = Math.max(best, windowMin * (right - left + 1));
        }

        return best;
    }

    // V1
    // IDEA: MONOTONIC STACK (for each index, the widest window where it is the minimum)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maximumScore_1(int[] nums, int k) {
        int n = nums.length;
        // leftSmaller[i] = index of the nearest element to the left that is < nums[i], or -1
        int[] leftSmaller = new int[n];
        // rightSmaller[i] = index of the nearest element to the right that is < nums[i], or n
        int[] rightSmaller = new int[n];

        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] >= nums[i]) {
                stack.pop();
            }
            leftSmaller[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(i);
        }

        stack.clear();
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && nums[stack.peek()] >= nums[i]) {
                stack.pop();
            }
            rightSmaller[i] = stack.isEmpty() ? n : stack.peek();
            stack.push(i);
        }

        int best = 0;
        for (int i = 0; i < n; i++) {
            // nums[i] is the minimum of (leftSmaller[i], rightSmaller[i]) exclusive
            int start = leftSmaller[i] + 1;
            int end = rightSmaller[i] - 1;
            // only windows containing k are "good"
            if (start <= k && k <= end) {
                best = Math.max(best, nums[i] * (end - start + 1));
            }
        }
        return best;
    }
}
