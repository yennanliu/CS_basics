package LeetCodeJava.Math;

// https://leetcode.com/problems/strictly-palindromic-number/description/

import java.util.*;

/**
 * 2396. Strictly Palindromic Number
 * Medium
 *
 * An integer n is strictly palindromic if, for every base b between 2 and n - 2 (inclusive), the string representation of the integer n in base b is palindromic.
 *
 * Given an integer n, return true if n is strictly palindromic and false otherwise.
 *
 * A string is palindromic if it reads the same forward and backward.
 *
 *
 * Example 1:
 *
 * Input: n = 9
 * Output: false
 * Explanation: In base 2: 9 = 1001 (base 2), which is palindromic.
 * In base 3: 9 = 100 (base 3), which is not palindromic.
 * Therefore, 9 is not strictly palindromic so we return false.
 * Note that in bases 4, 5, 6, and 7, n = 9 is also not palindromic.
 *
 * Example 2:
 *
 * Input: n = 4
 * Output: false
 * Explanation: We only consider base 2: 4 = 100 (base 2), which is not palindromic.
 * Therefore, we return false.
 *
 *
 * Constraints:
 *
 * 4 <= n <= 10^5
 *
 */
public class StrictlyPalindromicNumber {

    // V0
    // IDEA: MATH - IN BASE n - 2, n IS WRITTEN "12", WHICH IS NEVER A PALINDROME
    //   n = 1 * (n - 2) + 2, and 4 <= n guarantees base n - 2 >= 2 is in range -> always false.
    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean isStrictlyPalindromic(int n) {
        return false;
    }

    // V1
    // IDEA: BRUTE FORCE - CONVERT n TO EVERY BASE 2..n-2 AND CHECK FOR A PALINDROME
    /**
     * time = O(n log n)
     * space = O(log n)
     */
    public boolean isStrictlyPalindromic_1(int n) {
        for (int base = 2; base <= n - 2; base++) {
            // digits of n in this base, least significant first
            List<Integer> baseDigits = new ArrayList<>();
            int rest = n;
            while (rest > 0) {
                baseDigits.add(rest % base);
                rest /= base;
            }

            // two-pointer palindrome check
            int left = 0;
            int right = baseDigits.size() - 1;
            while (left < right) {
                if (!baseDigits.get(left).equals(baseDigits.get(right))) {
                    return false;
                }
                left++;
                right--;
            }
        }
        return true;
    }
}
