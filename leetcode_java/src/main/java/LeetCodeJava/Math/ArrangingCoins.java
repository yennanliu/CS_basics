package LeetCodeJava.Math;

// https://leetcode.com/problems/arranging-coins/

/**
 *  441. Arranging Coins
 *  Easy
 *
 *  You have n coins and you want to build a staircase with these coins.
 *  The staircase consists of k rows where the ith row has exactly i coins.
 *  The last row of the staircase may be incomplete.
 *
 *  Given the integer n, return the number of complete rows of the staircase you will build.
 *
 *  Example 1:
 *    Input: n = 5
 *    Output: 2
 *    Explanation: Because the 3rd row is incomplete, we return 2.
 *
 *  Example 2:
 *    Input: n = 8
 *    Output: 3
 *    Explanation: Because the 4th row is incomplete, we return 3.
 *
 *  Constraints:
 *    1 <= n <= 2^31 - 1
 */
public class ArrangingCoins {

    // V0
    // IDEA: binary search on k, largest k with k*(k+1)/2 <= n
    /**
     * time = O(log n)
     * space = O(1)
     */
    public int arrangeCoins(int n) {
        long left = 1;
        long right = n;
        long res = 0;
        while (left <= right) {
            long mid = left + (right - left) / 2;
            long total = mid * (mid + 1) / 2;
            if (total == n) {
                return (int) mid;
            } else if (total < n) {
                res = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return (int) res;
    }

    // V1
    // IDEA: math, solve k^2 + k - 2n <= 0  ->  k = (sqrt(8n + 1) - 1) / 2
    /**
     * time = O(1)
     * space = O(1)
     */
    public int arrangeCoins_1(int n) {
        return (int) ((java.lang.Math.sqrt(8.0 * n + 1) - 1) / 2);
    }
}
