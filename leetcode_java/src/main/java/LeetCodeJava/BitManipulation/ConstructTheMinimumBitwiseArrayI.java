package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/construct-the-minimum-bitwise-array-i/

/**
 *  3314. Construct the Minimum Bitwise Array I
 *  Easy
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
 *    Explanation: no x satisfies x | (x+1) = 2; 1 | 2 = 3; 4 | 5 = 5; 3 | 4 = 7.
 *
 *  Example 2:
 *    Input: nums = [11,13,31]
 *    Output: [9,12,15]
 *
 *  Constraints:
 *    1 <= nums.length <= 100
 *    2 <= nums[i] <= 1000
 *    nums[i] is a prime number.
 */
public class ConstructTheMinimumBitwiseArrayI {

    // V0
    // IDEA: x OR (x+1) ONLY EVER *SETS* x's LOWEST ZERO BIT
    //       adding 1 to x flips its trailing run of 1s to 0 and sets the next 0
    //       bit, so x | (x+1) equals x with that lowest zero bit switched on.
    //       hence the target is always odd - and 2 (the only even prime) has no
    //       solution.
    //       for an odd n, let b = position of n's lowest ZERO bit; clearing the
    //       bit just BELOW it gives the minimum answer:
    //           ans = n - 2^(b-1)
    //       e.g. 5 = 101 -> lowest zero at bit 1 -> 5 - 1 = 4, and 4 | 5 = 5.
    /**
     * time = O(N * log(max))
     * space = O(N)   // the output array
     */
    public int[] minBitwiseArray(int[] nums) {
        int n = nums.length;
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            int v = nums[i];
            if ((v & 1) == 0) {          // only the prime 2 lands here
                res[i] = -1;
                continue;
            }
            int b = 0;
            while (((v >> b) & 1) == 1) { // first zero bit from the bottom
                b++;
            }
            res[i] = v - (1 << (b - 1));
        }
        return res;
    }
}
