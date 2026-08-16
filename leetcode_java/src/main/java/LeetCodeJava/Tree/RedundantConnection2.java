package LeetCodeJava.Tree;

// https://leetcode.com/problems/redundant-connection-ii/description/

import java.util.HashSet;
import java.util.Set;
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


    // V1
    // IDEA: BRUTE FORCE -- remove each edge (from the back) and test validity
    /**
     *  Try deleting each edge, last one first, and check whether what remains is a
     *  rooted tree: exactly one node with no parent, every other with exactly one,
     *  and no cycle.
     *
     *  O(n^2) rather than O(n alpha), but it needs NO case analysis -- it is the
     *  oracle proving the three-case argument in V0.
     *
     *  time  = O(n^2 * alpha)
     *  space = O(n)
     */
    public int[] findRedundantDirectedConnection_1(int[][] edges) {
        int n = edges.length;
        for (int skip = n - 1; skip >= 0; skip--) {
            if (isRootedTree(edges, skip, n)) {
                return edges[skip];
            }
        }
        return new int[0];
    }

    private boolean isRootedTree(int[][] edges, int skip, int n) {
        int[] par = new int[n + 1];
        int[] indeg = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            par[i] = i;
        }

        for (int i = 0; i < edges.length; i++) {
            if (i == skip) {
                continue;
            }
            int u = edges[i][0];
            int v = edges[i][1];
            if (++indeg[v] > 1) {
                return false;            // two parents
            }
            int ru = rootOf(par, u);
            int rv = rootOf(par, v);
            if (ru == rv) {
                return false;            // a cycle
            }
            par[rv] = ru;
        }

        int roots = 0;
        for (int v = 1; v <= n; v++) {
            if (indeg[v] == 0) {
                roots += 1;
            }
        }
        return roots == 1;
    }

    private int rootOf(int[] par, int x) {
        while (par[x] != x) {
            par[x] = par[par[x]];
            x = par[x];
        }
        return x;
    }

    // V2
    // IDEA: EXPLICIT CYCLE DETECTION VIA PARENT-POINTER WALK
    /**
     *  Build the parent array, then walk parent pointers from every node to find a
     *  cycle directly (rather than inferring it from a union-find collision).
     *
     *  The cycle is materialised as a SET, so the answer is `the last edge in the
     *  input whose head lies on the cycle` -- which reads much closer to the
     *  problem statement than V0's three cases.
     *
     *  time  = O(n^2) worst case
     *  space = O(n)
     */
    public int[] findRedundantDirectedConnection_2(int[][] edges) {
        int n = edges.length;
        int[] parent = new int[n + 1];
        int[] first = null;
        int[] second = null;

        for (int[] e : edges) {
            if (parent[e[1]] != 0) {
                first = new int[] { parent[e[1]], e[1] };
                second = new int[] { e[0], e[1] };
            } else {
                parent[e[1]] = e[0];
            }
        }

        // if dropping `second` leaves a cycle, `first` is the culprit
        if (second != null) {
            parent[second[1]] = first[0];   // pretend only `first` exists
            if (hasCycle(parent, n)) {
                return first;
            }
            return second;
        }

        // no two-parent node -> the answer is the last edge closing the cycle
        Set<Integer> cycle = findCycle(parent, n);
        for (int i = edges.length - 1; i >= 0; i--) {
            if (cycle.contains(edges[i][0]) && cycle.contains(edges[i][1])) {
                return edges[i];
            }
        }
        return new int[0];
    }

    private boolean hasCycle(int[] parent, int n) {
        for (int start = 1; start <= n; start++) {
            int steps = 0;
            int cur = start;
            while (parent[cur] != 0 && steps <= n) {
                cur = parent[cur];
                steps += 1;
            }
            if (steps > n) {
                return true;
            }
        }
        return false;
    }

    private Set<Integer> findCycle(int[] parent, int n) {
        for (int start = 1; start <= n; start++) {
            Set<Integer> seen = new HashSet<>();
            int cur = start;
            while (parent[cur] != 0 && seen.add(cur)) {
                cur = parent[cur];
            }
            if (parent[cur] != 0) {
                // walk the cycle itself
                Set<Integer> cycle = new HashSet<>();
                int node = cur;
                do {
                    cycle.add(node);
                    node = parent[node];
                } while (node != cur);
                return cycle;
            }
        }
        return new HashSet<>();
    }

    // V3
    // IDEA: UNION FIND BY RANK, TESTING BOTH CANDIDATES EXPLICITLY
    /**
     *  When a node has two parents, simply TRY dropping the second edge; if the
     *  rest still forms a valid rooted tree, that is the answer, otherwise it is
     *  the first.
     *
     *  Two direct validity checks instead of V0's implicit case analysis, so
     *  nothing has to be argued -- each candidate is verified.
     *
     *  time  = O(n * alpha)
     *  space = O(n)
     */
    public int[] findRedundantDirectedConnection_3(int[][] edges) {
        int n = edges.length;
        int[] parentOf = new int[n + 1];
        int[] first = null;
        int[] second = null;

        for (int[] e : edges) {
            if (parentOf[e[1]] != 0) {
                first = new int[] { parentOf[e[1]], e[1] };
                second = new int[] { e[0], e[1] };
            } else {
                parentOf[e[1]] = e[0];
            }
        }

        if (second == null) {
            // pure cycle: return the edge that closes it
            int[] par = new int[n + 1];
            for (int i = 0; i <= n; i++) {
                par[i] = i;
            }
            for (int[] e : edges) {
                int ru = rootOf(par, e[0]);
                int rv = rootOf(par, e[1]);
                if (ru == rv) {
                    return e;
                }
                par[rv] = ru;
            }
            return new int[0];
        }

        // verify each candidate directly
        int skipSecond = indexOfEdge(edges, second);
        if (isRootedTree(edges, skipSecond, n)) {
            return second;
        }
        return first;
    }

    private int indexOfEdge(int[][] edges, int[] target) {
        for (int i = 0; i < edges.length; i++) {
            if (edges[i][0] == target[0] && edges[i][1] == target[1]) {
                return i;
            }
        }
        return -1;
    }

}
