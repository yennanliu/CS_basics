package LeetCodeJava.Tree;

// https://leetcode.com/problems/distribute-coins-in-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  979. Distribute Coins in Binary Tree
 *  Medium
 *
 *  You are given the root of a binary tree with n nodes where each node in the
 *  tree has node.val coins. There are n coins in total throughout the whole tree.
 *
 *  In one move, we may choose two adjacent nodes and move one coin from one node
 *  to another. A move may be from parent to child, or from child to parent.
 *
 *  Return the minimum number of moves required to make every node have exactly
 *  one coin.
 *
 *  Example 1:
 *    Input: root = [3,0,0]
 *    Output: 2
 *
 *  Example 2:
 *    Input: root = [0,3,0]
 *    Output: 3
 *
 *  Constraints:
 *    The number of nodes in the tree is n.
 *    1 <= n <= 100
 *    0 <= Node.val <= n
 *    The sum of all Node.val is n.
 */
public class DistributeCoinsInBinaryTree {

    // V0
    // IDEA: POST-ORDER DFS
    //       each subtree returns its "balance" = (coins it has) - (coins it needs).
    //       the number of coins crossing the edge to the parent is abs(balance),
    //       and every such crossing costs one move.
    /**
     * time = O(N)
     * space = O(H)   // recursion stack
     */
    private int moves;

    public int distributeCoins(TreeNode root) {
        this.moves = 0;
        balance(root);
        return this.moves;
    }

    private int balance(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int left = balance(node.left);
        int right = balance(node.right);

        // coins that must flow through the edges to the children
        this.moves += Math.abs(left) + Math.abs(right);

        // surplus (or deficit) handed up to the parent
        return node.val + left + right - 1;
    }
}
