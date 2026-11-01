package LeetCodeJava.Math;

// https://leetcode.com/problems/find-the-count-of-numbers-which-are-not-special/description/

/**
 * 3233. Find the Count of Numbers Which Are Not Special
 * Medium
 *
 * You are given 2 positive integers l and r. For any number x, all positive divisors of x except x are
 * called the proper divisors of x.
 *
 * A number is called special if it has exactly 2 proper divisors. For example:
 *
 * The number 4 is special because it has proper divisors 1 and 2.
 * The number 6 is not special because it has proper divisors 1, 2, and 3.
 *
 * Return the count of numbers in the range [l, r] that are not special.
 *
 *
 * Example 1:
 *
 * Input: l = 5, r = 7
 * Output: 3
 * Explanation:
 * There are no special numbers in the range [5, 7].
 *
 * Example 2:
 *
 * Input: l = 4, r = 16
 * Output: 11
 * Explanation:
 * The special numbers in the range [4, 16] are 4 and 9.
 *
 *
 * Constraints:
 *
 * 1 <= l <= r <= 10^9
 *
 */
public class FindTheCountOfNumbersWhichAreNotSpecial {

    // V0
    // IDEA: SPECIAL <=> SQUARE OF A PRIME; SIEVE PRIMES UP TO sqrt(r) AND COUNT p WITH l <= p^2 <= r
    /**
     * time = O(sqrt(R) log log sqrt(R))
     * space = O(sqrt(R))
     */
    public int nonSpecialCount(int l, int r) {
        // largest integer whose square is <= r (fix floating-point rounding both ways)
        int limit = (int) Math.sqrt(r);
        while ((long) (limit + 1) * (limit + 1) <= r) {
            limit++;
        }
        while ((long) limit * limit > r) {
            limit--;
        }

        // sieve of Eratosthenes on [0, limit]
        boolean[] isComposite = new boolean[limit + 1];
        for (int p = 2; (long) p * p <= limit; p++) {
            if (!isComposite[p]) {
                for (int multiple = p * p; multiple <= limit; multiple += p) {
                    isComposite[multiple] = true;
                }
            }
        }

        int specialCount = 0;
        for (int p = 2; p <= limit; p++) {
            // p^2 <= r by the choice of limit; also need p^2 >= l
            if (!isComposite[p] && (long) p * p >= l) {
                specialCount++;
            }
        }
        return (r - l + 1) - specialCount;
    }
}
