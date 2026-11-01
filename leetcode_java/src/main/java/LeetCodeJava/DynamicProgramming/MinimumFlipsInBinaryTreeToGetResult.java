package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-flips-in-binary-tree-to-get-result/description/

import LeetCodeJava.DataStructure.TreeNode;

/**
 * 2313. Minimum Flips in Binary Tree to Get Result
 * Hard
 *
 * You are given the root of a binary tree with the following properties:
 *
 * Leaf nodes have either the value 0 or 1, representing false and true respectively.
 * Non-leaf nodes have either the value 2, 3, 4, or 5, representing the boolean operations OR, AND, XOR, and NOT, respectively.
 *
 * You are also given a boolean result, which is the desired result of the evaluation of the root node.
 *
 * The evaluation of a node is as follows:
 *
 * If the node is a leaf node, the evaluation is the value of the node, i.e. true or false.
 * Otherwise, evaluate the node's children and apply the boolean operation of its value with the children's evaluations.
 *
 * In one operation, you can flip a leaf node, which causes a false node to become true, and a true node to become false.
 *
 * Return the minimum number of operations that need to be performed such that the evaluation of root yields result. It can be shown that there is always a way to achieve result.
 *
 * A leaf node is a node that has zero children.
 *
 * Note: NOT nodes have either a left child or a right child, but other non-leaf nodes have both a left child and a right child.
 *
 *
 * Example 1:
 *
 * Input: root = [3,5,4,2,null,1,1,1,0], result = true
 * Output: 2
 * Explanation:
 * It can be shown that a minimum of 2 nodes have to be flipped to make the root of the tree
 * evaluate to true. One way to achieve this is shown in the diagram above.
 *
 * Example 2:
 *
 * Input: root = [0], result = false
 * Output: 0
 * Explanation:
 * The root of the tree already evaluates to false, so 0 nodes have to be flipped.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 10^5].
 * 0 <= Node.val <= 5
 * OR, AND, and XOR nodes have 2 children.
 * NOT nodes have 1 child.
 * Leaf nodes have a value of 0 or 1.
 * Non-leaf nodes have a value of 2, 3, 4, or 5.
 *
 */
public class MinimumFlipsInBinaryTreeToGetResult {

    private static final int FALSE_LEAF = 0;
    private static final int TRUE_LEAF = 1;
    private static final int OR = 2;
    private static final int AND = 3;
    private static final int XOR = 4;
    private static final int INF = Integer.MAX_VALUE / 2;

    // V0
    // IDEA: TREE DP — EACH NODE RETURNS {MIN FLIPS TO EVALUATE false, MIN FLIPS TO EVALUATE true}
    /**
     * time = O(N)
     * space = O(H)
     */
    public int minimumFlips(TreeNode root, boolean result) {
        int[] cost = minFlips(root);
        return result ? cost[1] : cost[0];
    }

    /** Returns {flips to make node false, flips to make node true}. */
    private int[] minFlips(TreeNode node) {
        if (node == null) {
            // a missing child (only for NOT) must never be chosen
            return new int[]{INF, INF};
        }
        // leaf: free to keep its value, one flip to change it
        if (node.val == FALSE_LEAF) {
            return new int[]{0, 1};
        }
        if (node.val == TRUE_LEAF) {
            return new int[]{1, 0};
        }

        int[] left = minFlips(node.left);
        int[] right = minFlips(node.right);
        int leftFalse = left[0];
        int leftTrue = left[1];
        int rightFalse = right[0];
        int rightTrue = right[1];

        if (node.val == OR) {
            // false only if both false; true if at least one true
            int makeFalse = leftFalse + rightFalse;
            int makeTrue = Math.min(leftTrue + rightTrue, Math.min(leftTrue + rightFalse, leftFalse + rightTrue));
            return new int[]{makeFalse, makeTrue};
        }
        if (node.val == AND) {
            // true only if both true; false if at least one false
            int makeTrue = leftTrue + rightTrue;
            int makeFalse = Math.min(leftFalse + rightFalse, Math.min(leftFalse + rightTrue, leftTrue + rightFalse));
            return new int[]{makeFalse, makeTrue};
        }
        if (node.val == XOR) {
            // false when children agree, true when they differ
            int makeFalse = Math.min(leftFalse + rightFalse, leftTrue + rightTrue);
            int makeTrue = Math.min(leftFalse + rightTrue, leftTrue + rightFalse);
            return new int[]{makeFalse, makeTrue};
        }
        // NOT: exactly one child exists; the other reports INF so min picks the real one
        int makeFalse = Math.min(leftTrue, rightTrue);
        int makeTrue = Math.min(leftFalse, rightFalse);
        return new int[]{makeFalse, makeTrue};
    }
}
