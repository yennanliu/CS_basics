package LeetCodeJava.Math;

// https://leetcode.com/problems/consecutive-numbers-sum/

/**
 *  829. Consecutive Numbers Sum
 *  Hard
 *
 *  Given an integer n, return the number of ways you can write n as the sum of
 *  consecutive positive integers.
 *
 *  Example 1:
 *   Input: n = 5
 *   Output: 2
 *   Explanation: 5 = 2 + 3
 *
 *  Example 2:
 *   Input: n = 9
 *   Output: 3
 *   Explanation: 9 = 4 + 5 = 2 + 3 + 4
 *
 *  Example 3:
 *   Input: n = 15
 *   Output: 4
 *   Explanation: 15 = 8 + 7 = 4 + 5 + 6 = 1 + 2 + 3 + 4 + 5
 *
 *  Constraints:
 *   - 1 <= n <= 10^9
 */
public class ConsecutiveNumbersSum {

    // V0
    // IDEA: n = a + (a+1) + ... + (a+k-1) = k*a + k(k-1)/2
    //       -> for each length k, a valid start `a` exists iff
    //          (n - k(k-1)/2) > 0 and divisible by k.
    /**
     * time = O(sqrt(n))
     * space = O(1)
     */
    public int consecutiveNumbersSum(int n) {
        int count = 0;
        for (long k = 1; k * (k - 1) / 2 < n; k++) {
            long rest = n - k * (k - 1) / 2;
            if (rest % k == 0) {
                count++;
            }
        }
        return count;
    }

    // V1
    // IDEA: MATH - writing n = 2^p * m (m odd), the answer is exactly the
    //       number of odd divisors of n, i.e. the number of divisors of m.
    /**
     * time = O(sqrt(n))
     * space = O(1)
     */
    public int consecutiveNumbersSum_1(int n) {
        while (n % 2 == 0) {
            n /= 2;
        }
        int res = 1;
        for (int i = 3; (long) i * i <= n; i += 2) {
            int cnt = 0;
            while (n % i == 0) {
                n /= i;
                cnt++;
            }
            res *= (cnt + 1);
        }
        if (n > 1) {
            res *= 2;
        }
        return res;
    }
}
