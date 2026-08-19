package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/minimum-time-to-break-locks-i/

import java.util.List;

/**
 *  3376. Minimum Time to Break Locks I
 *  Medium
 *
 *  Bob is stuck in a dungeon and must break n locks, each requiring some amount
 *  of energy to break. The required energy for each lock is stored in an array
 *  called strength where strength[i] indicates the energy needed to break the
 *  ith lock.
 *
 *  To break a lock, Bob uses a sword with the following characteristics:
 *    - The initial energy of the sword is 0.
 *    - The initial factor X by which the energy increases is 1.
 *    - Every minute, the energy of the sword increases by the current factor X.
 *    - To break the ith lock, the energy must reach at least strength[i].
 *    - After breaking a lock, the energy resets to 0, and the factor X increases
 *      by a given value K.
 *
 *  Return the minimum time in minutes required for Bob to break all n locks.
 *
 *  Example 1:
 *    Input: strength = [3,4,1], K = 1
 *    Output: 4
 *    Explanation: break lock 3 (X=1, 1 min), lock 2 (X=2, 2 min),
 *                 lock 1 (X=3, 1 min) -> 4
 *
 *  Example 2:
 *    Input: strength = [2,5,4], K = 2
 *    Output: 5
 *
 *  Constraints:
 *    n == strength.length
 *    1 <= n <= 8
 *    1 <= K <= 10
 *    1 <= strength[i] <= 10^6
 */
public class MinimumTimeToBreakLocksI {

    // V0
    // IDEA: n <= 8, SO TRY EVERY ORDER - THE COST OF AN ORDER IS FORCED
    //       the only decision is the SEQUENCE of locks. once that is fixed,
    //       breaking the t-th lock (0-indexed) takes ceil(strength / X) minutes
    //       with X = 1 + t*K, since waiting longer than necessary never helps
    //       and the factor only changes on a break.
    //       8! = 40320 orders, each scored in 8 steps; the running-total bound
    //       prunes most of them away.
    /**
     * time = O(n! * n)
     * space = O(n)
     */
    private int best;

    public int findMinimumTime(List<Integer> strength, int K) {
        int n = strength.size();
        int[] s = new int[n];
        for (int i = 0; i < n; i++) {
            s[i] = strength.get(i);
        }
        this.best = Integer.MAX_VALUE;
        dfs(s, new boolean[n], 0, 1, 0, K);
        return this.best;
    }

    private void dfs(int[] s, boolean[] used, int done, int x, int total, int K) {
        if (total >= this.best) {
            return; // bound: this order can no longer win
        }
        if (done == s.length) {
            this.best = total;
            return;
        }
        for (int i = 0; i < s.length; i++) {
            if (used[i]) {
                continue;
            }
            used[i] = true;
            // ceil(s[i] / x)
            dfs(s, used, done + 1, x + K, total + (s[i] + x - 1) / x, K);
            used[i] = false;
        }
    }
}
