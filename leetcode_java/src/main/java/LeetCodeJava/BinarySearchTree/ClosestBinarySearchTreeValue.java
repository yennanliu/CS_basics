package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/closest-binary-search-tree-value/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  270. Closest Binary Search Tree Value
 *  Easy
 *
 *  Given the root of a binary search tree and a target value, return the value
 *  in the BST that is closest to the target. If there are multiple answers,
 *  print the smallest.
 *
 *  Example 1:
 *   Input: root = [4,2,5,1,3], target = 3.714286
 *   Output: 4
 *
 *  Example 2:
 *   Input: root = [1], target = 4.428571
 *   Output: 1
 *
 *  Constraints:
 *   The number of nodes in the tree is in the range [1, 10^4].
 *   0 <= Node.val <= 10^9
 *   -10^9 <= target <= 10^9
 */
public class ClosestBinarySearchTreeValue {

    // V0
    // IDEA: BST property (left < root < right) -> walk down one path only,
    //       tracking the best candidate; ties are broken by the smaller value.
    /**
     * time = O(h)   // h = tree height
     * space = O(1)
     */
    public int closestValue(TreeNode root, double target) {
        int best = root.val;
        TreeNode cur = root;

        while (cur != null) {
            double curDiff = Math.abs(cur.val - target);
            double bestDiff = Math.abs(best - target);
            if (curDiff < bestDiff || (curDiff == bestDiff && cur.val < best)) {
                best = cur.val;
            }
            if (target < cur.val) {
                cur = cur.left;
            } else if (target > cur.val) {
                cur = cur.right;
            } else {
                return cur.val;
            }
        }
        return best;
    }
}
