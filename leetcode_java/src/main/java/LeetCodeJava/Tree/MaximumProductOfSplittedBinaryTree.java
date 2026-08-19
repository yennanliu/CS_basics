package LeetCodeJava.Tree;

// https://leetcode.com/problems/maximum-product-of-splitted-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  1339. Maximum Product of Splitted Binary Tree
 *  Medium
 *
 *  Given the root of a binary tree, split the binary tree into two subtrees by
 *  removing one edge such that the product of the sums of the subtrees is
 *  maximized.
 *
 *  Return the maximum product of the sums of the two subtrees. Since the answer
 *  may be too large, return it modulo 10^9 + 7.
 *
 *  Note that you need to maximize the answer before taking the mod and not
 *  after taking it.
 *
 *  Example 1:
 *    Input: root = [1,2,3,4,5,6]
 *    Output: 110
 *    Explanation: Remove the red edge and get 2 binary trees with sum 11 and 10.
 *                 Their product is 110 (11*10)
 *
 *  Example 2:
 *    Input: root = [1,null,2,3,4,null,null,5,6]
 *    Output: 90
 *
 *  Constraints:
 *    The number of nodes in the tree is in the range [2, 5 * 10^4].
 *    1 <= Node.val <= 10^4
 */
public class MaximumProductOfSplittedBinaryTree {

    // V0
    // IDEA: 2 PASS DFS
    //       pass 1: total sum of the whole tree.
    //       pass 2: for every subtree with sum s, cutting its parent edge gives
    //               product s * (total - s); keep the max (in `long`, mod at the end).
    /**
     * time = O(N)
     * space = O(H)   // recursion stack
     */
    private static final long MOD = 1_000_000_007L;

    private long total;
    private long best;

    public int maxProduct(TreeNode root) {
        this.best = 0L;
        this.total = sum(root);   // pass 1 : total sum of the whole tree
        collect(root);            // pass 2 : best split product
        return (int) (this.best % MOD);
    }

    private long sum(TreeNode node) {
        if (node == null) {
            return 0L;
        }
        return node.val + sum(node.left) + sum(node.right);
    }

    private long collect(TreeNode node) {
        if (node == null) {
            return 0L;
        }
        long cur = node.val + collect(node.left) + collect(node.right);
        // cutting the edge above `node` splits the tree into (cur, total - cur)
        this.best = Math.max(this.best, cur * (this.total - cur));
        return cur;
    }
}
