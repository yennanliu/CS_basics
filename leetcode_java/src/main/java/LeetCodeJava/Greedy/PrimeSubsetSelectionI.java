package LeetCodeJava.Greedy;

// https://leetcode.com/problems/prime-subset-selection-i/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 4079. Prime Subset Selection I
 * Medium
 *
 * You are given two integers n and s.
 *
 * Consider all prime numbers less than or equal to n. Select some of these
 * primes such that:
 *
 * - The sum of the selected primes is less than or equal to s.
 * - The number of selected primes is maximized.
 * - Each prime can be selected at most once.
 *
 * Return an integer array containing the selected primes in increasing order.
 *
 * If multiple optimal selections exist, return any of them.
 *
 * If no prime can be selected, return an empty array.
 *
 *
 * Example 1:
 *
 * Input: n = 7, s = 20
 * Output: [2,3,5,7]
 * Explanation:
 * - The prime numbers less than or equal to n = 7 are 2, 3, 5, and 7.
 * - The sum of all these primes is 2 + 3 + 5 + 7 = 17, which is less than or
 *   equal to s = 20.
 * - Thus, the answer is [2, 3, 5, 7].
 *
 * Example 2:
 *
 * Input: n = 15, s = 15
 * Output: [3,5,7]
 * Explanation:
 * - The prime numbers less than or equal to n = 15 are 2, 3, 5, 7, 11, and 13.
 * - One possible selection is [3, 5, 7], whose sum is 3 + 5 + 7 = 15.
 * - The four smallest available primes have sum 2 + 3 + 5 + 7 = 17, which
 *   exceeds s = 15. Therefore, selecting four or more primes is impossible, and
 *   the maximum number of selected primes is three.
 * - Thus, [3, 5, 7] is one possible answer. Other valid answers include
 *   [2, 3, 5], [2, 3, 7], and [2, 5, 7].
 *
 * Example 3:
 *
 * Input: n = 5, s = 1
 * Output: []
 * Explanation:
 * - The smallest prime number is 2, which is greater than s = 1.
 * - Thus, no prime can be selected, so the answer is an empty array.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^6
 * 1 <= s <= 10^9
 *
 */
public class PrimeSubsetSelectionI {

    // V0
    // IDEA: SIEVE OF ERATOSTHENES + GREEDY (TAKE THE SMALLEST PRIMES FIRST)
    // to maximise HOW MANY primes fit under the budget s, spend it on the cheapest ones:
    // the k smallest primes have the smallest sum of any k primes, so if they do not fit, no k primes fit.
    // e.g. n = 15, s = 15 -> 2 + 3 + 5 = 10, + 7 = 17 > 15 -> [2, 3, 5] (any 3 primes summing <= 15 is accepted)
    /**
     * time = O(N log log N)
     * space = O(N)
     */
    public List<Integer> maxPrimes(int n, int s) {
        List<Integer> selected = new ArrayList<>();
        // edge case: there is no prime <= 1
        if (n < 2) {
            return selected;
        }

        // isComposite[x] == false means x is prime, for 2 <= x <= n
        boolean[] isComposite = new boolean[n + 1];
        for (long i = 2; i * i <= n; i++) {
            if (!isComposite[(int) i]) {
                // every multiple of i from i*i on is composite
                // (smaller multiples were already crossed out by smaller primes)
                for (long multiple = i * i; multiple <= n; multiple += i) {
                    isComposite[(int) multiple] = true;
                }
            }
        }

        // greedy: walk the primes in increasing order, take each while it still fits
        long total = 0;
        for (int x = 2; x <= n; x++) {
            if (isComposite[x]) {
                continue;
            }
            // primes only get bigger, so the first one that does not fit means none of the later ones do
            if (total + x > s) {
                break;
            }
            selected.add(x);
            total += x;
        }
        return selected;
    }

    // V1
    // IDEA: TRIAL DIVISION BY THE PRIMES FOUND SO FAR + GREEDY, STOP EARLY
    // same greedy as V0, but primes are generated one by one in increasing order instead of sieving [2, n]:
    // a candidate is prime iff no already-found prime p with p * p <= candidate divides it.
    // the selected primes ARE the found primes, so the loop stops as soon as the budget runs out
    // (the sum of primes up to x grows like x^2, so with s <= 10^9 we never test much past ~1.6 * 10^5).
    // V0 is the one to write in an interview; V1 shows why the early stop matters when s is small vs n.
    /**
     * time = O(M * sqrt(M) / log M)   (M = min(n, largest prime that still fits))
     * space = O(M / log M)
     */
    public List<Integer> maxPrimes_1(int n, int s) {
        List<Integer> selected = new ArrayList<>();
        long total = 0;

        for (int candidate = 2; candidate <= n; candidate++) {
            if (!isPrime(candidate, selected)) {
                continue;
            }
            // the budget ran out: every later prime is bigger, so stop
            if (total + candidate > s) {
                break;
            }
            selected.add(candidate);
            total += candidate;
        }
        return selected;
    }

    /** True if no prime p <= sqrt(candidate) in primes divides candidate. */
    private boolean isPrime(int candidate, List<Integer> primes) {
        for (int p : primes) {
            if ((long) p * p > candidate) {
                break;
            }
            if (candidate % p == 0) {
                return false;
            }
        }
        return true;
    }
}
