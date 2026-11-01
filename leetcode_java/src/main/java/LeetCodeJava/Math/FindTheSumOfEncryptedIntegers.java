package LeetCodeJava.Math;

// https://leetcode.com/problems/find-the-sum-of-encrypted-integers/description/

/**
 * 3079. Find the Sum of Encrypted Integers
 * Easy
 *
 * You are given an integer array nums containing positive integers. We define a function encrypt such that
 * encrypt(x) replaces every digit in x with the largest digit in x.
 *
 * For example, encrypt(523) = 555 and encrypt(213) = 333.
 *
 * Return the sum of encrypted elements.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3]
 * Output: 6
 * Explanation: The encrypted elements are [1,2,3]. The sum of encrypted elements is 1 + 2 + 3 == 6.
 *
 * Example 2:
 *
 * Input: nums = [10,21,31]
 * Output: 66
 * Explanation: The encrypted elements are [11,22,33]. The sum of encrypted elements is 11 + 22 + 33 == 66.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 50
 * 1 <= nums[i] <= 1000
 *
 */
public class FindTheSumOfEncryptedIntegers {

    // V0
    // IDEA: DIGIT PEELING — FIND THE MAX DIGIT AND THE DIGIT COUNT, THEN BUILD maxDigit * 11...1
    /**
     * time = O(N * D)
     * space = O(1)
     */
    public int sumOfEncryptedInt(int[] nums) {
        int total = 0;
        for (int x : nums) {
            int maxDigit = 0;
            // repunit = 1, 11, 111, ... with as many ones as x has digits
            int repunit = 0;
            int remaining = x;
            while (remaining > 0) {
                maxDigit = Math.max(maxDigit, remaining % 10);
                repunit = repunit * 10 + 1;
                remaining /= 10;
            }
            // every digit replaced by maxDigit == maxDigit * 11...1
            total += maxDigit * repunit;
        }
        return total;
    }
}
