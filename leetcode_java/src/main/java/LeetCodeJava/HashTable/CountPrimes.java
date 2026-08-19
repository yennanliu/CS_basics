package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-primes/

import java.util.*;

/**
 *  204. Count Primes
 *  Medium
 *
 *  Given an integer n, return the number of prime numbers that are strictly
 *  less than n.
 *
 *  Example 1:
 *  Input: n = 10
 *  Output: 4
 *  Explanation: there are 4 primes less than 10 : 2, 3, 5, 7.
 *
 *  Example 2:
 *  Input: n = 0
 *  Output: 0
 *
 *  Example 3:
 *  Input: n = 1
 *  Output: 0
 *
 *  Constraints:
 *   - 0 <= n <= 5 * 10^6
 */
public class CountPrimes {

    // V0
    // IDEA: SIEVE OF ERATOSTHENES
    /**
     * time = O(n log log n)
     * space = O(n)
     */
    public int countPrimes(int n) {

        if (n < 3) {
            return 0;
        }

        boolean[] notPrime = new boolean[n]; // notPrime[i] : i is NOT a prime
        int res = 0;

        for (int i = 2; i < n; i++) {
            if (notPrime[i]) {
                continue;
            }
            res++;
            /**
             *  NOTE !!!
             *
             *  start from i * i (smaller multiples are already marked),
             *  use `long` on the product to avoid int overflow
             */
            for (long j = (long) i * i; j < n; j += i) {
                notPrime[(int) j] = true;
            }
        }

        return res;
    }
}
