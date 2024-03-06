package LeetCodeJava.Tree;

// https://leetcode.com/problems/binary-tree-maximum-path-sum/description/

import LeetCodeJava.DataStructure.TreeNode;

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */

public class BinaryTreeMaximumPathSum {

    // V0
    // TODO : implement
//    public int maxPathSum(TreeNode root) {
//
//        return 0;
//    }


    // V1
    // IDEA : DFS
    // https://leetcode.com/problems/binary-tree-maximum-path-sum/solutions/4586190/beat-100-1/
    int max = Integer.MIN_VALUE;

    public int maxPath_1(TreeNode root) {

        if(root == null) return 0;

        int value = root.val;

        int left_sum = Math.max(maxPath_1(root.left),0);
        int right_sum = Math.max(maxPath_1(root.right),0);

        max = Math.max(max, left_sum + right_sum + value);

        return Math.max(left_sum, right_sum) + value;
    }

    public int maxPathSum_1(TreeNode root) {

        maxPath_1(root);
        return max;
    }

    // V2
    // IDEA : DFS
    // https://leetcode.com/problems/binary-tree-maximum-path-sum/solutions/4538150/simple-java-solution-dfs/
    class INT {
        int val;
    }
    public int maxPathSum_2(TreeNode root) {
        INT maxPathLen = new INT();
        maxPathLen.val = Integer.MIN_VALUE;
        dfs_2(root, maxPathLen);
        return maxPathLen.val;
    }

    int dfs_2(TreeNode root, INT maxLen) {
        if(root == null)
            return 0;
        int left = Math.max(dfs_2(root.left, maxLen), 0);
        int right = Math.max(dfs_2(root.right, maxLen), 0);
        maxLen.val = Math.max(maxLen.val, root.val + left + right);
        return root.val + Math.max(left, right);
    }

}
