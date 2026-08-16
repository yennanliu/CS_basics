package LeetCodeJava.Tree;

// https://leetcode.com/problems/logical-or-of-two-binary-grids-represented-as-quad-trees/description/
/**
 * 558. Logical OR of Two Binary Grids Represented as Quad-Trees
 * Medium
 *
 * A Binary Matrix is a matrix in which all the elements are either 0 or 1.
 *
 * Given quadTree1 and quadTree2. quadTree1 represents a n * n binary matrix and
 * quadTree2 represents another n * n binary matrix.
 *
 * Return a Quad-Tree representing the n * n binary matrix which is the result of logical
 * bitwise OR of the two binary matrixes represented by quadTree1 and quadTree2.
 *
 * Notice that you can assign the value of a node to True or False when isLeaf is False,
 * and both are accepted in the answer.
 *
 * A Quad-Tree is a tree data structure in which each internal node has exactly four
 * children. Besides, each node has two attributes:
 *
 * - val: True if the node represents a grid of 1's or False if the node represents a
 *   grid of 0's.
 * - isLeaf: True if the node is leaf node on the tree or False if the node has the four
 *   children.
 *
 * We can construct a Quad-Tree from a two-dimensional area using the following steps:
 *
 * 1. If the current grid has the same value (i.e all 1's or all 0's) set isLeaf True and
 *    set val to the value of the grid and set the four children to Null and stop.
 * 2. If the current grid has different values, set isLeaf to False and set val to any
 *    value and divide the current grid into four sub-grids as shown in the photo.
 * 3. Recurse for each of the children with the proper sub-grid.
 *
 * Example 1:
 *
 * Input: quadTree1 = [[0,1],[1,1],[1,1],[1,0],[1,0]]
 * , quadTree2 = [[0,1],[1,1],[0,1],[1,1],[1,0],null,null,null,null,[1,0],[1,0],[1,1],[1,1]]
 * Output: [[0,0],[1,1],[1,1],[1,1],[1,0]]
 *
 * Example 2:
 *
 * Input: quadTree1 = [[1,0]], quadTree2 = [[1,0]]
 * Output: [[1,0]]
 * Explanation: Each tree represents a binary matrix of size 1*1. Each matrix contains
 * only zero. The resulting matrix is of size 1*1 with also zero.
 *
 *
 * Constraints:
 *
 * quadTree1 and quadTree2 are both valid Quad-Trees each representing a n * n grid.
 * n == 2^x where 0 <= x <= 9.
 *
 */
public class LogicalOrOfTwoBinaryGridsRepresentedAsQuadTrees {

    // Definition for a QuadTree node (offered by LC platform)
    static class Node {
        public boolean val;
        public boolean isLeaf;
        public Node topLeft;
        public Node topRight;
        public Node bottomLeft;
        public Node bottomRight;

        public Node() {
            this.val = false;
            this.isLeaf = false;
        }

        public Node(boolean val, boolean isLeaf) {
            this.val = val;
            this.isLeaf = isLeaf;
        }

        public Node(boolean val, boolean isLeaf, Node topLeft, Node topRight,
                    Node bottomLeft, Node bottomRight) {
            this.val = val;
            this.isLeaf = isLeaf;
            this.topLeft = topLeft;
            this.topRight = topRight;
            this.bottomLeft = bottomLeft;
            this.bottomRight = bottomRight;
        }
    }

    // V0
    // IDEA: RECURSION (divide and conquer on the 4 quadrants)
    /**
     *   1) if a node is a LEAF:
     *        - leaf val is TRUE (all 1s)  -> OR is all 1s -> return that leaf
     *        - leaf val is FALSE (all 0s) -> OR is the OTHER subtree -> return the other
     *
     *   2) otherwise RECURSE on the 4 quadrants, then MERGE:
     *      if the 4 results are leaves with the SAME val, COLLAPSE them into a single leaf
     *
     *   NOTE !!! step 1 is what keeps this linear -- a `true` leaf short-circuits an
     *            entire subtree of the other tree without ever descending into it.
     *
     *   NOTE !!! the collapse in step 2 is REQUIRED, not an optimisation: a quad tree
     *            with four identical leaf children is not a valid canonical answer.
     *
     *   time  = O(n1 + n2)  // number of nodes of both quad trees
     *   space = O(log N)    // recursion depth = tree height
     */
    public Node intersect(Node quadTree1, Node quadTree2) {
        // leaf shortcut : 1 OR anything = 1, 0 OR x = x
        if (quadTree1.isLeaf) {
            return quadTree1.val ? quadTree1 : quadTree2;
        }
        if (quadTree2.isLeaf) {
            return quadTree2.val ? quadTree2 : quadTree1;
        }

        Node tl = intersect(quadTree1.topLeft, quadTree2.topLeft);
        Node tr = intersect(quadTree1.topRight, quadTree2.topRight);
        Node bl = intersect(quadTree1.bottomLeft, quadTree2.bottomLeft);
        Node br = intersect(quadTree1.bottomRight, quadTree2.bottomRight);

        // all 4 quadrants uniform with the SAME value -> collapse to ONE leaf
        if (tl.isLeaf && tr.isLeaf && bl.isLeaf && br.isLeaf
                && tl.val == tr.val && tr.val == bl.val && bl.val == br.val) {
            return new Node(tl.val, true, null, null, null, null);
        }

        return new Node(false, false, tl, tr, bl, br);
    }


    // V1
    // IDEA: MATERIALISE BOTH GRIDS, OR THEM, REBUILD THE QUAD TREE
    /**
     *  Decode each quad tree into an n x n matrix, take the element-wise OR, then
     *  re-encode.
     *
     *  O(n^2) rather than O(nodes), so it is far heavier -- but it needs no
     *  reasoning about leaves at all, which makes it the oracle for the recursive
     *  merge.
     *
     *  time  = O(n^2)
     *  space = O(n^2)
     */
    public Node intersect_1(Node quadTree1, Node quadTree2) {
        /** NOTE !!!
         *
         *  the side length must be taken as the MAX over BOTH trees: a uniform
         *  grid collapses to a single leaf, so one tree on its own does not reveal
         *  how large the grid actually is.
         */
        int n = Math.max(sizeOf(quadTree1), sizeOf(quadTree2));
        boolean[][] a = new boolean[n][n];
        boolean[][] b = new boolean[n][n];
        fill(quadTree1, a, 0, 0, n);
        fill(quadTree2, b, 0, 0, n);

        boolean[][] c = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                c[i][j] = a[i][j] || b[i][j];
            }
        }
        return build(c, 0, 0, n);
    }

    /**
     * the side length a quad tree covers: 1 for a leaf, otherwise twice the
     * DEEPEST quadrant (any single quadrant may itself be a collapsed leaf)
     */
    private int sizeOf(Node node) {
        if (node == null || node.isLeaf) {
            return 1;
        }
        int a = Math.max(sizeOf(node.topLeft), sizeOf(node.topRight));
        int b = Math.max(sizeOf(node.bottomLeft), sizeOf(node.bottomRight));
        return 2 * Math.max(a, b);
    }

    private void fill(Node node, boolean[][] g, int r, int c, int size) {
        if (node.isLeaf) {
            for (int i = r; i < r + size; i++) {
                for (int j = c; j < c + size; j++) {
                    g[i][j] = node.val;
                }
            }
            return;
        }
        int half = size / 2;
        fill(node.topLeft, g, r, c, half);
        fill(node.topRight, g, r, c + half, half);
        fill(node.bottomLeft, g, r + half, c, half);
        fill(node.bottomRight, g, r + half, c + half, half);
    }

    private Node build(boolean[][] g, int r, int c, int size) {
        boolean first = g[r][c];
        boolean uniform = true;
        for (int i = r; i < r + size && uniform; i++) {
            for (int j = c; j < c + size; j++) {
                if (g[i][j] != first) {
                    uniform = false;
                    break;
                }
            }
        }
        if (uniform) {
            return new Node(first, true, null, null, null, null);
        }
        int half = size / 2;
        return new Node(false, false,
                build(g, r, c, half),
                build(g, r, c + half, half),
                build(g, r + half, c, half),
                build(g, r + half, c + half, half));
    }

    // V2
    // IDEA: RECURSIVE MERGE WITHOUT THE LEAF SHORT-CIRCUIT
    /**
     *  Always descend to matching quadrants, splitting a leaf into four copies of
     *  itself when the other side is internal, and collapse on the way back up.
     *
     *  Slower than V0 (which short-circuits on a `true` leaf) but perfectly
     *  UNIFORM -- there is a single recursive case, which is easier to prove
     *  correct and easier to adapt to AND / XOR.
     *
     *  time  = O(n1 + n2) with a larger constant
     *  space = O(log N)
     */
    public Node intersect_2(Node quadTree1, Node quadTree2) {
        if (quadTree1.isLeaf && quadTree2.isLeaf) {
            return new Node(quadTree1.val || quadTree2.val, true, null, null, null, null);
        }

        Node tl = intersect_2(quadrant(quadTree1, 0), quadrant(quadTree2, 0));
        Node tr = intersect_2(quadrant(quadTree1, 1), quadrant(quadTree2, 1));
        Node bl = intersect_2(quadrant(quadTree1, 2), quadrant(quadTree2, 2));
        Node br = intersect_2(quadrant(quadTree1, 3), quadrant(quadTree2, 3));

        if (tl.isLeaf && tr.isLeaf && bl.isLeaf && br.isLeaf
                && tl.val == tr.val && tr.val == bl.val && bl.val == br.val) {
            return new Node(tl.val, true, null, null, null, null);
        }
        return new Node(false, false, tl, tr, bl, br);
    }

    /** a leaf behaves as four copies of itself */
    private Node quadrant(Node node, int which) {
        if (node.isLeaf) {
            return node;
        }
        switch (which) {
            case 0: return node.topLeft;
            case 1: return node.topRight;
            case 2: return node.bottomLeft;
            default: return node.bottomRight;
        }
    }

    // V3
    // IDEA: SHORT-CIRCUIT ON EITHER SIDE, RETURNING SHARED SUBTREES
    /**
     *  Same short-circuits as V0, but when one side is a `false` leaf we RETURN THE
     *  OTHER SUBTREE ITSELF rather than copying it.
     *
     *  Structural sharing: the result aliases the inputs, so the merge allocates
     *  only for the genuinely mixed regions. Fine here because nothing mutates the
     *  trees afterwards -- and it is exactly what a persistent data structure does.
     *
     *  time  = O(min(n1, n2))
     *  space = O(log N)
     */
    public Node intersect_3(Node quadTree1, Node quadTree2) {
        if (quadTree1.isLeaf) {
            return quadTree1.val ? quadTree1 : quadTree2;   // shared, not copied
        }
        if (quadTree2.isLeaf) {
            return quadTree2.val ? quadTree2 : quadTree1;
        }

        Node tl = intersect_3(quadTree1.topLeft, quadTree2.topLeft);
        Node tr = intersect_3(quadTree1.topRight, quadTree2.topRight);
        Node bl = intersect_3(quadTree1.bottomLeft, quadTree2.bottomLeft);
        Node br = intersect_3(quadTree1.bottomRight, quadTree2.bottomRight);

        if (tl.isLeaf && tr.isLeaf && bl.isLeaf && br.isLeaf
                && tl.val == tr.val && tr.val == bl.val && bl.val == br.val) {
            return new Node(tl.val, true, null, null, null, null);
        }
        return new Node(false, false, tl, tr, bl, br);
    }

}
