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

}
