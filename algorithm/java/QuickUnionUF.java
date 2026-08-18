import java.util.Arrays;

/**
 *  QUICK UNION -- the lazy union-find, with weighting and path compression
 *
 *  Quick Find (see QuickFindUF.java) relabels the whole array on every
 *  union. Quick Union is LAZY: id[] now stores a PARENT pointer, the
 *  groups become trees, and union only has to rewire one root.
 *
 *      id = [0, 1, 2, 3, 4, 5]      six singleton trees
 *      union(1, 3)  ->  id[1] = 3        3
 *                                        |
 *                                        1
 *      union(1, 4)  ->  root(1) is 3, so id[3] = 4
 *                                        4
 *                                        |
 *                                        3
 *                                        |
 *                                        1
 *
 *  find() now walks parent pointers to the root, so the cost of every
 *  operation is the TREE HEIGHT. Left alone, unions can build a tree
 *  that is one long chain and every find degrades to O(N). Two cheap
 *  fixes prevent that:
 *
 *    WEIGHTING            always hang the SMALLER tree under the
 *                         larger one, which caps the height at log N
 *    PATH COMPRESSION     while walking to the root, point each node
 *                         at its grandparent, flattening the tree as a
 *                         side effect of searching it
 *
 *  Together they make each operation effectively constant: O(alpha(N)),
 *  where alpha is the inverse Ackermann function -- below 5 for any N
 *  that fits in memory.
 *
 *  Time  : constructor O(N); find / union / connected O(alpha(N)) ~ O(1)
 *          (without weighting + compression, O(N) worst case)
 *  Space : O(N)
 *
 *  Reference: https://www.coursera.org/learn/algorithms-part1/lecture/ZgecU/quick-union
 */
public class QuickUnionUF {

    private final int[] parent;   // parent[i] = i's parent; i is a root when parent[i] == i
    private final int[] size;     // size[i] = number of nodes in the tree rooted at i
    private int count;            // number of distinct groups

    /** Start with N elements, each its own root. */
    public QuickUnionUF(int n) {
        parent = new int[n];
        size = new int[n];
        count = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    /** Number of groups remaining. */
    public int count() {
        return count;
    }

    /**
     *  The root of p's tree -- the group's identity.
     *
     *  Walks up parent pointers, and applies PATH COMPRESSION on the
     *  way: pointing each node at its grandparent halves the remaining
     *  path, so repeated finds get cheaper and cheaper.
     */
    public int find(int p) {
        validate(p);
        while (p != parent[p]) {
            parent[p] = parent[parent[p]];   // path compression
            p = parent[p];
        }
        return p;
    }

    /** True when p and q share a root. */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    /**
     *  Merge the two trees -- one pointer write, not a full scan.
     *
     *  WEIGHTING: the smaller tree goes UNDER the larger one. Hanging
     *  them the other way is what builds the pathological chain.
     */
    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) {
            return;                          // already together
        }
        if (size[rootP] < size[rootQ]) {
            parent[rootP] = rootQ;
            size[rootQ] += size[rootP];
        } else {
            parent[rootQ] = rootP;
            size[rootP] += size[rootQ];
        }
        count--;
    }

    /** Number of elements in p's group. */
    public int groupSize(int p) {
        return size[find(p)];
    }

    @Override
    public String toString() {
        return Arrays.toString(parent);
    }

    private void validate(int p) {
        if (p < 0 || p >= parent.length) {
            throw new IndexOutOfBoundsException("element " + p + " is not in 0.." + (parent.length - 1));
        }
    }

    public static void main(String[] args) {
        QuickUnionUF uf = new QuickUnionUF(10);
        assertThat(uf.count() == 10, "every element starts alone");
        assertThat(uf.find(7) == 7, "a lone element is its own root");

        uf.union(4, 3);
        uf.union(3, 8);
        uf.union(6, 5);
        uf.union(9, 4);
        assertThat(uf.connected(4, 8) && uf.connected(9, 3), "transitively connected");
        assertThat(!uf.connected(4, 6), "separate groups stay separate");
        assertThat(uf.count() == 6, "{3,4,8,9} {5,6} {0} {1} {2} {7}");
        assertThat(uf.groupSize(9) == 4, "four elements in that group");

        uf.union(4, 9);
        assertThat(uf.count() == 6, "a redundant union does not change the count");

        // weighting + path compression keep the tree flat even for a
        // sequence designed to build a chain
        QuickUnionUF chain = new QuickUnionUF(1000);
        for (int i = 0; i < 999; i++) {
            chain.union(i, i + 1);
        }
        assertThat(chain.count() == 1, "all 1000 in one group");
        assertThat(chain.connected(0, 999), "ends are connected");

        int depth = 0;
        for (int i = 999; chain.parent[i] != i; i = chain.parent[i]) {
            depth++;
        }
        assertThat(depth <= 2, "the tree is 2 levels deep, not 999 -- weighting worked");

        try {
            uf.find(-1);
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
