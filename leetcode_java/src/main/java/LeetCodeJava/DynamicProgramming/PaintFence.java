package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/paint-fence/

/**
 *  276. Paint Fence
 *  Medium
 *
 *  You are painting a fence of n posts with k different colors. You must paint the posts
 *  following these rules:
 *
 *   - Every post must be painted exactly one color.
 *   - There cannot be three or more consecutive posts with the same color.
 *     (i.e. no more than two adjacent posts may share a color)
 *
 *  Given the two integers n and k, return the number of ways you can paint the fence.
 *
 *  Example 1:
 *  Input: n = 3, k = 2
 *  Output: 6
 *
 *  Example 2:
 *  Input: n = 1, k = 1
 *  Output: 1
 *
 *  Example 3:
 *  Input: n = 7, k = 2
 *  Output: 42
 *
 *  Constraints:
 *
 *   1 <= n <= 50
 *   1 <= k <= 10^5
 *   The testcases are generated such that the answer fits in a 32-bit integer.
 */
public class PaintFence {

    // V0
    // IDEA: DP with 2 rolling states
    //       same = # ways where post i has the SAME color as post i-1
    //       diff = # ways where post i has a DIFFERENT color from post i-1
    //
    //       same[i] = diff[i-1]                        (can't extend a same-pair again)
    //       diff[i] = (same[i-1] + diff[i-1]) * (k-1)
    /**
     * time = O(n)
     * space = O(1)
     */
    public int numWays(int n, int k) {
        if (n == 0 || k == 0) {
            return 0;
        }
        if (n == 1) {
            return k;
        }
        long same = k;                     // post 0 and post 1 share a color
        long diff = (long) k * (k - 1);    // post 0 and post 1 differ
        for (int i = 2; i < n; i++) {
            long prevSame = same;
            long prevDiff = diff;
            same = prevDiff;
            diff = (prevSame + prevDiff) * (k - 1);
        }
        return (int) (same + diff);
    }

    // V1
    // IDEA: TOP-DOWN MEMOIZATION on the merged recurrence
    //       f(n) = (k-1) * (f(n-1) + f(n-2))
    //       (post n differs from post n-1  ->  f(n-1) choices;
    //        post n equals post n-1, which then had to differ from n-2 -> f(n-2) choices)
    /**
     * time = O(n)
     * space = O(n)
     */
    public int numWays_1(int n, int k) {
        if (n == 0 || k == 0) {
            return 0;
        }
        long[] memo = new long[n + 1];
        Arrays.fill(memo, -1);
        return (int) helper_1(n, k, memo);
    }

    private long helper_1(int n, int k, long[] memo) {
        if (n == 1) {
            return k;
        }
        if (n == 2) {
            return (long) k * k;
        }
        if (memo[n] >= 0) {
            return memo[n];
        }
        memo[n] = (long) (k - 1) * (helper_1(n - 1, k, memo) + helper_1(n - 2, k, memo));
        return memo[n];
    }

    // V2
    // IDEA: MATRIX EXPONENTIATION of the same linear recurrence
    //       [f(n), f(n-1)]^T = [[k-1, k-1], [1, 0]]^(n-2) * [f(2), f(1)]^T
    //       -> O(log n) instead of O(n)
    /**
     * time = O(log n)
     * space = O(1)
     */
    public int numWays_2(int n, int k) {
        if (n == 0 || k == 0) {
            return 0;
        }
        if (n == 1) {
            return k;
        }
        if (n == 2) {
            return (int) ((long) k * k);
        }
        long[][] pow = matPow_2(new long[][] { { k - 1, k - 1 }, { 1, 0 } }, n - 2);
        long f2 = (long) k * k;
        long f1 = k;
        return (int) (pow[0][0] * f2 + pow[0][1] * f1);
    }

    private long[][] matPow_2(long[][] m, int p) {
        long[][] res = { { 1, 0 }, { 0, 1 } };
        while (p > 0) {
            if ((p & 1) == 1) {
                res = matMul_2(res, m);
            }
            m = matMul_2(m, m);
            p >>= 1;
        }
        return res;
    }

    private long[][] matMul_2(long[][] a, long[][] b) {
        long[][] c = new long[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                long v = 0;
                for (int t = 0; t < 2; t++) {
                    v += a[i][t] * b[t][j];
                }
                c[i][j] = v;
            }
        }
        return c;
    }
}
