package LeetCodeJava.Greedy;

// https://leetcode.com/problems/split-with-minimum-sum/description/

import java.util.*;

/**
 * 2578. Split With Minimum Sum
 * Easy
 *
 * Given a positive integer num, split it into two non-negative integers num1 and num2 such that:
 *
 * The concatenation of num1 and num2 is a permutation of num.
 * In other words, the sum of the number of occurrences of each digit in num1 and num2 is equal to
 * the number of occurrences of that digit in num.
 * num1 and num2 can contain leading zeros.
 *
 * Return the minimum possible sum of num1 and num2.
 *
 * Notes:
 *
 * It is guaranteed that num does not contain any leading zeros.
 * The order of occurrence of the digits in num1 and num2 may differ from the order of occurrence of num.
 *
 *
 * Example 1:
 *
 * Input: num = 4325
 * Output: 59
 * Explanation: We can split 4325 so that num1 is 24 and num2 is 35, giving a sum of 59.
 * We can prove that 59 is indeed the minimal possible sum.
 *
 * Example 2:
 *
 * Input: num = 687
 * Output: 75
 * Explanation: We can split 687 so that num1 is 68 and num2 is 7, which would give an optimal sum of 75.
 *
 *
 * Constraints:
 *
 * 10 <= num <= 10^9
 *
 */
public class SplitWithMinimumSum {

    // V0
    // IDEA: SORT DIGITS + GREEDY ALTERNATING DEAL
    //       keep the two numbers' lengths balanced and put small digits on high places:
    //       sort the digits ascending and deal them alternately into num1 and num2.
    /**
     * time = O(D log D)   (D = number of digits, <= 10)
     * space = O(D)
     */
    public int splitNum(int num) {
        char[] digits = String.valueOf(num).toCharArray();
        Arrays.sort(digits);

        int num1 = 0;
        int num2 = 0;
        for (int i = 0; i < digits.length; i++) {
            int digit = digits[i] - '0';
            // even positions go to num1, odd ones to num2; appending shifts earlier digits up a place
            if (i % 2 == 0) {
                num1 = num1 * 10 + digit;
            } else {
                num2 = num2 * 10 + digit;
            }
        }
        return num1 + num2;
    }
}
