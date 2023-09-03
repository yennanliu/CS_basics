package LeetCodeJava.Tree;

import LeetCodeJava.DataStructure.TreeNode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.OptionalInt;

// https://leetcode.com/problems/maximum-binary-tree/

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
    public TreeNode constructMaximumBinaryTree_1(int[] nums) {
        return construct(nums, 0, nums.length);
    }

    /** NOTE !!! : parameters : l, r */
    public TreeNode construct(int[] nums, int l, int r) {
        if (l == r)
            return null;
        int max_i = max(nums, l, r);
        TreeNode root = new TreeNode(nums[max_i]);
        root.left = construct(nums, l, max_i);
        root.right = construct(nums, max_i + 1, r);
        return root;
    }
    public int max(int[] nums, int l, int r) {
        int max_i = l;
        for (int i = l; i < r; i++) {
            if (nums[max_i] < nums[i])
                max_i = i;
        }
        return max_i;
    }


//    public static void main(String[] args) {
//
//        Integer[] _nums = new Integer[]{1,2,3};
//        Integer[] sub = Arrays.copyOfRange(_nums, 0, 2);
//
//        System.out.println(_nums);
//        System.out.println(Arrays.asList(_nums).indexOf(3));
//
//        Integer[] array1 = {2, 4, 6, 8, 10};
//        int index = Arrays.asList(array1).indexOf(8);
//        System.out.println("Found element at location at index:"+index);
//
//
////        for(int x :sub){
////            System.out.println(x);
////        }
//        //System.out.println(Arrays.copyOfRange(_nums, 0,3));
//    }

}
