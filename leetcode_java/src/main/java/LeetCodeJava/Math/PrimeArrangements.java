package LeetCodeJava.Math;

// https://leetcode.com/problems/prime-arrangements/description/

/**
 * 1175. Prime Arrangements
 * Easy
 *
 * Return the number of permutations of 1 to n so that prime numbers are at prime indices
 * (1-indexed.)
 *
 * (Recall that an integer is prime if and only if it is greater than 1, and cannot be written as
 * a product of two positive integers both smaller than it.)
 *
 * Since the answer may be large, return the answer modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: n = 5
 * Output: 12
 * Explanation: For example [1,2,5,4,3] is a valid permutation, but [5,2,3,4,1] is not because
 * the prime number 5 is at index 1.
 *
 * Example 2:
 *
 * Input: n = 100
 * Output: 682289015
 *
 *
 * Constraints:
 *
 * 1 <= n <= 100
 *
 */
public class PrimeArrangements {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: SIEVE OF ERATOSTHENES + FACTORIALS
    //   with primeCount primes in [1, n] there are primeCount prime indices:
    //   primes permute among prime indices (primeCount!), the rest among the rest
    //   ((n - primeCount)!), so answer = primeCount! * (n - primeCount)! mod 1e9+7.
    /**
     * time = O(N log log N)
     * space = O(N)
     */
    public int numPrimeArrangements(int n) {
        // sieve: isComposite[i] is true once a smaller prime divides i
        boolean[] isComposite = new boolean[n + 1];
        int primeCount = 0;
        for (int i = 2; i <= n; i++) {
            if (isComposite[i]) {
                continue;
            }
            primeCount++;
            // multiples below i * i were already crossed out by smaller primes
            for (long multiple = (long) i * i; multiple <= n; multiple += i) {
                isComposite[(int) multiple] = true;
            }
        }

        long result = factorialMod(primeCount) * factorialMod(n - primeCount) % MOD;
        return (int) result;
    }

    /** x! modulo MOD. */
    private long factorialMod(int x) {
        long result = 1;
        for (int k = 2; k <= x; k++) {
            result = result * k % MOD;
        }
        return result;
    }
}
