package AlgorithmJava;

import java.util.Arrays;

/**
 *  UNION FIND (V1) -- union by size, with path compression
 *
 *  Scope: the general-purpose disjoint-set structure over 0..n-1.
 *         See UnionFind2 for the union-by-RANK variant, UnionFind3 for
 *         the edge-list constructor used by LC 684, and QuickUnion for
 *         the bare version without either optimisation.
 *
 *  Union-Find (Disjoint Set Union) answers one question fast: are p and
 *  q in the same group? Each group is a tree; the ROOT is the group's
 *  identity, and two elements are connected exactly when they share a root.
 *
 *      parent = [0, 1, 2, 3]           four singleton trees
 *      union(1, 2)                          1
 *                                           |
 *                                           2
 *      union(1, 3)  -> size[1]=2 > size[3]=1, so 3 hangs under 1
 *                                           1
 *                                          / \
 *                                         2   3
 *
 *  find() walks parent pointers to the root, so every operation costs
 *  the TREE HEIGHT. Two cheap tricks keep that height tiny:
 *
 *    UNION BY SIZE       always hang the SMALLER tree under the larger
 *                        one. Merging the other way is what builds a
 *                        pathological chain; this caps the height at log N.
 *    PATH COMPRESSION    while walking to the root, point each node at
 *                        its GRANDPARENT. The tree flattens as a side
 *                        effect of searching it.
 *
 *  Together they make each operation effectively constant: O(alpha(N)),
 *  where alpha is the inverse Ackermann function -- below 5 for any N
 *  that fits in memory.
 *
 *  Used by: LC 200 Number of Islands, LC 547 Number of Provinces,
 *           LC 684 Redundant Connection, LC 721 Accounts Merge,
 *           LC 323 Number of Connected Components.
 *
 *  Time  : constructor O(N); find / union / connected O(alpha(N)) ~ O(1)
 *  Space : O(N)
 *
 *  Reference: labuladong's algorithm book, p.418
 */
public class UnionFind {

    /** Number of groups remaining. */
    private int count;

    /** parent[i] = i's parent; i is a root exactly when parent[i] == i. */
    private int[] parent;

    /** size[i] = number of nodes in the tree rooted at i (only meaningful at a root). */
    private int[] size;

    /** Start with n elements, each in its own group. */
    public UnionFind(int n) {
        this.count = n;
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    /**
     *  Merge the groups containing p and q.
     *
     *  Only ONE pointer is rewritten -- that is what makes this "lazy",
     *  as opposed to quick-find's full relabel of the array.
     */
    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) {
            return;                       // already together
        }

        // union by size: the smaller tree hangs under the larger one
        if (size[rootP] > size[rootQ]) {
            parent[rootQ] = rootP;
            size[rootP] += size[rootQ];
        } else {
            parent[rootP] = rootQ;
            size[rootQ] += size[rootP];
        }
        count--;
    }

    /** True when p and q share a root. */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    /**
     *  The root of x's tree -- the group's identity.
     *
     *  Path compression: `parent[x] = parent[parent[x]]` re-points x at
     *  its grandparent before stepping up, halving the remaining path.
     *  Repeated finds therefore get cheaper and cheaper.
     */
    public int find(int x) {
        validate(x);
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];   // path compression
            x = parent[x];
        }
        return x;
    }

    /** Number of groups remaining. */
    public int count() {
        return count;
    }

    /** Number of elements in the group containing x. */
    public int size(int x) {
        return size[find(x)];
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
        UnionFind uf = new UnionFind(10);
        assertThat(uf.count() == 10, "every element starts alone");
        assertThat(uf.find(7) == 7, "a lone element is its own root");
        assertThat(uf.size(7) == 1, "a lone group has size 1");

        uf.union(4, 3);
        uf.union(3, 8);
        uf.union(6, 5);
        uf.union(9, 4);

        assertThat(uf.connected(4, 8), "directly connected");
        assertThat(uf.connected(9, 3), "transitively connected: 9-4, 4-3");
        assertThat(!uf.connected(4, 6), "separate groups stay separate");
        assertThat(uf.count() == 6, "{3,4,8,9} {5,6} {0} {1} {2} {7}");
        assertThat(uf.size(9) == 4, "four elements in that group");
        assertThat(uf.size(5) == 2, "two in the other");

        uf.union(4, 9);
        assertThat(uf.count() == 6, "a redundant union does not change the count");

        uf.union(4, 6);
        assertThat(uf.count() == 5, "merging two multi-element groups");
        assertThat(uf.size(3) == 6, "sizes add up");

        // union by size + path compression keep the tree flat even for a
        // sequence deliberately built to make a chain
        UnionFind chain = new UnionFind(10_000);
        for (int i = 0; i < 9_999; i++) {
            chain.union(i, i + 1);
        }
        assertThat(chain.count() == 1, "all 10000 in one group");
        assertThat(chain.connected(0, 9_999), "ends are connected");

        int depth = 0;
        for (int i = 9_999; chain.parent[i] != i; i = chain.parent[i]) {
            depth++;
        }
        assertThat(depth <= 2, "the tree is 2 levels deep, not 9999");

        try {
            uf.find(999);
            assertThat(false, "expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // ok
        }

        System.out.println("parent: " + uf);
        System.out.println("groups: " + uf.count());
        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
