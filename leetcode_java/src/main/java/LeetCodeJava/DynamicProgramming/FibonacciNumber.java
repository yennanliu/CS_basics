package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/fibonacci-number/description/
/**
 * 509. Fibonacci Number
 * Attempted
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * The Fibonacci numbers, commonly denoted F(n) form a sequence, called the Fibonacci sequence, such that each number is the sum of the two preceding ones, starting from 0 and 1. That is,
 *
 * F(0) = 0, F(1) = 1
 * F(n) = F(n - 1) + F(n - 2), for n > 1.
 * Given n, calculate F(n).
 *
 *
 *
 * Example 1:
 *
 * Input: n = 2
 * Output: 1
 * Explanation: F(2) = F(1) + F(0) = 1 + 0 = 1.
 * Example 2:
 *
 * Input: n = 3
 * Output: 2
 * Explanation: F(3) = F(2) + F(1) = 1 + 1 = 2.
 * Example 3:
 *
 * Input: n = 4
 * Output: 3
 * Explanation: F(4) = F(3) + F(2) = 2 + 1 = 3.
 *
 *
 * Constraints:
 *
 * 0 <= n <= 30
 *
 */
public class FibonacciNumber {

    // V0
    // IDEA: DP
    /**
     * time = O(N)
     * space = O(N)
     */
    public int fib(int n) {
        // edge
        if (n <= 1) {
            return n;
        }
        int[] dp = new int[n + 1];
        dp[0] = 0;
        dp[1] = 1;
        for (int i = 2; i < n + 1; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }

        return dp[n];
    }

    // V1
    // IDEA: BRUTE FORCE
    // https://leetcode.com/problems/fibonacci-number/solutions/4586217/5-different-approach-full-explained-java-b4lh/
    int[] fib_nums = {
            0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181,
            6765, 10946, 17711, 28657, 46368, 75025, 121393, 196418, 317811, 514229, 832040,
            1346269, 2178309, 3524578, 5702887, 9227465, 14930352, 24157817, 39088169, 63245986,
            102334155, 165580141, 267914296, 433494437, 701408733, 1134903170, 1836311903
    };

    /**
     * time = O(N)
     * space = O(N)
     */
    public int fib_1(int n) {
        return fib_nums[n];
    }


    // V2
    // IDEA: DP
    // https://leetcode.com/problems/fibonacci-number/solutions/6032855/0-ms-runtime-beats-100-user-step-by-step-rnht/
    /**
     * time = O(N)
     * space = O(N)
     */
    public int fib_2(int n) {
        if (n <= 1)
            return n;
        int a = 0, b = 1;
        for (int i = 2; i <= n; i++) {
            int temp = b;
            b = a + b;
            a = temp;
        }
        return b;
    }


    // V3



}
