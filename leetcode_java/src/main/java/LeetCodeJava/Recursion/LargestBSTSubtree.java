package LeetCodeJava.Recursion;

// https://leetcode.com/problems/largest-bst-subtree/description/
// https://leetcode.ca/all/333.html

import LeetCodeJava.DataStructure.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 333. Largest BST Subtree
 * Given a binary tree, find the largest subtree which is a Binary Search Tree (BST), where largest means subtree with largest number of nodes in it.
 *
 * Note:
 * A subtree must include all of its descendants.
 *
 * Example:
 *
 * Input: [10,5,15,1,8,null,7]
 *
 *    10
 *    / \
 *   5  15
 *  / \   \
 * 1   8   7
 *
 * Output: 3
 * Explanation: The Largest BST Subtree in this case is the highlighted one.
 *              The return value is the subtree's size, which is 3.
 * Follow up:
 * Can you figure out ways to solve it with O(n) time complexity?
 *
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Apple Google Lyft Microsoft
 * Problem Solution
 * 333-Largest-BST-Subtree
 */
public class LargestBSTSubtree {

    // V0
//    public int largestBSTSubtree(TreeNode root) {
//    }

    // V0-1
    // IDEA: BFS + BST + NODE cnt (fixed by gpt)
    // TODO: validate
    int maxBSTNodeCnt = 0;

    public int largestBSTSubtree_0_1(TreeNode root) {
        if (root == null) {
            return 0;
        }

        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);

        while (!q.isEmpty()) {
            TreeNode node = q.poll();
            if (isBST(node, Long.MIN_VALUE, Long.MAX_VALUE)) {
                maxBSTNodeCnt = Math.max(maxBSTNodeCnt, getSubTreeNode(node));
            }
            if (node.left != null) {
                q.add(node.left);
            }
            if (node.right != null) {
                q.add(node.right);
            }
        }

        return maxBSTNodeCnt;
    }

    private boolean isBST(TreeNode root, long minVal, long maxVal) {
        if (root == null) {
            return true;
        }
        if (root.val <= minVal || root.val >= maxVal) {
            return false;
        }
        return isBST(root.left, minVal, root.val) &&
                isBST(root.right, root.val, maxVal);
    }

    private int getSubTreeNode(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return 1 + getSubTreeNode(root.left) + getSubTreeNode(root.right);
    }

    // V0-2
    // IDEA: IMPROVED OF V0-1 (gpt)
    int maxBST = 0;

    public int largestBSTSubtree_0_2(TreeNode root) {
        dfs_0_2(root);
        return maxBST;
    }

    private int[] dfs_0_2(TreeNode node) {
        // return {minVal, maxVal, size}
        if (node == null) {
            return new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE, 0};
        }

        int[] left = dfs_0_2(node.left);
        int[] right = dfs_0_2(node.right);

        // if either left or right is invalid BST
        if (left[2] == -1 || right[2] == -1 || node.val <= left[1] || node.val >= right[0]) {
            return new int[]{0, 0, -1}; // mark as invalid BST
        }

        int size = 1 + left[2] + right[2];
        maxBST = Math.max(maxBST, size);

        int minVal = Math.min(node.val, left[0]);
        int maxVal = Math.max(node.val, right[1]);

        return new int[]{minVal, maxVal, size};
    }


    // V0-3
    // IDEA: DFS (gemini)
    // TODO: validate
    // Global variable to track the maximum size of a valid BST found so far.
    private int maxBSTNodeCnt_0_3 = 0;

    public int largestBSTSubtree_0_3(TreeNode root) {
        if (root == null) {
            return 0;
        }

        // Start the recursive, post-order helper function.
        traverse(root);

        return maxBSTNodeCnt_0_3;
    }

    /**
     * Post-order traversal (Bottom-Up) to check BST properties and calculate size.
     * For each node, returns an array: {size, min_value, max_value}.
     * * If the subtree is NOT a BST, we return size = -1.
     */
    private int[] traverse(TreeNode root) {
        // Base case: A null node is a valid BST of size 0.
        // We use Integer.MAX_VALUE and MIN_VALUE to satisfy the comparison
        // in the parent node (any value is greater than MAX_VALUE of left,
        // and smaller than MIN_VALUE of right).
        if (root == null) {
            return new int[]{0, Integer.MAX_VALUE, Integer.MIN_VALUE};
        }

        // 1. Traverse left and right subtrees (Post-Order).
        int[] leftResult = traverse(root.left);
        int[] rightResult = traverse(root.right);

        int leftSize = leftResult[0];
        int leftMax = leftResult[2]; // Max value in left subtree

        int rightSize = rightResult[0];
        int rightMin = rightResult[1]; // Min value in right subtree

        // 2. Check the BST condition for the current node:
        // A node forms a BST if:
        // a) Both children are valid BSTs (size >= 0).
        // b) Current value is GREATER than max value in the left subtree.
        // c) Current value is SMALLER than min value in the right subtree.

        if (leftSize != -1 && rightSize != -1 &&
                root.val > leftMax && root.val < rightMin) {

            // 3. If it IS a BST: Calculate the size and update the global max.
            int currentSize = leftSize + rightSize + 1;
            maxBSTNodeCnt_0_3 = Math.max(maxBSTNodeCnt_0_3, currentSize);

            // Return the new size, and the new min/max for this consolidated BST.
            int newMin = Math.min(root.val, leftResult[1]);
            int newMax = Math.max(root.val, rightResult[2]);

            return new int[]{currentSize, newMin, newMax};

        } else {
            // 4. If it is NOT a BST: Mark it as invalid by returning size = -1.
            return new int[]{-1, 0, 0};
        }
    }


    // V1
    // IDEA: DFS
    // https://leetcode.ca/2016-10-28-333-Largest-BST-Subtree/
    private int ans;
    public int largestBSTSubtree_1(TreeNode root) {
        ans = 0;
        dfs(root);
        return ans;
    }

    private int[] dfs(TreeNode root) {
        if (root == null) {
            return new int[] {Integer.MAX_VALUE, Integer.MIN_VALUE, 0};
        }
        int[] left = dfs(root.left);
        int[] right = dfs(root.right);
        if (left[1] < root.val && root.val < right[0]) {
            ans = Math.max(ans, left[2] + right[2] + 1);
            return new int[] {
                    Math.min(root.val, left[0]), Math.max(root.val, right[1]), left[2] + right[2] + 1};
        }
        return new int[] {Integer.MIN_VALUE, Integer.MAX_VALUE, 0};
    }


    // V2


}
