package LeetCodeJava.Recursion;

import LeetCodeJava.DataStructure.TreeNode;

import java.util.Arrays;
import java.util.HashMap;

// https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/

public class ConstructBinaryTreeFromInorderAndPostorderTraversal {

    // TODO : fix V0
    // V0
    // IDEA :
    //TreeNode root = new TreeNode();
//    public TreeNode buildTree(int[] inorder, int[] postorder) {
//
//        if (inorder.length == 1){
//            return new TreeNode(inorder[0]);
//        }
//
//        //TreeNode _root = new TreeNode(inorder[0]);
//
//        // dfs
//        return _help(inorder, postorder, 0);
//    }
//
//    private TreeNode _help(int[] inorder, int[] postorder, int idx){
//
//        if (inorder.length == 0){
//            return null;
//        }
//
//        int root_val = postorder[postorder.length-1];
//        TreeNode _root = new TreeNode(root_val);
//        int root_idx = getIdx(inorder, root_val);
//        int left_val = inorder[root_idx-1];
//        int left_idx = getIdx(postorder, left_val);
//
//        _root.left = _help(Arrays.copyOfRange(inorder, 0, root_idx), Arrays.copyOfRange(postorder, 0, left_idx), root_idx);
//        _root.right = _help(Arrays.copyOfRange(inorder, root_idx+1, inorder.length-1), Arrays.copyOfRange(postorder, left_idx+1, root_idx), root_idx);
//        return _root;
//    }
//
//    private int getIdx(int[] input, int val){
//        int idx = -1;
//        for (int i = 0; i < input.length; i++){
//            if(input[i] == val){
//                return i;
//            }
//        }
//        return idx;
//    }

    // V1
    // IDEA : RECURSION
    // https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/editorial/
    int post_idx;
    int[] postorder;
    int[] inorder;
    HashMap<Integer, Integer> idx_map = new HashMap<Integer, Integer>();

    public TreeNode helper(int in_left, int in_right) {
        // if there is no elements to construct subtrees
        if (in_left > in_right)
            return null;

        // pick up post_idx element as a root
        int root_val = postorder[post_idx];
        TreeNode root = new TreeNode(root_val);

        // root splits inorder list
        // into left and right subtrees
        int index = idx_map.get(root_val);

        // recursion
        post_idx--;
        // build right subtree
        root.right = helper(index + 1, in_right);
        // build left subtree
        root.left = helper(in_left, index - 1);
        return root;
    }

    public TreeNode buildTree_1(int[] inorder, int[] postorder) {
        this.postorder = postorder;
        this.inorder = inorder;
        // start from the last postorder element
        post_idx = postorder.length - 1;

        // build a hashmap value -> its index
        int idx = 0;
        for (Integer val : inorder)
            idx_map.put(val, idx++);
        return helper(0, inorder.length - 1);
    }

}
