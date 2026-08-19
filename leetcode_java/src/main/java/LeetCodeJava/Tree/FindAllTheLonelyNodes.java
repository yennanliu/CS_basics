package LeetCodeJava.Tree;

// https://leetcode.com/problems/find-all-the-lonely-nodes/

import java.util.ArrayList;
import java.util.List;

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  1469. Find All The Lonely Nodes
 *  Easy
 *
 *  In a binary tree, a lonely node is a node that is the only child of its parent
 *  node. The root of the tree is not lonely because it does not have a parent node.
 *
 *  Given the root of a binary tree, return an array containing the values of all
 *  lonely nodes in the tree. Return the list in any order.
 *
 *  Example 1:
 *    Input: root = [1,2,3,null,4]
 *    Output: [4]
 *    Explanation: node 1 is the root; nodes 2 and 3 share a parent.
 *
 *  Example 2:
 *    Input: root = [7,1,4,6,null,5,3,null,null,null,null,null,2]
 *    Output: [6,2]
 *
 *  Constraints:
 *    The number of nodes in the tree is in the range [1, 1000].
 *    1 <= Node.val <= 10^6
 */
public class FindAllTheLonelyNodes {

    // V0
    // IDEA: DFS -- a node reports its own SINGLE child, never itself.
    //       look at each node as a PARENT: if it has exactly one child, that
    //       child is lonely -> record the child's value, then recurse.
    //       this way the root is never considered (nobody is its parent) and
    //       every other node is examined exactly once, by its parent.
    /**
     * time = O(N)
     * space = O(H)   // H = tree height (recursion stack)
     */
    public List<Integer> getLonelyNodes(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        dfs(root, res);
        return res;
    }

    private void dfs(TreeNode node, List<Integer> res) {
        if (node == null) {
            return;
        }
        if (node.left != null && node.right == null) {
            res.add(node.left.val);
        }
        if (node.right != null && node.left == null) {
            res.add(node.right.val);
        }
        dfs(node.left, res);
        dfs(node.right, res);
    }
}
