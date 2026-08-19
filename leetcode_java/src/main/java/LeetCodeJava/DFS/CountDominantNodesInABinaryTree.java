package LeetCodeJava.DFS;

// https://leetcode.com/problems/count-dominant-nodes-in-a-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  3997. Count Dominant Nodes in a Binary Tree
 *  Medium
 *
 *  You are given the root of a complete binary tree.
 *
 *  A node x is called dominant if its value is equal to the maximum value among all
 *  nodes in the subtree rooted at x.
 *
 *  Return the number of dominant nodes in the tree.
 *
 *  Example 1:
 *    Input: root = [5,3,8,2,4,7,1]
 *    Output: 5
 *    (leaves 2, 4, 7, 1 are dominant, and 8 is the max of subtree [8,7,1])
 *
 *  Example 2:
 *    Input: root = [1,2,3,1,2]
 *    Output: 4
 *
 *  Constraints:
 *    The number of nodes in the tree is in the range [1, 10^5].
 *    1 <= Node.val <= 10^9
 *    The tree is guaranteed to be a complete binary tree.
 */
public class CountDominantNodesInABinaryTree {

    // V0
    // IDEA: post-order DFS returning the max value of a subtree; a node is dominant
    //       when its own value is >= the max of both child subtrees.
    //       (complete tree -> depth is O(log n), recursion is safe)
    /**
     * time = O(n)
     * space = O(log n)
     */
    private int cnt = 0;

    public int countDominantNodes(TreeNode root) {
        this.cnt = 0;
        subTreeMax(root);
        return this.cnt;
    }

    private long subTreeMax(TreeNode node) {
        if (node == null) {
            // empty subtree never wins a max comparison
            return Long.MIN_VALUE;
        }

        long leftMax = subTreeMax(node.left);
        long rightMax = subTreeMax(node.right);
        long childMax = Math.max(leftMax, rightMax);

        if (node.val >= childMax) {
            this.cnt++;
        }

        return Math.max((long) node.val, childMax);
    }
}
