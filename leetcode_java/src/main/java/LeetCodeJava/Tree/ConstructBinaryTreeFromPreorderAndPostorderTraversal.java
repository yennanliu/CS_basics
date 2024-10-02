package LeetCodeJava.Tree;

// https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 *
 * 889. Construct Binary Tree from Preorder and Postorder Traversal
 *
 *
 * Given two integer arrays, preorder and postorder where preorder is the preorder traversal of a binary tree of distinct values and postorder is the postorder traversal of the same tree, reconstruct and return the binary tree.
 *
 * If there exist multiple answers, you can return any of them.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: preorder = [1,2,4,5,3,6,7], postorder = [4,5,2,6,7,3,1]
 * Output: [1,2,3,4,5,6,7]
 * Example 2:
 *
 * Input: preorder = [1], postorder = [1]
 * Output: [1]
 *
 *
 * Constraints:
 *
 * 1 <= preorder.length <= 30
 * 1 <= preorder[i] <= preorder.length
 * All the values of preorder are unique.
 * postorder.length == preorder.length
 * 1 <= postorder[i] <= postorder.length
 * All the values of postorder are unique.
 * It is guaranteed that preorder and postorder are the preorder traversal and postorder traversal of the same binary tree.
 *
 *
 */
public class ConstructBinaryTreeFromPreorderAndPostorderTraversal {

    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     *     int val;
     *     TreeNode left;
     *     TreeNode right;
     *     TreeNode() {}
     *     TreeNode(int val) { this.val = val; }
     *     TreeNode(int val, TreeNode left, TreeNode right) {
     *         this.val = val;
     *         this.left = left;
     *         this.right = right;
     *     }
     * }
     */

    // V0
    // TODO : implement
//    public TreeNode constructFromPrePost(int[] preorder, int[] postorder) {
//
//    }

    // V1

    // V2
    // https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/solutions/2351831/java-explanation-no-hashmap-easy-to-implement/
    /**
     * There are two keys in this problem:
     *
     * (1) First element of preorder is root.
     * (2) The first element of left-subtree of preorder[ ] is the last element of left-subtree. It's because the order difference between preorder traversal and postorder traversal.
     * For example, we have preorder = [1,2,4,5,3,6,7] and postorder = [4,5,2,6,7,3,1].
     * Now, we know 1 is root and 2 is first element of left-subtree.
     *
     * Q. How do we figure out the left-subtree and right-subtree at this point?
     *
     * A. Preorder means we process the root -> left -> right, and because of preorder traversal we can identify the root node. Postorder process left -> right -> root. Therefore, [2] is the first node in left-subtree from preorder, and [2] is also the last node of left-subtree in postorder.
     * Therefore, [4, 5, 2] are all left-subtree elements.
     *
     */
    public TreeNode constructFromPrePost(int[] preorder, int[] postorder) {
        // O(n) time | O(h) space
        if(preorder == null || postorder == null || preorder.length == 0 || postorder.length == 0)  return null;

        TreeNode root = new TreeNode(preorder[0]);
        int mid = 0;

        if(preorder.length == 1)    return root;

        // update mid
        for(int i = 0; i < postorder.length; i++)
        {
            if(preorder[1] == postorder[i])
            {
                mid = i;
                break;
            }
        }

        root.left = constructFromPrePost(
                Arrays.copyOfRange(preorder, 1, 1 + mid + 1),
                Arrays.copyOfRange(postorder, 0, mid + 1));

        root.right = constructFromPrePost(
                Arrays.copyOfRange(preorder, 1 + mid + 1, preorder.length),
                Arrays.copyOfRange(postorder, mid + 1, postorder.length - 1));
        return root;
    }

    // V3
    // IDEA : TREE PROPERTY
    // https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/solutions/357699/java-o-n/
    int pre[];
    int post[];
    public TreeNode constructFromPrePost_3(int[] pre, int[] post) {
        this.pre = pre;
        this.post = post;
        return helper(0, pre.length-1, 0, post.length-1);
    }

    public TreeNode helper(int pre_start, int pre_end, int post_start, int post_end){
        if (post_start > post_end){
            return null;
        }

        int cur = pre[pre_start];
        TreeNode root = new TreeNode(cur);
        if (pre_start == pre_end){
            return root;
        }
        int left = pre[pre_start+1];
        int index = 0;
        for(int i = post_start; i<=post_end; i++){
            if (post[i]==left){
                index = i;
            }
        }
        root.left = helper(pre_start+1, pre_start+index-post_start+1, post_start, index);
        root.right = helper(pre_start+index-post_start+2, pre_end, index+1, post_end-1);
        return root;
    }

    // V4
    // IDEA: RECURSIVE + TREE
    // https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/solutions/3305293/beats-100-java-solution-using-recursion-simple-and-clean-code/
    public TreeNode f(int[] pre, int[] post, int preS, int preE, int postS, int postE){
        if(postS>postE) return null;
        int rootVal=pre[preS];
        TreeNode root=new TreeNode(rootVal);
        if(preS==preE) return root;
        int p=postS;
        for(int i=postS; i<=postE-1; i++){
            if(pre[preS+1]==post[i]) {
                p=i;
                break;
            }
        }

        int lPostS=postS;
        int lPostE=p;
        int rPostS=p+1;
        int rPostE=postE-1;
        int lPreS=preS+1;
        int lPreE=lPostE-lPostS+preS+1;
        int rPreS=lPreE+1;
        int rPreE=preE;
        root.left=f(pre, post, lPreS, lPreE, lPostS, lPostE);
        root.right=f(pre, post, rPreS, rPreE, rPostS, rPostE);
        return root;
    }
    public TreeNode constructFromPrePost_4(int[] preorder, int[] postorder) {
        return f(preorder, postorder, 0, preorder.length-1, 0, preorder.length-1);
    }

    // V5
    // https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/solutions/1647023/concise-java-solution-with-explanation/
    /**
     * Explanation - We know that first element in pre array is the root. We also know that second element in the pre array is the root of the left subtree. Now if we can find this second element in the post array then all elements before it and including it would belong to the left sub tree and all elements after it (excluding the last element which is the root) will be the elements in the right subtree. We can apply this logic recursively while processing each element in the pre array and build the tree. We use a hashmap for fast lookups in the post array.
     */
    private Map<Integer,Integer> postMap = new HashMap<>();
    private int preIdx;

    public TreeNode constructFromPrePost_5(int[] pre, int[] post) {
        IntStream.range(0, post.length).forEach(i -> postMap.put(post[i], i));
        return buildTree(0, post.length - 1, pre, post);
    }

    private TreeNode buildTree(int lo, int hi, int[] pre, int[] post) {
        if (preIdx == pre.length || lo > hi) return null;

        TreeNode root = new TreeNode(pre[preIdx++]);
        if (lo == hi) return root;

        int leftSubRootPostIdx = postMap.get(pre[preIdx]);

        root.left = buildTree(lo, leftSubRootPostIdx, pre, post);
        root.right = buildTree(leftSubRootPostIdx + 1, hi - 1, pre, post);
        return root;
    }

}
