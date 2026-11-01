package LeetCodeJava.Math;

// https://leetcode.com/problems/next-special-palindrome-number/description/

import java.util.*;

/**
 * 3646. Next Special Palindrome Number
 * Hard
 *
 * You are given an integer n.
 *
 * A number is called special if:
 *
 * It is a palindrome.
 *
 * Every digit k in the number appears exactly k times.
 *
 * Return the smallest special number strictly greater than n.
 *
 * Example 1:
 *
 * Input: n = 2
 * Output: 22
 * Explanation:
 * 22 is the smallest special number greater than 2, as it is a palindrome and
 * the digit 2 appears exactly 2 times.
 *
 * Example 2:
 *
 * Input: n = 33
 * Output: 212
 * Explanation:
 * 212 is the smallest special number greater than 33, as it is a palindrome
 * and the digits 1 and 2 appear exactly 1 and 2 times respectively.
 *
 * Constraints:
 *
 * 0 <= n <= 10^15
 *
 */
public class NextSpecialPalindromeNumber {

    private static final int MAX_LENGTH = 16; // n <= 10^15, so the answer has at most 16 digits

    // sorted list of every special number with at most MAX_LENGTH digits, built once
    private static long[] specials;

    // V0
    // IDEA: ENUMERATE EVERY SPECIAL NUMBER ONCE, THEN BINARY SEARCH
    //       a special number is fixed by the SET of digits it uses (digit d appears exactly d
    //       times, 0 can never appear), so its length is the sum of the set.
    //       a palindrome allows at most one digit with an odd count (the middle), so at most one
    //       odd digit may be chosen. the number is then any arrangement of the "half" multiset
    //       (d / 2 copies of each chosen d), mirrored around the optional middle digit.
    //       all 2^9 digit sets, capped at 16 digits, give a few tens of thousands of candidates.
    /**
     * time = O(C log C) once, then O(log C) per call    (C = number of special numbers)
     * space = O(C)
     */
    public long specialPalindrome(long n) {
        if (specials == null) {
            specials = buildSpecials();
        }
        // first special number strictly greater than n
        int left = 0;
        int right = specials.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (specials[mid] > n) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return specials[left];
    }

    /** Every special palindrome with at most MAX_LENGTH digits, sorted ascending. */
    private long[] buildSpecials() {
        TreeSet<Long> found = new TreeSet<>();
        for (int mask = 1; mask < (1 << 9); mask++) {
            // chosen digits: bit d-1 of mask set means digit d is used (d times)
            List<Integer> chosen = new ArrayList<>();
            int length = 0;
            int oddDigitCount = 0;
            int middleDigit = 0;
            for (int digit = 1; digit <= 9; digit++) {
                if (((mask >> (digit - 1)) & 1) == 1) {
                    chosen.add(digit);
                    length += digit;
                    if (digit % 2 == 1) {
                        oddDigitCount++;
                        middleDigit = digit;
                    }
                }
            }
            if (length > MAX_LENGTH || oddDigitCount > 1) {
                continue;
            }

            // the left half: digit / 2 copies of each chosen digit, in ascending order
            List<Integer> halfList = new ArrayList<>();
            for (int digit : chosen) {
                for (int copy = 0; copy < digit / 2; copy++) {
                    halfList.add(digit);
                }
            }
            int[] half = new int[halfList.size()];
            for (int i = 0; i < half.length; i++) {
                half[i] = halfList.get(i);
            }

            // visit every distinct arrangement of the half (next-permutation skips duplicates)
            do {
                found.add(buildPalindrome(half, middleDigit));
            } while (nextPermutation(half));
        }

        long[] sorted = new long[found.size()];
        int index = 0;
        for (long value : found) {
            sorted[index++] = value;
        }
        return sorted;
    }

    /** half + optional middle digit (0 = none) + reversed half, read as a number. */
    private long buildPalindrome(int[] half, int middleDigit) {
        long value = 0;
        for (int digit : half) {
            value = value * 10 + digit;
        }
        if (middleDigit != 0) {
            value = value * 10 + middleDigit;
        }
        for (int i = half.length - 1; i >= 0; i--) {
            value = value * 10 + half[i];
        }
        return value;
    }

    /** Rearranges digits into the next lexicographic permutation; false when it was the last. */
    private boolean nextPermutation(int[] digits) {
        // find the rightmost position that is smaller than its successor
        int pivot = digits.length - 2;
        while (pivot >= 0 && digits[pivot] >= digits[pivot + 1]) {
            pivot--;
        }
        if (pivot < 0) {
            return false;
        }
        // swap it with the rightmost element larger than it
        int successor = digits.length - 1;
        while (digits[successor] <= digits[pivot]) {
            successor--;
        }
        swap(digits, pivot, successor);
        // the suffix is descending; reverse it to make it the smallest arrangement
        for (int i = pivot + 1, j = digits.length - 1; i < j; i++, j--) {
            swap(digits, i, j);
        }
        return true;
    }

    /** Swaps two entries of an array. */
    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
