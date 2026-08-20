package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/smallest-subarrays-with-maximum-bitwise-or/

/**
 *  2411. Smallest Subarrays With Maximum Bitwise OR
 *  Medium
 *
 *  You are given a 0-indexed array nums of length n, consisting of non-negative
 *  integers. For each index i from 0 to n - 1, you must determine the size of the
 *  minimum sized non-empty subarray of nums starting at i (inclusive) that has
 *  the maximum possible bitwise OR.
 *
 *  In other words, let Bij be the bitwise OR of the subarray nums[i...j]. You
 *  need to find the smallest subarray starting at i, such that bitwise OR of this
 *  subarray is equal to max(Bik) where i <= k <= n - 1.
 *
 *  Return an integer array answer of size n where answer[i] is the length of the
 *  minimum sized subarray starting at i with maximum bitwise OR.
 *
 *  Example 1:
 *    Input: nums = [1,0,2,1,3]
 *    Output: [3,3,2,2,1]
 *    Explanation: the maximum possible OR from any index is 3; starting at index
 *                 0 the shortest subarray that yields it is [1,0,2], etc.
 *
 *  Example 2:
 *    Input: nums = [1,2]
 *    Output: [2,1]
 *
 *  Constraints:
 *    n == nums.length
 *    1 <= n <= 10^5
 *    0 <= nums[i] <= 10^9
 */
public class SmallestSubarraysWithMaximumBitwiseOr {

    // V0
    // IDEA: PER BIT, REMEMBER THE NEAREST INDEX TO THE RIGHT THAT SETS IT
    //       extending a subarray can only ADD bits, so the maximum OR starting at i
    //       is the OR of the whole suffix nums[i..n-1]; the shortest window achieving
    //       it must reach far enough right to pick up every bit the suffix contains.
    //       scanning right to left with last[b] = smallest index >= i where bit b is
    //       set, the answer is max over present bits of (last[b] - i + 1), min 1.
    //       nums[i] <= 10^9 < 2^30, so 30 bit slots suffice.
    /**
     * time = O(N * 30)
     * space = O(30)   // excluding the output
     */
    public int[] smallestSubarrays(int[] nums) {
        final int BITS = 30;
        int n = nums.length;
        int[] last = new int[BITS];
        for (int b = 0; b < BITS; b++) {
            last[b] = -1;
        }
        int[] res = new int[n];

        for (int i = n - 1; i >= 0; i--) {
            for (int b = 0; b < BITS; b++) {
                if (((nums[i] >> b) & 1) == 1) {
                    last[b] = i;
                }
            }
            int far = i;
            for (int b = 0; b < BITS; b++) {
                if (last[b] > far) {
                    far = last[b];
                }
            }
            res[i] = far - i + 1;
        }
        return res;
    }
}
