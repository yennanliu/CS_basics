package LeetCodeJava.Math;

// https://leetcode.com/problems/sum-of-largest-prime-substrings/description/

import java.util.*;

/**
 * 3556. Sum of Largest Prime Substrings
 * Medium
 *
 * Given a string s, find the sum of the 3 largest unique prime numbers that can be
 * formed using any of its substrings.
 *
 * Return the sum of the three largest unique prime numbers that can be formed. If
 * fewer than three exist, return the sum of all available primes. If no prime
 * numbers can be formed, return 0.
 *
 * Note: Each prime number should be counted only once, even if it appears in
 * multiple substrings. Additionally, when converting a substring to an integer,
 * any leading zeros are ignored.
 *
 * Example 1:
 *
 * Input: s = "12234"
 *
 * Output: 1469
 *
 * Explanation:
 *
 * The unique prime numbers formed from the substrings of "12234" are 2, 3, 23,
 * 223, and 1223.
 *
 * The 3 largest primes are 1223, 223, and 23. Their sum is 1469.
 *
 * Example 2:
 *
 * Input: s = "111"
 *
 * Output: 11
 *
 * Explanation:
 *
 * The unique prime number formed from the substrings of "111" is 11.
 *
 * Since there is only one prime number, the sum is 11.
 *
 * Constraints:
 *
 * 1 <= s.length <= 10
 *
 * s consists of only digits.
 *
 */
public class SumOfLargestPrimeSubstrings {

    // V0
    // IDEA: ENUMERATE ALL SUBSTRINGS, DEDUPLICATE IN A SET, TAKE THE TOP THREE PRIMES
    //       s has at most 10 characters -> at most 55 substrings, each < 10^10, so trial
    //       division up to sqrt is cheap. a TreeSet removes duplicates (and "07" parses to 7,
    //       which handles the leading-zero rule) and keeps the primes sorted.
    /**
     * time = O(N^2 * sqrt(10^N))    (N <= 10)
     * space = O(N^2)
     */
    public long sumOfLargestPrimes(String s) {
        int n = s.length();
        TreeSet<Long> primes = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                long value = Long.parseLong(s.substring(i, j));
                if (isPrime(value)) {
                    primes.add(value);
                }
            }
        }

        // add up to three of the largest distinct primes
        long sum = 0;
        int taken = 0;
        Iterator<Long> largestFirst = primes.descendingIterator();
        while (largestFirst.hasNext() && taken < 3) {
            sum += largestFirst.next();
            taken++;
        }
        return sum;
    }

    /** Trial division up to sqrt(x). */
    private boolean isPrime(long x) {
        if (x < 2) {
            return false;
        }
        if (x < 4) {
            return true;
        }
        if (x % 2 == 0) {
            return false;
        }
        for (long factor = 3; factor * factor <= x; factor += 2) {
            if (x % factor == 0) {
                return false;
            }
        }
        return true;
    }
}
