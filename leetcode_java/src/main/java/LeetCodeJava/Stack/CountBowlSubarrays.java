package LeetCodeJava.Stack;

// https://leetcode.com/problems/count-bowl-subarrays/

/**
 *  3676. Count Bowl Subarrays
 *  Medium
 *
 *  You are given an integer array nums with distinct elements.
 *
 *  A subarray nums[l...r] of nums is called a bowl if:
 *   - the subarray has length at least 3 (r - l + 1 >= 3)
 *   - the minimum of its two ends is strictly greater than the maximum of all
 *     elements in between, i.e.
 *     min(nums[l], nums[r]) > max(nums[l + 1], ..., nums[r - 1])
 *
 *  Return the number of bowl subarrays in nums.
 *
 *  Example 1:
 *    Input: nums = [2,5,3,1,4]
 *    Output: 2
 *    Explanation: [3,1,4] and [5,3,1,4]
 *
 *  Example 2:
 *    Input: nums = [5,1,2,3,4]
 *    Output: 3
 *    Explanation: [5,1,2], [5,1,2,3] and [5,1,2,3,4]
 *
 *  Constraints:
 *    3 <= nums.length <= 10^5
 *    1 <= nums[i] <= 10^9
 *    nums consists of distinct elements.
 */
public class CountBowlSubarrays {

    // V0
    // IDEA: MONOTONIC STACK - COUNT "MUTUALLY VISIBLE" INDEX PAIRS
    //       The bowl condition says l and r can "see each other over" the
    //       valley between them -> the classic visible-pair relation.
    //       Keeping a strictly decreasing stack of indices, index j sees:
    //         * every index it pops,
    //         * plus the single index left on top afterwards.
    //       Nothing else is visible, so each pair is emitted exactly once.
    //       Finally keep only the pairs at distance >= 2 (a bowl needs at
    //       least one element strictly between its two ends).
    /**
     * time = O(N)
     * space = O(N)
     */
    public long bowlSubarrays(int[] nums) {
        int n = nums.length;
        long res = 0;
        int[] st = new int[n];
        int top = -1;

        for (int j = 0; j < n; j++) {
            int x = nums[j];
            while (top >= 0 && nums[st[top]] < x) {
                int i = st[top--];
                if (j - i > 1) {
                    res++;
                }
            }
            if (top >= 0 && j - st[top] > 1) {
                res++;
            }
            st[++top] = j;
        }
        return res;
    }
}
