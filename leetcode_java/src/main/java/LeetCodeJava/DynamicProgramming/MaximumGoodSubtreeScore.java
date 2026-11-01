package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-good-subtree-score/description/

import java.util.*;

/**
 * 3575. Maximum Good Subtree Score
 * Hard
 *
 * You are given an undirected tree rooted at node 0 with n nodes numbered
 * from 0 to n - 1. Each node i has an integer value vals[i], and its parent is
 * given by par[i].
 *
 * A subset of nodes within the subtree of a node is called good if every digit
 * from 0 to 9 appears at most once in the decimal representation of the values
 * of the selected nodes.
 *
 * The score of a good subset is the sum of the values of its nodes.
 *
 * Define an array maxScore of length n, where maxScore[u] represents the
 * maximum possible sum of values of a good subset of nodes that belong to the
 * subtree rooted at node u, including u itself and all its descendants.
 *
 * Return the sum of all values in maxScore.
 *
 * Since the answer may be large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: vals = [2,3], par = [-1,0]
 * Output: 8
 * Explanation:
 * - The subtree rooted at node 0 includes nodes {0, 1}. The subset {2, 3} is
 *   good as the digits 2 and 3 appear only once. The score of this subset is
 *   2 + 3 = 5.
 * - The subtree rooted at node 1 includes only node {1}. The subset {3} is
 *   good. The score of this subset is 3.
 * - The maxScore array is [5, 3], and the sum of all values in maxScore is
 *   5 + 3 = 8. Thus, the answer is 8.
 *
 * Example 2:
 *
 * Input: vals = [1,5,2], par = [-1,0,0]
 * Output: 15
 * Explanation:
 * - The subtree rooted at node 0 includes nodes {0, 1, 2}. The subset
 *   {1, 5, 2} is good as the digits 1, 5 and 2 appear only once. The score of
 *   this subset is 1 + 5 + 2 = 8.
 * - The subtree rooted at node 1 includes only node {1}. The subset {5} is
 *   good. The score of this subset is 5.
 * - The subtree rooted at node 2 includes only node {2}. The subset {2} is
 *   good. The score of this subset is 2.
 * - The maxScore array is [8, 5, 2], and the sum of all values in maxScore is
 *   8 + 5 + 2 = 15. Thus, the answer is 15.
 *
 * Example 3:
 *
 * Input: vals = [34,1,2], par = [-1,0,1]
 * Output: 42
 * Explanation:
 * - The subtree rooted at node 0 includes nodes {0, 1, 2}. The subset
 *   {34, 1, 2} is good as the digits 3, 4, 1 and 2 appear only once. The score
 *   of this subset is 34 + 1 + 2 = 37.
 * - The subtree rooted at node 1 includes node {1, 2}. The subset {1, 2} is
 *   good as the digits 1 and 2 appear only once. The score of this subset is
 *   1 + 2 = 3.
 * - The subtree rooted at node 2 includes only node {2}. The subset {2} is
 *   good. The score of this subset is 2.
 * - The maxScore array is [37, 3, 2], and the sum of all values in maxScore is
 *   37 + 3 + 2 = 42. Thus, the answer is 42.
 *
 * Example 4:
 *
 * Input: vals = [3,22,5], par = [-1,0,1]
 * Output: 18
 * Explanation:
 * - The subtree rooted at node 0 includes nodes {0, 1, 2}. The subset
 *   {3, 22, 5} is not good, as digit 2 appears twice. Therefore, the subset
 *   {3, 5} is valid. The score of this subset is 3 + 5 = 8.
 * - The subtree rooted at node 1 includes nodes {1, 2}. The subset {22, 5} is
 *   not good, as digit 2 appears twice. Therefore, the subset {5} is valid.
 *   The score of this subset is 5.
 * - The subtree rooted at node 2 includes {2}. The subset {5} is good. The
 *   score of this subset is 5.
 * - The maxScore array is [8, 5, 5], and the sum of all values in maxScore is
 *   8 + 5 + 5 = 18. Thus, the answer is 18.
 *
 *
 * Constraints:
 *
 * 1 <= n == vals.length <= 500
 * 1 <= vals[i] <= 10^9
 * par.length == n
 * par[0] == -1
 * 0 <= par[i] < n for i in [1, n - 1]
 * The input is generated such that the parent array par represents a valid
 * tree.
 *
 */
public class MaximumGoodSubtreeScore {

    private static final int MOD = 1_000_000_007;
    private static final int ALL_DIGITS = 1 << 10;
    // marks a digit set no good subset can produce
    private static final long UNREACHABLE = -1;

    // V0
    // IDEA: TREE DP OVER DIGIT MASKS, MERGING CHILD TABLES ON DISJOINT MASKS
    //   a value is usable only if its decimal digits are distinct; it then is an
    //   item with a 10-bit digit mask. A good subset = items with pairwise disjoint masks.
    //   dp[u][S] = best sum of a good subset of subtree(u) whose digits are exactly S
    //   Merging two independent parts: merged[S | T] = max(a[S] + b[T]) for S & T == 0,
    //   enumerated as T over the submasks of ~S (3^10 pairs per merge).
    //   maxScore[u] = max over S of dp[u][S]; answer = sum of maxScore[u].
    /**
     * time = O(N * 3^10)
     * space = O(N * 2^10)
     */
    public int goodSubtreeSum(int[] vals, int[] par) {
        int n = vals.length;
        List<List<Integer>> children = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            children.add(new ArrayList<>());
        }
        for (int i = 1; i < n; i++) {
            children.get(par[i]).add(i);
        }

        // BFS order from the root; reversed it finishes children before parents
        List<Integer> order = new ArrayList<>();
        order.add(0);
        for (int idx = 0; idx < order.size(); idx++) {
            order.addAll(children.get(order.get(idx)));
        }

        long[][] dp = new long[n][];
        long total = 0;
        for (int idx = n - 1; idx >= 0; idx--) {
            int u = order.get(idx);
            // start with u's own item: either skip it or take it alone
            long[] table = new long[ALL_DIGITS];
            Arrays.fill(table, UNREACHABLE);
            table[0] = 0;
            int mask = digitMask(vals[u]);
            if (mask >= 0) {
                table[mask] = Math.max(table[mask], vals[u]);
            }
            // fold in every child's finished table
            for (int child : children.get(u)) {
                table = merge(table, dp[child]);
                dp[child] = null;
            }
            dp[u] = table;

            long maxScore = 0;
            for (long value : table) {
                maxScore = Math.max(maxScore, value);
            }
            total = (total + maxScore) % MOD;
        }
        return (int) total;
    }

    /** Best sums of the union of two independent parts whose digit sets do not overlap. */
    private long[] merge(long[] first, long[] second) {
        long[] merged = new long[ALL_DIGITS];
        Arrays.fill(merged, UNREACHABLE);
        for (int s = 0; s < ALL_DIGITS; s++) {
            if (first[s] == UNREACHABLE) {
                continue;
            }
            int freeDigits = (ALL_DIGITS - 1) & ~s;
            // walk every submask t of freeDigits, including the empty one
            int t = freeDigits;
            while (true) {
                if (second[t] != UNREACHABLE) {
                    merged[s | t] = Math.max(merged[s | t], first[s] + second[t]);
                }
                if (t == 0) {
                    break;
                }
                // next smaller submask of freeDigits
                t = (t - 1) & freeDigits;
            }
        }
        return merged;
    }

    /** 10-bit mask of the digits of value, or -1 when a digit repeats. */
    private int digitMask(int value) {
        int mask = 0;
        while (value > 0) {
            int bit = 1 << (value % 10);
            if ((mask & bit) != 0) {
                return -1;
            }
            mask |= bit;
            value /= 10;
        }
        return mask;
    }
}
