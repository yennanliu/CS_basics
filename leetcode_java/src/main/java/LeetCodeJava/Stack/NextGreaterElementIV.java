package LeetCodeJava.Stack;

// https://leetcode.com/problems/next-greater-element-iv/

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 *  2454. Next Greater Element IV
 *  Hard
 *
 *  You are given a 0-indexed array of non-negative integers nums. For each
 *  integer in nums, you must find its respective second greater integer.
 *
 *  The second greater integer of nums[i] is nums[j] such that:
 *    j > i
 *    nums[j] > nums[i]
 *    There exists exactly one index k such that nums[k] > nums[i] and i < k < j.
 *
 *  If there is no such nums[j], the second greater integer is considered to be -1.
 *
 *  Return an integer array answer, where answer[i] is the second greater integer
 *  of nums[i].
 *
 *  Example 1:
 *    Input: nums = [2,4,0,9,6]
 *    Output: [9,6,6,-1,-1]
 *
 *  Example 2:
 *    Input: nums = [3,3]
 *    Output: [-1,-1]
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    0 <= nums[i] <= 10^9
 */
public class NextGreaterElementIV {

    // V0
    // IDEA: TWO MONOTONIC STACKS - "WAITING FOR THE FIRST" AND "WAITING FOR THE SECOND"
    //       an index lives in `first` until something bigger appears; at that
    //       moment it has met greater #1 and GRADUATES to `second`. the next time
    //       something bigger appears it has met greater #2 and is answered.
    //       per element x at index i:
    //         1. resolve everything in `second` that x exceeds (these get answers)
    //         2. move everything in `first` that x exceeds over to `second`
    //         3. push i onto `first`
    //       step 2 must PRESERVE the decreasing order, hence popping into a temp
    //       buffer and pushing it back reversed - otherwise `second` would stop
    //       being monotonic and step 1 could miss entries.
    //       each index moves at most twice -> the whole sweep is linear.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[] secondGreaterElement(int[] nums) {
        int n = nums.length;
        int[] res = new int[n];
        Arrays.fill(res, -1);

        Deque<Integer> first = new ArrayDeque<>();   // seen no greater element yet
        Deque<Integer> second = new ArrayDeque<>();  // seen exactly one greater element
        int[] promoted = new int[n];

        for (int i = 0; i < n; i++) {
            int x = nums[i];

            while (!second.isEmpty() && nums[second.peek()] < x) {
                res[second.pop()] = x;
            }

            int cnt = 0;
            while (!first.isEmpty() && nums[first.peek()] < x) {
                promoted[cnt++] = first.pop();
            }
            // push back in reverse order so `second` stays decreasing
            for (int k = cnt - 1; k >= 0; k--) {
                second.push(promoted[k]);
            }

            first.push(i);
        }
        return res;
    }
}
