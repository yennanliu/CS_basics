package LeetCodeJava.Tree;

// https://leetcode.com/problems/longest-zigzag-path-in-a-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  1372. Longest ZigZag Path in a Binary Tree
 *  Medium
 *
 *  You are given the root of a binary tree.
 *
 *  A ZigZag path for a binary tree is defined as follow:
 *    - Choose any node in the binary tree and a direction (right or left).
 *    - If the current direction is right, move to the right child of the current
 *      node; otherwise, move to the left child.
 *    - Change the direction from right to left or from left to right.
 *    - Repeat the second and third steps until you can't move in the tree.
 *
 *  Zigzag length is defined as the number of nodes visited - 1.
 *  (A single node has a length of 0).
 *
 *  Return the longest ZigZag path contained in that tree.
 *
 *  Example 1:
 *    Input: root = [1,null,1,1,1,null,null,1,1,null,1,null,null,null,1]
 *    Output: 3
 *
 *  Example 2:
 *    Input: root = [1,1,1,null,1,null,null,1,1,null,1]
 *    Output: 4
 *
 *  Example 3:
 *    Input: root = [1]
 *    Output: 0
 *
 *  Constraints:
 *    The number of nodes in the tree is in the range [1, 5 * 10^4].
 *    1 <= Node.val <= 100
 */
public class LongestZigZagPathInABinaryTree {

    // V0
    // IDEA: DFS carrying (arrival direction, current path length)
    //       if we arrived at a node by going LEFT, the zigzag continues by going
    //       RIGHT (length + 1); going LEFT again restarts the path at length 1.
    /**
     * time = O(N)
     * space = O(H)   // recursion stack
     */
    private int maxLen;

    public int longestZigZag(TreeNode root) {
        this.maxLen = 0;
        if (root == null) {
            return 0;
        }
        dfs(root.left, true, 1);    // arrived at root.left by moving LEFT
        dfs(root.right, false, 1);  // arrived at root.right by moving RIGHT
        return this.maxLen;
    }

    private void dfs(TreeNode node, boolean cameFromLeft, int len) {
        if (node == null) {
            return;
        }
        this.maxLen = Math.max(this.maxLen, len);

        if (cameFromLeft) {
            dfs(node.right, false, len + 1); // zigzag continues
            dfs(node.left, true, 1);         // same direction -> restart
        } else {
            dfs(node.left, true, len + 1);   // zigzag continues
            dfs(node.right, false, 1);       // same direction -> restart
        }
    }
}
