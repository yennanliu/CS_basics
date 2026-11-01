package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-operations-to-make-every-element-palindromic/description/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 4053. Minimum Operations to Make Every Element Palindromic
 * Medium
 *
 * You are given an integer array nums.
 *
 * In one operation, you may choose an index i and either increment or
 * decrement nums[i] by 2.
 *
 * Return the minimum number of operations required to make every element in
 * nums a positive palindrome. Different elements may be changed into
 * different palindromic integers.
 *
 *
 * Example 1:
 *
 * Input: nums = [10,12,14,16]
 * Output: 9
 * Explanation:
 * One optimal sequence of operations is:
 * Decrement nums[0] by 2 once to change it from 10 to 8.
 * Decrement nums[1] by 2 twice to change it from 12 to 8.
 * Decrement nums[2] by 2 three times to change it from 14 to 8.
 * Increment nums[3] by 2 three times to change it from 16 to 22.
 * After 1 + 2 + 3 + 3 = 9 operations, nums = [8, 8, 8, 22], and every element
 * is a positive palindromic integer.
 * It can be shown that fewer than 9 operations cannot achieve this.
 *
 * Example 2:
 *
 * Input: nums = [9,10,11,10]
 * Output: 2
 * Explanation:
 * Decrement nums[1] and nums[3] by 2 once each.
 * After 2 operations, nums = [9, 8, 11, 8], and every element is a positive
 * palindromic integer. At least one operation is needed for each of these two
 * elements, so the minimum number of operations is 2.
 *
 * Example 3:
 *
 * Input: nums = [125]
 * Output: 2
 * Explanation:
 * Decrement nums[0] by 2 twice to change it from 125 to 121, which is a
 * positive palindromic integer. A single operation would change it to 123 or
 * 127, neither of which is palindromic.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 *
 */
public class MinimumOperationsToMakeEveryElementPalindromic {

    /** Palindromes up to this many digits; 10 digits already reaches past the 10^9 cap. */
    private static final int MAX_DIGITS = 10;

    // V0
    // IDEA: PARITY IS INVARIANT -> BINARY SEARCH THE NEAREST SAME-PARITY PALINDROME
    //       +2 / -2 never changes parity, so an even x can only land on an even palindrome and
    //       an odd x on an odd one. elements are independent: answer = sum of |x - p| / 2 for
    //       the closest same-parity palindrome p. over a sorted list "closest" is only ever the
    //       predecessor or the successor, so precompute every palindrome (~2 * 10^5 of them,
    //       each fixed by its first half), split by parity, and binary search each x.
    /**
     * time = O(P log P + N log P), P = number of palindromes (~2 * 10^5)
     * space = O(P)
     */
    public long minOperations(int[] nums) {
        // edge case: nothing to change
        if (nums == null || nums.length == 0) {
            return 0;
        }

        long[] evenPalindromes = buildPalindromes(0);
        long[] oddPalindromes = buildPalindromes(1);

        long totalOperations = 0;
        for (int x : nums) {
            long[] candidates = (x % 2 == 0) ? evenPalindromes : oddPalindromes;

            // first palindrome >= x (lands ON x when x is already a palindrome -> cost 0)
            int idx = lowerBound(candidates, x);

            long best = Long.MAX_VALUE;
            // successor
            if (idx < candidates.length) {
                best = Math.min(best, (candidates[idx] - x) / 2);
            }
            // predecessor
            if (idx > 0) {
                best = Math.min(best, (x - candidates[idx - 1]) / 2);
            }
            totalOperations += best;
        }
        return totalOperations;
    }

    /** Every positive palindrome with at most MAX_DIGITS digits whose parity is `parity`, sorted. */
    private long[] buildPalindromes(int parity) {
        List<Long> palindromes = new ArrayList<>();

        for (int length = 1; length <= MAX_DIGITS; length++) {
            // a palindrome is fixed by its first ceil(length / 2) digits
            int halfLength = (length + 1) / 2;
            long lowHalf = pow10(halfLength - 1);
            long highHalf = pow10(halfLength);

            for (long half = lowHalf; half < highHalf; half++) {
                // mirror the half; an ODD length re-uses the middle digit only once
                long mirrored = half;
                long rest = (length % 2 == 1) ? half / 10 : half;
                while (rest > 0) {
                    mirrored = mirrored * 10 + rest % 10;
                    rest /= 10;
                }
                if (mirrored % 2 == parity) {
                    palindromes.add(mirrored);
                }
            }
        }

        Collections.sort(palindromes);
        long[] result = new long[palindromes.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = palindromes.get(i);
        }
        return result;
    }

    /** Index of the first element >= target (or arr.length if none). */
    private int lowerBound(long[] arr, long target) {
        int left = 0;
        int right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    /** 10^exponent as a long. */
    private long pow10(int exponent) {
        long value = 1;
        for (int i = 0; i < exponent; i++) {
            value *= 10;
        }
        return value;
    }
}
