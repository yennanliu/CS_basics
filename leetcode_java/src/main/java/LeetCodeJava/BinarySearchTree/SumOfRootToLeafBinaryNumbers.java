package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/sum-of-root-to-leaf-binary-numbers/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  1022. Sum of Root To Leaf Binary Numbers
 *  Easy
 *
 *  You are given the root of a binary tree where each node has a value 0 or 1.
 *  Each root-to-leaf path represents a binary number starting with the most
 *  significant bit.
 *
 *  For example, if the path is 0 -> 1 -> 1 -> 0 -> 1, then this could represent
 *  01101 in binary, which is 13.
 *
 *  For all leaves in the tree, consider the numbers represented by the path from
 *  the root to that leaf. Return the sum of these numbers.
 *
 *  Example 1:
 *   Input: root = [1,0,1,0,1,0,1]
 *   Output: 22   ((100) + (101) + (110) + (111) = 4 + 5 + 6 + 7)
 *
 *  Example 2:
 *   Input: root = [0]
 *   Output: 0
 *
 *  Constraints:
 *   The number of nodes in the tree is in the range [1, 1000].
 *   Node.val is 0 or 1.
 */
public class SumOfRootToLeafBinaryNumbers {

    // V0
    // IDEA: DFS carrying the binary number built so far (cur = cur * 2 + val);
    //       add it to the answer when a leaf is reached.
    /**
     * time = O(n)
     * space = O(h)   // h = tree height (recursion stack)
     */
    public int sumRootToLeaf(TreeNode root) {
        return dfs(root, 0);
    }

    private int dfs(TreeNode node, int cur) {
        if (node == null) {
            return 0;
        }
        cur = (cur << 1) | node.val;
        if (node.left == null && node.right == null) {
            return cur;
        }
        return dfs(node.left, cur) + dfs(node.right, cur);
    }
}
