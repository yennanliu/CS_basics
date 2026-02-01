package LeetCodeJava.BinarySearchTree;

// https://leetcode.ca/all/272.html
// https://leetcode.com/problems/closest-binary-search-tree-value-ii/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
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
    // Commented out - no implementation
//    public List<Integer> closestKValues(TreeNode root, double target, int k) {
//    }

    // V0-1
    // IDEA: INORDER TRAVERSE + 2 POINTERS (fixed by gpt)
    // TODO: validate
    /**
     * time = O(N)
     * space = O(N)
     */
    public List<Integer> closestKValues_0_1(TreeNode root, double target, int k) {
        List<Integer> res = new ArrayList<>();
        if (root == null || k == 0) return res;

        List<TreeNode> list = new ArrayList<>();
        buildInorder_0_1(root, list); // inorder gives sorted nodes by val

        // 1️⃣ find index of the closest value to target
        int closestIdx = findClosest(list, target);

        // 2️⃣ use two pointers expanding outwards
        int left = closestIdx - 1;
        int right = closestIdx;

        while (k > 0 && (left >= 0 || right < list.size())) {
            if (left < 0) {
                res.add(list.get(right++).val);
            } else if (right >= list.size()) {
                res.add(list.get(left--).val);
            } else {
                double diffL = Math.abs(list.get(left).val - target);
                double diffR = Math.abs(list.get(right).val - target);
                if (diffL <= diffR) {
                    res.add(list.get(left--).val);
                } else {
                    res.add(list.get(right++).val);
                }
            }
            k--;
        }

        return res;
    }

    // ✅ Proper binary search to find closest index
    /** NOTE !!!
     *
     *
     *  the binary search for `finding close val`
     *
     *  - LC 35 Search Insert Position
     */
    private int findClosest(List<TreeNode> list, double target) {
        int l = 0, r = list.size() - 1;
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (list.get(mid).val < target) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }

        // Now l == r is the smallest val >= target, compare with previous
        if (l > 0 && Math.abs(list.get(l).val - target) >= Math.abs(list.get(l - 1).val - target)) {
            return l - 1;
        }
        return l;
    }

    private void buildInorder_0_1(TreeNode root, List<TreeNode> list) {
        if (root == null) return;
        buildInorder_0_1(root.left, list);
        list.add(root);
        buildInorder_0_1(root.right, list);
    }


    // V0-2
    // IDEA: INORDER TRAVERSE + 2 POINTERS (fixed by gemini)
    // TODO: validate
    /**
     * Main function to find k closest values.
     * time = O(N)
     * space = O(N)
     */
    public List<Integer> closestKValues_0_2(TreeNode root, double target, int k) {
        List<Integer> res = new ArrayList<>();
        if (root == null || k == 0) {
            return res;
        }

        // 1. Get the in-order (sorted) list of all nodes.
        List<Integer> sortedList = new ArrayList<>();
        buildInorder(root, sortedList);

        // 2. Find the index of the element *closest* to the target.
        int closestIdx = findClosestIndex(sortedList, target);

        // 3. Use two pointers to expand from the closest index.
        // l = pointer to the left (smaller values)
        // r = pointer to the right (larger or equal values)
        // We initialize r to the *next* element to check.
        int l = closestIdx - 1;
        int r = closestIdx + 1;

        // Add the absolute closest element first.
        res.add(sortedList.get(closestIdx));
        k--; // We have found 1 of the k values.

        // Expand outwards, "merging" the two sides (left and right)
        // by always picking the one closer to the target.
        while (k > 0) {
            // Check if one pointer has gone out of bounds
            if (l < 0) {
                // Left is out, must take from the right
                res.add(sortedList.get(r));
                r++;
            } else if (r >= sortedList.size()) {
                // Right is out, must take from the left
                res.add(sortedList.get(l));
                l--;
            } else {
                // Both pointers are valid, compare their distances to the target
                double diffL = Math.abs(target - sortedList.get(l));
                double diffR = Math.abs(target - sortedList.get(r));

                if (diffL <= diffR) {
                    // Left is closer (or equal), take from the left
                    res.add(sortedList.get(l));
                    l--;
                } else {
                    // Right is closer, take from the right
                    res.add(sortedList.get(r));
                    r++;
                }
            }
            k--; // Decrement k as we've added a value
        }

        return res;
    }

    /**
     * Corrected: Finds the index of the single closest value in a sorted list.
     * Uses double for comparisons.
     */
    private int findClosestIndex(List<Integer> list, double target) {
        int l = 0;
        int r = list.size() - 1;
        int closestIdx = 0;
        double minDiff = Double.MAX_VALUE;

        while (l <= r) {
            int mid = l + (r - l) / 2;
            double currentVal = list.get(mid);
            double diff = Math.abs(currentVal - target);

            // Update the closest index if this one is better
            if (diff < minDiff) {
                minDiff = diff;
                closestIdx = mid;
            }

            // Standard binary search movement
            if (currentVal < target) {
                l = mid + 1;
            } else if (currentVal > target) {
                r = mid - 1;
            } else {
                // Exact match found, this is the closest
                return mid;
            }
        }
        return closestIdx;
    }

    /**
     * Corrected: In-order traversal.
     * We only need to store the values (int), not the full nodes.
     */
    private void buildInorder(TreeNode root, List<Integer> list) {
        if (root == null) {
            return;
        }
        buildInorder(root.left, list);
        list.add(root.val);
        buildInorder(root.right, list);
    }


    // V1
    // https://leetcode.ca/2016-08-28-272-Closest-Binary-Search-Tree-Value-II/
    private List<Integer> ans;
    private double target;
    private int k;

    /**
     * time = O(N)
     * space = O(K)
     */
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
