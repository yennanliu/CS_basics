package LeetCodeJava.Tree;

// https://leetcode.com/problems/binary-tree-tilt/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  563. Binary Tree Tilt
 *  Easy
 *
 *  Given the root of a binary tree, return the sum of every tree node's tilt.
 *
 *  The tilt of a tree node is the absolute difference between the sum of all
 *  left subtree node values and all right subtree node values. If a node does
 *  not have a left child, the sum of the left subtree node values is treated
 *  as 0 (the same for the right child).
 *
 *  Example 1:
 *
 *  Input: root = [1,2,3]
 *  Output: 1
 *  Explanation: tilt of node 2 = 0, node 3 = 0, node 1 = |2-3| = 1.
 *
 *  Example 2:
 *
 *  Input: root = [4,2,9,3,5,null,7]
 *  Output: 15
 *
 *  Constraints:
 *
 *  The number of nodes in the tree is in the range [0, 10^4].
 *  -1000 <= Node.val <= 1000
 */
public class BinaryTreeTilt {

    private int tiltSum = 0;

    // V0
    // IDEA: post-order DFS, each call returns its subtree sum and
    //       accumulates |leftSum - rightSum| into a running total
    /**
     * time = O(n)
     * space = O(h)
     */
    public int findTilt(TreeNode root) {
        this.tiltSum = 0;
        subTreeSum(root);
        return this.tiltSum;
    }

    private int subTreeSum(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int left = subTreeSum(node.left);
        int right = subTreeSum(node.right);
        this.tiltSum += Math.abs(left - right);
        return left + right + node.val;
    }
}
