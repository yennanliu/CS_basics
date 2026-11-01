package LeetCodeJava.Math;

// https://leetcode.com/problems/count-ways-to-make-array-with-product/description/

/**
 * 1735. Count Ways to Make Array With Product
 * Hard
 *
 * You are given a 2D integer array, queries. For each queries[i], where queries[i] = [ni, ki], find the number of different ways you can place positive integers into an array of size ni such that the product of the integers is ki. As the number of ways may be too large, the answer to the ith query is the number of ways modulo 10^9 + 7.
 *
 * Return an integer array answer where answer.length == queries.length, and answer[i] is the answer to the ith query.
 *
 *
 * Example 1:
 *
 * Input: queries = [[2,6],[5,1],[73,660]]
 * Output: [4,1,50734910]
 * Explanation: Each query is independent.
 * [2,6]: There are 4 ways to fill an array of size 2 that multiply to 6: [1,6], [2,3], [3,2], [6,1].
 * [5,1]: There is 1 way to fill an array of size 5 that multiply to 1: [1,1,1,1,1].
 * [73,660]: There are 1050734917 ways to fill an array of size 73 that multiply to 660. 1050734917 modulo 10^9 + 7 = 50734910.
 *
 * Example 2:
 *
 * Input: queries = [[1,1],[2,2],[3,3],[4,4],[5,5]]
 * Output: [1,2,3,10,5]
 *
 *
 * Constraints:
 *
 * 1 <= queries.length <= 10^4
 * 1 <= ni, ki <= 10^4
 *
 */
public class CountWaysToMakeArrayWithProduct {

    private static final int MOD = 1_000_000_007;
    private static final int LIMIT = 10_000;
    // n + e - 1 <= 10^4 + log2(10^4), so a little headroom is enough
    private static final int SIZE = LIMIT + 20;

    // V0
    // IDEA: PRIME FACTORISATION + STARS AND BARS
    //   k = p1^e1 * p2^e2 * ...; each prime is split among the n slots independently, so
    //   ways(n, k) = product over primes of C(e + n - 1, n - 1)  (e identical items into n boxes).
    //   precompute a smallest-prime-factor sieve (factorise in O(log k))
    //   and factorials + inverse factorials mod 1e9+7 (C(a, b) in O(1), inverse via Fermat).
    //   k = 1 has no prime factors -> empty product = 1 (all ones).
    /**
     * time = O(K log log K + Q * log K)
     * space = O(K)
     */
    public int[] waysToFillArray(int[][] queries) {
        int[] smallestPrime = buildSmallestPrimeFactor();

        long[] fact = new long[SIZE];
        fact[0] = 1;
        for (int i = 1; i < SIZE; i++) {
            fact[i] = fact[i - 1] * i % MOD;
        }
        long[] invFact = new long[SIZE];
        invFact[SIZE - 1] = modPow(fact[SIZE - 1], MOD - 2);
        for (int i = SIZE - 1; i > 0; i--) {
            invFact[i - 1] = invFact[i] * i % MOD;
        }

        int[] answer = new int[queries.length];
        for (int q = 0; q < queries.length; q++) {
            int n = queries[q][0];
            int k = queries[q][1];
            long ways = 1;
            while (k > 1) {
                int prime = smallestPrime[k];
                int exponent = 0;
                while (k % prime == 0) {
                    k /= prime;
                    exponent++;
                }
                // C(exponent + n - 1, exponent)
                int top = exponent + n - 1;
                long combination = fact[top] * invFact[exponent] % MOD * invFact[top - exponent] % MOD;
                ways = ways * combination % MOD;
            }
            answer[q] = (int) ways;
        }
        return answer;
    }

    /** spf[x] = smallest prime dividing x, for x in [2, LIMIT]. */
    private int[] buildSmallestPrimeFactor() {
        int[] spf = new int[LIMIT + 1];
        for (int i = 0; i <= LIMIT; i++) {
            spf[i] = i;
        }
        for (int i = 2; i * i <= LIMIT; i++) {
            // i is prime iff nothing smaller marked it
            if (spf[i] != i) {
                continue;
            }
            for (int j = i * i; j <= LIMIT; j += i) {
                if (spf[j] == j) {
                    spf[j] = i;
                }
            }
        }
        return spf;
    }

    /** base^exponent mod MOD by repeated squaring. */
    private long modPow(long base, long exponent) {
        long result = 1;
        base %= MOD;
        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = result * base % MOD;
            }
            base = base * base % MOD;
            exponent /= 2;
        }
        return result;
    }
}
