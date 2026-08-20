package LeetCodeJava.Tree;

import LeetCodeJava.DataStructure.TreeNode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.OptionalInt;

// https://leetcode.com/problems/maximum-binary-tree/

public class MaximumBinaryTree {

    // V0
    // IDEA : RECURSION + `max val as root` (divide & conquer)
    /**
     *  NOTE !!!
     *
     *   for the sub array nums[left, right):
     *
     *    1) the `max val` in that range is the sub tree root
     *    2) everything on its LEFT  -> left sub tree
     *    3) everything on its RIGHT -> right sub tree
     *
     *   -> we pass (left, right) INDEXES around instead of copying arrays
     */
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public TreeNode constructMaximumBinaryTree(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return null;
        }
        return buildHelper(nums, 0, nums.length);
    }

    // NOTE !!! `right` is EXCLUSIVE, e.g. we handle nums[left, right)
    private TreeNode buildHelper(int[] nums, int left, int right) {
        // empty range -> no node
        if (left >= right) {
            return null;
        }

        // 1) find idx of the `max val` within nums[left, right)
        int maxIdx = left;
        for (int i = left; i < right; i++) {
            if (nums[i] > nums[maxIdx]) {
                maxIdx = i;
            }
        }

        // 2) max val is the root of this sub tree
        TreeNode node = new TreeNode(nums[maxIdx]);

        // 3) recursively build sub left, sub right tree
        node.left = buildHelper(nums, left, maxIdx);
        node.right = buildHelper(nums, maxIdx + 1, right);

        return node;
    }

    // V1
    // IDEA : Recursive Solution
    // https://leetcode.com/problems/maximum-binary-tree/editorial/
    /**
     * time = O(log N)
     * space = O(1)
     */
    public TreeNode constructMaximumBinaryTree_1(int[] nums) {
        return construct(nums, 0, nums.length);
    }

    /** NOTE !!! : parameters : l, r */
    /**
     * time = O(N)
     * space = O(H)
     */
    public TreeNode construct(int[] nums, int l, int r) {
        if (l == r)
            return null;
        int max_i = max(nums, l, r);
        TreeNode root = new TreeNode(nums[max_i]);
        root.left = construct(nums, l, max_i);
        root.right = construct(nums, max_i + 1, r);
        return root;
    }
    /**
     * time = O(N)
     * space = O(H)
     */
    public int max(int[] nums, int l, int r) {
        int max_i = l;
        for (int i = l; i < r; i++) {
            if (nums[max_i] < nums[i])
                max_i = i;
        }
        return max_i;
    }

}
