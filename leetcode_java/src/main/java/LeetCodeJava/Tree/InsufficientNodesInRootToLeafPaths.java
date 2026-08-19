package LeetCodeJava.Tree;

// https://leetcode.com/problems/insufficient-nodes-in-root-to-leaf-paths/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  1080. Insufficient Nodes in Root to Leaf Paths
 *  Medium
 *
 *  Given the root of a binary tree and an integer limit, delete all insufficient
 *  nodes in the tree simultaneously, and return the root of the resulting binary
 *  tree.
 *
 *  A node is insufficient if every root to leaf path intersecting this node has a
 *  sum strictly less than limit.
 *
 *  A leaf is a node with no children.
 *
 *  Example 1:
 *    Input: root = [1,2,3,4,-99,-99,7,8,9,-99,-99,12,13,-99,14], limit = 1
 *    Output: [1,2,3,4,null,null,7,8,9,null,14]
 *
 *  Example 2:
 *    Input: root = [5,4,8,11,null,17,4,7,1,null,null,5,3], limit = 22
 *    Output: [5,4,8,11,null,17,4,7,null,null,null,5]
 *
 *  Example 3:
 *    Input: root = [1,2,-3,-5,null,4,null], limit = -1
 *    Output: [1,null,-3,4]
 *
 *  Constraints:
 *    The number of nodes in the tree is in the range [1, 5000].
 *    -10^5 <= Node.val <= 10^5
 *    -10^9 <= limit <= 10^9
 */
public class InsufficientNodesInRootToLeafPaths {

    // V0
    // IDEA: DFS (post order) + pass the REMAINING limit down
    //       walk down subtracting node.val from `limit`, so at a leaf the
    //       remaining limit tells us whether that root -> leaf path made it:
    //         limit > 0 -> path sum < original limit -> leaf insufficient -> cut
    //       for an internal node, prune both children FIRST; if both came back
    //       null then every path through this node was insufficient, so this
    //       node dies too. that single bottom-up pass performs all the
    //       deletions "simultaneously".
    /**
     * time = O(N)
     * space = O(H)   // H = tree height
     */
    public TreeNode sufficientSubset(TreeNode root, int limit) {
        return helper(root, (long) limit);
    }

    private TreeNode helper(TreeNode root, long limit) {
        if (root == null) {
            return null;
        }

        limit -= root.val;

        // leaf : keep only if the path reached the limit
        if (root.left == null && root.right == null) {
            return limit > 0 ? null : root;
        }

        root.left = helper(root.left, limit);
        root.right = helper(root.right, limit);

        // NOTE !!! all children pruned -> no surviving path goes through here
        return (root.left == null && root.right == null) ? null : root;
    }
}
