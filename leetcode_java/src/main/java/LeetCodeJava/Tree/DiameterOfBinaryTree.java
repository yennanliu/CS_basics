package LeetCodeJava.Tree;

import LeetCodeJava.DataStructure.TreeNode;

// https://leetcode.com/problems/diameter-of-binary-tree/

public class DiameterOfBinaryTree {

    // V1
    // IDEA : DFS
    // https://leetcode.com/problems/diameter-of-binary-tree/editorial/
    private int diameter;
    public int diameterOfBinaryTree_2(TreeNode root) {
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
    public int diameterOfBinaryTree_3(TreeNode root) {
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
    public int diameterOfBinaryTree_4(TreeNode root) {
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

}
