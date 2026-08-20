package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/construct-the-minimum-bitwise-array-ii/

/**
 *  3315. Construct the Minimum Bitwise Array II
 *  Medium
 *
 *  You are given an array nums consisting of n prime integers.
 *
 *  You need to construct an array ans of length n, such that, for each index i,
 *  the bitwise OR of ans[i] and ans[i] + 1 is equal to nums[i], i.e.
 *  ans[i] OR (ans[i] + 1) == nums[i].
 *
 *  Additionally, you must minimize each value of ans[i] in the resulting array.
 *
 *  If it is not possible to find such a value for ans[i], set ans[i] = -1.
 *
 *  Example 1:
 *    Input: nums = [2,3,5,7]
 *    Output: [-1,1,4,3]
 *
 *  Example 2:
 *    Input: nums = [11,13,31]
 *    Output: [9,12,15]
 *
 *  Constraints:
 *    1 <= nums.length <= 100
 *    2 <= nums[i] <= 10^9
 *    nums[i] is a prime number.
 */
public class ConstructTheMinimumBitwiseArrayII {

    // V0
    // IDEA: SAME BIT RULE AS LC 3314 - ONLY THE VALUE RANGE GROWS
    //       x | (x+1) turns x's lowest zero bit on and leaves the rest alone, so
    //       to hit a target n we clear the bit just below n's lowest zero:
    //           b   = position of n's lowest 0 bit
    //           ans = n - 2^(b-1)
    //       the only unreachable target is an even one, which among primes is 2.
    //       nums[i] now reaches 10^9, so the scan covers ~30 bits (still inside
    //       an int since 10^9 < 2^30); nothing else changes.
    /**
     * time = O(N * log(max))
     * space = O(N)   // the output array
     */
    public int[] minBitwiseArray(int[] nums) {
        int n = nums.length;
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            int v = nums[i];
            if ((v & 1) == 0) {
                res[i] = -1;
                continue;
            }
            int b = 0;
            while (((v >> b) & 1) == 1) {
                b++;
            }
            res[i] = v - (1 << (b - 1));
        }
        return res;
    }
}
