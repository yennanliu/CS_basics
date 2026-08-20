package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/maximum-possible-number-by-binary-concatenation/

/**
 *  3309. Maximum Possible Number by Binary Concatenation
 *  Medium
 *
 *  You are given an array of integers nums of size 3.
 *
 *  Return the maximum possible number whose binary representation can be formed by
 *  concatenating the binary representation of all elements in nums in some order.
 *
 *  Note that the binary representation of any number does not contain leading zeros.
 *
 *  Example 1:
 *    Input: nums = [1,2,3]
 *    Output: 30
 *    Explanation: Concatenate in the order [3, 1, 2] to get "11110", which is 30.
 *
 *  Example 2:
 *    Input: nums = [2,8,16]
 *    Output: 1296
 *    Explanation: Concatenate in the order [2, 8, 16] to get "10100010000", i.e. 1296.
 *
 *  Constraints:
 *    nums.length == 3
 *    1 <= nums[i] <= 127
 */
public class MaximumPossibleNumberByBinaryConcatenation {

    // V0
    // IDEA: ONLY SIX ORDERINGS EXIST -> BUILD THEM ALL
    //
    //  with exactly three numbers there are 3! = 6 concatenations, so no ordering
    //  rule has to be derived. concatenating is "shift the accumulator left by the
    //  bit length of the next value, then OR it in".
    //
    //  each value is <= 127 -> 7 bits, so the result is at most 21 bits and fits an int.
    /**
     * time = O(1)
     * space = O(1)
     */
    public int maxGoodNumber(int[] nums) {
        int res = 0;
        for (int a = 0; a < 3; a++) {
            for (int b = 0; b < 3; b++) {
                if (b == a) {
                    continue;
                }
                int c = 3 - a - b;
                int[] perm = new int[]{a, b, c};

                int cur = 0;
                for (int i = 0; i < 3; i++) {
                    int v = nums[perm[i]];
                    int len = 32 - Integer.numberOfLeadingZeros(v);   // bit length, v >= 1
                    cur = (cur << len) | v;
                }
                res = Math.max(res, cur);
            }
        }
        return res;
    }
}
