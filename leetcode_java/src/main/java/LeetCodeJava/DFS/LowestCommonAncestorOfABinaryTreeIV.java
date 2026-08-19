package LeetCodeJava.DFS;

// https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iv/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.HashSet;
import java.util.Set;

/**
 *  1676. Lowest Common Ancestor of a Binary Tree IV
 *  Medium
 *
 *  Given the root of a binary tree and an array of TreeNode objects nodes, return the lowest
 *  common ancestor (LCA) of all the nodes in nodes. All the nodes will exist in the tree, and
 *  all values of the tree's nodes are unique.
 *
 *  Example 1:
 *  Input: root = [3,5,1,6,2,0,8,null,null,7,4], nodes = [4,7]
 *  Output: 2
 *
 *  Example 2:
 *  Input: root = [3,5,1,6,2,0,8,null,null,7,4], nodes = [1]
 *  Output: 1
 *
 *  Example 3:
 *  Input: root = [3,5,1,6,2,0,8,null,null,7,4], nodes = [7,6,2,4]
 *  Output: 5
 *
 *  Constraints:
 *  The number of nodes in the tree is in the range [1, 10^4].
 *  -10^9 <= Node.val <= 10^9
 *  All Node.val are unique.
 *  All nodes[i] will exist in the tree.
 *  All nodes[i] are distinct.
 */
public class LowestCommonAncestorOfABinaryTreeIV {

    // V0
    // IDEA: classic LCA recursion generalised to k targets - dfs returns the LCA found inside
    //       a subtree (or null). A node that is itself a target returns itself; a node whose
    //       both children reported a hit is the split point -> it is the LCA.
    /**
     * time = O(n + k)
     * space = O(n + k)
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode[] nodes) {
        Set<Integer> targets = new HashSet<>();
        for (TreeNode node : nodes) {
            targets.add(node.val);
        }
        return dfs(root, targets);
    }

    private TreeNode dfs(TreeNode node, Set<Integer> targets) {
        if (node == null) {
            return null;
        }
        // node itself is a target -> a node can be a descendant of itself
        if (targets.contains(node.val)) {
            return node;
        }
        TreeNode left = dfs(node.left, targets);
        TreeNode right = dfs(node.right, targets);
        if (left != null && right != null) {
            return node;
        }
        return left != null ? left : right;
    }
}
