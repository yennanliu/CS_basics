package LeetCodeJava.Tree;

// https://leetcode.com/problems/maximum-difference-between-node-and-ancestor/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  1026. Maximum Difference Between Node and Ancestor
 *  Medium
 *
 *  Given the root of a binary tree, find the maximum value v for which there
 *  exist different nodes a and b where v = |a.val - b.val| and a is an
 *  ancestor of b.
 *
 *  A node a is an ancestor of b if either: any child of a is equal to b, or
 *  any child of a is an ancestor of b.
 *
 *  Example 1:
 *    Input: root = [8,3,10,1,6,null,14,null,null,4,7,13]
 *    Output: 7
 *    Explanation: |8 - 1| = 7 is the largest ancestor-descendant difference.
 *
 *  Example 2:
 *    Input: root = [1,null,2,null,0,3]
 *    Output: 3
 *
 *  Constraints:
 *    The number of nodes in the tree is in the range [2, 5000].
 *    0 <= Node.val <= 10^5
 */
public class MaximumDifferenceBetweenNodeAndAncestor {

    // V0
    // IDEA: DFS CARRYING THE MIN AND MAX SEEN ON THE ROOT PATH
    //       for a node b the best |a.val - b.val| over all ancestors a is
    //       max(maxAncestor - b.val, b.val - minAncestor), so only 2 numbers
    //       need to travel down, not the whole path.
    //       equivalently: at every node the running (hi - lo) of its root path
    //       is a candidate -> take the overall max.
    /**
     * time = O(N)
     * space = O(H)   // recursion depth
     */
    private int res;

    public int maxAncestorDiff(TreeNode root) {
        if (root == null) {
            return 0;
        }
        this.res = 0;
        dfs(root, root.val, root.val);
        return this.res;
    }

    private void dfs(TreeNode node, int lo, int hi) {
        if (node == null) {
            return;
        }
        lo = Math.min(lo, node.val);
        hi = Math.max(hi, node.val);
        this.res = Math.max(this.res, hi - lo);
        dfs(node.left, lo, hi);
        dfs(node.right, lo, hi);
    }
}
