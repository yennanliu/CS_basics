package LeetCodeJava.Array;

// https://leetcode.com/problems/rotate-function/

/**
 *  396. Rotate Function
 *  Medium
 *
 *  You are given an integer array nums of length n.
 *
 *  Assume arr_k to be an array obtained by rotating nums by k positions
 *  clock-wise. We define F(k) as follows:
 *
 *   F(k) = 0 * arr_k[0] + 1 * arr_k[1] + ... + (n - 1) * arr_k[n - 1]
 *
 *  Return the maximum value of F(0), F(1), ..., F(n-1).
 *  The test cases are generated so that the answer fits in a 32-bit integer.
 *
 *  Example 1:
 *   Input: nums = [4,3,2,6]
 *   Output: 26
 *   (F(0)=25, F(1)=16, F(2)=23, F(3)=26)
 *
 *  Example 2:
 *   Input: nums = [1000]
 *   Output: 0
 *
 *  Constraints:
 *   n == nums.length
 *   1 <= n <= 10^5
 *   -100 <= nums[i] <= 100
 */
public class RotateFunction {

    // V0
    // IDEA: MATH RECURRENCE - F(k) = F(k-1) + sum - n * nums[n-k]
    /**
     * time = O(n)
     * space = O(1)
     */
    public int maxRotateFunction(int[] nums) {
        int n = nums.length;

        long sum = 0;
        long f = 0;
        for (int i = 0; i < n; i++) {
            sum += nums[i];
            f += (long) i * nums[i];
        }

        long res = f;
        // NOTE !!! rotating clockwise by 1 moves nums[n-k] from idx (n-1) to idx 0
        for (int k = 1; k < n; k++) {
            f = f + sum - (long) n * nums[n - k];
            res = Math.max(res, f);
        }

        return (int) res;
    }
}
