package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-almost-equal-pairs-ii/description/

import java.util.*;

/**
 * 3267. Count Almost Equal Pairs II
 * Hard
 *
 * Attention: In this version, the number of operations that can be performed, has been increased to twice.
 *
 * You are given an array nums consisting of positive integers.
 *
 * We call two integers x and y almost equal if both integers can become equal after performing the following operation at most twice:
 *
 * Choose either x or y and swap any two digits within the chosen number.
 *
 * Return the number of indices i and j in nums where i < j such that nums[i] and nums[j] are almost equal.
 *
 * Note that it is allowed for an integer to have leading zeros after performing an operation.
 *
 *
 * Example 1:
 *
 * Input: nums = [1023,2310,2130,213]
 * Output: 4
 * Explanation:
 * The almost equal pairs of elements are:
 * 1023 and 2310. By swapping the digits 1 and 2, and then the digits 0 and 3, you get 2310.
 * 1023 and 213. By swapping the digits 1 and 0, and then the digits 1 and 2, you get 0213 which is 213.
 * 2310 and 213. By swapping the digits 2 and 0, and then the digits 3 and 2, you get 0213 which is 213.
 * 2310 and 2130. By swapping the digits 3 and 1, you get 2130.
 *
 * Example 2:
 *
 * Input: nums = [1,10,100]
 * Output: 3
 * Explanation:
 * The almost equal pairs of elements are:
 * 1 and 10. By swapping the digits 1 and 0, you get 01 which is 1.
 * 1 and 100. By swapping the second 0 with the digit 1, you get 001 which is 1.
 * 10 and 100. By swapping the first 0 with the digit 1, you get 010 which is 10.
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 5000
 * 1 <= nums[i] < 10^7
 *
 */
public class CountAlmostEqualPairsII {

    // V0
    // IDEA: ENUMERATE EVERY VALUE REACHABLE FROM x IN AT MOST TWO SWAPS, COUNT SEEN ONES
    //   a swap applied to y can be replayed as the same swap on x, so "a swaps on
    //   x plus b swaps on y with a + b <= 2" equals "at most two swaps on x
    //   alone". with values < 10^7 the padded width is at most 8 digits, so one
    //   number has under 1 + 28 + 28 * 28 variants. sweeping left to right and
    //   counting how many ALREADY SEEN numbers are among the current number's
    //   (deduplicated) variants counts every i < j pair exactly once.
    //   all numbers share one width, so a padded string and its integer value
    //   are interchangeable - the variants are kept as ints.
    /**
     * time = O(N * D^4)
     * space = O(N + D^4)
     */
    public int countPairs(int[] nums) {
        int width = 0;
        for (int num : nums) {
            width = Math.max(width, String.valueOf(num).length());
        }

        Map<Integer, Integer> seenCount = new HashMap<>();
        int pairs = 0;
        for (int num : nums) {
            for (int variant : variantsWithinTwoSwaps(num, width)) {
                pairs += seenCount.getOrDefault(variant, 0);
            }
            seenCount.put(num, seenCount.getOrDefault(num, 0) + 1);
        }
        return pairs;
    }

    /** Every value reachable from num (padded to width digits) by zero, one or two swaps. */
    private Set<Integer> variantsWithinTwoSwaps(int num, int width) {
        char[] digits = new char[width];
        int value = num;
        // fill from the right; untouched leading slots are the zero padding
        for (int i = width - 1; i >= 0; i--) {
            digits[i] = (char) ('0' + value % 10);
            value /= 10;
        }

        Set<Integer> variants = new HashSet<>();
        variants.add(num);
        for (int i = 0; i < width; i++) {
            for (int j = i + 1; j < width; j++) {
                swap(digits, i, j);
                variants.add(toInt(digits));
                // a second swap on top of the first
                for (int a = 0; a < width; a++) {
                    for (int b = a + 1; b < width; b++) {
                        swap(digits, a, b);
                        variants.add(toInt(digits));
                        swap(digits, a, b);
                    }
                }
                swap(digits, i, j);
            }
        }
        return variants;
    }

    /** Swaps two positions of a digit array in place. */
    private void swap(char[] digits, int i, int j) {
        char temp = digits[i];
        digits[i] = digits[j];
        digits[j] = temp;
    }

    /** Reads a digit array (leading zeros allowed) as an int. */
    private int toInt(char[] digits) {
        int value = 0;
        for (char d : digits) {
            value = value * 10 + (d - '0');
        }
        return value;
    }
}
