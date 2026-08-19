package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-numbers-with-unique-digits/

/**
 *  357. Count Numbers with Unique Digits
 *  Medium
 *
 *  Given an integer n, return the count of all numbers with unique digits, x,
 *  where 0 <= x < 10^n.
 *
 *  Example 1:
 *
 *  Input: n = 2
 *  Output: 91
 *  Explanation: The answer should be the total numbers in the range of 0 <= x < 100,
 *  excluding 11,22,33,44,55,66,77,88,99
 *
 *  Example 2:
 *
 *  Input: n = 0
 *  Output: 1
 *
 *  Constraints:
 *
 *  0 <= n <= 8
 */
public class CountNumbersWithUniqueDigits {

    // V0
    // IDEA: COMBINATORICS / DP
    //  f(1) = 10 (0..9)
    //  f(k) = 9 * 9 * 8 * ... * (10 - k + 1)  for k >= 2  (first digit can't be 0)
    //  answer = sum of f(1..n)
    /**
     * time = O(n)
     * space = O(1)
     */
    public int countNumbersWithUniqueDigits(int n) {
        if (n == 0) {
            return 1;
        }
        int count = 10; // n == 1
        int cur = 9;    // count of k-digit numbers with unique digits
        for (int k = 2; k <= n; k++) {
            cur *= (10 - (k - 1));
            count += cur;
        }
        return count;
    }

    // V1
    // IDEA: pure DP array, dp[k] = numbers with exactly k unique digits
    /**
     * time = O(n)
     * space = O(n)
     */
    public int countNumbersWithUniqueDigits_1(int n) {
        if (n == 0) {
            return 1;
        }
        int[] dp = new int[n + 1];
        dp[0] = 1;
        dp[1] = 9;
        for (int k = 2; k <= n; k++) {
            dp[k] = dp[k - 1] * (10 - (k - 1));
        }
        int res = 1; // the number 0
        for (int k = 1; k <= n; k++) {
            res += dp[k];
        }
        return res;
    }
}
