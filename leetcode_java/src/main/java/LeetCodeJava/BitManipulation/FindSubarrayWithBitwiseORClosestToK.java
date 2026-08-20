package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/find-subarray-with-bitwise-or-closest-to-k/

import java.util.HashSet;
import java.util.Set;

/**
 *  3171. Find Subarray With Bitwise OR Closest to K
 *  Hard
 *
 *  You are given an array nums and an integer k. You need to find a subarray of
 *  nums such that the absolute difference between k and the bitwise OR of the
 *  subarray elements is as small as possible. In other words, select a subarray
 *  nums[l..r] such that |k - (nums[l] OR nums[l + 1] ... OR nums[r])| is minimum.
 *
 *  Return the minimum possible value of the absolute difference.
 *
 *  Example 1:
 *    Input: nums = [1,2,4,5], k = 3
 *    Output: 0
 *    Explanation: nums[0..1] has OR value 3, so |3 - 3| = 0.
 *
 *  Example 2:
 *    Input: nums = [1,3,1,3], k = 2
 *    Output: 1
 *    Explanation: nums[1..1] has OR value 3, so |3 - 2| = 1.
 *
 *  Example 3:
 *    Input: nums = [1], k = 10
 *    Output: 9
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    1 <= nums[i] <= 10^9
 *    1 <= k <= 10^9
 */
public class FindSubarrayWithBitwiseORClosestToK {

    // V0
    // IDEA: THE ORs OF ALL SUBARRAYS ENDING AT i FORM AT MOST ~30 DISTINCT VALUES
    //       extending a subarray leftwards can only ADD bits, so the ORs of the
    //       subarrays ending at index i form a chain that gains bits and never
    //       loses any - with 30-bit values that chain has <= 31 distinct entries.
    //       so carry the small set of ORs ending at the previous index, OR each
    //       with nums[i], add nums[i] itself, and dedupe; the set stays tiny.
    //       every candidate is a real subarray OR, so testing |v - k| over the
    //       set at each i covers every subarray exactly once.
    /**
     * time = O(30 * N)
     * space = O(30)
     */
    public int minimumDifference(int[] nums, int k) {
        int best = Integer.MAX_VALUE;
        Set<Integer> prev = new HashSet<>();
        for (int x : nums) {
            Set<Integer> cur = new HashSet<>();
            cur.add(x);
            for (int v : prev) {
                cur.add(v | x);
            }
            for (int v : cur) {
                int d = Math.abs(v - k);
                if (d < best) {
                    best = d;
                }
            }
            prev = cur;
        }
        return best;
    }
}
