package LeetCodeJava.Tree;

// https://leetcode.com/problems/equal-tree-partition/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 *  663. Equal Tree Partition
 *  Medium
 *
 *  Given the root of a binary tree, return true if you can partition the tree
 *  into two trees with equal sums of values after removing exactly one edge on
 *  the original tree.
 *
 *  Example 1:
 *
 *  Input: root = [5,10,10,null,null,2,3]
 *  Output: true
 *  Explanation: removing the edge above the right child of the root gives
 *               two trees of sum 15 each.
 *
 *  Example 2:
 *
 *  Input: root = [1,2,10,null,null,2,20]
 *  Output: false
 *
 *  Constraints:
 *
 *  The number of nodes in the tree is in the range [1, 10^4].
 *  -10^5 <= Node.val <= 10^5
 */
public class EqualTreePartition {

    // V0
    // IDEA: collect every subtree sum (post-order, so the whole-tree sum is
    //       recorded last). Cutting an edge above a proper subtree with sum s
    //       leaves (total - s) on the other side, so we need s == total / 2.
    //       Drop the whole-tree sum, since we must remove an actual edge.
    /**
     * time = O(n)
     * space = O(n)
     */
    public boolean checkEqualTree(TreeNode root) {
        if (root == null) {
            return false;
        }
        List<Long> sums = new ArrayList<>();
        long total = subTreeSum(root, sums);

        // the last recorded sum is the whole tree -> not a valid cut
        sums.remove(sums.size() - 1);

        if (total % 2 != 0) {
            return false;
        }
        long half = total / 2;
        for (Long s : sums) {
            if (s == half) {
                return true;
            }
        }
        return false;
    }

    private long subTreeSum(TreeNode node, List<Long> sums) {
        if (node == null) {
            return 0L;
        }
        long cur = node.val
                + subTreeSum(node.left, sums)
                + subTreeSum(node.right, sums);
        sums.add(cur);
        return cur;
    }
}
