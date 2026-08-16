package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/optimal-account-balancing/description/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 465. Optimal Account Balancing
 * Hard
 * Lock: Prime
 *
 * You are given an array of transactions transactions where
 * transactions[i] = [fromi, toi, amounti] indicates that the person with
 * ID = fromi gave amounti $ to the person with ID = toi.
 *
 * Return the minimum number of transactions required to settle the debt.
 *
 * Example 1:
 *
 * Input: transactions = [[0,1,10],[2,0,5]]
 * Output: 2
 * Explanation:
 * Person #0 gave person #1 $10.
 * Person #2 gave person #0 $5.
 * Two transactions are needed. One way to settle the debt is person #1 pays
 * person #0 and #2 $5 each.
 *
 * Example 2:
 *
 * Input: transactions = [[0,1,10],[1,0,1],[1,2,5],[2,0,5]]
 * Output: 1
 * Explanation:
 * Person #0 gave person #1 $10.
 * Person #1 gave person #0 $1.
 * Person #1 gave person #2 $5.
 * Person #2 gave person #0 $5.
 * Therefore, person #1 only need to give person #0 $4, and all debt is settled.
 *
 * Constraints:
 *
 * 1 <= transactions.length <= 8
 * transactions[i].length == 3
 * 0 <= fromi, toi < 12
 * fromi != toi
 * 1 <= amounti <= 100
 *
 */
public class OptimalAccountBalancing {

    // V0
    // IDEA: NET BALANCES + DFS BACKTRACKING
    /**
     *  WHO paid WHOM does not matter, only each person's NET balance does.
     *  Drop everyone whose net balance is 0 -> we get a list `bal` of m non-zero
     *  balances that SUMS TO 0.
     *
     *  Settling m non-zero people needs at most m - 1 transactions. To do better we
     *  want to split them into AS MANY zero-sum groups as possible (a group of size
     *  g costs g - 1). DFS does exactly that IMPLICITLY:
     *
     *    at index i, try to pay off bal[i] using any LATER person of OPPOSITE sign,
     *    push bal[i] into them (1 transaction), recurse on i + 1, then undo.
     *
     *  time  = O(m!)  // m = number of non-zero balances (<= 12), heavily pruned
     *  space = O(m)
     */

    private int[] bal;
    private int m;

    public int minTransfers(int[][] transactions) {
        Map<Integer, Integer> net = new HashMap<>();
        for (int[] t : transactions) {
            net.put(t[0], net.getOrDefault(t[0], 0) - t[2]);
            net.put(t[1], net.getOrDefault(t[1], 0) + t[2]);
        }

        List<Integer> nonZero = new ArrayList<>();
        for (int v : net.values()) {
            if (v != 0) {
                nonZero.add(v);
            }
        }

        this.m = nonZero.size();
        this.bal = new int[m];
        for (int i = 0; i < m; i++) {
            bal[i] = nonZero.get(i);
        }

        return dfs(0);
    }

    private int dfs(int i) {
        // skip ALREADY-SETTLED people
        while (i < m && bal[i] == 0) {
            i += 1;
        }
        if (i == m) {
            return 0;
        }

        int best = Integer.MAX_VALUE;
        for (int j = i + 1; j < m; j++) {
            /** NOTE !!!
             *
             *  only a person of the OPPOSITE sign can absorb bal[i]
             *  -> pushing into a same-sign person can never help
             */
            if ((long) bal[i] * bal[j] < 0) {
                bal[j] += bal[i];
                best = Math.min(best, 1 + dfs(i + 1));
                bal[j] -= bal[i];
            }
        }
        return best;
    }

    // V0-1
    // IDEA: BITMASK DP OVER SUBSETS (provably optimal, no backtracking)
    /**
     *  For a subset S of the non-zero balances:
     *    - if sum(S) == 0, S can be settled on its own with at most |S| - 1 moves
     *    - and we can do BETTER by splitting S into two zero-sum halves:
     *        f[S] = min(|S| - 1, min over sub-subsets T of f[T] + f[S ^ T])
     *    - if sum(S) != 0, S is UNSETTLEABLE on its own -> INF
     *
     *  time  = O(3^m)   // subset-of-subset enumeration, m <= 12
     *  space = O(2^m)
     */
    public int minTransfers_0_1(int[][] transactions) {
        Map<Integer, Integer> net = new HashMap<>();
        for (int[] t : transactions) {
            net.put(t[0], net.getOrDefault(t[0], 0) - t[2]);
            net.put(t[1], net.getOrDefault(t[1], 0) + t[2]);
        }

        List<Integer> nonZero = new ArrayList<>();
        for (int v : net.values()) {
            if (v != 0) {
                nonZero.add(v);
            }
        }

        int mm = nonZero.size();
        if (mm == 0) {
            return 0;
        }

        final int INF = Integer.MAX_VALUE / 2;
        int[] f = new int[1 << mm];
        java.util.Arrays.fill(f, INF);
        f[0] = 0;

        for (int mask = 1; mask < (1 << mm); mask++) {
            int s = 0;
            int bits = 0;
            for (int j = 0; j < mm; j++) {
                if ((mask >> j & 1) == 1) {
                    s += nonZero.get(j);
                    bits += 1;
                }
            }
            if (s != 0) {
                continue; // cannot settle on its own
            }

            f[mask] = bits - 1; // worst case: chain them

            /** NOTE !!!
             *
             *  `sub = (sub - 1) & mask` walks EVERY subset of mask in
             *  decreasing order -> this is what gives the O(3^m) total
             */
            for (int sub = (mask - 1) & mask; sub > 0; sub = (sub - 1) & mask) {
                if (f[sub] != INF && f[mask ^ sub] != INF) {
                    f[mask] = Math.min(f[mask], f[sub] + f[mask ^ sub]);
                }
            }
        }

        return f[(1 << mm) - 1];
    }

}
