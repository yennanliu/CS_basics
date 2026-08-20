package LeetCodeJava.Design;

// https://leetcode.com/problems/block-placement-queries/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 *  3161. Block Placement Queries
 *  Hard
 *
 *  There exists an infinite number line, with its origin at 0 and extending towards
 *  the positive x-axis.
 *
 *  You are given a 2D array queries, which contains two types of queries:
 *
 *   - For a query of type 1, queries[i] = [1, x]. Build an obstacle at distance x
 *     from the origin. It is guaranteed that there is no obstacle at distance x when
 *     the query is asked.
 *   - For a query of type 2, queries[i] = [2, x, sz]. Check if it is possible to place
 *     a block of size sz anywhere in the range [0, x] on the line, such that the block
 *     entirely lies in the range [0, x]. A block cannot be placed if it intersects with
 *     any obstacle, but it may touch it. Note that you do not actually place the block.
 *
 *  Return a boolean array results, where results[i] is true if you can place the block
 *  specified in the ith query of type 2, and false otherwise.
 *
 *  Example 1:
 *    Input: queries = [[1,2],[2,3,3],[2,3,1],[2,2,2]]
 *    Output: [false,true,true]
 *    Explanation: For query 0, place an obstacle at x = 2. A block of size at most 2
 *                 can be placed before x = 3.
 *
 *  Example 2:
 *    Input: queries = [[1,7],[2,7,6],[1,2],[2,7,5],[2,7,6]]
 *    Output: [true,true,false]
 *    Explanation: Place an obstacle at x = 7 for query 0. A block of size at most 7 can
 *                 be placed before x = 7. Place an obstacle at x = 2 for query 2. Now,
 *                 a block of size at most 5 can be placed before x = 7, and a block of
 *                 size at most 2 before x = 2.
 *
 *  Constraints:
 *    1 <= queries.length <= 15 * 10^4
 *    2 <= queries[i].length <= 3
 *    1 <= queries[i][0] <= 2
 *    1 <= x, sz <= min(5 * 10^4, 3 * queries.length)
 *    The input is generated such that for queries of type 1, no obstacle exists at
 *    distance x when the query is asked.
 *    The input is generated such that there is at least one query of type 2.
 */
public class BlockPlacementQueries {

    private static final int NEG = -1;

    /** iterative MAX segment tree over a fixed coordinate axis */
    private static class MaxTree {
        private final int n;
        private final int[] t;

        MaxTree(int n) {
            this.n = n;
            this.t = new int[2 * n];
            Arrays.fill(this.t, NEG);
        }

        void update(int i, int v) {
            i += this.n;
            this.t[i] = v;
            i >>= 1;
            while (i > 0) {
                this.t[i] = Math.max(this.t[2 * i], this.t[2 * i + 1]);
                i >>= 1;
            }
        }

        /** max over the INCLUSIVE range [lo, hi] */
        int query(int lo, int hi) {
            int res = NEG;
            lo += this.n;
            hi += this.n + 1;
            while (lo < hi) {
                if ((lo & 1) == 1) {
                    res = Math.max(res, this.t[lo]);
                    lo++;
                }
                if ((hi & 1) == 1) {
                    hi--;
                    res = Math.max(res, this.t[hi]);
                }
                lo >>= 1;
                hi >>= 1;
            }
            return res;
        }
    }

    // V0
    // IDEA: RUN THE QUERIES BACKWARDS SO INSERTIONS BECOME DELETIONS
    //
    //   a block of size sz fits in [0, x] iff some obstacle-free stretch inside
    //   [0, x] is at least sz long. treating the origin as an obstacle at 0, the
    //   candidate stretches are
    //       every gap that ENDS at an obstacle p <= x   (length p - prev(p))
    //       the tail from the last obstacle <= x out to x
    //
    //   inserting obstacles SPLITS gaps, which is awkward to maintain. processing
    //   the queries in REVERSE turns every insertion into a REMOVAL, and removing
    //   an obstacle simply MERGES its gap into the next one -- a single update.
    //
    //   two max-segment-trees over the coordinate axis answer the rest:
    //       treeGap : gap length stored at the obstacle that ends it
    //       treePos : the position itself, to find the last obstacle <= x
    //   both are prefix-max queries, and a doubly linked list over the sorted
    //   obstacle positions supplies each removal's neighbours in O(1).
    /**
     * time = O((N + maxX) log maxX)
     * space = O(N + maxX)
     */
    public List<Boolean> getResults(int[][] queries) {
        int maxX = 0;
        for (int[] q : queries) {
            maxX = Math.max(maxX, q[1]);
        }
        int size = maxX + 1;

        // every obstacle that ever exists, plus the origin
        TreeSet<Integer> set = new TreeSet<>();
        set.add(0);
        for (int[] q : queries) {
            if (q[0] == 1) {
                set.add(q[1]);
            }
        }
        int m = set.size();
        int[] positions = new int[m];
        Map<Integer, Integer> idx = new HashMap<>();
        int p = 0;
        for (Integer v : set) {
            positions[p] = v;
            idx.put(v, p);
            p++;
        }

        int[] prevI = new int[m];
        int[] nextI = new int[m];
        for (int i = 0; i < m; i++) {
            prevI[i] = i - 1;
            nextI[i] = (i + 1 < m) ? i + 1 : -1;
        }

        MaxTree treeGap = new MaxTree(size);
        MaxTree treePos = new MaxTree(size);
        treePos.update(0, 0);
        for (int i = 1; i < m; i++) {
            treeGap.update(positions[i], positions[i] - positions[i - 1]);
            treePos.update(positions[i], positions[i]);
        }

        List<Boolean> res = new ArrayList<>();
        for (int qi = queries.length - 1; qi >= 0; qi--) {
            int[] q = queries[qi];
            if (q[0] == 2) {
                int x = q[1];
                int sz = q[2];
                int best = treeGap.query(0, x);          // gaps closing at an obstacle <= x
                int last = treePos.query(0, x);          // last obstacle at or before x
                if (last != NEG) {
                    best = Math.max(best, x - last);     // the open tail up to x
                }
                res.add(best >= sz);
            } else {
                int x = q[1];
                int i = idx.get(x);
                int a = prevI[i];
                int b = nextI[i];
                treeGap.update(x, NEG);
                treePos.update(x, NEG);
                nextI[a] = b;
                if (b != -1) {
                    prevI[b] = a;
                    treeGap.update(positions[b], positions[b] - positions[a]);
                }
            }
        }

        Collections.reverse(res);
        return res;
    }
}
