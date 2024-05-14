package LeetCodeJava.Recursion;

// https://leetcode.com/problems/maximum-depth-of-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;
import java.util.LinkedList;

public class MaximumDepthOfBinaryTree {

    // V0
    // IDEA : RECURSIVE
    public int maxDepth(TreeNode root) {

        if (root == null){
            return 0;
        }

        // NOTE : below conditon is optional (have or not use is OK)
//        if (root.left == null && root.right == null){
//            return 1;
//        }

        /** NOTE !!! we get max left, right depth first, then return bigger one as final answer */
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


    // V0''
    // IDEA : RECURSIVE
    public int maxDepth_1(TreeNode root) {

        if (root == null){
            return 0;
        }
        return Math.max(maxDepth_1(root.left) + 1, maxDepth_1(root.right) + 1);
    }


    // V0''
    // IDEA : DFS (RECURSION)
    public int maxDepth__(TreeNode root) {

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


    // V1
    // IDEA : RECURSION
    // https://leetcode.com/problems/maximum-depth-of-binary-tree/editorial/
    public int maxDepth_2(TreeNode root) {
        if (root == null) {
            return 0;
        } else {
            int left_height = maxDepth_2(root.left);
            int right_height = maxDepth_2(root.right);
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
