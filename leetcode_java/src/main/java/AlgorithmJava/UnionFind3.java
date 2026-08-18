package AlgorithmJava;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *  UNION FIND (V3) -- built from an edge list, 1-indexed (LC 684)
 *
 *  Scope: the shape LC 684 "Redundant Connection" wants -- construct
 *         from edges, then feed the edges back through union() and
 *         watch for the first false. See UnionFind for the general
 *         0-indexed version and UnionFind2 for union by rank.
 *
 *  TWO THINGS DIFFER FROM THE USUAL VERSION:
 *
 *    1) 1-BASED INDEXING. LC 684 numbers nodes 1..n, so the arrays are
 *       allocated at n+1 and slot 0 goes unused. Mixing this up with
 *       0-based code is the most common bug in these problems.
 *
 *    2) n IS DERIVED FROM THE EDGES. The node count is the number of
 *       distinct endpoints. That is safe for LC 684 specifically, where
 *       the input is a tree plus one extra edge and therefore has
 *       exactly as many nodes as edges. On a general graph an isolated
 *       vertex would be missed -- use the (int n) constructor there.
 *
 *  THE LC 684 IDEA: a tree on n nodes has exactly n-1 edges, so n edges
 *  means exactly one is redundant. Union the edges in order; the first
 *  one whose endpoints are ALREADY connected is the answer.
 *
 *      edges [[1,2],[1,3],[2,3]]
 *        union(1,2) -> true
 *        union(1,3) -> true
 *        union(2,3) -> FALSE   2 and 3 already connected -> [2,3] is redundant
 *
 *  Time  : constructor O(E); getParent / union O(alpha(N)) ~ O(1)
 *  Space : O(N)
 */
public class UnionFind3 {

    /** parents[x] = x's parent; x is a root exactly when parents[x] == x. */
    int[] parents;

    /** size[x] = number of nodes in the tree rooted at x. */
    int[] size;

    /** Number of nodes, indexed 1..n. */
    int n;

    /**
     *  Build from an edge list, sizing from the distinct endpoints.
     *
     *  Assumes nodes are labelled 1..n with no gaps, which holds for
     *  LC 684. See the class comment for when it does not.
     */
    public UnionFind3(int[][] edges) {
        Set<Integer> nodes = new HashSet<>();
        for (int[] edge : edges) {
            nodes.add(edge[0]);
            nodes.add(edge[1]);
        }
        init(nodes.size());
    }

    /** Build with an explicit node count, for graphs that may have isolated vertices. */
    public UnionFind3(int n) {
        init(n);
    }

    private void init(int n) {
        this.n = n;
        // 1-based: slot 0 is allocated but never used
        this.parents = new int[n + 1];
        this.size = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            parents[i] = i;
            size[i] = 1;
        }
    }

    /**
     *  The root of x's set, with path compression.
     *
     *  The `x != parents[x]` test is the compact way to say "x is not a
     *  root". The recursive call must take parents[x], NOT x -- the
     *  point is to move UP the tree, and passing x would recurse forever.
     */
    public int getParent(int x) {
        if (x != parents[x]) {
            parents[x] = getParent(parents[x]);   // path compression
        }
        return parents[x];
    }

    /**
     *  Merge the sets containing x and y.
     *
     *  @return true if they were merged; false if they were ALREADY
     *          connected -- i.e. this edge closes a cycle
     */
    public boolean union(int x, int y) {
        int rootX = getParent(x);
        int rootY = getParent(y);

        if (rootX == rootY) {
            return false;                  // cycle detected
        }

        // union by size: the smaller tree hangs under the larger one
        if (size[rootX] < size[rootY]) {
            parents[rootX] = rootY;
            size[rootY] += size[rootX];
        } else {
            parents[rootY] = rootX;
            size[rootX] += size[rootY];
        }
        return true;
    }

    /** True when x and y share a root. */
    public boolean connected(int x, int y) {
        return getParent(x) == getParent(y);
    }

    /**
     *  LC 684 Redundant Connection: the first edge whose endpoints are
     *  already connected. Returns an empty array if there is none.
     */
    public static int[] findRedundantConnection(int[][] edges) {
        UnionFind3 uf = new UnionFind3(edges);
        for (int[] edge : edges) {
            if (!uf.union(edge[0], edge[1])) {
                return edge;
            }
        }
        return new int[0];
    }

    public static void main(String[] args) {
        // LC 684 example 1
        int[][] edges1 = {{1, 2}, {1, 3}, {2, 3}};
        assertThat(Arrays.toString(findRedundantConnection(edges1)).equals("[2, 3]"),
                "the LAST edge that closes a cycle");

        // LC 684 example 2
        int[][] edges2 = {{1, 2}, {2, 3}, {3, 4}, {1, 4}, {1, 5}};
        assertThat(Arrays.toString(findRedundantConnection(edges2)).equals("[1, 4]"),
                "the cycle closes at [1,4]");

        // a plain tree has no redundant edge
        int[][] tree = {{1, 2}, {1, 3}, {1, 4}};
        assertThat(findRedundantConnection(tree).length == 0, "a tree has no redundant edge");

        // the structure on its own
        UnionFind3 uf = new UnionFind3(5);
        assertThat(uf.n == 5, "five nodes, indexed 1..5");
        assertThat(uf.getParent(3) == 3, "a lone node is its own root");
        assertThat(!uf.connected(1, 2), "nothing is connected yet");

        assertThat(uf.union(1, 2), "a fresh union returns true");
        assertThat(uf.union(2, 3), "chained union");
        assertThat(uf.connected(1, 3), "connection is transitive");
        assertThat(!uf.union(3, 1), "already connected -> false");
        assertThat(uf.size[uf.getParent(1)] == 3, "three nodes in that set");

        assertThat(!uf.connected(1, 5), "node 5 is still on its own");

        // isolated vertices need the explicit-count constructor: the
        // edge-list one would size to 2, not 4
        UnionFind3 fromEdges = new UnionFind3(new int[][] {{1, 2}});
        assertThat(fromEdges.n == 2, "sized from the distinct endpoints");
        UnionFind3 explicit = new UnionFind3(4);
        assertThat(explicit.n == 4, "explicit count keeps the isolated vertices");

        System.out.println("redundant edge: " + Arrays.toString(findRedundantConnection(edges2)));
        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
