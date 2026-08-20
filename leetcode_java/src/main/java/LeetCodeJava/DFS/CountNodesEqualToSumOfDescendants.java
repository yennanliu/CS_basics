package LeetCodeJava.DFS;

// https://leetcode.com/problems/count-nodes-equal-to-sum-of-descendants/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  1973. Count Nodes Equal to Sum of Descendants
 *  Medium
 *
 *  Given the root of a binary tree, return the number of nodes where the value of the
 *  node is equal to the sum of the values of its descendants.
 *
 *  A descendant of a node x is any node that is on the path from node x to some leaf
 *  node. The sum is considered to be 0 if the node has no descendants.
 *
 *  Example 1:
 *    Input: root = [10,3,4,2,1]
 *    Output: 2
 *    Explanation: node 10 -> descendants sum 3+4+2+1 = 10,
 *                 node 3  -> descendants sum 2+1 = 3.
 *
 *  Example 2:
 *    Input: root = [2,3,null,2,null]
 *    Output: 0
 *
 *  Example 3:
 *    Input: root = [0]
 *    Output: 1
 *    Explanation: a leaf has descendant sum 0, so a leaf holding 0 counts.
 *
 *  Constraints:
 *    The number of nodes in the tree is in the range [1, 10^5].
 *    0 <= Node.val <= 10^5
 */
public class CountNodesEqualToSumOfDescendants {

    // V0
    // IDEA: POST-ORDER DFS (bottom-up subtree sums), ITERATIVE
    //       subSum(node) = val + subSum(left) + subSum(right), so the DESCENDANT sum
    //       of node is subSum(left) + subSum(right) -> compare that to node.val.
    //       NOTE: a leaf has descendant sum 0, so a leaf with val == 0 counts.
    //       NOTE: n reaches 10^5 and the tree may be a chain, so we collect a
    //             pre-order list and walk it BACKWARDS - a parent always appears
    //             before its children in pre-order, so the reverse is a valid
    //             bottom-up order. Sums need long (up to 10^5 * 10^5).
    /**
     * time = O(n)
     * space = O(n)
     */
    public int equalToDescendants(TreeNode root) {

        if (root == null) {
            return 0;
        }

        // iterative pre-order
        List<TreeNode> order = new ArrayList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode cur = stack.pop();
            order.add(cur);
            if (cur.left != null) {
                stack.push(cur.left);
            }
            if (cur.right != null) {
                stack.push(cur.right);
            }
        }

        Map<TreeNode, Long> subSum = new HashMap<>();
        int res = 0;

        for (int i = order.size() - 1; i >= 0; i--) {
            TreeNode cur = order.get(i);
            long left = cur.left == null ? 0L : subSum.get(cur.left);
            long right = cur.right == null ? 0L : subSum.get(cur.right);
            long descendants = left + right;
            if (descendants == cur.val) {
                res++;
            }
            subSum.put(cur, cur.val + descendants);
        }

        return res;
    }
}
