package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/balanced-k-factor-decomposition/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  3669. Balanced K-Factor Decomposition
 *  Medium
 *
 *  Given two integers n and k, split the number n into exactly k positive
 *  integers such that the product of these integers is equal to n.
 *
 *  Return any one split in which the maximum difference between any two
 *  numbers is minimized. You may return the result in any order.
 *
 *  Example 1:
 *    Input: n = 100, k = 2
 *    Output: [10,10]
 *    Explanation: The split [10, 10] yields 10 * 10 = 100 and a max-min
 *                 difference of 0, which is minimal.
 *
 *  Example 2:
 *    Input: n = 44, k = 3
 *    Output: [2,2,11]
 *    Explanation: [1,1,44] -> diff 43, [1,2,22] -> diff 21,
 *                 [1,4,11] -> diff 10, [2,2,11] -> diff 9 (minimal).
 *
 *  Constraints:
 *    4 <= n <= 10^5
 *    2 <= k <= 5
 *    k is strictly less than the total number of positive divisors of n.
 */
public class BalancedKFactorDecomposition {

    // V0
    // IDEA: DFS OVER DIVISORS IN NON-DECREASING ORDER
    //       a factorisation is a multiset, so forcing the factors out in
    //       non-decreasing order kills all k! permutations of the same answer
    //       and lets the spread be read off as (last - first) at the leaf.
    //       at each level only divisors of what is left AND >= the previous
    //       factor are viable; the extra prune d^(levels left) <= remaining
    //       collapses the search to a handful of nodes for k <= 5.
    /**
     * time = O(d(n)^(k-1) * sqrt(n))  // tiny in practice
     * space = O(k)
     */
    private int[] best;
    private int bestSpread;

    public int[] minDifference(int n, int k) {
        this.best = null;
        this.bestSpread = Integer.MAX_VALUE;
        dfs(n, k, new ArrayList<>());
        return best;
    }

    private void dfs(int rem, int left, List<Integer> cur) {
        if (left == 1) {
            if (cur.isEmpty() || rem >= cur.get(cur.size() - 1)) {
                int first = cur.isEmpty() ? rem : cur.get(0);
                int spread = rem - first;
                if (spread < bestSpread) {
                    bestSpread = spread;
                    int[] out = new int[cur.size() + 1];
                    for (int i = 0; i < cur.size(); i++) {
                        out[i] = cur.get(i);
                    }
                    out[cur.size()] = rem;
                    best = out;
                }
            }
            return;
        }

        int lo = cur.isEmpty() ? 1 : cur.get(cur.size() - 1);
        for (Integer d : divisorsAtLeast(rem, lo)) {
            // prune: d is the SMALLEST remaining factor -> d^left <= rem
            if (pow(d, left) > rem) {
                break;
            }
            cur.add(d);
            dfs(rem / d, left - 1, cur);
            cur.remove(cur.size() - 1);
        }
    }

    private List<Integer> divisorsAtLeast(int m, int lo) {
        List<Integer> out = new ArrayList<>();
        for (int i = 1; (long) i * i <= m; i++) {
            if (m % i == 0) {
                if (i >= lo) {
                    out.add(i);
                }
                int j = m / i;
                if (j != i && j >= lo) {
                    out.add(j);
                }
            }
        }
        Collections.sort(out);
        return out;
    }

    // d^e capped so it never overflows
    private long pow(int d, int e) {
        long r = 1;
        for (int i = 0; i < e; i++) {
            r *= d;
            if (r > 1000000000L) {
                return r;
            }
        }
        return r;
    }
}
