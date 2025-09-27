package LeetCodeJava.DFS;

// https://leetcode.com/problems/path-sum/
/**
 * 112. Path Sum
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given the root of a binary tree and an integer targetSum, return true if the tree has a root-to-leaf path such that adding up all the values along the path equals targetSum.
 *
 * A leaf is a node with no children.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [5,4,8,11,null,13,4,7,2,null,null,null,1], targetSum = 22
 * Output: true
 * Explanation: The root-to-leaf path with the target sum is shown.
 * Example 2:
 *
 *
 * Input: root = [1,2,3], targetSum = 5
 * Output: false
 * Explanation: There are two root-to-leaf paths in the tree:
 * (1 --> 2): The sum is 3.
 * (1 --> 3): The sum is 4.
 * There is no root-to-leaf path with sum = 5.
 * Example 3:
 *
 * Input: root = [], targetSum = 0
 * Output: false
 * Explanation: Since the tree is empty, there are no root-to-leaf paths.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [0, 5000].
 * -1000 <= Node.val <= 1000
 * -1000 <= targetSum <= 1000
 *
 */

import LeetCodeJava.DataStructure.TreeNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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

    // V0-1
    // IDEA: HASHMAP + DFS (post order)
    Map<Integer, Integer> pathSumMap = new HashMap<>();
    public boolean hasPathSum_0_1(TreeNode root, int targetSum) {
        // edge
        if (root == null) {
            return false;
        }
        if (root.left == null && root.right == null) {
            return root.val == targetSum;
        }

        //System.out.println(">>> (before op) pathSumMap = " + pathSumMap);
        // post order traverse: left -> right -> root
        getPathHelper(root, 0);
        //System.out.println(">>> (after op) pathSumMap = " + pathSumMap);

        return pathSumMap.containsValue(targetSum);
    }

    private void getPathHelper(TreeNode root, Integer curSum) {
        if (root == null) {
            return;
        }

        int newSum = curSum + root.val;
        /**
         *  NOTE !!!
         *
         *  ONLY add to map if `has NO children`
         *
         */
        if (root.left == null && root.right == null) {
            pathSumMap.put(newSum, newSum);
        }

        /**
         *  NOTE !!!
         *
         *  recursively calculate sub left, right node depth
         */
        getPathHelper(root.left, newSum);
        getPathHelper(root.right, newSum);
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
