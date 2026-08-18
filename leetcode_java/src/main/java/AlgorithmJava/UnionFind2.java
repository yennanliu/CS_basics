package AlgorithmJava;

import java.util.Arrays;

/**
 *  UNION FIND (V2) -- union by rank, recursive path compression
 *
 *  Scope: the RANK-based variant, and the cycle-detection use of
 *         union()'s boolean return. See UnionFind for union by size
 *         with an iterative find, UnionFind3 for the LC 684 edge-list
 *         constructor, QuickUnion for the version without either
 *         optimisation.
 *
 *  RANK vs SIZE -- two ways to decide which tree hangs under which:
 *
 *      size[r]   how many NODES are in r's tree
 *      rank[r]   an upper bound on r's tree HEIGHT
 *
 *  Both keep the height at O(log N) and both are effectively O(1) once
 *  path compression is added. Rank uses slightly less bookkeeping;
 *  size is more useful when you also want "how big is this group?".
 *  NOTE rank stops being the exact height as soon as path compression
 *  starts flattening trees -- it remains a valid upper bound, which is
 *  all the argument needs.
 *
 *  CYCLE DETECTION -- why union() returns a boolean:
 *  If p and q already share a root, the edge p-q closes a CYCLE, so
 *  union() does nothing and returns false. Feeding a graph's edges
 *  through union() and watching for that false is the standard test for
 *  "is this a tree?" (LC 261) and "which edge is redundant?" (LC 684).
 *
 *      edges (0,1) (1,2) (2,0)
 *        union(0,1) -> true    joined
 *        union(1,2) -> true    joined
 *        union(2,0) -> FALSE   0 and 2 already connected -> cycle
 *
 *  Time  : constructor O(N); find / union / connected O(alpha(N)) ~ O(1)
 *  Space : O(N), plus O(log N) recursion depth inside find()
 */
public class UnionFind2 {

    /** parent[x] = x's parent; x is a root exactly when parent[x] == x. */
    private final int[] parent;

    /** rank[x] = an upper bound on the height of the tree rooted at x. */
    private final int[] rank;

    private int count;

    /** Start with n elements, each in its own group. */
    public UnionFind2(int n) {
        parent = new int[n];
        rank = new int[n];
        count = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;   // every node is its own root, so parent[x] == x means "x is a root"
            rank[i] = 0;
        }
    }

    /**
     *  The root of x's tree, with path compression.
     *
     *  The recursion has to be find(parent[x]), NOT find(x): the point
     *  is to move UP the tree. Passing x again would recurse forever.
     *  Unwinding then writes the root into every node on the path, so
     *  the whole path is one hop from the root next time.
     *
     *      parent = [0, 0, 1, 2]        3 -> 2 -> 1 -> 0
     *      find(3) recurses to the root, then on the way back sets
     *      parent[3] = parent[2] = parent[1] = 0
     *      parent = [0, 0, 0, 0]        every node one hop from the root
     */
    public int find(int x) {
        validate(x);
        if (parent[x] != x) {
            parent[x] = find(parent[x]);   // path compression
        }
        return parent[x];
    }

    /**
     *  Merge the groups containing x and y.
     *
     *  @return true if they were merged; false if they were ALREADY
     *          connected -- i.e. this edge closes a cycle
     */
    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return false;                  // cycle detected
        }

        // union by rank: the shorter tree hangs under the taller one, so
        // the taller one's height does not grow
        if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else {
            // equal heights: pick either, and the winner grows by one
            parent[rootY] = rootX;
            rank[rootX]++;
        }

        count--;
        return true;
    }

    /** True when x and y share a root. */
    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }

    /** Number of groups remaining. */
    public int count() {
        return count;
    }

    private void validate(int x) {
        if (x < 0 || x >= parent.length) {
            throw new IndexOutOfBoundsException("element " + x + " is not in 0.." + (parent.length - 1));
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(parent);
    }

    public static void main(String[] args) {
        UnionFind2 uf = new UnionFind2(6);
        assertThat(uf.count() == 6, "every element starts alone");
        assertThat(uf.find(3) == 3, "a lone element is its own root");
        assertThat(!uf.connected(0, 1), "nothing is connected yet");

        assertThat(uf.union(0, 1), "a fresh union returns true");
        assertThat(uf.union(1, 2), "chained union");
        assertThat(uf.connected(0, 2), "connection is transitive");
        assertThat(uf.count() == 4, "{0,1,2} {3} {4} {5}");

        // the redundant edge -- this false is the cycle signal
        assertThat(!uf.union(2, 0), "already connected -> false, and a cycle");
        assertThat(uf.count() == 4, "a rejected union does not change the count");

        // path compression flattens the path as a side effect of find()
        UnionFind2 deep = new UnionFind2(4);
        deep.parent[1] = 0;
        deep.parent[2] = 1;
        deep.parent[3] = 2;                 // 3 -> 2 -> 1 -> 0
        assertThat(deep.find(3) == 0, "walks to the root");
        assertThat(deep.toString().equals("[0, 0, 0, 0]"), "every node now points straight at the root");

        // cycle detection over a graph's edges: LC 684 / LC 261
        int[][] tree = {{0, 1}, {0, 2}, {0, 3}};
        assertThat(!hasCycle(4, tree), "a tree has no cycle");

        int[][] cyclic = {{0, 1}, {1, 2}, {2, 0}};
        assertThat(hasCycle(3, cyclic), "0-1-2-0 is a cycle");

        try {
            uf.find(99);
            assertThat(false, "expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // ok
        }

        System.out.println("parent: " + uf);
        System.out.println("groups: " + uf.count());
        System.out.println("Success.");
    }

    /** True if the undirected edge list closes a cycle. */
    private static boolean hasCycle(int n, int[][] edges) {
        UnionFind2 uf = new UnionFind2(n);
        for (int[] edge : edges) {
            if (!uf.union(edge[0], edge[1])) {
                return true;
            }
        }
        return false;
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
