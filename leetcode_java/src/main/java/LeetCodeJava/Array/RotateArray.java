package LeetCodeJava.Array;

// https://leetcode.com/problems/rotate-array/description/

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * 189. Rotate Array
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * Given an integer array nums, rotate the array to the right by k steps, where k is non-negative.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4,5,6,7], k = 3
 * Output: [5,6,7,1,2,3,4]
 * Explanation:
 * rotate 1 steps to the right: [7,1,2,3,4,5,6]
 * rotate 2 steps to the right: [6,7,1,2,3,4,5]
 * rotate 3 steps to the right: [5,6,7,1,2,3,4]
 * Example 2:
 *
 * Input: nums = [-1,-100,3,99], k = 2
 * Output: [3,99,-1,-100]
 * Explanation:
 * rotate 1 steps to the right: [99,-1,-100,3]
 * rotate 2 steps to the right: [3,99,-1,-100]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * -231 <= nums[i] <= 231 - 1
 * 0 <= k <= 105
 *
 *
 * Follow up:
 *
 * Try to come up with as many solutions as you can. There are at least three different ways to solve this problem.
 * Could you do it in-place with O(1) extra space?
 *
 *
 */
public class RotateArray {

    // V0
    // IDEA: DEQEUE
    /**
     * time = O(N + K)
     * space = O(N)
     */
    public void rotate(int[] nums, int k) {
        // edge
        if (nums == null || nums.length == 0) {
            return;
        }

        Deque<Integer> dq = new LinkedList<>();
        for (int n : nums) {
            dq.add(n);
        }

        int i = 0;
        for (i = 0; i < k; i++) {
            /**
             *  NOTE !!! below
             *
             *   we get dequeue last element,
             *   then append it to first idx
             */
            dq.addFirst(dq.pollLast());
        }

       // System.out.println(">>> dq = " + dq);

        int j = 0;
        while (!dq.isEmpty()) {
            nums[j] = dq.pollFirst();
            j += 1;
        }
    }

    // V0-1
    // IDEA: ARRAY OP (TLE)
    /**
     * time = O(N*K)
     * space = O(N)
     */
    public void rotate_0_1(int[] nums, int k) {
        // edge
        if (nums == null || nums.length == 0) {
            return;
        }

        List<Integer> list = new ArrayList<>();
        for (int n : nums) {
            list.add(n);
        }

        int i = 0;
        for (i = 0; i < k; i++) {
            // int last = nums[nums.length - 1];
            int last = list.get(list.size() - 1);
            list.remove(list.size() - 1);
            list.add(0, last);
        }

        for (int j = 0; j < list.size(); j++) {
            nums[j] = list.get(j);
        }
    }

    // V1
    // IDEA: rotate ( `%` op)  (gpt)
    /**
     * time = O(N)
     * space = O(1)
     */
    public void rotate_1(int[] nums, int k) {
        // Edge case
        if (nums == null || nums.length == 0) {
            return;
        }

        // Adjust k in case it's greater than the array length
        k = k % nums.length;
        if (k == 0) {
            return;
        }

        // Reverse the entire array
        reverse(nums, 0, nums.length - 1);

        // Reverse the first k elements
        reverse(nums, 0, k - 1);

        // Reverse the remaining elements
        reverse(nums, k, nums.length - 1);
    }

    private void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start++;
            end--;
        }
    }

    // V2

}
