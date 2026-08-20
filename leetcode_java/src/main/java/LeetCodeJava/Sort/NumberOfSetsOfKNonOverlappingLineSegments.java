package LeetCodeJava.Sort;

// https://leetcode.com/problems/number-of-sets-of-k-non-overlapping-line-segments/

/**
 *  1621. Number of Sets of K Non-Overlapping Line Segments
 *  Medium
 *
 *  Given n points on a 1-D plane, where the ith point (from 0 to n-1) is at x = i,
 *  find the number of ways we can draw exactly k non-overlapping line segments
 *  such that each segment covers two or more points. The endpoints of each segment
 *  must have integral coordinates. The k line segments do not have to cover all n
 *  points, and they are allowed to share endpoints.
 *
 *  Return the number of ways we can draw k non-overlapping line segments. Since
 *  this number can be huge, return it modulo 10^9 + 7.
 *
 *  Example 1:
 *    Input: n = 4, k = 2
 *    Output: 5
 *    Explanation: {(0,2),(2,3)}, {(0,1),(1,3)}, {(0,1),(2,3)}, {(1,2),(2,3)},
 *                 {(0,1),(1,2)}
 *
 *  Example 2:
 *    Input: n = 3, k = 1
 *    Output: 3
 *
 *  Example 3:
 *    Input: n = 30, k = 7
 *    Output: 796297179
 *
 *  Constraints:
 *    2 <= n <= 1000
 *    1 <= k <= n-1
 */
public class NumberOfSetsOfKNonOverlappingLineSegments {

    // V0
    // IDEA: COMBINATORICS - the answer is C(n + k - 1, 2k)
    //       a configuration is fixed by its 2k endpoints x1 <= x2 <= ... <= x2k in
    //       [0, n-1], the only constraints being x1 < x2, x3 < x4, ... (a segment
    //       covers >= 2 points) while CONSECUTIVE segments may share an endpoint
    //       (x2 <= x3 is allowed).
    //
    //       the standard "make it strict" substitution yi = xi + (i-1) turns this
    //       weakly-increasing-with-k-strict-gaps sequence into a strictly
    //       increasing choice of 2k values out of n + k - 1 slots -> C(n+k-1, 2k).
    //
    //       sanity check: n=4,k=2 -> C(5,4) = 5 ; n=3,k=1 -> C(3,2) = 3.
    //
    //       computed with factorials mod 1e9+7 and a Fermat modular inverse.
    /**
     * time = O(N + log MOD)
     * space = O(N)
     */
    public int numberOfSets(int n, int k) {
        final long MOD = 1_000_000_007L;
        int top = n + k - 1;
        int r = 2 * k;
        if (r > top) {
            return 0;
        }

        long[] fact = new long[top + 1];
        fact[0] = 1L;
        for (int i = 1; i <= top; i++) {
            fact[i] = fact[i - 1] * i % MOD;
        }

        // C(top, r) = top! / (r! * (top - r)!)
        long denom = fact[r] * fact[top - r] % MOD;
        return (int) (fact[top] * modPow(denom, MOD - 2, MOD) % MOD);
    }

    private long modPow(long base, long exp, long mod) {
        long res = 1L;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1L) == 1L) {
                res = res * base % mod;
            }
            base = base * base % mod;
            exp >>= 1;
        }
        return res;
    }
}
