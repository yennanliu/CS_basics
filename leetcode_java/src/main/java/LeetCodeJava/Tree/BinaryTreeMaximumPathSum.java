package LeetCodeJava.Tree;

// https://leetcode.com/problems/binary-tree-maximum-path-sum/description/
/**
 *  124. Binary Tree Maximum Path Sum
 * Solved
 * Hard
 * Topics
 * Companies
 * A path in a binary tree is a sequence of nodes where each pair of adjacent nodes in the sequence has an edge connecting them. A node can only appear in the sequence at most once. Note that the path does not need to pass through the root.
 *
 * The path sum of a path is the sum of the node's values in the path.
 *
 * Given the root of a binary tree, return the maximum path sum of any non-empty path.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [1,2,3]
 * Output: 6
 * Explanation: The optimal path is 2 -> 1 -> 3 with a path sum of 2 + 1 + 3 = 6.
 * Example 2:
 *
 *
 * Input: root = [-10,9,20,null,null,15,7]
 * Output: 42
 * Explanation: The optimal path is 15 -> 20 -> 7 with a path sum of 15 + 20 + 7 = 42.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 3 * 104].
 * -1000 <= Node.val <= 1000
 *
 */
import LeetCodeJava.DataStructure.TreeNode;


public class BinaryTreeMaximumPathSum {

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
    // V0
    // TODO : implement
//    public int maxPathSum(TreeNode root) {
//
//        return 0;
//    }

    // V0-1
    // IDEA: DFS (GPT)
    private int maxSum = Integer.MIN_VALUE;

    public int maxPathSum_0_1(TreeNode root) {
        if (root == null) {
            return Integer.MIN_VALUE; // Handle null case
        }

        dfs(root);
        return maxSum;
    }

    private int dfs(TreeNode node) {
        if (node == null) {
            return 0;
        }

        // Compute max path sum of left and right children, discard negative values
        int leftMax = Math.max(dfs(node.left), 0);
        int rightMax = Math.max(dfs(node.right), 0);

        // Update global max sum with current node as the highest ancestor
        maxSum = Math.max(maxSum, node.val + leftMax + rightMax);

        // Return max sum path including this node (but only one subtree path)
        return node.val + Math.max(leftMax, rightMax);
    }


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
