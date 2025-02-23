package LeetCodeJava.Recursion;

import LeetCodeJava.DataStructure.TreeNode;

import java.util.Arrays;
import java.util.HashMap;

// https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/
/**
 * 106. Construct Binary Tree from Inorder and Postorder Traversal
 * Solved
 * Medium
 * Topics
 * Companies
 * Given two integer arrays inorder and postorder where inorder is the inorder traversal of a binary tree and postorder is the postorder traversal of the same tree, construct and return the binary tree.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: inorder = [9,3,15,20,7], postorder = [9,15,7,20,3]
 * Output: [3,9,20,null,null,15,7]
 * Example 2:
 *
 * Input: inorder = [-1], postorder = [-1]
 * Output: [-1]
 *
 *
 * Constraints:
 *
 * 1 <= inorder.length <= 3000
 * postorder.length == inorder.length
 * -3000 <= inorder[i], postorder[i] <= 3000
 * inorder and postorder consist of unique values.
 * Each value of postorder also appears in inorder.
 * inorder is guaranteed to be the inorder traversal of the tree.
 * postorder is guaranteed to be the postorder traversal of the tree.
 */
public class ConstructBinaryTreeFromInorderAndPostorderTraversal {

    // V0
    // IDEA : DFS, LC 105
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Recursion/construct-binary-tree-from-inorder-and-postorder-traversal.py
    public TreeNode buildTree(int[] inorder, int[] postorder) {

        if (postorder.length == 0) {
            return null;
        }

        // either each of below works
        if (postorder.length == 1) {
            return new TreeNode(postorder[0]);
        }

//        if (inorder.length == 1) {
//            return new TreeNode(inorder[0]);
//        }

        // NOTE !!! : get root from postorder
        TreeNode root = new TreeNode(postorder[postorder.length - 1]);
        /**
         *  NOTE !!!
         *
         *  root_idx is actually `radius`
         *
         */
        int root_idx = -1;
        for (int i = 0; i < inorder.length; i++) {

            /** NOTE !!! need to get root_idx from inorder */
            if (inorder[i] == postorder[postorder.length - 1]) {
                root_idx = i;
                break;
            }

        }

        root.left = this.buildTree(
                /**
                 *  NOTE !!! below
                 *
                 *  root_idx is actually radius
                 *
                 *  1) inorder part is as same as LC 105
                 *
                 *  2) since postorder is  left -> right -> root
                 *   -> so, sub left root should within [0, radius]
                 */
                Arrays.copyOfRange(inorder, 0, root_idx),
                Arrays.copyOfRange(postorder, 0, root_idx)
        );
        root.right = this.buildTree(
                /**
                 *  NOTE !!! below
                 *
                 *  root_idx is actually radius
                 *
                 *  1) inorder part is as same as LC 105
                 *
                 *  2) since postorder is  left -> right -> root
                 *   -> so, sub right root should within [radius, postorder.len - 1]
                 */
                Arrays.copyOfRange(inorder, root_idx+1, inorder.length),
                Arrays.copyOfRange(postorder, root_idx, postorder.length-1)
        );

        return root;
    }

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
