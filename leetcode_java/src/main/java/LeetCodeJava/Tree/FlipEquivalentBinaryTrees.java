package LeetCodeJava.Tree;

// https://leetcode.com/problems/flip-equivalent-binary-trees/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.Stack;

/**
 * 951. Flip Equivalent Binary Trees
 * Medium
 * Topics
 * Companies
 * For a binary tree T, we can define a flip operation as follows: choose any node, and swap the left and right child subtrees.
 *
 * A binary tree X is flip equivalent to a binary tree Y if and only if we can make X equal to Y after some number of flip operations.
 *
 * Given the roots of two binary trees root1 and root2, return true if the two trees are flip equivalent or false otherwise.
 *
 *
 *
 * Example 1:
 *
 * Flipped Trees Diagram
 * Input: root1 = [1,2,3,4,5,6,null,null,null,7,8], root2 = [1,3,2,null,6,4,5,null,null,null,null,8,7]
 * Output: true
 * Explanation: We flipped at nodes with values 1, 3, and 5.
 * Example 2:
 *
 * Input: root1 = [], root2 = []
 * Output: true
 * Example 3:
 *
 * Input: root1 = [], root2 = [1]
 * Output: false
 *
 *
 * Constraints:
 *
 * The number of nodes in each tree is in the range [0, 100].
 * Each tree will have unique node values in the range [0, 99].
 *
 */
public class FlipEquivalentBinaryTrees {

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
    // TODO : implement
//    public boolean flipEquiv(TreeNode root1, TreeNode root2) {
//
//    }

    // V1
    // IDEA : DFS (fixed by gpt)
    public boolean flipEquiv_1(TreeNode root1, TreeNode root2) {
        // Both trees are null, they are equivalent
        if (root1 == null && root2 == null) {
            return true;
        }

        // If one is null and the other is not, or the values don't match
        if (root1 == null || root2 == null || root1.val != root2.val) {
            return false;
        }

        // Check both configurations:
        // 1. No flip: compare root1.left with root2.left and root1.right with root2.right
        // 2. Flip: compare root1.left with root2.right and root1.right with root2.left
        return (flipEquiv_1(root1.left, root2.left) && flipEquiv_1(root1.right, root2.right)) ||
                (flipEquiv_1(root1.left, root2.right) && flipEquiv_1(root1.right, root2.left));
    }

    // V2-1
    // IDEA : Recursion (Top-down Traversal)
    // https://leetcode.com/problems/flip-equivalent-binary-trees/editorial/
    public boolean flipEquiv_2_1(TreeNode root1, TreeNode root2) {
        // Both trees are empty
        if (root1 == null && root2 == null) {
            return true;
        }
        // Just one of the trees is empty
        if (root1 == null || root2 == null) {
            return false;
        }
        // Corresponding values differ
        if (root1.val != root2.val) {
            return false;
        }

        // Check if corresponding subtrees are flip equivalent
        boolean noSwap =
                flipEquiv_2_1(root1.left, root2.left) &&
                        flipEquiv_2_1(root1.right, root2.right);
        // Check if opposite subtrees are flip equivalent
        boolean swap =
                flipEquiv_2_1(root1.left, root2.right) &&
                        flipEquiv_2_1(root1.right, root2.left);

        return noSwap || swap;
    }


    // V2-2
    // IDEA : Iterative DFS (using a Stack)
    // https://leetcode.com/problems/flip-equivalent-binary-trees/editorial/

    // Checks whether the given pair of nodes should be examined -
    // be pushed into the stack
    public boolean checkNodeValues(TreeNode node1, TreeNode node2) {
        if (node1 == null && node2 == null) return true;
        if (
                node1 != null && node2 != null && node1.val == node2.val
        ) return true;
        return false;
    }

    public boolean flipEquiv_2_2(TreeNode root1, TreeNode root2) {
        // Initialize stack to store pairs of nodes
        Stack<TreeNode[]> nodePairStack = new Stack<>();
        nodePairStack.push(new TreeNode[] { root1, root2 });

        // While the stack is not empty:
        while (!nodePairStack.isEmpty()) {
            TreeNode[] current = nodePairStack.pop();
            TreeNode node1 = current[0];
            TreeNode node2 = current[1];

            if (node1 == null && node2 == null) continue;
            if (node1 == null || node2 == null) return false;
            if (node1.val != node2.val) return false;

            // Check both configurations: no swap and swap
            if (
                    checkNodeValues(node1.left, node2.left) &&
                            checkNodeValues(node1.right, node2.right)
            ) {
                nodePairStack.push(new TreeNode[] { node1.left, node2.left });
                nodePairStack.push(new TreeNode[] { node1.right, node2.right });
            } else if (
                    checkNodeValues(node1.left, node2.right) &&
                            checkNodeValues(node1.right, node2.left)
            ) {
                nodePairStack.push(new TreeNode[] { node1.left, node2.right });
                nodePairStack.push(new TreeNode[] { node1.right, node2.left });
            } else {
                return false;
            }
        }
        return true;
    }


    // V2-3
    // IDEA : Canonical Forms
    // https://leetcode.com/problems/flip-equivalent-binary-trees/editorial/
    public void findCanonicalForm(TreeNode root) {
        if (root == null) return;

        // Post-order traversal: first bring subtrees into their canonical form
        findCanonicalForm(root.left);
        findCanonicalForm(root.right);

        if (root.right == null) return;

        // Swap subtrees, so that left is non-empty
        if (root.left == null) {
            root.left = root.right;
            root.right = null;
            return;
        }

        TreeNode left = root.left;
        TreeNode right = root.right;
        // Swap subtrees
        if (left.val > right.val) {
            root.left = right;
            root.right = left;
        }
    }

    public boolean areEquivalent(TreeNode root1, TreeNode root2) {
        if (root1 == null && root2 == null) return true;
        if (root1 == null || root2 == null) return false;
        if (root1.val != root2.val) return false;

        return (
                areEquivalent(root1.left, root2.left) &&
                        areEquivalent(root1.right, root2.right)
        );
    }

    public boolean flipEquiv_2_3(TreeNode root1, TreeNode root2) {
        findCanonicalForm(root1);
        findCanonicalForm(root2);
        return areEquivalent(root1, root2);
    }

}
