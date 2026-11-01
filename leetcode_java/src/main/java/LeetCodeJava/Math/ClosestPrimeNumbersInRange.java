package LeetCodeJava.Math;

// https://leetcode.com/problems/closest-prime-numbers-in-range/description/

/**
 * 2523. Closest Prime Numbers in Range
 * Medium
 *
 * Given two positive integers left and right, find the two integers num1 and num2
 * such that:
 *
 * left <= num1 < num2 <= right.
 * Both num1 and num2 are prime numbers.
 * num2 - num1 is the minimum amongst all other pairs satisfying the above conditions.
 *
 * Return the positive integer array ans = [num1, num2]. If there are multiple pairs
 * satisfying these conditions, return the one with the smallest num1 value.
 * If no such numbers exist, return [-1, -1].
 *
 *
 * Example 1:
 *
 * Input: left = 10, right = 19
 * Output: [11,13]
 * Explanation: The prime numbers between 10 and 19 are 11, 13, 17, and 19.
 * The closest gap between any pair is 2, which can be achieved by [11,13] or [17,19].
 * Since 11 is smaller than 17, we return the first pair.
 *
 * Example 2:
 *
 * Input: left = 4, right = 6
 * Output: [-1,-1]
 * Explanation: There exists only one prime number in the given range, so the
 * conditions cannot be satisfied.
 *
 *
 * Constraints:
 *
 * 1 <= left <= right <= 10^6
 *
 */
public class ClosestPrimeNumbersInRange {

    // V0
    // IDEA: SIEVE OF ERATOSTHENES + SCAN ADJACENT PRIMES (the closest pair is always two consecutive primes)
    /**
     * time = O(R log log R)
     * space = O(R)
     */
    public int[] closestPrimes(int left, int right) {
        int[] result = {-1, -1};
        if (right < 2) {
            return result;
        }

        // isComposite[v] == true means v is NOT prime
        boolean[] isComposite = new boolean[right + 1];
        isComposite[0] = true;
        isComposite[1] = true;
        for (int i = 2; (long) i * i <= right; i++) {
            if (!isComposite[i]) {
                // smaller multiples of i were already crossed out by a smaller prime
                for (int j = i * i; j <= right; j += i) {
                    isComposite[j] = true;
                }
            }
        }

        int bestGap = Integer.MAX_VALUE;
        int prevPrime = -1;
        for (int v = Math.max(left, 2); v <= right; v++) {
            if (isComposite[v]) {
                continue;
            }
            // strict "<" keeps the FIRST pair on ties (smallest num1)
            if (prevPrime != -1 && v - prevPrime < bestGap) {
                bestGap = v - prevPrime;
                result[0] = prevPrime;
                result[1] = v;
            }
            prevPrime = v;
        }
        return result;
    }
}
