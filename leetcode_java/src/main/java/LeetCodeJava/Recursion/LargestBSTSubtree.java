package LeetCodeJava.Recursion;

// https://leetcode.com/problems/largest-bst-subtree/description/
// https://leetcode.ca/all/333.html

import LeetCodeJava.DataStructure.TreeNode;

/**
 * 333. Largest BST Subtree
 * Given a binary tree, find the largest subtree which is a Binary Search Tree (BST), where largest means subtree with largest number of nodes in it.
 *
 * Note:
 * A subtree must include all of its descendants.
 *
 * Example:
 *
 * Input: [10,5,15,1,8,null,7]
 *
 *    10
 *    / \
 *   5  15
 *  / \   \
 * 1   8   7
 *
 * Output: 3
 * Explanation: The Largest BST Subtree in this case is the highlighted one.
 *              The return value is the subtree's size, which is 3.
 * Follow up:
 * Can you figure out ways to solve it with O(n) time complexity?
 *
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Apple Google Lyft Microsoft
 * Problem Solution
 * 333-Largest-BST-Subtree
 */
public class LargestBSTSubtree {

    // V0
//    public int largestBSTSubtree(TreeNode root) {
//    }


    // V1
    // IDEA: DFS
    // https://leetcode.ca/2016-10-28-333-Largest-BST-Subtree/
    private int ans;
    public int largestBSTSubtree_1(TreeNode root) {
        ans = 0;
        dfs(root);
        return ans;
    }

    private int[] dfs(TreeNode root) {
        if (root == null) {
            return new int[] {Integer.MAX_VALUE, Integer.MIN_VALUE, 0};
        }
        int[] left = dfs(root.left);
        int[] right = dfs(root.right);
        if (left[1] < root.val && root.val < right[0]) {
            ans = Math.max(ans, left[2] + right[2] + 1);
            return new int[] {
                    Math.min(root.val, left[0]), Math.max(root.val, right[1]), left[2] + right[2] + 1};
        }
        return new int[] {Integer.MIN_VALUE, Integer.MAX_VALUE, 0};
    }


    // V2


}
