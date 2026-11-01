package LeetCodeJava.Array;

// https://leetcode.com/problems/number-of-subarrays-having-even-product/description/

/**
 * 2495. Number of Subarrays Having Even Product
 * Medium
 *
 * Given a 0-indexed integer array nums, return the number of subarrays of nums having an even product.
 *
 *
 * Example 1:
 *
 * Input: nums = [9,6,7,13]
 * Output: 6
 * Explanation: There are 6 subarrays with an even product:
 * - nums[0..1] = 9 * 6 = 54.
 * - nums[0..2] = 9 * 6 * 7 = 378.
 * - nums[0..3] = 9 * 6 * 7 * 13 = 4914.
 * - nums[1..1] = 6.
 * - nums[1..2] = 6 * 7 = 42.
 * - nums[1..3] = 6 * 7 * 13 = 546.
 *
 * Example 2:
 *
 * Input: nums = [7,3,5]
 * Output: 0
 * Explanation: There are no subarrays with an even product.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^5
 *
 */
public class NumberOfSubarraysHavingEvenProduct {

    // V0
    // IDEA: COUNT SUBARRAYS ENDING AT i - EVERY LEFT END AT OR BEFORE THE LAST EVEN INDEX GIVES AN EVEN PRODUCT
    /**
     * time = O(N)
     * space = O(1)
     */
    public long evenProduct(int[] nums) {
        long result = 0;

        // index of the most recent even element; -1 means none seen yet
        int lastEvenIndex = -1;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] % 2 == 0) {
                lastEvenIndex = i;
            }
            // left ends 0 .. lastEvenIndex all contain an even element
            result += lastEvenIndex + 1;
        }
        return result;
    }

    // V1
    // IDEA: COMPLEMENT - TOTAL SUBARRAYS MINUS ALL-ODD SUBARRAYS (COUNTED PER RUN OF ODD NUMBERS)
    /**
     * time = O(N)
     * space = O(1)
     */
    public long evenProduct_1(int[] nums) {
        long n = nums.length;
        long total = n * (n + 1) / 2;

        // oddRun = length of the run of odd numbers ending at i;
        // it is also the number of all-odd subarrays ending at i
        long oddRun = 0;
        long allOdd = 0;
        for (int num : nums) {
            if (num % 2 == 1) {
                oddRun++;
            } else {
                oddRun = 0;
            }
            allOdd += oddRun;
        }
        return total - allOdd;
    }
}
