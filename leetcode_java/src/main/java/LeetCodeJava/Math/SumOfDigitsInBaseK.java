package LeetCodeJava.Math;

// https://leetcode.com/problems/sum-of-digits-in-base-k/description/

/**
 * 1837. Sum of Digits in Base K
 * Easy
 *
 * Given an integer n (in base 10) and a base k, return the sum of the digits of n after converting n from base 10 to base k.
 *
 * After converting, each digit should be interpreted as a base 10 number, and the sum should be returned in base 10.
 *
 *
 * Example 1:
 *
 * Input: n = 34, k = 6
 * Output: 9
 * Explanation: 34 (base 10) expressed in base 6 is 54. 5 + 4 = 9.
 *
 * Example 2:
 *
 * Input: n = 10, k = 10
 * Output: 1
 * Explanation: n is already in base 10. 1 + 0 = 1.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 100
 * 2 <= k <= 10
 *
 */
public class SumOfDigitsInBaseK {

    // V0
    // IDEA: REPEATED DIVMOD (peel off base-k digits from the least significant end)
    /**
     * time = O(log_k N)
     * space = O(1)
     */
    public int sumBase(int n, int k) {
        int digitSum = 0;
        while (n > 0) {
            // n % k is the lowest base-k digit; n / k drops it
            digitSum += n % k;
            n /= k;
        }
        return digitSum;
    }
}
