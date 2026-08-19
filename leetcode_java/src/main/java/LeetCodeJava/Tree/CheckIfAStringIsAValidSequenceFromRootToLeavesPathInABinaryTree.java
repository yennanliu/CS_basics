package LeetCodeJava.Tree;

// https://leetcode.com/problems/check-if-a-string-is-a-valid-sequence-from-root-to-leaves-path-in-a-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  1430. Check If a String Is a Valid Sequence from Root to Leaves Path in a Binary Tree
 *  Medium
 *
 *  Given a binary tree where each path going from the root to any leaf form a
 *  valid sequence, check if a given string is a valid sequence in such binary tree.
 *
 *  We get the given string from the concatenation of an array of integers arr and
 *  the concatenation of all values of the nodes along a path results in a sequence
 *  in the given binary tree.
 *
 *  Example 1:
 *    Input: root = [0,1,0,0,1,0,null,null,1,0,0], arr = [0,1,0,1]
 *    Output: true
 *    Explanation: The path 0 -> 1 -> 0 -> 1 is a valid sequence.
 *
 *  Example 2:
 *    Input: root = [0,1,0,0,1,0,null,null,1,0,0], arr = [0,0,1]
 *    Output: false
 *    Explanation: The path 0 -> 0 -> 1 does not exist.
 *
 *  Example 3:
 *    Input: root = [0,1,0,0,1,0,null,null,1,0,0], arr = [0,1,1]
 *    Output: false
 *    Explanation: 0 -> 1 -> 1 is a sequence, but not a valid (root to LEAF) one.
 *
 *  Constraints:
 *    1 <= arr.length <= 5000
 *    0 <= arr[i] <= 9
 *    Each node's value is between [0 - 9].
 */
public class CheckIfAStringIsAValidSequenceFromRootToLeavesPathInABinaryTree {

    // V0
    // IDEA: DFS, walk the tree and the array in lock step (index i)
    //       prune as soon as node.val != arr[i]; "valid" means the WHOLE arr
    //       is consumed AND we landed on a LEAF (this is what makes
    //       0 -> 1 -> 1 false: it exists, but the last node is not a leaf).
    /**
     * time = O(N)
     * space = O(H)   // H = tree height (recursion stack)
     */
    public boolean isValidSequence(TreeNode root, int[] arr) {
        return dfs(root, arr, 0);
    }

    private boolean dfs(TreeNode node, int[] arr, int i) {
        if (node == null || i >= arr.length || node.val != arr[i]) {
            return false;
        }
        // NOTE !!! must be at the LAST arr element AND at a leaf
        if (i == arr.length - 1) {
            return node.left == null && node.right == null;
        }
        return dfs(node.left, arr, i + 1) || dfs(node.right, arr, i + 1);
    }
}
