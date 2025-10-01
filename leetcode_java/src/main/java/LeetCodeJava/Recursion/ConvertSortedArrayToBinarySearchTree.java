package LeetCodeJava.Recursion;

// https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/description/

import LeetCodeJava.DataStructure.TreeNode;

/**
 * 108. Convert Sorted Array to Binary Search Tree
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given an integer array nums where the elements are sorted in ascending order, convert it to a height-balanced binary search tree.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: nums = [-10,-3,0,5,9]
 * Output: [0,-3,9,-10,null,5]
 * Explanation: [0,-10,5,null,-3,null,9] is also accepted:
 *
 * Example 2:
 *
 *
 * Input: nums = [1,3]
 * Output: [3,1]
 * Explanation: [1,null,3] and [3,1] are both height-balanced BSTs.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 104
 * -104 <= nums[i] <= 104
 * nums is sorted in a strictly increasing order.
 *
 *
 */
public class ConvertSortedArrayToBinarySearchTree {

    // V0
//    public TreeNode sortedArrayToBST(int[] nums) {
//
//    }

    // V0-1
    // IDEA: DFS build tree + BST property (fixed by gpt)
    public TreeNode sortedArrayToBST_0_1(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return null;
        }
        return bstBuilder(nums, 0, nums.length - 1);
    }

    private TreeNode bstBuilder(int[] nums, int start, int end) {
        if (start > end) {
            return null;
        }

        // pick mid as root to ensure balance
        int mid = start + (end - start) / 2;
        TreeNode node = new TreeNode(nums[mid]);

        // build left and right subtrees recursively
        node.left = bstBuilder(nums, start, mid - 1);
        node.right = bstBuilder(nums, mid + 1, end);

        return node;
    }
    // V1
    // https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/solutions/6892739/video-find-middle-of-tree-or-subtree-by-94thd/
    public TreeNode sortedArrayToBST_1(int[] nums) {
        return convert(nums, 0, nums.length - 1);
    }

    private TreeNode convert(int[] nums, int left, int right) {
        if (left > right) {
            return null;
        }

        int mid = left + (right - left) / 2;

        TreeNode node = new TreeNode(nums[mid]);

        node.left = convert(nums, left, mid - 1);
        node.right = convert(nums, mid + 1, right);

        return node;
    }

    // V2
    // https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/solutions/6025974/0-ms-runtime-beats-100-user-step-by-step-88p3/
    public TreeNode sortedArrayToBST_2(int[] nums) {
        return helper(nums, 0, nums.length - 1);
    }

    private TreeNode helper(int[] nums, int left, int right) {
        if (left > right)
            return null;
        int mid = (left + right) / 2;
        TreeNode root = new TreeNode(nums[mid]);
        root.left = helper(nums, left, mid - 1);
        root.right = helper(nums, mid + 1, right);
        return root;
    }


    // V3
    // https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/solutions/6907442/beats-100-beginner-friendly-explanation-dna9u/
    public TreeNode sortedArrayToBST_3(int[] nums) {
        return insert(nums, 0, nums.length);
    }

    // Recursive helper to build BST
    TreeNode insert(int[] nums, int start, int end) {
        if (start == end)
            return null;

        int mid = (start + end) / 2;
        TreeNode node = new TreeNode(nums[mid]);
        node.left = insert(nums, start, mid);
        node.right = insert(nums, mid + 1, end);
        return node;
    }


}
