package LeetCodeJava.DFS;

// https://leetcode.com/problems/path-sum/

import LeetCodeJava.DataStructure.TreeNode;
import java.util.LinkedList;

public class PathSum {

    // V0
    public boolean hasPathSum(TreeNode root, int targetSum) {

        if (root == null){
            return false;
        }

        if (root.left == null && root.right == null){
            return root.val == targetSum;
        }

        return checkSum(root, targetSum);
    }

    private Boolean checkSum(TreeNode root, int targetSum){

        if (root == null){
            return false;
        }

        targetSum -= root.val;

        if (root.left == null && root.right == null){
            return targetSum == 0;
        }

        return checkSum(root.left ,targetSum) || checkSum(root.right ,targetSum);
    }

    // V1
    // https://leetcode.com/problems/path-sum/editorial/
    // IDEA : RECURSION
    public boolean hasPathSum_2(TreeNode root, int sum) {
        if (root == null)
            return false;

        sum -= root.val;
        if ((root.left == null) && (root.right == null))
            return (sum == 0);
        return hasPathSum_2(root.left, sum) || hasPathSum_2(root.right, sum);
    }

    // V2
    // https://leetcode.com/problems/path-sum/editorial/
    // IDEA : Iterations
    public boolean hasPathSum_3(TreeNode root, int sum) {
        if (root == null)
            return false;

        LinkedList<TreeNode> node_stack = new LinkedList();
        LinkedList<Integer> sum_stack = new LinkedList();
        node_stack.add(root);
        sum_stack.add(sum - root.val);

        TreeNode node;
        int curr_sum;
        while ( !node_stack.isEmpty() ) {
            node = node_stack.pollLast();
            curr_sum = sum_stack.pollLast();
            if ((node.right == null) && (node.left == null) && (curr_sum == 0))
                return true;

            if (node.right != null) {
                node_stack.add(node.right);
                sum_stack.add(curr_sum - node.right.val);
            }
            if (node.left != null) {
                node_stack.add(node.left);
                sum_stack.add(curr_sum - node.left.val);
            }
        }
        return false;
    }

}
