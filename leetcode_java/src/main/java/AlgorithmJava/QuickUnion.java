package AlgorithmJava;

import java.util.Arrays;

/**
 *  QUICK UNION -- the lazy union-find, path compression only
 *
 *  Scope: the MINIMAL disjoint-set structure -- parent pointers plus
 *         path compression, and nothing else. See UnionFind (union by
 *         size) and UnionFind2 (union by rank) for the versions that
 *         also balance the trees, and algorithm/python/quick_find.py
 *         for the eager alternative.
 *
 *  Quick FIND stores a flat group label and relabels the entire array on
 *  every union -- O(N) per union. Quick UNION is LAZY: root[] stores a
 *  PARENT pointer, groups become trees, and union rewires ONE root.
 *
 *      n = 5, edges = [[0,1], [1,2], [3,4]]
 *
 *      init          root = [0, 1, 2, 3, 4]     five singleton trees
 *      union(0, 1)   root = [0, 0, 2, 3, 4]     1 hangs under 0
 *      union(1, 2)   root = [0, 0, 0, 3, 4]     find(1) is 0, so 2 hangs under 0
 *      union(3, 4)   root = [0, 0, 0, 3, 3]     4 hangs under 3
 *
 *      two sets: {0, 1, 2} rooted at 0, and {3, 4} rooted at 3
 *      connected(1, 2) -> find(1) == find(2) == 0 -> true
 *
 *  PATH COMPRESSION is what keeps this fast. find() recurses to the
 *  root and, on the way back out, points every node it passed straight
 *  at that root:
 *
 *      before find(3):  3 -> 2 -> 1 -> 0        (three hops)
 *      after  find(3):  3 -> 0, 2 -> 0, 1 -> 0  (one hop each, forever)
 *
 *  WHAT IS MISSING: without union by size or rank, a union can still
 *  hang a tall tree under a short one. Compression alone gives
 *  O(log N) amortised; adding the balancing rule gets you to
 *  O(alpha(N)). For teaching, compression alone is the clearer story --
 *  for real use, prefer UnionFind.
 *
 *  Time  : constructor O(N); find / union / connected O(log N) amortised
 *  Space : O(N)
 */
public class QuickUnion {

    /** root[i] = i's parent; i is a root exactly when root[i] == i. */
    private final int[] root;

    /** Start with `size` elements, each its own root. */
    public QuickUnion(int size) {
        root = new int[size];
        for (int i = 0; i < size; i++) {
            root[i] = i;
        }
    }

    /**
     *  The root of e's tree, with path compression.
     *
     *  The recursive call must take root[e], NOT e -- the point is to
     *  move UP the tree, and passing e again would recurse forever.
     */
    public int find(int e) {
        validate(e);
        if (root[e] == e) {
            return e;                     // e is its own parent -> it IS the root
        }
        root[e] = find(root[e]);          // path compression
        return root[e];
    }

    /** Merge the sets containing e1 and e2 -- one pointer write. */
    public void union(int e1, int e2) {
        int rootE1 = find(e1);
        int rootE2 = find(e2);
        if (rootE1 != rootE2) {
            root[rootE1] = rootE2;
        }
    }

    /** True when e1 and e2 share a root. */
    public boolean connected(int e1, int e2) {
        return find(e1) == find(e2);
    }

    private void validate(int e) {
        if (e < 0 || e >= root.length) {
            throw new IndexOutOfBoundsException("element " + e + " is not in 0.." + (root.length - 1));
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(root);
    }

    public static void main(String[] args) {
        QuickUnion qu = new QuickUnion(5);
        assertThat(qu.toString().equals("[0, 1, 2, 3, 4]"), "each element starts as its own root");
        assertThat(!qu.connected(0, 1), "nothing is connected yet");

        qu.union(0, 1);
        assertThat(qu.toString().equals("[1, 1, 2, 3, 4]"), "0 now hangs under 1");

        qu.union(1, 2);
        qu.union(3, 4);
        assertThat(qu.connected(0, 2), "0-1 and 1-2 makes 0 and 2 connected");
        assertThat(qu.connected(3, 4), "3 and 4 are connected");
        assertThat(!qu.connected(0, 3), "the two sets are separate");

        qu.union(0, 2);
        assertThat(qu.connected(0, 2), "a redundant union changes nothing");

        // path compression flattens as a side effect of searching
        QuickUnion deep = new QuickUnion(4);
        deep.union(1, 0);
        deep.union(2, 1);
        deep.union(3, 2);                 // a chain, one node at a time
        assertThat(deep.find(3) == deep.find(0), "same set");
        int root = deep.find(0);
        for (int i = 0; i < 4; i++) {
            assertThat(deep.root[i] == root || i == root, "node " + i + " points straight at the root");
        }

        try {
            qu.find(99);
            assertThat(false, "expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // ok
        }

        System.out.println("0 and 2 connected: " + qu.connected(0, 2));   // true
        System.out.println("0 and 3 connected: " + qu.connected(0, 3));   // false
        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
