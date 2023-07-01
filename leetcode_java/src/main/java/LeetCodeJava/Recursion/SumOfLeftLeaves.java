package LeetCodeJava.Recursion;

// https://leetcode.com/problems/sum-of-left-leaves/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class SumOfLeftLeaves {

    // VO
    // IDEA: DFS

//    public int sumOfLeftLeaves(TreeNode root) {
//
//        if (root == null){
//            return 0;
//        }
//
//        if (root.left == null && root.left == null){
//            return 0;
//        }
//
//        List<Integer> cache = Arrays.asList();
//        getLeftNodeSum(root, cache);
//        int res = 0;
//        for (int i: cache){
//            res += i;
//        }
//
//        return res;
//    }
//
//    private int getLeftNodeSum(TreeNode root, List<Integer> cache){
//
//        if (root == null){
//            return 0;
//        }
//
//        if (root.left == null){
//            return getLeftNodeSum(root.right, cache);
//        }
//
//        if (root.right == null){
//            if (root.left.left == null){
//                cache.add(root.left.val);
//            }
//            return getLeftNodeSum(root.left, cache);
//        }
//
//        return getLeftNodeSum(root.left, cache) + getLeftNodeSum(root.right, cache);
//    }
//
//    public int sumOfLeftLeaves(TreeNode root) {
//        return processSubtree(root, false);
//    }

    // V1
    // https://leetcode.com/problems/sum-of-left-leaves/editorial/
    // IDEA : Recursive Tree Traversal (Pre-order)
    // NOTE!!! we can pass previous status as param to the method (isLeft)
    private int processSubtree_2(TreeNode subtree, boolean isLeft) {

        // Base case: This is an empty subtree.
        if (subtree == null) {
            return 0;
        }

        // Base case: This is a leaf node.
        if (subtree.left == null && subtree.right == null) {
            if (isLeft){
                return subtree.val;
            }else{
                return 0;
            }
        }

        // Recursive case: We need to add and return the results of the
        // left and right subtrees.
        return processSubtree_2(subtree.left, true) + processSubtree_2(subtree.right, false);
    }

    // V2
    // https://leetcode.com/problems/sum-of-left-leaves/editorial/
    // IDEA : Iterative Tree Traversal (Pre-order)
    private boolean isLeaf(TreeNode node) {
        return node != null && node.left == null && node.right == null;
    }

    public int sumOfLeftLeaves_3(TreeNode root) {

        if (root == null) {
            return 0;
        }

        int total = 0;
        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode subRoot = stack.pop();
            // Check if the left node is a leaf node.
            if (isLeaf(subRoot.left)) {
                total += subRoot.left.val;
            }
            // If the right node exists, put it on the stack.
            if (subRoot.right != null) {
                stack.push(subRoot.right);
            }
            // If the left node exists, put it on the stack.
            if (subRoot.left != null) {
                stack.push(subRoot.left);
            }
        }

        return total;
    }

}
