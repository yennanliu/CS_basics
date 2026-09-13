package LeetCodeJava.Tree;

import LeetCodeJava.DataStructure.TreeNode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.OptionalInt;

// https://leetcode.com/problems/maximum-binary-tree/

/**
 * 654. Maximum Binary Tree
 * Medium
 *
 * You are given an integer array nums with no duplicates. A maximum binary tree can be built recursively from nums using the following algorithm:
 *
 * Create a root node whose value is the maximum value in nums.
 * Recursively build the left subtree on the subarray prefix to the left of the maximum value.
 * Recursively build the right subtree on the subarray suffix to the right of the maximum value.
 *
 * Return the maximum binary tree built from nums.
 *
 * Example 1:
 *
 * https://assets.leetcode.com/uploads/2020/12/24/tree1.jpg
 *
 * Input: nums = [3,2,1,6,0,5]
 * Output: [6,3,5,null,2,0,null,null,1]
 * Explanation: The recursive calls are as follow:
 * - The largest value in [3,2,1,6,0,5] is 6. Left prefix is [3,2,1] and right suffix is [0,5].
 *     - The largest value in [3,2,1] is 3. Left prefix is [] and right suffix is [2,1].
 *         - Empty array, so no child.
 *         - The largest value in [2,1] is 2. Left prefix is [] and right suffix is [1].
 *             - Empty array, so no child.
 *             - Only one element, so child is a node with value 1.
 *     - The largest value in [0,5] is 5. Left prefix is [0] and right suffix is [].
 *         - Only one element, so child is a node with value 0.
 *         - Empty array, so no child.
 *
 * Example 2:
 *
 * https://assets.leetcode.com/uploads/2020/12/24/tree2.jpg
 *
 * Input: nums = [3,2,1]
 * Output: [3,null,2,null,1]
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 0 <= nums[i] <= 1000
 * All integers in nums are unique.
 *
 */
public class MaximumBinaryTree {

    TreeNode root = new TreeNode();

    // TODO : fix below
    // V0
//    public TreeNode constructMaximumBinaryTree(int[] nums) {
//
//        if (nums.length == 1){
//            return new TreeNode(nums[0]);
//        }
//
//        // get max val in nums
////        int max_val = Arrays.stream(nums).max().getAsInt();
////        System.out.println("max_val = " + max_val);
////
////        int idx = Arrays.asList(nums).indexOf(max_val);
//
//        // recursive
//        return _help(nums);
//    }
//
//    private TreeNode _help(int[] nums){
//
//        if (nums.length == 0){
//            return null;
//        }
//
//        // ??
//        if (nums.length == 1){
//            return new TreeNode(nums[0]);
//        }
//
//        Integer[] _nums = toConvertInteger(nums);
//        // get max val in nums
//        //Optional<Integer> max_val = Arrays.stream(_nums).max(Comparator.comparing(x, y));
//        Integer max_val = getMax(_nums);
//        // get idx of max val in nums
//        Integer idx = Arrays.asList(_nums).indexOf(max_val);
//        System.out.println("max_val = " + max_val + " idx = " + idx);
//
//        this.root.val = max_val;
//        this.root.left = _help(Arrays.copyOfRange(nums, 0, idx+1));
//        this.root.right = _help(Arrays.copyOfRange(nums, idx+1, nums.length+1));
//
//        System.out.println("root.left = " + root.left.val + " root.right = " + root.right.val);
//
//        return this.root;
//    }
//
//    private int getMax(Integer[] input){
//        int res = -1;
//        for(Integer x : input){
//            if(x > res){
//                res = x;
//            }
//        }
//        return res;
//    }
//
//    public static Integer[] toConvertInteger(int[] ids) {
//
//        Integer[] newArray = new Integer[ids.length];
//        for (int i = 0; i < ids.length; i++) {
//            newArray[i] = Integer.valueOf(ids[i]);
//        }
//        return newArray;
//    }

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
