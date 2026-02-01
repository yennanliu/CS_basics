package LeetCodeJava.Tree;

// https://leetcode.com/problems/count-univalue-subtrees/description/

import LeetCodeJava.DataStructure.TreeNode;

/**
 * 250. Count Univalue Subtrees
 * Given a binary tree, count the number of uni-value subtrees.
 *
 * A Uni-value subtree means all nodes of the subtree have the same value.
 *
 * Example :
 *
 * Input:  root = [5,1,5,5,5,null,5]
 *
 *               5
 *              / \
 *             1   5
 *            / \   \
 *           5   5   5
 *
 * Output: 4
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Bloomberg Box Facebook Google
 * Problem Solution
 * 250-Count-Univalue-Subtrees
 * All Problems:
 * Link to All Problems
 *
 */
public class CountUnivalueSubtrees {

    // V0
    // TODO: implement
//    public int countUnivalSubtrees_1(TreeNode root) {
//    }

    // V1
    //https://leetcode.ca/2016-08-06-250-Count-Univalue-Subtrees/
    private int ans;

    /**
     * time = O(N)
     * space = O(H)
     */
    public int countUnivalSubtrees_1(TreeNode root) {
        dfs(root);
        return ans;
    }

    private boolean dfs(TreeNode root) {
        if (root == null) {
            return true;
        }
        boolean l = dfs(root.left);
        boolean r = dfs(root.right);
        if (!l || !r) {
            return false;
        }
        int a = root.left == null ? root.val : root.left.val;
        int b = root.right == null ? root.val : root.right.val;
        if (a == b && b == root.val) {
            ++ans;
            return true;
        }
        return false;
    }

    // V2

}
