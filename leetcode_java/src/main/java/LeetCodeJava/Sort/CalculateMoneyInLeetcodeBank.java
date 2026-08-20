package LeetCodeJava.Sort;

// https://leetcode.com/problems/calculate-money-in-leetcode-bank/

/**
 *  1716. Calculate Money in Leetcode Bank
 *  Easy
 *
 *  Hercy wants to save money for his first car. He puts money in the Leetcode
 *  bank every day.
 *
 *  He starts by putting in $1 on Monday, the first day. Every day from Tuesday to
 *  Sunday, he will put in $1 more than the day before. On every subsequent Monday,
 *  he will put in $1 more than the previous Monday.
 *
 *  Given n, return the total amount of money he will have in the Leetcode bank at
 *  the end of the nth day.
 *
 *  Example 1:
 *    Input: n = 4
 *    Output: 10
 *    Explanation: After the 4th day, the total is 1 + 2 + 3 + 4 = 10.
 *
 *  Example 2:
 *    Input: n = 10
 *    Output: 37
 *    Explanation: (1+2+3+4+5+6+7) + (2+3+4) = 37.
 *
 *  Constraints:
 *    1 <= n <= 1000
 */
public class CalculateMoneyInLeetcodeBank {

    // V0
    // IDEA: MATH (arithmetic series over whole weeks + the leftover days)
    //       week k (1-indexed) deposits k, k+1, ..., k+6 -> sum = 7k + 21.
    //       with k = n / 7 full weeks and b = n % 7 leftover days:
    //         full weeks    = 7 * k(k+1)/2 + 21k
    //         leftover days = week k+1 starts at (k+1), so the b days are
    //                         (k+1) ... (k+b) -> b*(k+1) + b(b-1)/2
    /**
     * time = O(1)
     * space = O(1)
     */
    public int totalMoney(int n) {
        int k = n / 7;
        int b = n % 7;

        int full = 7 * k * (k + 1) / 2 + 21 * k;
        int rest = b * (k + 1) + b * (b - 1) / 2;

        return full + rest;
    }
}
