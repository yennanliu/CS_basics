package LeetCodeJava.Recursion;

// https://leetcode.com/problems/maximum-depth-of-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;
import java.util.LinkedList;

public class MaximumDepthOfBinaryTree {

    // V0
//    public int maxDepth(TreeNode root) {
//
//        if (root == null){
//            return 0;
//        }
//
//        int resultL = findDepth(root.left, 0);
//        int resultR = findDepth(root.right, 0);
//
//        return Math.max(resultL, resultR);
//    }
//
//    private int findDepth(TreeNode root, int layer) {
//
//        if (root == null){
//            return layer;
//        }
//
//        if (root.left != null){
//            layer += 1;
//            findDepth(root.left, layer);
//        }
//
//        if (root.right != null){
//            layer += 1;
//            findDepth(root.right, layer);
//        }
//
//        return layer;
//    }

    // V1
    // IDEA : RECURSION
    // https://leetcode.com/problems/maximum-depth-of-binary-tree/editorial/
    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        } else {
            int left_height = maxDepth(root.left);
            int right_height = maxDepth(root.right);
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
