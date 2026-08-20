package LeetCodeJava.DFS;

// https://leetcode.com/problems/count-nodes-equal-to-average-of-subtree/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  2265. Count Nodes Equal to Average of Subtree
 *  Medium
 *
 *  Given the root of a binary tree, return the number of nodes where the value of the
 *  node is equal to the average of the values in its subtree.
 *
 *  Note:
 *    The average of n elements is the sum of the n elements divided by n and rounded
 *    down to the nearest integer.
 *    A subtree of root is a tree consisting of root and all of its descendants.
 *
 *  Example 1:
 *    Input: root = [4,8,5,0,1,null,6]
 *    Output: 5
 *    Explanation: node 4 -> (4+8+5+0+1+6)/6 = 4, node 5 -> (5+6)/2 = 5,
 *                 and nodes 0, 1, 6 are leaves equal to their own value.
 *
 *  Example 2:
 *    Input: root = [1]
 *    Output: 1
 *
 *  Constraints:
 *    The number of nodes in the tree is in the range [1, 1000].
 *    0 <= Node.val <= 1000
 */
public class CountNodesEqualToAverageOfSubtree {

    // V0
    // IDEA: POST-ORDER DFS (bubble up (sum, count) per subtree)
    //       every node needs its subtree sum and node count, which are only available
    //       after both children are done -> post-order.
    //         sum[u]   = val[u] + sum[left] + sum[right]
    //         count[u] = 1      + cnt[left] + cnt[right]
    //       node u counts when sum[u] / count[u] == val[u]  (integer division)
    /**
     * time = O(n)
     * space = O(h)
     */
    private int res = 0;

    public int averageOfSubtree(TreeNode root) {
        res = 0;
        dfs(root);
        return res;
    }

    // returns {subtree sum, subtree node count}
    private int[] dfs(TreeNode node) {
        if (node == null) {
            return new int[]{0, 0};
        }
        int[] l = dfs(node.left);
        int[] r = dfs(node.right);

        int sum = node.val + l[0] + r[0];
        int cnt = 1 + l[1] + r[1];

        if (sum / cnt == node.val) {
            res++;
        }
        return new int[]{sum, cnt};
    }
}
