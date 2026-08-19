package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/closest-binary-search-tree-value/

import java.util.ArrayDeque;
import java.util.Deque;

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

    // V1
    // IDEA: brute force -- ignore the BST ordering entirely and DFS every node,
    //       keeping the best (closest, then smallest) value seen. Kept as a
    //       readable correctness reference for V0 / V2.
    /**
     * time = O(n)
     * space = O(h)
     */
    public int closestValue_1(TreeNode root, double target) {
        return dfs_1(root, root.val, target);
    }

    private int dfs_1(TreeNode node, int best, double target) {
        if (node == null) {
            return best;
        }
        double curDiff = Math.abs(node.val - target);
        double bestDiff = Math.abs(best - target);
        if (curDiff < bestDiff || (curDiff == bestDiff && node.val < best)) {
            best = node.val;
        }
        best = dfs_1(node.left, best, target);
        best = dfs_1(node.right, best, target);
        return best;
    }

    // V2
    // IDEA: in-order traversal gives the values in sorted order, so the answer is
    //       either the predecessor (last value <= target) or the successor (first
    //       value > target). Walk in-order with an explicit stack and stop at the
    //       first value > target -- no need to visit the rest of the tree.
    /**
     * time = O(h + k)   // k = nodes visited before passing target
     * space = O(h)
     */
    public int closestValue_2(TreeNode root, double target) {
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode cur = root;
        Integer pred = null;

        while (cur != null || !stack.isEmpty()) {
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();

            if (cur.val > target) {
                // cur is the successor; compare it against the predecessor
                if (pred == null) {
                    return cur.val;
                }
                // NOTE !!! tie -> the smaller value (the predecessor) wins
                return (target - pred <= cur.val - target) ? pred : cur.val;
            }
            pred = cur.val;

            cur = cur.right;
        }
        return pred;
    }
}
