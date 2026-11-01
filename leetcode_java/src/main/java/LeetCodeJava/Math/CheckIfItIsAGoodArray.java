package LeetCodeJava.Math;

// https://leetcode.com/problems/check-if-it-is-a-good-array/description/

/**
 * 1250. Check If It Is a Good Array
 * Hard
 *
 * Given an array nums of positive integers. Your task is to select some subset of nums, multiply
 * each element by an integer and add all these numbers. The array is said to be good if you can
 * obtain a sum of 1 from the array by any possible subset and multiplicand.
 *
 * Return True if the array is good otherwise return False.
 *
 *
 * Example 1:
 *
 * Input: nums = [12,5,7,23]
 * Output: true
 * Explanation: Pick numbers 5 and 7.
 * 5*3 + 7*(-2) = 1
 *
 * Example 2:
 *
 * Input: nums = [29,6,10]
 * Output: true
 * Explanation: Pick numbers 29, 6 and 10.
 * 29*1 + 6*(-3) + 10*(-1) = 1
 *
 * Example 3:
 *
 * Input: nums = [3,6]
 * Output: false
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 *
 */
public class CheckIfItIsAGoodArray {

    // V0
    // IDEA: BEZOUT'S IDENTITY
    //   integer combinations of a1..ak reach exactly the multiples of gcd(a1, ..., ak),
    //   so a sum of 1 is reachable <=> gcd(nums) == 1. adding numbers can only keep or shrink
    //   the gcd, so the gcd of the whole array decides it (no need to hunt for a subset).
    /**
     * time = O(N log M), M = max(nums)
     * space = O(1)
     */
    public boolean isGoodArray(int[] nums) {
        int gcd = 0;
        for (int num : nums) {
            gcd = gcd(gcd, num);
            // the gcd cannot go lower than 1
            if (gcd == 1) {
                return true;
            }
        }
        return gcd == 1;
    }

    /** Euclid's algorithm; gcd(0, b) == b. */
    private int gcd(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
