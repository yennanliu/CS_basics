package LeetCodeJava.Recursion;

// https://leetcode.com/problems/maximum-depth-of-binary-tree/
/**
 *  104. Maximum Depth of Binary Tree
 * Solved
 * Easy
 * Topics
 * Companies
 * Given the root of a binary tree, return its maximum depth.
 *
 * A binary tree's maximum depth is the number of nodes along the longest path from the root node down to the farthest leaf node.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [3,9,20,null,null,15,7]
 * Output: 3
 * Example 2:
 *
 * Input: root = [1,null,2]
 * Output: 2
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [0, 104].
 * -100 <= Node.val <= 100
 *
 *
 */

import LeetCodeJava.DataStructure.TreeNode;
import java.util.LinkedList;

public class MaximumDepthOfBinaryTree {

    // V0
    // IDEA : RECURSIVE
    public int maxDepth(TreeNode root) {

        if (root == null){
            return 0;
        }

        // NOTE : below condition is optional (have or not use is OK)
//        if (root.left == null && root.right == null){
//            return 1;
//        }

        /** NOTE !!!
         *
         *  we get max left, right depth first, then return bigger one as final answer
         *
         *  -> ONlY need to track/return `depth`, no need to track node itself
         */
        int leftD = maxDepth(root.left) + 1;
        int rightD = maxDepth(root.right) + 1;

        return Math.max(leftD, rightD);
    }

    // V0'
    // NOTE !!! below is wrong, we should get max left, right depth first, then compare them
//    public int maxDepth(TreeNode root) {
//
//        if (root == null){
//            return 0;
//        }
//
//        if (root.left != null){
//            return 1 + this.maxDepth(root.left);
//        }
//
//        if (root.right != null){
//            return 1 + this.maxDepth(root.right);
//        }
//
//        return Math.max(this.maxDepth(root.left), this.maxDepth(root.right));
//    }


    // V0-1
    // IDEA : RECURSIVE
    public int maxDepth_0_1(TreeNode root) {

        if (root == null){
            return 0;
        }
        return Math.max(maxDepth_0_1(root.left) + 1, maxDepth_0_1(root.right) + 1);
    }


    // V0-2
    // IDEA : DFS (RECURSION)
    public int maxDepth_0_2(TreeNode root) {

        if (root == null){
            return 0;
        }
        int cur = 0;
        int depth = 0;
        return dfs(root, cur, depth);
    }
    private int dfs(TreeNode root, int cur, int depth){
        if (root == null){
            depth =  Math.max(cur, depth);
            return depth;
        }
        return Math.max(dfs(root.left, cur+1, depth), dfs(root.right, cur+1, depth));
    }

    // V0-3
    // IDEA: RECURSIVE (GPT)
    public int maxDept0_3(TreeNode root) {
        // edge case: if the root is null, the depth is 0
        if (root == null) {
            return 0;
        }
        // dfs: calculate the maximum depth starting from root
        return getMaxDepth(root, 1);
    }

    private int getMaxDepth(TreeNode root, int depth) {
        // base case: if the current node is null, return the current depth
        if (root == null) {
            return depth - 1; // subtract 1 because depth starts from 1
        }

        // recursively calculate the depth for left and right subtrees
        int leftDepth = getMaxDepth(root.left, depth + 1);
        int rightDepth = getMaxDepth(root.right, depth + 1);

        // return the maximum depth of the left or right subtree
        return Math.max(leftDepth, rightDepth);
    }

    // V0-4
    // IDEA: RECURSIVE (fixed by gpt)
    public int maxDepth_0_4(TreeNode root) {
        // Edge case: if the tree is empty, return 0
        if (root == null) {
            return 0;
        }
        // Start the DFS to calculate the maximum depth
        return getMaxDepthDfs(root);
    }

    public int getMaxDepthDfs(TreeNode root) {
        // Base case: if the node is null, return 0 (no depth)
        if (root == null) {
            return 0;
        }

        // Recursively find the maximum depth of the left and right subtrees
        int leftDepth = getMaxDepthDfs(root.left);
        int rightDepth = getMaxDepthDfs(root.right);

        // Return the maximum of the two depths plus 1 (for the current node)
        return Math.max(leftDepth, rightDepth) + 1;
    }

    // V1
    // IDEA : RECURSION
    // https://leetcode.com/problems/maximum-depth-of-binary-tree/editorial/
    public int maxDepth_1(TreeNode root) {
        if (root == null) {
            return 0;
        } else {
            int left_height = maxDepth_1(root.left);
            int right_height = maxDepth_1(root.right);
            return java.lang.Math.max(left_height, right_height) + 1;
        }
    }


    // V2
    // IDEA : Tail Recursion + BFS
    // https://leetcode.com/problems/maximum-depth-of-binary-tree/editorial/
    // only available in C++ (not in Java, Python)

    // V3
    // IDEA : Iteration
    // https://leetcode.com/problems/maximum-depth-of-binary-tree/editorial/
    public int maxDepth_3(TreeNode root) {
        LinkedList<TreeNode> stack = new LinkedList<>();
        LinkedList<Integer> depths = new LinkedList<>();
        if (root == null) return 0;

        stack.add(root);
        depths.add(1);

        int depth = 0, current_depth = 0;
        while(!stack.isEmpty()) {
            root = stack.pollLast();
            current_depth = depths.pollLast();
            if (root != null) {
                depth = Math.max(depth, current_depth);
                stack.add(root.left);
                stack.add(root.right);
                depths.add(current_depth + 1);
                depths.add(current_depth + 1);
            }
        }
        return depth;
    }

}
