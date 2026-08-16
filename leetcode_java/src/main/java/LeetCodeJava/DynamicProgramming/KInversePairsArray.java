package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/k-inverse-pairs-array/description/
/**
 * 629. K Inverse Pairs Array
 * Hard
 *
 * For an integer array nums, an inverse pair is a pair of integers [i, j] where
 * 0 <= i < j < nums.length and nums[i] > nums[j].
 *
 * Given two integers n and k, return the number of different arrays consisting of
 * numbers from 1 to n such that there are exactly k inverse pairs. Since the answer
 * can be huge, return it modulo 10^9 + 7.
 *
 * Example 1:
 *
 * Input: n = 3, k = 0
 * Output: 1
 * Explanation: Only the array [1,2,3] which consists of numbers from 1 to 3 has exactly
 * 0 inverse pairs.
 *
 * Example 2:
 *
 * Input: n = 3, k = 1
 * Output: 2
 * Explanation: The array [1,3,2] and [2,1,3] have exactly 1 inverse pair.
 *
 * Constraints:
 *
 * 1 <= n <= 1000
 * 0 <= k <= 1000
 *
 */
public class KInversePairsArray {

    // V0
    // IDEA: DP + PREFIX SUM (rolling 1D array)
    /**
     *  DP def:
     *    - f[i][j] = # of permutations of 1..i having EXACTLY j inverse pairs
     *
     *  Transition: build the permutation of 1..i by INSERTING the largest number i
     *  into a permutation of 1..i-1. Inserting i at the position that leaves t
     *  numbers to its RIGHT creates exactly t new inverse pairs (i is bigger than
     *  everything after it), with t in [0, i-1]. So:
     *
     *    f[i][j] = sum_{t=0}^{i-1} f[i-1][j-t]
     *            = prefix[j+1] - prefix[lo]        (a SLIDING WINDOW of width i)
     *
     *  The inner sum is a CONTIGUOUS window of the previous row -> use prefix sums
     *  so each row costs O(k) instead of O(k * i).
     *
     *  NOTE !!! the subtraction of two mod values can go NEGATIVE in java
     *           -> add MOD before the final `% MOD`.
     *
     *  time  = O(n * k)
     *  space = O(k)
     */
    public int kInversePairs(int n, int k) {
        final int MOD = 1_000_000_007;

        // f = row for i = 0 : the empty permutation has 0 inverse pairs
        long[] f = new long[k + 1];
        f[0] = 1;

        for (int i = 1; i <= n; i++) {
            // prefix[j] = f[0] + ... + f[j-1]
            long[] prefix = new long[k + 2];
            for (int j = 0; j <= k; j++) {
                prefix[j + 1] = (prefix[j] + f[j]) % MOD;
            }

            long[] g = new long[k + 1];
            for (int j = 0; j <= k; j++) {
                // window is f[lo .. j], where lo = max(0, j - (i - 1))
                int lo = Math.max(0, j - (i - 1));
                g[j] = (prefix[j + 1] - prefix[lo] + MOD) % MOD;
            }
            f = g;
        }

        return (int) (f[k] % MOD);
    }

    // V0-1
    // IDEA: PLAIN 2D DP (no prefix sum)
    /**
     *  clearer, but O(n * k^2) -> would TLE on the maximum input.
     *  kept as the un-optimised reference of the same recurrence.
     *
     *  time  = O(n * k * k)
     *  space = O(n * k)
     */
    public int kInversePairs_0_1(int n, int k) {
        final int MOD = 1_000_000_007;

        long[][] f = new long[n + 1][k + 1];
        f[0][0] = 1;

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= k; j++) {
                // insert i so that t numbers end up to its right
                for (int t = 0; t <= Math.min(i - 1, j); t++) {
                    f[i][j] = (f[i][j] + f[i - 1][j - t]) % MOD;
                }
            }
        }

        return (int) f[n][k];
    }


    // V1
    // IDEA: SLIDING WINDOW SUM (no prefix array)
    /**
     *  The window f[j-i+1 .. j] moves by one as j advances, so it can be maintained
     *  incrementally: add f[j], drop f[j - i].
     *
     *  Removes the O(k) prefix array allocated per row -- one running total instead.
     *
     *  time  = O(n * k)
     *  space = O(k)
     */
    public int kInversePairs_1(int n, int k) {
        final int MOD = 1_000_000_007;
        long[] f = new long[k + 1];
        f[0] = 1;

        for (int i = 1; i <= n; i++) {
            long[] g = new long[k + 1];
            long window = 0;
            for (int j = 0; j <= k; j++) {
                window = (window + f[j]) % MOD;
                if (j - i >= 0) {
                    window = (window - f[j - i] + MOD) % MOD;
                }
                g[j] = window;
            }
            f = g;
        }
        return (int) f[k];
    }

    // V2
    // IDEA: IN-PLACE PREFIX TRICK f[j] += f[j-1], then subtract the tail
    /**
     *  Turn the row INTO its own prefix sum, read the window as a difference, then
     *  restore. One array, no second allocation per row.
     *
     *  The classic memory-tight rewrite of the sliding-window DP.
     *
     *  time  = O(n * k)
     *  space = O(k)
     */
    public int kInversePairs_2(int n, int k) {
        final int MOD = 1_000_000_007;
        long[] f = new long[k + 2];
        f[0] = 1;

        for (int i = 1; i <= n; i++) {
            // turn f into its prefix sums
            for (int j = 1; j <= k; j++) {
                f[j] = (f[j] + f[j - 1]) % MOD;
            }
            // read the window and write the new row backwards
            for (int j = k; j >= 0; j--) {
                long lo = (j - i >= 0) ? f[j - i] : 0;
                f[j] = (f[j] - lo + MOD) % MOD;
            }
        }
        return (int) f[k];
    }

    // V3
    // IDEA: BRUTE FORCE -- count inversions of every permutation
    /**
     *  Generate all n! permutations of 1..n and count those with exactly k
     *  inversions.
     *
     *  Only usable for n <= 8, but it counts the objects the statement describes,
     *  which is what validates the insertion recurrence.
     *
     *  time  = O(n! * n^2)
     *  space = O(n)
     */
    public int kInversePairs_3(int n, int k) {
        int[] perm = new int[n];
        for (int i = 0; i < n; i++) {
            perm[i] = i + 1;
        }
        int[] count = { 0 };
        permuteCount(perm, 0, k, count);
        return count[0];
    }

    private void permuteCount(int[] perm, int pos, int k, int[] count) {
        if (pos == perm.length) {
            int inv = 0;
            for (int i = 0; i < perm.length; i++) {
                for (int j = i + 1; j < perm.length; j++) {
                    if (perm[i] > perm[j]) {
                        inv += 1;
                    }
                }
            }
            if (inv == k) {
                count[0] += 1;
            }
            return;
        }
        for (int i = pos; i < perm.length; i++) {
            swapK(perm, pos, i);
            permuteCount(perm, pos + 1, k, count);
            swapK(perm, pos, i);
        }
    }

    private void swapK(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

}
