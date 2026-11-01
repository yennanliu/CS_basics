package LeetCodeJava.Tree;

// https://leetcode.com/problems/find-root-of-a-colored-tree/description/

/**
 * 4085. Find Root of a Colored Tree
 * Medium
 *
 * You are given an undirected tree of n nodes labeled from 0 to n - 1,
 * represented by a 2D integer array edges of length n - 1, where
 * edges[i] = [ui, vi] indicates that there is an undirected edge between
 * nodes ui and vi.
 *
 * You are also given a string colors of length n, where colors[i] is one of
 * 'R', 'B', or 'W', and denotes the color of node i.
 *
 * A node chosen as root must satisfy both of the following conditions once
 * the tree is oriented away from it:
 *
 * Binary-tree property: Every node has at most two children.
 *
 * Layered color pattern: All nodes at the same depth have the same color.
 * Starting at depth 0, these colors follow one of the three repeating
 * sequences:
 *
 *     R -> B -> W -> R -> B -> W -> ...
 *     B -> W -> R -> B -> W -> R -> ...
 *     W -> R -> B -> W -> R -> B -> ...
 *
 * Return the index of any root that satisfies both conditions, or -1 if no
 * such root exists.
 *
 * A tree is a connected undirected graph with no cycles.
 *
 * The depth of a node is the number of edges on the path from the root to
 * that node. The root has depth 0. A child of a node is an adjacent node
 * whose depth is one greater.
 *
 * The degree of a node is the number of edges incident to it.
 *
 *
 * Example 1:
 *
 * Input: edges = [[3,1],[3,2],[1,4],[2,0]], colors = "BRRWB"
 * Output: 3
 * Explanation: Rooting at node 3 gives each node at most two children and every edge
 * advances to the next color in the cycle (W -> R -> B), so return 3.
 *
 * Example 2:
 *
 * Input: edges = [[0,1],[1,2],[2,3]], colors = "RBRB"
 * Output: -1
 * Explanation: No root satisfies the required color step on every edge, so return -1.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 10^5
 * edges.length == n - 1
 * edges[i] = [ui, vi]
 * 0 <= ui, vi < n
 * colors.length == n
 * colors[i] is one of 'R', 'B', 'W'.
 * The input graph is a tree and every node has degree at most three.
 *
 */
public class FindRootOfAColoredTree {

    // V0
    // IDEA: EACH EDGE'S COLOR STEP FIXES ITS DIRECTION -> ROOT = THE ONLY NODE WITH NO PARENT
    //   all 3 allowed sequences are the SAME cycle R -> B -> W -> R started at a different
    //   color, so "same depth, same color" is equivalent to: for every edge parent -> child,
    //   color[child] == next(color[parent]).
    //   for an edge with two different colors exactly one direction is a step forward, so the
    //   direction is FORCED; equal colors on an edge -> no root works -> -1.
    //   after orienting all n - 1 edges: a node with 2+ parents -> -1; a node with 3 children
    //   breaks the binary-tree property -> -1; otherwise the one node with no parent is the root.
    //   (brute force — try every root and DFS-check — is O(N^2) and TLEs at 10^5)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int findColoredRoot(int[][] edges, String colors) {
        int n = colors.length();

        // edge case: a single node is trivially a valid root
        if (n == 1) {
            return 0;
        }

        // parentCount[v] = in-degree once edges are oriented; childCount[v] = out-degree
        int[] parentCount = new int[n];
        int[] childCount = new int[n];

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int parent;
            int child;

            if (nextColor(colors.charAt(u)) == colors.charAt(v)) {
                // u's color steps forward to v's: u is one level above v
                parent = u;
                child = v;
            } else if (nextColor(colors.charAt(v)) == colors.charAt(u)) {
                parent = v;
                child = u;
            } else {
                // equal colors on an edge: whichever end is the parent,
                // the child's color would not advance
                return -1;
            }

            parentCount[child]++;
            childCount[parent]++;
        }

        int root = -1;
        for (int node = 0; node < n; node++) {
            // reached from two parents -> no orientation of the tree works
            if (parentCount[node] > 1) {
                return -1;
            }
            // binary-tree property: at most two children
            if (childCount[node] > 2) {
                return -1;
            }
            // n - 1 edges and every other node has exactly one parent,
            // so exactly one node is left without a parent
            if (parentCount[node] == 0) {
                root = node;
            }
        }

        return root;
    }

    /** The color a child must have, given its parent's color, in the cycle R -> B -> W -> R. */
    private char nextColor(char color) {
        if (color == 'R') {
            return 'B';
        }
        if (color == 'B') {
            return 'W';
        }
        return 'R';
    }
}
