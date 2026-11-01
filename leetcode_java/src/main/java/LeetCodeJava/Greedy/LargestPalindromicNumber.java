package LeetCodeJava.Greedy;

// https://leetcode.com/problems/largest-palindromic-number/description/

import java.util.*;

/**
 * 2384. Largest Palindromic Number
 * Medium
 *
 * You are given a string num consisting of digits only.
 *
 * Return the largest palindromic integer (in the form of a string) that can be formed using digits taken from num. It should not contain leading zeroes.
 *
 * Notes:
 *
 * You do not need to use all the digits of num, but you must use at least one digit.
 * The digits can be reordered.
 *
 *
 * Example 1:
 *
 * Input: num = "444947137"
 * Output: "7449447"
 * Explanation: Use the digits "4449477" from "444947137" to form the palindromic integer "7449447".
 * It can be shown that "7449447" is the largest palindromic integer that can be formed.
 *
 * Example 2:
 *
 * Input: num = "00009"
 * Output: "9"
 * Explanation: It can be shown that "9" is the largest palindromic integer that can be formed.
 * Note that the integer returned should not contain leading zeroes.
 *
 *
 * Constraints:
 *
 * 1 <= num.length <= 10^5
 * num consists of digits.
 *
 */
public class LargestPalindromicNumber {

    // V0
    // IDEA: COUNT DIGITS, BUILD THE LEFT HALF FROM '9' DOWN IN PAIRS, THEN PICK A CENTRE
    //       a pair of '0's may only be used once the half is non-empty (no leading zero);
    //       the centre is the largest digit with a count left over.
    /**
     * time = O(N)
     * space = O(N)
     */
    public String largestPalindromic(String num) {
        int[] count = new int[10];
        for (char c : num.toCharArray()) {
            count[c - '0']++;
        }

        // left half: largest digits first, in pairs
        StringBuilder half = new StringBuilder();
        for (int d = 9; d >= 0; d--) {
            int pairs = count[d] / 2;
            if (pairs == 0) {
                continue;
            }
            // zeros at the front would be leading zeros
            if (d == 0 && half.length() == 0) {
                continue;
            }
            for (int p = 0; p < pairs; p++) {
                half.append(d);
            }
            count[d] -= pairs * 2;
        }

        // centre: the largest digit still available (a lone zero is fine here)
        String middle = "";
        for (int d = 9; d >= 0; d--) {
            if (count[d] > 0) {
                middle = String.valueOf(d);
                break;
            }
        }

        String leftHalf = half.toString();
        String rightHalf = half.reverse().toString();
        String result = leftHalf + middle + rightHalf;
        // only possible when every digit was a paired zero
        if (result.isEmpty()) {
            return "0";
        }
        return result;
    }
}
