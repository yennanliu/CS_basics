package LeetCodeJava.Tree;

// https://leetcode.com/problems/binary-tree-postorder-traversal/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 *  145. Binary Tree Postorder Traversal
 * Easy
 * Topics
 * Companies
 * Given the root of a binary tree, return the postorder traversal of its nodes' values.
 *
 *
 *
 * Example 1:
 *
 * Input: root = [1,null,2,3]
 *
 * Output: [3,2,1]
 *
 * Explanation:
 *
 *
 *
 * Example 2:
 *
 * Input: root = [1,2,3,4,5,null,8,null,null,6,7,9]
 *
 * Output: [4,6,7,5,2,9,8,3,1]
 *
 * Explanation:
 *
 *
 *
 * Example 3:
 *
 * Input: root = []
 *
 * Output: []
 *
 * Example 4:
 *
 * Input: root = [1]
 *
 * Output: [1]
 *
 *
 *
 * Constraints:
 *
 * The number of the nodes in the tree is in the range [0, 100].
 * -100 <= Node.val <= 100
 *
 *
 * Follow up: Recursive solution is trivial, could you do it iteratively?
 *
 *
 *
 */
public class BinaryTreePostorderTraversal {

    // V0
    // IDEA: DFS
    // TODO: validate it
    List<Integer> res2 = new ArrayList<>();
    public List<Integer> postorderTraversal(TreeNode root) {
        // edge
        if(root == null){
            return res2;
        }
        if(root.left == null && root.right == null){
            res2.add(root.val);
            return res2;
        }

        postOrderDfs(root);
        return res2;
    }

    public void postOrderDfs(TreeNode root){
        if(root == null){
            return;
        }
        // postorder :  left  -> right -> root
        postOrderDfs(root.left);
        postOrderDfs(root.right);
        res2.add(root.val);
    }

    // V0-1
    // IDEA: BFS

    // V1

    // V2

}
