package LeetCodeJava.BinarySearchTree;

// https://leetcode.ca/all/272.html
// https://leetcode.com/problems/closest-binary-search-tree-value-ii/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.LinkedList;
import java.util.List;

/**
 *  272. Closest Binary Search Tree Value II
 * Given a non-empty binary search tree and a target value, find k values in the BST that are closest to the target.
 *
 * Note:
 *
 * Given target value is a floating point.
 * You may assume k is always valid, that is: k ≤ total nodes.
 * You are guaranteed to have only one unique set of k values in the BST that are closest to the target.
 *  Example:
 *
 * Input: root = [4,2,5,1,3], target = 3.714286, and k = 2
 *
 *     4
 *    / \
 *   2   5
 *  / \
 * 1   3
 *
 * Output: [4,3]
 *  Follow up:
 * Assume that the BST is balanced, could you solve it in less than O(n) runtime (where n = total nodes)?
 *
 * Difficulty:
 * Hard
 * Lock:
 * Prime
 * Company:
 * Amazon Facebook ForUsAll Google LinkedIn
 */
public class ClosestBinarySearchTreeValue2 {

    // V0

    // V1
    // https://leetcode.ca/2016-08-28-272-Closest-Binary-Search-Tree-Value-II/
    private List<Integer> ans;
    private double target;
    private int k;

    public List<Integer> closestKValues_1(TreeNode root, double target, int k) {
        ans = new LinkedList<>();
        this.target = target;
        this.k = k;
        dfs(root);
        return ans;
    }

    private void dfs(TreeNode root) {
        if (root == null) {
            return;
        }
        dfs(root.left);
        if (ans.size() < k) {
            ans.add(root.val);
        } else {
            if (Math.abs(root.val - target) >= Math.abs(ans.get(0) - target)) {
                return;
            }
            ans.remove(0);
            ans.add(root.val);
        }
        dfs(root.right);
    }

    // V2

    // V3


}
