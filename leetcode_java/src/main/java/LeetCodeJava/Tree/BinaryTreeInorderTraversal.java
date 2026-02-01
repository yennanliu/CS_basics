package LeetCodeJava.Tree;

// https://leetcode.com/problems/binary-tree-inorder-traversal/description/
/**
 * 94. Binary Tree Inorder Traversal
 * Solved
 * Easy
 * Topics
 * Companies
 * Given the root of a binary tree, return the inorder traversal of its nodes' values.
 *
 *
 *
 * Example 1:
 *
 * Input: root = [1,null,2,3]
 *
 * Output: [1,3,2]
 *
 * Explanation:
 *
 *
 *
 * Example 2:
 *
 * Input: root = [1,2,3,4,5,null,8,null,null,6,7,9]
 *
 * Output: [4,2,6,5,7,1,3,9,8]
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
 * The number of nodes in the tree is in the range [0, 100].
 * -100 <= Node.val <= 100
 *
 */
import LeetCodeJava.DataStructure.TreeNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BinaryTreeInorderTraversal {

    // V0-1
    // IDEA: recursion
    /**
     * time = O(N)
     * space = O(H)
     */
    public List<Integer> inorderTraversal_0_1(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        helper(root, res);
        return res;
    }

    /**
     * time = O(N)
     * space = O(H)
     */
    public void helper(TreeNode root, List<Integer> res) {
        if (root != null) {
            helper(root.left, res);
            res.add(root.val);
            helper(root.right, res);
        }
    }

    // V0-2
    // IDEA: iteration
    /**
     * time = O(N)
     * space = O(H)
     */
    public List<Integer> inorderTraversal_0_2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode curr = root;
        /**
         *  NOTE !!!    inorder traversal BFS
         *
         *  for inorder traversal via BFS
         *  we CAN'T just simply do `left -> root -> right`
         *  instead, we need below handling
         *
         */
        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();
            res.add(curr.val);
            curr = curr.right;
        }
        return res;
    }

    // V0-3
    // IDEA : DFS
    List<Integer> res = new ArrayList<>();
    /**
     * time = O(N)
     * space = O(H)
     */
    public List<Integer> inorderTraversal_0_3(TreeNode root) {
        //List<Integer> res = new ArrayList<>();
        // edge
        if(root == null){
            return res;
        }
        if(root.left == null && root.right == null){
            res.add(root.val);
            return res;
        }

        inorderDfs(root);
        return res;
    }

    /**
     * time = O(V + E)
     * space = O(V)
     */
    public void inorderDfs(TreeNode root){
        if(root == null){
            return;
        }
        // inorder : left -> root -> right
        inorderDfs(root.left);
        res.add(root.val);
        inorderDfs(root.right);
    }

    // V0-4
    // IDEA: BFS
    /**
     * time = O(N)
     * space = O(H)
     */
    public List<Integer> inorderTraversal_0_4(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode current = root;

        // Iteratively traverse the tree
        while (current != null || !stack.isEmpty()) {
            /**
             *  NOTE !!!    inorder traversal BFS
             *
             *  for inorder traversal via BFS
             *  we CAN'T just simply do `left -> root -> right`
             *  instead, we need below handling
             *
             */
            // Reach the leftmost node of the current node
            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            // Pop the top element and add it to result
            current = stack.pop();
            res.add(current.val);

            // Now, visit the right subtree
            current = current.right;
        }

        return res;
    }

}
