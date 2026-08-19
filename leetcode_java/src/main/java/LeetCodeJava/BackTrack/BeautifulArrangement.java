package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/beautiful-arrangement/

/**
 *  526. Beautiful Arrangement
 *  Medium
 *
 *  Suppose you have n integers labeled 1 through n. A permutation of those n
 *  integers perm (1-indexed) is considered a beautiful arrangement if for every
 *  i (1 <= i <= n), either of the following is true:
 *   - perm[i] is divisible by i
 *   - i is divisible by perm[i]
 *
 *  Given an integer n, return the number of the beautiful arrangements that you
 *  can construct.
 *
 *  Example 1:
 *   Input: n = 2
 *   Output: 2
 *   Explanation: [1,2] -> 1 % 1 == 0, 2 % 2 == 0 ; [2,1] -> 2 % 1 == 0, 2 % 1 == 0
 *
 *  Example 2:
 *   Input: n = 1
 *   Output: 1
 *
 *  Constraints:
 *   1 <= n <= 15
 */
public class BeautifulArrangement {

    // V0
    // IDEA: backtracking - fill positions 1..n, only try values that satisfy the
    //       divisibility rule at that position (heavy pruning)
    /**
     * time = O(k), k = number of valid arrangements (far below n!)
     * space = O(n)
     */
    private int count = 0;

    public int countArrangement(int n) {
        this.count = 0;
        boolean[] used = new boolean[n + 1];
        backtrack(n, 1, used);
        return this.count;
    }

    private void backtrack(int n, int pos, boolean[] used) {
        if (pos > n) {
            this.count += 1;
            return;
        }
        for (int val = 1; val <= n; val++) {
            if (used[val]) {
                continue;
            }
            if (val % pos != 0 && pos % val != 0) {
                continue;
            }
            used[val] = true;
            backtrack(n, pos + 1, used);
            used[val] = false;
        }
    }

    // V1
    // IDEA: bitmask DP over the set of already used values
    /**
     * time = O(n * 2^n)
     * space = O(2^n)
     */
    public int countArrangement_1(int n) {
        int total = 1 << n;
        int[] dp = new int[total];
        dp[0] = 1;

        for (int mask = 0; mask < total; mask++) {
            if (dp[mask] == 0) {
                continue;
            }
            // number of bits set = number of positions already filled
            int pos = Integer.bitCount(mask) + 1;
            if (pos > n) {
                continue;
            }
            for (int val = 1; val <= n; val++) {
                int bit = 1 << (val - 1);
                if ((mask & bit) != 0) {
                    continue;
                }
                if (val % pos != 0 && pos % val != 0) {
                    continue;
                }
                dp[mask | bit] += dp[mask];
            }
        }
        return dp[total - 1];
    }
}
