package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/optimal-account-balancing/description/

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
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


    // V1
    // IDEA: ITERATIVE DEEPENING (exact, O(depth) memory)
    /**
     *  Try answer values 0, 1, 2, ... and DFS to each limit, cutting any branch
     *  that cannot finish within the remaining budget (each transaction settles at
     *  most one person, so `remaining non-zero people - 1` is a valid lower bound).
     *
     *  EXACT, like V0, but the memory is O(m) instead of the recursion tree, and
     *  the first depth that succeeds is the answer -- no min() folding at all.
     *
     *  time  = O(m!) worst case
     *  space = O(m)
     */
    public int minTransfers_1(int[][] transactions) {
        Map<Integer, Integer> net = new HashMap<>();
        for (int[] t : transactions) {
            net.merge(t[0], -t[2], Integer::sum);
            net.merge(t[1], t[2], Integer::sum);
        }
        List<Integer> nonZero = new ArrayList<>();
        for (int v : net.values()) {
            if (v != 0) {
                nonZero.add(v);
            }
        }
        int m = nonZero.size();
        if (m == 0) {
            return 0;
        }
        int[] bal = new int[m];
        for (int i = 0; i < m; i++) {
            bal[i] = nonZero.get(i);
        }

        for (int limit = 0; limit <= m; limit++) {
            if (canSettle(bal, 0, limit)) {
                return limit;
            }
        }
        return m - 1;
    }

    /** can the remaining balances be cleared in at most `budget` transactions? */
    private boolean canSettle(int[] bal, int i, int budget) {
        while (i < bal.length && bal[i] == 0) {
            i += 1;
        }
        if (i == bal.length) {
            return true;
        }
        if (budget <= 0) {
            return false;
        }
        // BOUND: every remaining non-zero person needs at least one transaction,
        // and the last one is settled for free by the others
        int remaining = 0;
        for (int t = i; t < bal.length; t++) {
            if (bal[t] != 0) {
                remaining += 1;
            }
        }
        if (remaining - 1 > budget) {
            return false;
        }

        for (int j = i + 1; j < bal.length; j++) {
            if ((long) bal[i] * bal[j] >= 0) {
                continue;
            }
            bal[j] += bal[i];
            boolean ok = canSettle(bal, i + 1, budget - 1);
            bal[j] -= bal[i];
            if (ok) {
                return true;
            }
        }
        return false;
    }

    // V2
    // IDEA: MAXIMISE THE NUMBER OF ZERO-SUM GROUPS (bitmask over subsets)
    /**
     *  m non-zero balances need m - groups transactions, where `groups` is the
     *  maximum number of disjoint zero-sum subsets they can be split into.
     *
     *  So compute, for every mask, the largest number of zero-sum parts it splits
     *  into, and answer m - groups[full]. A different objective from V0-1's
     *  `minimum transfers per subset`, reaching the same number.
     *
     *  time  = O(3^m)
     *  space = O(2^m)
     */
    public int minTransfers_2(int[][] transactions) {
        Map<Integer, Integer> net = new HashMap<>();
        for (int[] t : transactions) {
            net.merge(t[0], -t[2], Integer::sum);
            net.merge(t[1], t[2], Integer::sum);
        }
        List<Integer> nonZero = new ArrayList<>();
        for (int v : net.values()) {
            if (v != 0) {
                nonZero.add(v);
            }
        }
        int m = nonZero.size();
        if (m == 0) {
            return 0;
        }

        int[] sum = new int[1 << m];
        for (int mask = 1; mask < (1 << m); mask++) {
            int low = Integer.numberOfTrailingZeros(mask);
            sum[mask] = sum[mask & (mask - 1)] + nonZero.get(low);
        }

        // groups[mask] = max number of disjoint zero-sum parts inside mask
        int[] groups = new int[1 << m];
        for (int mask = 1; mask < (1 << m); mask++) {
            groups[mask] = 0;
            for (int sub = mask; sub > 0; sub = (sub - 1) & mask) {
                if (sum[sub] != 0) {
                    continue;
                }
                groups[mask] = Math.max(groups[mask], groups[mask ^ sub] + 1);
            }
        }

        return m - groups[(1 << m) - 1];
    }

    // V3
    // IDEA: DFS WITH PRUNING -- skip equal balances at the same depth
    /**
     *  Same backtracking as V0, but two extra cuts:
     *    - if bal[i] is already settled, skip forward
     *    - never push into two partners with the SAME balance at one depth
     *      (they are interchangeable, so the second is a duplicate branch)
     *
     *  Same worst case, dramatically fewer nodes on inputs with repeated amounts.
     *
     *  time  = O(m!) worst case
     *  space = O(m)
     */
    public int minTransfers_3(int[][] transactions) {
        Map<Integer, Integer> net = new HashMap<>();
        for (int[] t : transactions) {
            net.merge(t[0], -t[2], Integer::sum);
            net.merge(t[1], t[2], Integer::sum);
        }
        List<Integer> nonZero = new ArrayList<>();
        for (int v : net.values()) {
            if (v != 0) {
                nonZero.add(v);
            }
        }
        int[] arr = new int[nonZero.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = nonZero.get(i);
        }
        return dfsPrune(arr, 0);
    }

    private int dfsPrune(int[] bal, int i) {
        while (i < bal.length && bal[i] == 0) {
            i += 1;
        }
        if (i == bal.length) {
            return 0;
        }

        int best = Integer.MAX_VALUE;
        Set<Integer> tried = new HashSet<>();
        for (int j = i + 1; j < bal.length; j++) {
            if ((long) bal[i] * bal[j] >= 0) {
                continue;
            }
            if (!tried.add(bal[j])) {
                continue;   // an identical partner was already explored
            }
            bal[j] += bal[i];
            best = Math.min(best, 1 + dfsPrune(bal, i + 1));
            bal[j] -= bal[i];
        }
        return best;
    }

}
