package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/maximum-sum-bst-in-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  1373. Maximum Sum BST in Binary Tree
 *  Hard
 *
 *  Given a binary tree root, return the maximum sum of all keys of any sub-tree
 *  which is also a Binary Search Tree (BST).
 *
 *  Assume a BST is defined as follows:
 *   - The left subtree of a node contains only nodes with keys less than the
 *     node's key.
 *   - The right subtree of a node contains only nodes with keys greater than the
 *     node's key.
 *   - Both the left and right subtrees must also be binary search trees.
 *
 *  Example 1:
 *    Input: root = [1,4,3,2,4,2,5,null,null,null,null,null,null,4,6]
 *    Output: 20
 *    Explanation: The maximum sum in a valid BST is obtained in the sub-tree
 *                 rooted at the node with key 3.
 *
 *  Example 2:
 *    Input: root = [-4,-2,-5]
 *    Output: 0
 *    Explanation: All values are negative, so return an empty BST.
 *
 *  Constraints:
 *    The number of nodes in the tree is in the range [1, 4 * 10^4].
 *    -4 * 10^4 <= Node.val <= 4 * 10^4
 */
public class MaximumSumBSTInBinaryTree {

    private int res;

    // V0
    // IDEA: POST-ORDER DFS RETURNING (isBst, subtreeMin, subtreeMax, subtreeSum)
    //       a node roots a BST iff BOTH children root BSTs and
    //         max(left subtree) < node.val < min(right subtree)
    //       that check needs the extremes of each side, so every call returns the
    //       4 values at once and the whole tree is validated in ONE bottom-up pass.
    //       an EMPTY subtree returns (true, +INF, -INF, 0): those sentinels make
    //       the comparison above trivially pass for a leaf.
    //       NOTE: the answer starts at 0 because an EMPTY BST is allowed - an
    //             all-negative tree therefore returns 0, not its largest node.
    //       NOTE: once a subtree fails, only the false flag matters upward.
    /**
     * time = O(N)
     * space = O(H)     // H = tree height
     */
    public int maxSumBST(TreeNode root) {
        this.res = 0;
        dfs(root);
        return res;
    }

    // returns {isBst (1/0), minVal, maxVal, sum}
    private long[] dfs(TreeNode node) {
        if (node == null) {
            return new long[]{1, Long.MAX_VALUE, Long.MIN_VALUE, 0};
        }

        long[] l = dfs(node.left);
        long[] r = dfs(node.right);

        if (l[0] == 1 && r[0] == 1 && l[2] < node.val && node.val < r[1]) {
            long total = l[3] + r[3] + node.val;
            if (total > res) {
                res = (int) total;
            }
            return new long[]{
                    1,
                    Math.min(l[1], node.val),
                    Math.max(r[2], node.val),
                    total
            };
        }
        return new long[]{0, 0, 0, 0};
    }
}
