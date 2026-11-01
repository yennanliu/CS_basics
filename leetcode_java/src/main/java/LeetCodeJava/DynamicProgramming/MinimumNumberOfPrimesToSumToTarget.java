package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-number-of-primes-to-sum-to-target/description/

import java.util.*;

/**
 * 3610. Minimum Number of Primes to Sum to Target
 * Medium
 *
 * You are given two integers n and m.
 *
 * You have to select a multiset of prime numbers from the first m prime numbers
 * such that the sum of the selected primes is exactly n. You may use each prime
 * number multiple times.
 *
 * Return the minimum number of prime numbers needed to sum up to n, or -1 if it
 * is not possible.
 *
 *
 * Example 1:
 *
 * Input: n = 10, m = 2
 * Output: 4
 * Explanation:
 * The first 2 primes are [2, 3]. The sum 10 can be formed as 2 + 2 + 3 + 3,
 * requiring 4 primes.
 *
 * Example 2:
 *
 * Input: n = 15, m = 5
 * Output: 3
 * Explanation:
 * The first 5 primes are [2, 3, 5, 7, 11]. The sum 15 can be formed as 5 + 5 + 5,
 * requiring 3 primes.
 *
 * Example 3:
 *
 * Input: n = 7, m = 6
 * Output: 1
 * Explanation:
 * The first 6 primes are [2, 3, 5, 7, 11, 13]. The sum 7 can be formed directly by
 * prime 7, requiring only 1 prime.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 1000
 * 1 <= m <= 1000
 *
 */
public class MinimumNumberOfPrimesToSumToTarget {

    // V0
    // IDEA: SIEVE THE FIRST M PRIMES, THEN UNBOUNDED COIN CHANGE (MIN COINS, LC 322)
    //   minCount[i] = fewest primes summing to exactly i
    //   for each prime p, for i from p UP to n: minCount[i] = min(minCount[i], minCount[i - p] + 1)
    //   iterating i upward lets the same prime be reused. Primes above n can never
    //   be used, so the sieve stops at n.
    /**
     * time = O(N * min(M, pi(N)))
     * space = O(N)
     */
    public int minNumberOfPrimes(int n, int m) {
        // the first m primes, ignoring those bigger than n
        List<Integer> primes = new ArrayList<>();
        boolean[] composite = new boolean[n + 1];
        for (int x = 2; x <= n && primes.size() < m; x++) {
            if (composite[x]) {
                continue;
            }
            primes.add(x);
            for (long multiple = (long) x * x; multiple <= n; multiple += x) {
                composite[(int) multiple] = true;
            }
        }

        final int INF = Integer.MAX_VALUE;
        int[] minCount = new int[n + 1];
        Arrays.fill(minCount, INF);
        minCount[0] = 0;
        for (int prime : primes) {
            for (int total = prime; total <= n; total++) {
                if (minCount[total - prime] != INF) {
                    minCount[total] = Math.min(minCount[total], minCount[total - prime] + 1);
                }
            }
        }
        return minCount[n] == INF ? -1 : minCount[n];
    }
}
