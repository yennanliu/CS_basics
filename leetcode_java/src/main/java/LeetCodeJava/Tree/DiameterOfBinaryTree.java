package LeetCodeJava.Tree;

import LeetCodeJava.DataStructure.TreeNode;

// https://leetcode.com/problems/diameter-of-binary-tree/
/**
 * 543. Diameter of Binary Tree
 * Solved
 * Easy
 * Topics
 * Companies
 * Given the root of a binary tree, return the length of the diameter of the tree.
 *
 * The diameter of a binary tree is the length of the longest path between any two nodes in a tree. This path may or may not pass through the root.
 *
 * The length of a path between two nodes is represented by the number of edges between them.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [1,2,3,4,5]
 * Output: 3
 * Explanation: 3 is the length of the path [4,2,1,3] or [5,2,1,3].
 * Example 2:
 *
 * Input: root = [1,2]
 * Output: 1
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 104].
 * -100 <= Node.val <= 100
 *
 */

public class DiameterOfBinaryTree {

    // V0
    // IDEA : DFS
    // TODO: implement
//    public int diameterOfBinaryTree(TreeNode root) {
//
//    }

    // V1
    // IDEA : DFS
    // https://leetcode.com/problems/diameter-of-binary-tree/editorial/
    private int diameter;
    public int diameterOfBinaryTree_1(TreeNode root) {
        diameter = 0;
        longestPath(root);
        return diameter;
    }
    private int longestPath(TreeNode node){
        if(node == null) return 0;
        // recursively find the longest path in
        // both left child and right child
        int leftPath = longestPath(node.left);
        int rightPath = longestPath(node.right);

        // update the diameter if left_path plus right_path is larger
        diameter = Math.max(diameter, leftPath + rightPath);

        // return the longest one between left_path and right_path;
        // remember to add 1 for the path connecting the node and its parent
        return Math.max(leftPath, rightPath) + 1;
    }

    // V2
    // IDEA : DFS
    // https://leetcode.com/problems/diameter-of-binary-tree/solutions/3396281/solution/
    int result = -1;
    public int diameterOfBinaryTree_2(TreeNode root) {
        dfs(root);
        return result;
    }
    private int dfs(TreeNode current) {
        if (current == null) {
            return -1;
        }
        int left = 1 + dfs(current.left);
        int right = 1 + dfs(current.right);
        result = Math.max(result, (left + right));
        return Math.max(left, right);
    }

    // V3
    // IDEA: DFS
    // https://leetcode.com/problems/diameter-of-binary-tree/solutions/3280141/beats-100-onlycode-in-java/
    public int diameterOfBinaryTree_3(TreeNode root) {
        int dia[] = new int[1];
        ht(root,dia);
        return dia[0];
    }
    public int ht(TreeNode root , int dia[]){
        if(root == null) return 0;
        int lh = ht(root.left,dia);
        int rh = ht(root.right,dia);
        dia[0] = Math.max(dia[0],lh+rh);
        return 1 + Math.max(lh,rh);
    }

    // V4
    // IDEA; DFS (GPT)
    public int diameterOfBinaryTree_4(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int[] diameter = new int[1]; // Use an array to pass by reference
        calculateDiameter(root, diameter);
        return diameter[0];
    }

    private int calculateDiameter(TreeNode node, int[] diameter) {
        if (node == null) {
            return 0;
        }

        int leftHeight = calculateDiameter(node.left, diameter);
        int rightHeight = calculateDiameter(node.right, diameter);

        // Update the diameter if the current path is longer
        diameter[0] = Math.max(diameter[0], leftHeight + rightHeight);

        // Return the height of the current node (1 + max of left and right)
        return 1 + Math.max(leftHeight, rightHeight);
    }

}
