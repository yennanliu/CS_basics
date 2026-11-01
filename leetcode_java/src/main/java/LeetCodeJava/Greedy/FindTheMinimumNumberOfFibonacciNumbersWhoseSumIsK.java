package LeetCodeJava.Greedy;

// https://leetcode.com/problems/find-the-minimum-number-of-fibonacci-numbers-whose-sum-is-k/description/

import java.util.*;

/**
 * 1414. Find the Minimum Number of Fibonacci Numbers Whose Sum Is K
 * Medium
 *
 * Given an integer k, return the minimum number of Fibonacci numbers whose sum is equal to k.
 * The same Fibonacci number can be used multiple times.
 *
 * The Fibonacci numbers are defined as:
 *
 * F1 = 1
 * F2 = 1
 * Fn = Fn-1 + Fn-2 for n > 2.
 *
 * It is guaranteed that for the given constraints we can always find such Fibonacci numbers
 * that sum up to k.
 *
 *
 * Example 1:
 *
 * Input: k = 7
 * Output: 2
 * Explanation: The Fibonacci numbers are: 1, 1, 2, 3, 5, 8, 13, ...
 * For k = 7 we can use 2 + 5 = 7.
 *
 * Example 2:
 *
 * Input: k = 10
 * Output: 2
 * Explanation: For k = 10 we can use 2 + 8 = 10.
 *
 * Example 3:
 *
 * Input: k = 19
 * Output: 3
 * Explanation: For k = 19 we can use 1 + 5 + 13 = 19.
 *
 *
 * Constraints:
 *
 * 1 <= k <= 10^9
 *
 */
public class FindTheMinimumNumberOfFibonacciNumbersWhoseSumIsK {

    // V0
    // IDEA: GREEDY (ZECKENDORF): always subtract the largest Fibonacci number <= k
    /**
     * time = O(log K)
     * space = O(log K)
     */
    public int findMinFibonacciNumbers(int k) {
        // all Fibonacci numbers up to k (they grow exponentially, so ~45 of them)
        List<Integer> fib = new ArrayList<>();
        fib.add(1);
        fib.add(1);
        while (true) {
            int size = fib.size();
            // compare as long: the next number can pass Integer.MAX_VALUE near k = 10^9
            long next = (long) fib.get(size - 1) + fib.get(size - 2);
            if (next > k) {
                break;
            }
            fib.add((int) next);
        }

        // if F[i] <= k < F[i+1], then k - F[i] < F[i-1], so the greedy never
        // needs the same number twice and no decomposition uses fewer terms
        int count = 0;
        int remaining = k;
        int i = fib.size() - 1;
        while (remaining > 0) {
            if (fib.get(i) <= remaining) {
                remaining -= fib.get(i);
                count++;
            }
            i--;
        }
        return count;
    }
}
