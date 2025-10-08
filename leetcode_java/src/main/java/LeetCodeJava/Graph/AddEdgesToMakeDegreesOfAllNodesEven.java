package LeetCodeJava.Graph;

// https://leetcode.com/problems/add-edges-to-make-degrees-of-all-nodes-even/description/

import java.util.*;

/**
 * 2508. Add Edges to Make Degrees of All Nodes Even
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * There is an undirected graph consisting of n nodes numbered from 1 to n. You are given the integer n and a 2D array edges where edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi. The graph can be disconnected.
 *
 * You can add at most two additional edges (possibly none) to this graph so that there are no repeated edges and no self-loops.
 *
 * Return true if it is possible to make the degree of each node in the graph even, otherwise return false.
 *
 * The degree of a node is the number of edges connected to it.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: n = 5, edges = [[1,2],[2,3],[3,4],[4,2],[1,4],[2,5]]
 * Output: true
 * Explanation: The above diagram shows a valid way of adding an edge.
 * Every node in the resulting graph is connected to an even number of edges.
 * Example 2:
 *
 *
 * Input: n = 4, edges = [[1,2],[3,4]]
 * Output: true
 * Explanation: The above diagram shows a valid way of adding two edges.
 * Example 3:
 *
 *
 * Input: n = 4, edges = [[1,2],[1,3],[1,4]]
 * Output: false
 * Explanation: It is not possible to obtain a valid graph with adding at most 2 edges.
 *
 *
 * Constraints:
 *
 * 3 <= n <= 105
 * 2 <= edges.length <= 105
 * edges[i].length == 2
 * 1 <= ai, bi <= n
 * ai != bi
 * There are no repeated edges.
 *
 */
public class AddEdgesToMakeDegreesOfAllNodesEven {

    // V0
//    public boolean isPossible(int n, List<List<Integer>> edges) {
//
//    }

    // V0-1
    // IDEA: GRAPH + Very simple case analysis + graph adjacency checks (gpt)
    /**  IDEA:
     *
     *  1.	Nodes with odd degree are the ones that need fixing.
     *      Adding edges can only change parity (odd ↔ even).
     * 	2.	If the number of odd-degree nodes is 0, already good → return true.
     * 	3.	If it’s 2, you might connect those two odd nodes (if edge not exist),
     *    	or connect them via a third node.
     * 	4.	If it’s 4, pair them in some way — check possible pairings that avoid existing edges.
     * 	5.	For any other odd count (like 1, 3, 5, …), impossible with at most two edges.
     *
     *
     * 	 IDEA V2:
     *
     * 	1.	Build adjacency sets for O(1) existence checks between any two nodes.
     * 	2.	Find all nodes with odd degree (odd list).
     * 	3.	If m == 0 → already ok.
     * 	4.	If m == 2 → check whether we can fix with 1 edge (connect those two), else try to fix with 2 edges by connecting both odd nodes to a third node c such that neither a–c nor b–c already exists.
     * 	5.	If m == 4 → try the three possible pairings of the four odd nodes (pair them into two edges) and check if both edges in a pairing do not already exist.
     * 	6.	Otherwise return false.
     *
     * Complexities: building adjacency O(n + E).
     * The checks are small constant work (m ≤ n). Overall O(n + E + n) ≈ O(n + E).
     *
     */
    /**  CLARIFICATION:
     *
     * 1) Why we only need to consider m = 0, 2, 4 (and not 6, 8, ...)?
     *
     *   Two facts:
     *
     *      - A. Handshaking lemma → the number of odd-degree vertices is even.
     *           Sum of degrees = 2 * edges ⇒ sum of degrees is even ⇒ number of
     *           vertices with odd degree must be even. So m (count of odd-degree nodes)
     *           is always even: 0, 2, 4, 6, ....
     *
     *      - B. Each added edge toggles parity of exactly two nodes (its endpoints).
     * 	        - Add one edge → flips parity of two vertices.
     * 	        - Add two edges → flips parity of up to four vertices
     * 	         (if edges touch distinct endpoints), or fewer if endpoints overlap.
     *
     *
     * -> We may add `AT MOST 2 edges. `
     *    So we can change parity of at most `4 vertices. `
     *    To make all odd-degree vertices even we therefore need `m ≤ 4.`
     *    Combined with (A) that means the only possible m values we
     *    can hope to fix with ≤2 edges are 0, 2, 4. If m >= 6 you need at least 3 edges
     *    (each edge fixes at most two odd nodes), so impossible.
     *
     *
     */
    public boolean isPossible_0_1(int n, List<List<Integer>> edges) {
        // Build adjacency (undirected) in a Set for fast existence check
        /**
         *   NOTE !!
         *
         * - Create adjacency structure. Using n+1 and indexing 1..n
         *   (common in LC). Each graph[i] is a HashSet
         *   of neighbors of i for O(1) neighbor checks.
         *
         *
         *   -> e.g. graph[i] contains ALL neighbors for `node i`
         *
         */
        Set<Integer>[] graph = new Set[n + 1];
        for (int i = 1; i <= n; i++) {
            graph[i] = new HashSet<>();
        }

        // Fill the adjacency sets from the given edge list. Undirected graph: add both directions.
        for (List<Integer> e : edges) {
            int u = e.get(0);
            int v = e.get(1);
            graph[u].add(v);
            graph[v].add(u);
        }

        /**
         *  NOTE !!
         *
         *  Collect nodes with odd degree
         *
         */
        List<Integer> odd = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            if ((graph[i].size() % 2) != 0) {
                odd.add(i);
            }
        }

        // edge case: if ALL nodes are `even` degree, return `true` directly
        int m = odd.size();
        if (m == 0) {
            return true;
        }

        /**
         *  Case 1) m = 2
         *
         *  - If two odd nodes a and b:
         *
         * 	    - If a and b are not already adjacent,
         * 	    a single edge a–b fixes both → return true.
         *
         * 	    - If they are adjacent, adding a–b would duplicate
         * 	    existing edge (not allowed). We still can try two edges:
         * 	    choose some node c ≠ a,b such that neither a–c nor b–c already exists.
         * 	    Adding a–c and b–c flips a and b only
         * 	    (node c gets two new edges so its degree parity stays the same),
         * 	    so this fixes a and b. If such c exists → true; else false.
         */
        if (m == 2) {

            int a = odd.get(0), b = odd.get(1);
            // If there’s no edge between them already, connect directly
            if (!graph[a].contains(b)) {
                return true;
            }
            // Otherwise try a third node c to help:
            for (int c = 1; c <= n; c++) {
                if (c == a || c == b)
                    continue;
                // can we connect a–c and b–c without conflicts?
                if (!graph[a].contains(c) && !graph[b].contains(c)) {
                    return true;
                }
            }
            return false;
        }

        /**
         *  Case 1) m = 4
         *
         *  - If four odd nodes, we must pair them up into two new edges
         *    (each new edge flips parity of its endpoints).
         *    There are exactly 3 pairings of four labeled items into two unordered pairs:
         * 	    1.	(a,b) & (c,d)
         * 	    2.	(a,c) & (b,d)
         * 	    3.	(a,d) & (b,c)
         *
         * 	- For any pairing to be valid we must be able to add both edges
         * 	  (i.e. neither edge already exists).
         * 	  If any pairing works → true. Otherwise false.
         *
         *
         */
        if (m == 4) {
            int a = odd.get(0), b = odd.get(1), c = odd.get(2), d = odd.get(3);
            // check pairing possibilities (3 ways)
            if (!graph[a].contains(b) && !graph[c].contains(d))
                return true;
            if (!graph[a].contains(c) && !graph[b].contains(d))
                return true;
            if (!graph[a].contains(d) && !graph[b].contains(c))
                return true;
            return false;
        }

        /**
         * 	All other counts (1, 3, 5, …) are impossible
         * 	given constraints (handshaking lemma rules out odd counts, but code covers them).
         * 	Counts ≥ 6 cannot be fixed with ≤2 edges.
         */
        // any other odd count (1,3,5, …) not fixable with ≤2 edges
        return false;
    }

    // V0-2
    // IDEA: (gemini)
    /**
     * Checks if it's possible to make the degree of every node even by adding at most two edges.
     * The logic handles 0, 2, and 4 initial odd-degree nodes.
     */
    public boolean isPossible_0_2(int n, List<List<Integer>> edges) {
        // 1. Initialize Adjacency List and Degrees
        // We use 1-based indexing for nodes 1 to n.
        Set<Integer>[] adj = new Set[n + 1];
        int[] degree = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            adj[i] = new HashSet<>();
        }

        for (List<Integer> edge : edges) {
            int u = edge.get(0);
            int v = edge.get(1);
            degree[u]++;
            degree[v]++;
            adj[u].add(v);
            adj[v].add(u);
        }

        // 2. Identify Odd Degree Nodes
        List<Integer> oddNodes = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            if (degree[i] % 2 != 0) {
                oddNodes.add(i);
            }
        }

        int k = oddNodes.size();

        // 3. Handle cases based on k (number of odd nodes)

        // Case 1: k = 0 (Already all even)
        if (k == 0) {
            return true;
        }

        // Case 4: k >= 6 (Impossible with only 2 edges)
        if (k > 4) {
            return false;
        }

        // Case 2: k = 2
        if (k == 2) {
            int u = oddNodes.get(0);
            int v = oddNodes.get(1);

            // Subcase 2.1: 1 edge fix (add (u, v))
            // Can we add (u, v)?
            if (!adj[u].contains(v)) {
                return true;
            }

            // Subcase 2.2: 2 edge fix (add (u, w) and (v, w))
            // Find a third even-degree node w such that neither (u, w) nor (v, w) exists.
            for (int w = 1; w <= n; w++) {
                // w must be an even-degree node (and distinct from u, v since u, v are odd)
                if (degree[w] % 2 == 0) {
                    if (!adj[u].contains(w) && !adj[v].contains(w)) {
                        return true;
                    }
                }
            }

            return false;
        }

        // Case 3: k = 4
        // Must be fixed with two non-existent edges.
        if (k == 4) {
            int a = oddNodes.get(0);
            int b = oddNodes.get(1);
            int c = oddNodes.get(2);
            int d = oddNodes.get(3);

            // Check three possible pairings:

            // Pairing 1: (a, b) and (c, d)
            if (!adj[a].contains(b) && !adj[c].contains(d)) {
                return true;
            }

            // Pairing 2: (a, c) and (b, d)
            if (!adj[a].contains(c) && !adj[b].contains(d)) {
                return true;
            }

            // Pairing 3: (a, d) and (b, c)
            if (!adj[a].contains(d) && !adj[b].contains(c)) {
                return true;
            }

            return false;
        }

        // Should be unreachable because k must be even, and we've checked 0, 2, 4, >4.
        return false;
    }

    // V1

    // V2
    // https://leetcode.com/problems/add-edges-to-make-degrees-of-all-nodes-even/solutions/7118040/add-edges-to-make-degrees-of-all-nodes-e-1og4/
    public boolean isPossible_2(int n, List<List<Integer>> edges) {
        Set<Integer>[] g = new Set[n + 1];
        Arrays.setAll(g, k -> new HashSet<>());
        for (List<Integer> e : edges) {
            int a = e.get(0), b = e.get(1);
            g[a].add(b);
            g[b].add(a);
        }
        List<Integer> vs = new ArrayList<>();
        for (int i = 1; i <= n; ++i) {
            if (g[i].size() % 2 == 1) {
                vs.add(i);
            }
        }
        if (vs.size() == 0) {
            return true;
        }
        if (vs.size() == 2) {
            int a = vs.get(0), b = vs.get(1);
            if (!g[a].contains(b)) {
                return true;
            }
            for (int c = 1; c <= n; ++c) {
                if (a != c && b != c && !g[a].contains(c) && !g[c].contains(b)) {
                    return true;
                }
            }
            return false;
        }
        if (vs.size() == 4) {
            int a = vs.get(0), b = vs.get(1), c = vs.get(2), d = vs.get(3);
            if (!g[a].contains(b) && !g[c].contains(d)) {
                return true;
            }
            if (!g[a].contains(c) && !g[b].contains(d)) {
                return true;
            }
            if (!g[a].contains(d) && !g[b].contains(c)) {
                return true;
            }
            return false;
        }
        return false;
    }


}
