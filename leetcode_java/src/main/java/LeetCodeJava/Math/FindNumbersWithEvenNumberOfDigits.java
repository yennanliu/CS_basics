package LeetCodeJava.Math;

// https://leetcode.com/problems/find-numbers-with-even-number-of-digits/description/

/**
 * 1295. Find Numbers with Even Number of Digits
 * Easy
 *
 * Given an array nums of integers, return how many of them contain
 * an even number of digits.
 *
 * Example 1:
 *
 * Input: nums = [12,345,2,6,7896]
 * Output: 2
 * Explanation:
 * 12 contains 2 digits (even number of digits).
 * 345 contains 3 digits (odd number of digits).
 * 2 contains 1 digit (odd number of digits).
 * 6 contains 1 digit (odd number of digits).
 * 7896 contains 4 digits (even number of digits).
 * Therefore only 12 and 7896 contain an even number of digits.
 *
 * Example 2:
 *
 * Input: nums = [555,901,482,1771]
 * Output: 1
 * Explanation:
 * Only 1771 contains an even number of digits.
 *
 * Constraints:
 *
 * 1 <= nums.length <= 500
 * 1 <= nums[i] <= 10^5
 *
 */
public class FindNumbersWithEvenNumberOfDigits {

    // V0
    // IDEA: COUNT EACH NUMBER'S DIGITS AND CHECK THE PARITY
    /**
     * time = O(N log M), M = max(nums)
     * space = O(1)
     */
    public int findNumbers(int[] nums) {
        int evenDigitNumbers = 0;
        for (int value : nums) {
            if (countDigits(value) % 2 == 0) {
                evenDigitNumbers++;
            }
        }
        return evenDigitNumbers;
    }

    /** Number of decimal digits of a positive integer. */
    private int countDigits(int value) {
        int digits = 0;
        int remaining = value;
        do {
            digits++;
            remaining /= 10;
        } while (remaining > 0);
        return digits;
    }
}
