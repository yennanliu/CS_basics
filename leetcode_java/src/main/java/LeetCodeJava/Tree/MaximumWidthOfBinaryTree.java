package LeetCodeJava.Tree;

// https://leetcode.com/problems/maximum-width-of-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.HashMap;
import java.util.Map;

/**
 *  662. Maximum Width of Binary Tree
 *  Medium
 *
 *  Given the root of a binary tree, return the maximum width of the given tree.
 *
 *  The maximum width of a tree is the maximum width among all levels. The width
 *  of one level is the length between the end-nodes (the leftmost and rightmost
 *  non-null nodes), where the null nodes between the end-nodes are also counted
 *  into the length calculation.
 *
 *  Example 1:
 *
 *  Input: root = [1,3,2,5,3,null,9]
 *  Output: 4
 *
 *  Example 2:
 *
 *  Input: root = [1,3,null,5,3]
 *  Output: 2
 *
 *  Constraints:
 *
 *  The number of nodes in the tree is in the range [1, 3000].
 *  -100 <= Node.val <= 100
 */
public class MaximumWidthOfBinaryTree {

    // level -> index of the leftmost node seen on that level
    private Map<Integer, Long> leftMostIdx = new HashMap<>();
    private long maxWidth = 0;

    // V0
    // IDEA: give each node its "complete tree" index (left = 2*i, right = 2*i+1);
    //       pre-order DFS visits the leftmost node of a level first, so the width
    //       of a level is (curIdx - leftMostIdx[level] + 1).
    //       Indexes are normalized per level to avoid overflow.
    /**
     * time = O(n)
     * space = O(h)
     */
    public int widthOfBinaryTree(TreeNode root) {
        this.leftMostIdx = new HashMap<>();
        this.maxWidth = 0;
        helper(root, 0, 0L);
        return (int) this.maxWidth;
    }

    private void helper(TreeNode node, int level, long idx) {
        if (node == null) {
            return;
        }
        if (!this.leftMostIdx.containsKey(level)) {
            this.leftMostIdx.put(level, idx);
        }
        long base = this.leftMostIdx.get(level);
        // normalize so the indexes stay small
        long cur = idx - base;
        this.maxWidth = Math.max(this.maxWidth, cur + 1);

        helper(node.left, level + 1, cur * 2);
        helper(node.right, level + 1, cur * 2 + 1);
    }
}
