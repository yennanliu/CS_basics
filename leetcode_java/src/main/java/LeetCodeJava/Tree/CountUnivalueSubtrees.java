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
    // IDEA : DFS + post order traverse (bottom up)
    /**
     *  NOTE !!!
     *
     *   the helper func returns `is this sub tree uni-value ?`,
     *   and it updates the global counter as a side effect.
     *
     *   a sub tree rooted at `node` is uni-value if:
     *
     *     1) both of its sub trees are uni-value, AND
     *     2) every EXISTING child has the same val as `node`
     */
    private int uniCnt = 0;

    /**
     * time = O(N)
     * space = O(H)
     */
    public int countUnivalSubtrees(TreeNode root) {
        univalDfs(root);
        return uniCnt;
    }

    private boolean univalDfs(TreeNode node) {
        // an `empty` sub tree is uni-value (so a leaf can still be counted)
        if (node == null) {
            return true;
        }

        /** NOTE !!! we MUST visit BOTH sub trees (no short circuit),
         *           since the counter is updated during the recursion */
        boolean leftUni = univalDfs(node.left);
        boolean rightUni = univalDfs(node.right);

        if (!leftUni || !rightUni) {
            return false;
        }
        if (node.left != null && node.left.val != node.val) {
            return false;
        }
        if (node.right != null && node.right.val != node.val) {
            return false;
        }

        uniCnt += 1;
        return true;
    }

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
