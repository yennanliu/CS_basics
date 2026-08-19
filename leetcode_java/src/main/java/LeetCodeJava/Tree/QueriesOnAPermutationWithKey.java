package LeetCodeJava.Tree;

// https://leetcode.com/problems/queries-on-a-permutation-with-key/

import java.util.ArrayList;
import java.util.List;

/**
 *  1409. Queries on a Permutation With Key
 *  Medium
 *
 *  Given the array queries of positive integers between 1 and m, you have to
 *  process all queries[i] (from i = 0 to i = queries.length - 1) according to
 *  the following rules:
 *
 *    - In the beginning, you have the permutation P = [1,2,3,...,m].
 *    - For the current i, find the position of queries[i] in the permutation P
 *      (indexing from 0) and then move this at the beginning of P. Notice that
 *      the position of queries[i] in P is the result for queries[i].
 *
 *  Return an array containing the result for the given queries.
 *
 *  Example 1:
 *    Input: queries = [3,1,2,1], m = 5
 *    Output: [2,1,2,1]
 *    Explanation:
 *      i=0: P=[1,2,3,4,5], pos(3)=2 -> P=[3,1,2,4,5]
 *      i=1: P=[3,1,2,4,5], pos(1)=1 -> P=[1,3,2,4,5]
 *      i=2: P=[1,3,2,4,5], pos(2)=2 -> P=[2,1,3,4,5]
 *      i=3: P=[2,1,3,4,5], pos(1)=1 -> P=[1,2,3,4,5]
 *
 *  Example 2:
 *    Input: queries = [4,1,2,2], m = 4
 *    Output: [3,1,2,0]
 *
 *  Constraints:
 *    1 <= m <= 10^3
 *    1 <= queries.length <= m
 *    1 <= queries[i] <= m
 */
public class QueriesOnAPermutationWithKey {

    // V0
    // IDEA: SIMULATION (m <= 1000, so a plain list is fast enough)
    //       find the index, record it, remove and re-insert at the front.
    /**
     * time = O(M * Q)
     * space = O(M)
     */
    public int[] processQueries(int[] queries, int m) {
        List<Integer> p = new ArrayList<>();
        for (int i = 1; i <= m; i++) {
            p.add(i);
        }
        int[] res = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int v = queries[i];
            int j = p.indexOf(v);
            res[i] = j;
            p.remove(j);
            p.add(0, v);
        }
        return res;
    }

    // V1
    // IDEA: BINARY INDEXED TREE (Fenwick) OVER A VIRTUAL LINE OF SIZE M + Q
    //       slots [Q+1 .. Q+M] hold the initial permutation; slots Q, Q-1, ...
    //       are the free space IN FRONT that queried values get moved into.
    //       the 0-based position of a value = how many live values sit strictly
    //       to its left = prefix sum up to its slot (after removing itself).
    //       (materially different: O(log n) per query instead of O(m))
    /**
     * time = O((M + Q) * log(M + Q))
     * space = O(M + Q)
     */
    public int[] processQueriesBIT(int[] queries, int m) {
        int q = queries.length;
        int size = m + q;
        int[] tree = new int[size + 1];
        int[] pos = new int[m + 1];

        for (int i = 1; i <= m; i++) {
            pos[i] = q + i;
            bitUpdate(tree, size, q + i, 1);
        }

        int[] res = new int[q];
        for (int i = 0; i < q; i++) {
            int v = queries[i];
            int j = pos[v];
            bitUpdate(tree, size, j, -1);
            res[i] = bitQuery(tree, j);
            pos[v] = q - i;
            bitUpdate(tree, size, q - i, 1);
        }
        return res;
    }

    private void bitUpdate(int[] tree, int n, int x, int delta) {
        while (x <= n) {
            tree[x] += delta;
            x += x & (-x);
        }
    }

    private int bitQuery(int[] tree, int x) {
        int s = 0;
        while (x > 0) {
            s += tree[x];
            x -= x & (-x);
        }
        return s;
    }
}
