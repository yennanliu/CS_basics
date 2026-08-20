package LeetCodeJava.Design;

// https://leetcode.com/problems/kth-ancestor-of-a-tree-node/

/**
 *  1483. Kth Ancestor of a Tree Node
 *  Hard
 *
 *  You are given a tree with n nodes numbered from 0 to n - 1 in the form of a
 *  parent array parent where parent[i] is the parent of the ith node. The root of
 *  the tree is node 0. Find the kth ancestor of a given node.
 *
 *  The kth ancestor of a tree node is the kth node in the path from that node to
 *  the root node.
 *
 *  Implement the TreeAncestor class:
 *    TreeAncestor(int n, int[] parent) Initializes the object with the number of
 *      nodes in the tree and the parent array.
 *    int getKthAncestor(int node, int k) Returns the kth ancestor of the given node.
 *      If there is no such ancestor, return -1.
 *
 *  Example 1:
 *    Input
 *      ["TreeAncestor","getKthAncestor","getKthAncestor","getKthAncestor"]
 *      [[7,[-1,0,0,1,1,2,2]],[3,1],[5,2],[6,3]]
 *    Output
 *      [null, 1, 0, -1]
 *    Explanation
 *      getKthAncestor(3,1) -> 1  (parent of 3)
 *      getKthAncestor(5,2) -> 0  (grandparent of 5)
 *      getKthAncestor(6,3) -> -1 (no such ancestor)
 *
 *  Constraints:
 *    1 <= k <= n <= 5 * 10^4
 *    parent.length == n
 *    parent[0] == -1
 *    0 <= parent[i] < n for all 0 < i < n
 *    0 <= node < n
 *    There will be at most 5 * 10^4 queries.
 */
public class KthAncestorOfATreeNode {

    // V0
    // IDEA: BINARY LIFTING (jump 2^j steps at a time)
    //
    //       up[j][v] = the 2^j-th ancestor of v, built from
    //         up[j][v] = up[j-1][ up[j-1][v] ]
    //       i.e. two jumps of 2^(j-1) make one jump of 2^j.
    //
    //       a query decomposes k into its binary digits and takes one jump per set
    //       bit -> O(log n) hops instead of an O(k) parent walk.
    //       -1 means "past the root"; once we land on it we stop, because a missing
    //       ancestor stays missing. n <= 5*10^4 < 2^16, so 17 levels cover every k.
    /**
     * time = O(n log n) build, O(log n) per query
     * space = O(n log n)
     */
    private static final int LOG = 17;

    private final int[][] up;

    public KthAncestorOfATreeNode(int n, int[] parent) {
        this.up = new int[LOG][n];
        for (int v = 0; v < n; v++) {
            up[0][v] = parent[v];
        }
        for (int j = 1; j < LOG; j++) {
            for (int v = 0; v < n; v++) {
                int mid = up[j - 1][v];
                up[j][v] = mid < 0 ? -1 : up[j - 1][mid];
            }
        }
    }

    public int getKthAncestor(int node, int k) {
        int cur = node;
        for (int j = 0; j < LOG && cur >= 0; j++) {
            if (((k >> j) & 1) == 1) {
                cur = up[j][cur];
            }
        }
        return cur;
    }
}
