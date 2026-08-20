package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/shortest-subarray-with-or-at-least-k-i/

/**
 *  3095. Shortest Subarray With OR at Least K I
 *  Easy
 *
 *  You are given an array nums of non-negative integers and an integer k.
 *
 *  An array is called special if the bitwise OR of all of its elements is at
 *  least k.
 *
 *  Return the length of the shortest special non-empty subarray of nums, or
 *  return -1 if no special subarray exists.
 *
 *  Example 1:
 *    Input: nums = [1,2,3], k = 2
 *    Output: 1
 *    Explanation: the subarray [3] has OR value 3.
 *
 *  Example 2:
 *    Input: nums = [2,1,8], k = 10
 *    Output: 3
 *    Explanation: the subarray [2,1,8] has OR value 11.
 *
 *  Example 3:
 *    Input: nums = [1,2], k = 0
 *    Output: 1
 *
 *  Constraints:
 *    1 <= nums.length <= 50
 *    0 <= nums[i] <= 50
 *    0 <= k < 64
 */
public class ShortestSubarrayWithOrAtLeastKI {

    // V0
    // IDEA: n <= 50 -> GROW EVERY SUBARRAY AND STOP AS SOON AS THE OR CLEARS k
    //       the OR only ever gains bits as a subarray grows, so for each start
    //       index extend rightwards and break at the first index whose running OR
    //       reaches k — nothing longer from that same start can be shorter.
    //       (the sequel LC 3097 has the same statement at 2*10^5 elements and needs
    //        a sliding window with per-bit counters instead.)
    /**
     * time = O(N^2)
     * space = O(1)
     */
    public int minimumSubarrayLength(int[] nums, int k) {
        int n = nums.length;
        int best = Integer.MAX_VALUE;

        for (int i = 0; i < n; i++) {
            int cur = 0;
            for (int j = i; j < n; j++) {
                cur |= nums[j];
                if (cur >= k) {
                    best = Math.min(best, j - i + 1);
                    break;
                }
            }
        }
        return best == Integer.MAX_VALUE ? -1 : best;
    }
}
