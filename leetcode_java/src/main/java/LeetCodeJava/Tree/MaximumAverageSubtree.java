package LeetCodeJava.Tree;

// https://leetcode.com/problems/maximum-average-subtree/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  1120. Maximum Average Subtree
 *  Medium
 *
 *  Given the root of a binary tree, return the maximum average value of a
 *  subtree of that tree. Answers within 10^-5 of the actual answer will be
 *  accepted.
 *
 *  A subtree of a tree is any node of that tree plus all its descendants.
 *  The average value of a tree is the sum of its values, divided by the
 *  number of nodes.
 *
 *  Example 1:
 *    Input: root = [5,6,1]
 *    Output: 6.00000
 *    Explanation:
 *      node 5 -> (5 + 6 + 1) / 3 = 4
 *      node 6 -> 6 / 1 = 6
 *      node 1 -> 1 / 1 = 1
 *      so the answer is 6.
 *
 *  Example 2:
 *    Input: root = [0,null,1]
 *    Output: 1.00000
 *
 *  Constraints:
 *    The number of nodes in the tree is in the range [1, 10^4].
 *    0 <= Node.val <= 10^5
 */
public class MaximumAverageSubtree {

    // V0
    // IDEA: POST-ORDER DFS, EACH NODE RETURNS (subtree sum, subtree size)
    //       the average of a subtree needs 2 aggregates and both compose
    //       bottom-up:
    //         sum(node)  = node.val + sum(left)  + sum(right)
    //         size(node) = 1        + size(left) + size(right)
    //       so ONE post-order pass sees every subtree's average; keep the max.
    /**
     * time = O(N)
     * space = O(H)   // H = tree height (recursion stack)
     */
    private double res;

    public double maximumAverageSubtree(TreeNode root) {
        this.res = 0.0;
        dfs(root);
        return this.res;
    }

    // returns { subtree sum, subtree node count }
    private long[] dfs(TreeNode node) {
        if (node == null) {
            return new long[]{0L, 0L};
        }
        long[] l = dfs(node.left);
        long[] r = dfs(node.right);
        long curSum = node.val + l[0] + r[0];
        long curCnt = 1 + l[1] + r[1];
        this.res = Math.max(this.res, (double) curSum / (double) curCnt);
        return new long[]{curSum, curCnt};
    }
}
