package LeetCodeJava.Tree;

// https://leetcode.com/problems/redundant-connection-ii/description/
/**
 * 685. Redundant Connection II
 * Hard
 *
 * In this problem, a rooted tree is a directed graph such that, there is exactly one node
 * (the root) for which all other nodes are descendants of this node, plus every node has
 * exactly one parent, except for the root node which has no parents.
 *
 * The given input is a directed graph that started as a rooted tree with n nodes (with
 * distinct values from 1 to n), with one additional directed edge added. The added edge
 * has two different vertices chosen from 1 to n, and was not an edge that already existed.
 *
 * The resulting graph is given as a 2D-array of edges. Each element of edges is a pair
 * [ui, vi] that represents a directed edge connecting nodes ui and vi, where ui is a
 * parent of child vi.
 *
 * Return an edge that can be removed so that the resulting graph is a rooted tree of n
 * nodes. If there are multiple answers, return the answer that occurs last in the given
 * 2D-array.
 *
 * Example 1:
 *
 * Input: edges = [[1,2],[1,3],[2,3]]
 * Output: [2,3]
 *
 * Example 2:
 *
 * Input: edges = [[1,2],[2,3],[3,4],[4,1],[1,5]]
 * Output: [4,1]
 *
 * Constraints:
 *
 * n == edges.length
 * 3 <= n <= 1000
 * edges[i].length == 2
 * 1 <= ui, vi <= n
 * ui != vi
 *
 */
public class RedundantConnection2 {

    // V0
    // IDEA: CASE ANALYSIS + UNION FIND
    /**
     *   Adding one edge to a rooted tree breaks it in EXACTLY ONE of three ways:
     *
     *   Case A -- some node ends up with TWO PARENTS, and there is NO cycle.
     *             Removing the LATER of the two incoming edges fixes it.
     *
     *   Case B -- no node has two parents, so the extra edge closed a directed CYCLE.
     *             Remove the edge that closes the cycle (the last one seen in a
     *             union-find scan).
     *
     *   Case C -- a node has two parents AND there is a cycle. Then the cycle must
     *             run through the FIRST of the two incoming edges (otherwise dropping
     *             the second would already fix everything), so remove that FIRST one.
     *
     *   Implementation:
     *     pass 1 -- find a node with two parents; remember both incoming edges as
     *               candFirst (earlier) and candSecond (later).
     *     pass 2 -- union everything EXCEPT candSecond.
     *               * cycle detected -> Case C if a two-parent node exists (return
     *                 candFirst), else Case B (return the closing edge).
     *               * no cycle       -> Case A (return candSecond).
     *
     *   time  = O(n * alpha(n))
     *   space = O(n)
     */

    private int[] uf;

    public int[] findRedundantDirectedConnection(int[][] edges) {
        int n = edges.length;

        int[] parentOf = new int[n + 1];
        int[] candFirst = null;  // earlier edge into the two-parent node
        int[] candSecond = null; // later edge into the two-parent node

        // pass 1 : look for a node with TWO parents
        for (int[] e : edges) {
            int u = e[0];
            int v = e[1];
            if (parentOf[v] != 0) {
                candFirst = new int[] { parentOf[v], v };
                candSecond = new int[] { u, v };
            } else {
                parentOf[v] = u;
            }
        }

        // pass 2 : union find, SKIPPING candSecond
        this.uf = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            uf[i] = i;
        }

        for (int[] e : edges) {
            int u = e[0];
            int v = e[1];

            if (candSecond != null && u == candSecond[0] && v == candSecond[1]) {
                continue;
            }

            int ru = find(u);
            int rv = find(v);
            if (ru == rv) {
                /** NOTE !!!
                 *
                 *  a cycle exists EVEN WITHOUT candSecond
                 *  -> so candSecond was not the culprit
                 */
                if (candFirst != null) {
                    return candFirst; // Case C
                }
                return new int[] { u, v }; // Case B
            }
            uf[rv] = ru;
        }

        return candSecond; // Case A
    }

    private int find(int x) {
        while (uf[x] != x) {
            uf[x] = uf[uf[x]]; // path compression (halving)
            x = uf[x];
        }
        return x;
    }

}
