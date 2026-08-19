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
}
