package LeetCodeJava.Math;

// https://leetcode.com/problems/numbers-with-repeated-digits/description/

import java.util.*;

/**
 * 1012. Numbers With Repeated Digits
 * Hard
 *
 * Given an integer n, return the number of positive integers in the range [1, n] that have at
 * least one repeated digit.
 *
 *
 * Example 1:
 *
 * Input: n = 20
 * Output: 1
 * Explanation: The only positive number (<= 20) with at least 1 repeated digit is 11.
 *
 * Example 2:
 *
 * Input: n = 100
 * Output: 10
 * Explanation: The positive numbers (<= 100) with atleast 1 repeated digit are 11, 22, 33, 44,
 * 55, 66, 77, 88, 99, and 100.
 *
 * Example 3:
 *
 * Input: n = 1000
 * Output: 262
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^9
 *
 */
public class NumbersWithRepeatedDigits {

    // V0
    // IDEA: COMBINATORICS - COUNT THE COMPLEMENT
    //   answer = n - (numbers in [1, n] whose digits are all distinct)
    //   with L = digits of (n + 1), count the distinct-digit numbers below n + 1:
    //   1) shorter numbers (length i < L): 9 choices for the first digit, then P(9, i - 1)
    //   2) length-L numbers: walk n + 1's digits; at position i place any unused digit smaller
    //      than the real one, the remaining L - i - 1 positions are free -> P(9 - i, L - i - 1).
    //      stop as soon as the real digit was already used (no longer prefix is distinct).
    //   walking n + 1 (strict "smaller than") makes n itself counted without a special case.
    /**
     * time = O(L^2), L = number of digits
     * space = O(L)
     */
    public int numDupDigitsAtMostN(int n) {
        String digits = String.valueOf((long) n + 1);
        int length = digits.length();

        // 1) all distinct-digit numbers with fewer digits than n + 1
        int distinct = 0;
        for (int i = 1; i < length; i++) {
            distinct += 9 * permutations(9, i - 1);
        }

        // 2) distinct-digit numbers with the same length, below n + 1, by shared prefix
        Set<Integer> used = new HashSet<>();
        for (int i = 0; i < length; i++) {
            int digit = digits.charAt(i) - '0';
            // the leading digit cannot be 0
            int lowest = (i == 0) ? 1 : 0;
            for (int candidate = lowest; candidate < digit; candidate++) {
                if (used.contains(candidate)) {
                    continue;
                }
                // i + 1 digits are fixed, the rest choose from 10 - (i + 1) unused digits
                distinct += permutations(9 - i, length - i - 1);
            }
            // a repeated digit in the prefix means no longer prefix can be all-distinct
            if (used.contains(digit)) {
                break;
            }
            used.add(digit);
        }

        return n - distinct;
    }

    /** P(m, k) = m * (m - 1) * ... * (m - k + 1). */
    private int permutations(int m, int k) {
        int result = 1;
        for (int i = 0; i < k; i++) {
            result *= (m - i);
        }
        return result;
    }
}
