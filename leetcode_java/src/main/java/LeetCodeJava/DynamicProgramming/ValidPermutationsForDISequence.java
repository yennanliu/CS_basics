package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/valid-permutations-for-di-sequence/description/
/**
 * 903. Valid Permutations for DI Sequence
 * Hard
 *
 * You are given a string s of length n where s[i] is either:
 *
 * 'D' means decreasing, or
 * 'I' means increasing.
 *
 * A permutation perm of n + 1 integers of all the integers in the range [0, n] is called
 * a valid permutation if for all valid i:
 *
 * If s[i] == 'D', then perm[i] > perm[i + 1], and
 * If s[i] == 'I', then perm[i] < perm[i + 1].
 *
 * Return the number of valid permutations perm. Since the answer may be large, return it
 * modulo 10^9 + 7.
 *
 * Example 1:
 *
 * Input: s = "DID"
 * Output: 5
 * Explanation: The 5 valid permutations of (0, 1, 2, 3) are:
 * (1, 0, 3, 2)
 * (2, 0, 3, 1)
 * (2, 1, 3, 0)
 * (3, 0, 2, 1)
 * (3, 1, 2, 0)
 *
 * Example 2:
 *
 * Input: s = "D"
 * Output: 1
 *
 * Constraints:
 *
 * n == s.length
 * 1 <= n <= 200
 * s[i] is either 'I' or 'D'.
 *
 */
public class ValidPermutationsForDISequence {

    // V0
    // IDEA: DP + PREFIX SUM
    /**
     *  DP def:
     *     - f[i][j] = number of valid arrangements of the first i + 1 positions
     *                 where the value placed at position i has RELATIVE RANK j
     *                 among the (i + 1) values used so far (0 = smallest)
     *
     *     NOTE !!! using RELATIVE RANK (instead of the absolute value) is the trick --
     *              it makes the state independent of which concrete numbers were used.
     *
     *  Init:
     *     - f[0][0] = 1
     *
     *  DP eq:
     *     - s[i-1] == 'D' (must go DOWN):  f[i][j] = sum(f[i-1][k]) for k in [j, i-1]
     *     - s[i-1] == 'I' (must go UP):    f[i][j] = sum(f[i-1][k]) for k in [0, j-1]
     *
     *  Both are CONTIGUOUS range sums over the previous row, so a PREFIX SUM
     *  turns the naive O(n^3) into O(n^2).
     *
     *  Answer: sum(f[n][j]) for j in [0, n]
     *
     *  time  = O(n^2)
     *  space = O(n)
     */
    public int numPermsDISequence(String s) {
        final int MOD = 1_000_000_007;
        int n = s.length();

        long[] f = new long[n + 1];
        f[0] = 1;

        for (int i = 1; i <= n; i++) {
            // pre[t] = f[0] + f[1] + ... + f[t-1]  (PREVIOUS row)
            long[] pre = new long[n + 2];
            for (int t = 0; t <= n; t++) {
                pre[t + 1] = (pre[t] + f[t]) % MOD;
            }

            long[] nxt = new long[n + 1];
            for (int j = 0; j <= i; j++) {
                if (s.charAt(i - 1) == 'D') {
                    // sum over k in [j, i-1]
                    nxt[j] = (pre[i] - pre[j] + MOD) % MOD;
                } else {
                    // sum over k in [0, j-1]
                    nxt[j] = pre[j];
                }
            }
            f = nxt;
        }

        long res = 0;
        for (long v : f) {
            res = (res + v) % MOD;
        }
        return (int) res;
    }


    // V1
    // IDEA: 2D TABLE (no prefix sums) -- the O(n^3) reference
    /**
     *  f[i][j] filled with an explicit inner SUM rather than a prefix array.
     *
     *  O(n^3) so it is slower, but it states the recurrence literally -- the prefix
     *  optimisation in V0 is exactly this sum turned into a range query.
     *
     *  time  = O(n^3)
     *  space = O(n^2)
     */
    public int numPermsDISequence_1(String s) {
        final int MOD = 1_000_000_007;
        int n = s.length();
        long[][] f = new long[n + 1][n + 1];
        f[0][0] = 1;

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= i; j++) {
                long total = 0;
                if (s.charAt(i - 1) == 'D') {
                    for (int k = j; k <= i - 1; k++) {
                        total += f[i - 1][k];
                    }
                } else {
                    for (int k = 0; k <= j - 1; k++) {
                        total += f[i - 1][k];
                    }
                }
                f[i][j] = total % MOD;
            }
        }

        long res = 0;
        for (int j = 0; j <= n; j++) {
            res = (res + f[n][j]) % MOD;
        }
        return (int) res;
    }

    // V2
    // IDEA: SUFFIX/PREFIX SUM depending on the direction (single pass per row)
    /**
     *  'D' needs a SUFFIX sum of the previous row and 'I' needs a PREFIX sum, so
     *  building whichever one the current character calls for -- and nothing else --
     *  halves the per-row work.
     *
     *  time  = O(n^2)
     *  space = O(n)
     */
    public int numPermsDISequence_2(String s) {
        final int MOD = 1_000_000_007;
        int n = s.length();
        long[] f = new long[n + 1];
        f[0] = 1;

        for (int i = 1; i <= n; i++) {
            long[] nxt = new long[n + 1];
            if (s.charAt(i - 1) == 'D') {
                // suffix sums of the previous row
                long running = 0;
                for (int j = i - 1; j >= 0; j--) {
                    running = (running + f[j]) % MOD;
                    nxt[j] = running;
                }
            } else {
                // prefix sums of the previous row
                long running = 0;
                for (int j = 0; j <= i - 1; j++) {
                    nxt[j + 1] = (running = (running + f[j]) % MOD);
                }
            }
            f = nxt;
        }

        long res = 0;
        for (long v : f) {
            res = (res + v) % MOD;
        }
        return (int) res;
    }

    // V3
    // IDEA: BRUTE FORCE -- enumerate every permutation of 0..n
    /**
     *  Generate all (n+1)! permutations and count those matching the pattern.
     *
     *  Only usable for n <= ~8, but it counts the objects the statement defines,
     *  which is what validates the relative-rank state.
     *
     *  time  = O((n+1)! * n)
     *  space = O(n)
     */
    public int numPermsDISequence_3(String s) {
        int n = s.length();
        int[] perm = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            perm[i] = i;
        }
        int[] count = { 0 };
        permDI(perm, 0, s, count);
        return count[0];
    }

    private void permDI(int[] perm, int pos, String s, int[] count) {
        if (pos == perm.length) {
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == 'D' && perm[i] <= perm[i + 1]) {
                    return;
                }
                if (s.charAt(i) == 'I' && perm[i] >= perm[i + 1]) {
                    return;
                }
            }
            count[0] += 1;
            return;
        }
        for (int i = pos; i < perm.length; i++) {
            int t = perm[pos];
            perm[pos] = perm[i];
            perm[i] = t;
            permDI(perm, pos + 1, s, count);
            t = perm[pos];
            perm[pos] = perm[i];
            perm[i] = t;
        }
    }

}
