package LeetCodeJava.Stack;

// https://leetcode.com/problems/recover-a-tree-from-preorder-traversal/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.Stack;

/**
 *  1028. Recover a Tree From Preorder Traversal
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * We run a preorder depth-first search (DFS) on the root of a binary tree.
 *
 * At each node in this traversal, we output D dashes (where D is the depth of this node), then we output the value of this node.  If the depth of a node is D, the depth of its immediate child is D + 1.  The depth of the root node is 0.
 *
 * If a node has only one child, that child is guaranteed to be the left child.
 *
 * Given the output traversal of this traversal, recover the tree and return its root.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: traversal = "1-2--3--4-5--6--7"
 * Output: [1,2,5,3,4,6,7]
 * Example 2:
 *
 *
 * Input: traversal = "1-2--3---4-5--6---7"
 * Output: [1,2,5,3,null,6,null,4,null,7]
 * Example 3:
 *
 *
 * Input: traversal = "1-401--349---90--88"
 * Output: [1,401,null,349,88,90]
 *
 *
 * Constraints:
 *
 * The number of nodes in the original tree is in the range [1, 1000].
 * 1 <= Node.val <= 109
 *
 *
 *
 */
public class RecoverATreeFromPreorderTraversal {
    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     *     int val;
     *     TreeNode left;
     *     TreeNode right;
     *     TreeNode() {}
     *     TreeNode(int val) { this.val = val; }
     *     TreeNode(int val, TreeNode left, TreeNode right) {
     *         this.val = val;
     *         this.left = left;
     *         this.right = right;
     *     }
     * }
     */

    // V0
//    public TreeNode recoverFromPreorder(String traversal) {
//
//        return null;
//    }



    // V1-1
    // IDEA:  Brute Force (Recursive with String Manipulation)
    // https://leetcode.com/problems/recover-a-tree-from-preorder-traversal/editorial/
    static int index = 0;

    public TreeNode recoverFromPreorder_1_1(String traversal) {
        index = 0;
        return helper(traversal, 0);
    }

    private TreeNode helper(String traversal, int depth) {
        if (index >= traversal.length())
            return null;
        // Count the number of dashes
        int dashCount = 0;
        while ((index + dashCount) < traversal.length() &&
                traversal.charAt(index + dashCount) == '-') {
            dashCount++;
        }

        // If the number of dashes doesn't match the current depth, return null
        if (dashCount != depth)
            return null;

        // Move index past the dashes
        index += dashCount;

        // Extract the node value
        int value = 0;
        while (index < traversal.length() &&
                Character.isDigit(traversal.charAt(index))) {
            value = value * 10 + (traversal.charAt(index++) - '0');
        }

        // Create the current node
        TreeNode node = new TreeNode(value);

        // Recursively build the left and right subtrees
        node.left = helper(traversal, depth + 1);
        node.right = helper(traversal, depth + 1);

        return node;
    }




    // V1-2
    // IDEA: Iterative Approach with Stack (Single Pass)
    // https://leetcode.com/problems/recover-a-tree-from-preorder-traversal/editorial/
    public TreeNode recoverFromPreorder_1_2(String traversal) {
        Stack<TreeNode> stack = new Stack<>();
        int index = 0;

        while (index < traversal.length()) {
            // Count the number of dashes
            int depth = 0;
            while (index < traversal.length() && traversal.charAt(index) == '-') {
                depth++;
                index++;
            }

            // Extract the node value
            int value = 0;
            while (index < traversal.length() &&
                    Character.isDigit(traversal.charAt(index))) {
                value = value * 10 + (traversal.charAt(index) - '0');
                index++;
            }

            // Create the current node
            TreeNode node = new TreeNode(value);

            // Adjust the stack to the correct depth
            while (stack.size() > depth) {
                stack.pop();
            }

            // Attach the node to the parent
            if (!stack.empty()) {
                if (stack.peek().left == null) {
                    stack.peek().left = node;
                } else {
                    stack.peek().right = node;
                }
            }

            // Push the current node onto the stack
            stack.push(node);
        }

        // The root is the first node in the stack
        while (stack.size() > 1) {
            stack.pop();
        }

        return stack.peek();
    }





    // V2





}
