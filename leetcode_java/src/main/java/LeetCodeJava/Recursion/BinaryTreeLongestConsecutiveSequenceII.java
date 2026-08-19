package LeetCodeJava.Recursion;

// https://leetcode.com/problems/binary-tree-longest-consecutive-sequence-ii/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  549. Binary Tree Longest Consecutive Sequence II
 *  Medium
 *
 *  Given the root of a binary tree, return the length of the longest
 *  consecutive path in the tree.
 *
 *  A consecutive path is a path where the values increase by one along the
 *  path or decrease by one along the path. For example [1,2,3,4] and
 *  [4,3,2,1] are both considered valid, but the path [1,2,4,3] is not valid.
 *
 *  On the other hand, the path can be in the child-Parent-child order, where
 *  not necessarily be parent-child order.
 *
 *
 *  Example 1:
 *
 *  Input: root = [1,2,3]
 *  Output: 2
 *  Explanation: the longest consecutive path is [1, 2] or [2, 1].
 *
 *  Example 2:
 *
 *  Input: root = [2,1,3]
 *  Output: 3
 *  Explanation: the longest consecutive path is [1, 2, 3] or [3, 2, 1].
 *
 *
 *  Constraints:
 *
 *  The number of nodes in the tree is in the range [1, 3 * 10^4].
 *  -3 * 10^4 <= Node.val <= 3 * 10^4
 */
public class BinaryTreeLongestConsecutiveSequenceII {

    // V0
    // IDEA: POST ORDER DFS returning (incLen, decLen) of the downward paths
    //       starting at the node; a node can join its increasing branch with
    //       its decreasing branch -> inc + dec - 1.
    private int maxLen = 0;

    /**
     * time = O(n)
     * space = O(h)   (h = tree height, recursion stack)
     */
    public int longestConsecutive(TreeNode root) {
        this.maxLen = 0;
        helper(root);
        return this.maxLen;
    }

    // returns int[]{ longest increasing downward path, longest decreasing downward path }
    private int[] helper(TreeNode node) {
        if (node == null) {
            return new int[]{0, 0};
        }
        int[] left = helper(node.left);
        int[] right = helper(node.right);

        int inc = 1;
        int dec = 1;

        if (node.left != null) {
            if (node.left.val == node.val + 1) {
                inc = Math.max(inc, left[0] + 1);
            } else if (node.left.val == node.val - 1) {
                dec = Math.max(dec, left[1] + 1);
            }
        }
        if (node.right != null) {
            if (node.right.val == node.val + 1) {
                inc = Math.max(inc, right[0] + 1);
            } else if (node.right.val == node.val - 1) {
                dec = Math.max(dec, right[1] + 1);
            }
        }

        this.maxLen = Math.max(this.maxLen, inc + dec - 1);
        return new int[]{inc, dec};
    }
}
