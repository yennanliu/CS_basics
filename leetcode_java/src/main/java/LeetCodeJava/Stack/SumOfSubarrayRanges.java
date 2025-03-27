package LeetCodeJava.Stack;

// https://leetcode.com/problems/sum-of-subarray-ranges/

import java.util.Stack;

/**
 *  2104. Sum of Subarray Ranges
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * You are given an integer array nums. The range of a subarray of nums is the difference between the largest and smallest element in the subarray.
 *
 * Return the sum of all subarray ranges of nums.
 *
 * A subarray is a contiguous non-empty sequence of elements within an array.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3]
 * Output: 4
 * Explanation: The 6 subarrays of nums are the following:
 * [1], range = largest - smallest = 1 - 1 = 0
 * [2], range = 2 - 2 = 0
 * [3], range = 3 - 3 = 0
 * [1,2], range = 2 - 1 = 1
 * [2,3], range = 3 - 2 = 1
 * [1,2,3], range = 3 - 1 = 2
 * So the sum of all ranges is 0 + 0 + 0 + 1 + 1 + 2 = 4.
 * Example 2:
 *
 * Input: nums = [1,3,3]
 * Output: 4
 * Explanation: The 6 subarrays of nums are the following:
 * [1], range = largest - smallest = 1 - 1 = 0
 * [3], range = 3 - 3 = 0
 * [3], range = 3 - 3 = 0
 * [1,3], range = 3 - 1 = 2
 * [3,3], range = 3 - 3 = 0
 * [1,3,3], range = 3 - 1 = 2
 * So the sum of all ranges is 0 + 0 + 0 + 2 + 0 + 2 = 4.
 * Example 3:
 *
 * Input: nums = [4,-2,-3,4,1]
 * Output: 59
 * Explanation: The sum of all subarray ranges of nums is 59.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * -109 <= nums[i] <= 109
 *
 *
 * Follow-up: Could you find a solution with O(n) time complexity?
 *
 */
public class SumOfSubarrayRanges {
    // V0
//    public long subArrayRanges(int[] nums) {
//
//    }

    // V1-1
    // https://leetcode.com/problems/sum-of-subarray-ranges/editorial/
    // IDEA: 2 LOOPS
    public long subArrayRanges_1_1(int[] nums) {
        int n = nums.length;
        long answer = 0;

        for (int left = 0; left < n; ++left) {
            int minVal = nums[left], maxVal = nums[left];
            for (int right = left; right < n; ++right) {
                minVal = Math.min(minVal, nums[right]);
                maxVal = Math.max(maxVal, nums[right]);
                answer += maxVal - minVal;
            }
        }
        return answer;
    }


    // V1-2
    // https://leetcode.com/problems/sum-of-subarray-ranges/editorial/
    // IDEA:  Monotonic Stack
    public long subArrayRanges_1_2(int[] nums) {
        int n = nums.length;
        long answer = 0;
        Stack<Integer> stack = new Stack<>();

        // Find the sum of all the minimum.
        for (int right = 0; right <= n; ++right) {
            while (!stack.isEmpty() &&
                    (right == n || nums[stack.peek()] >= nums[right])) {
                int mid = stack.peek();
                stack.pop();
                int left = stack.isEmpty() ? -1 : stack.peek();
                answer -= (long) nums[mid] * (right - mid) * (mid - left);
            }
            stack.add(right);
        }

        // Find the sum of all the maximum.
        stack.clear();
        for (int right = 0; right <= n; ++right) {
            while (!stack.isEmpty() &&
                    (right == n || nums[stack.peek()] <= nums[right])) {
                int mid = stack.peek();
                stack.pop();
                int left = stack.isEmpty() ? -1 : stack.peek();
                answer += (long) nums[mid] * (right - mid) * (mid - left);
            }
            stack.add(right);
        }
        return answer;
    }

    // V2
    // https://leetcode.ca/2021-09-03-2104-Sum-of-Subarray-Ranges/
    public long subArrayRanges_2(int[] nums) {
        long ans = 0;
        int n = nums.length;
        for (int i = 0; i < n - 1; ++i) {
            int mi = nums[i], mx = nums[i];
            for (int j = i + 1; j < n; ++j) {
                mi = Math.min(mi, nums[j]);
                mx = Math.max(mx, nums[j]);
                ans += (mx - mi);
            }
        }
        return ans;
    }

}
