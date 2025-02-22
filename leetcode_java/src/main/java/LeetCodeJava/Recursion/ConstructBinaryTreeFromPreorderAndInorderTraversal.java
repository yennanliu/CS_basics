package LeetCodeJava.Recursion;

// https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
/**
 * 105. Construct Binary Tree from Preorder and Inorder Traversal
 * Solved
 * Medium
 * Topics
 * Companies
 * Given two integer arrays preorder and inorder where preorder is the preorder traversal of a binary tree and inorder is the inorder traversal of the same tree, construct and return the binary tree.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: preorder = [3,9,20,15,7], inorder = [9,3,15,20,7]
 * Output: [3,9,20,null,null,15,7]
 * Example 2:
 *
 * Input: preorder = [-1], inorder = [-1]
 * Output: [-1]
 *
 *
 * Constraints:
 *
 * 1 <= preorder.length <= 3000
 * inorder.length == preorder.length
 * -3000 <= preorder[i], inorder[i] <= 3000
 * preorder and inorder consist of unique values.
 * Each value of inorder also appears in preorder.
 * preorder is guaranteed to be the preorder traversal of the tree.
 * inorder is guaranteed to be the inorder traversal of the tree.
 *
 */
import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

public class ConstructBinaryTreeFromPreorderAndInorderTraversal {

    // V0
    // IDEA : RECURSIVE + PRE-ORDER, IN-ORDER PROPERTY
    // https://www.youtube.com/watch?v=ihj4IQGZ2zc
    public TreeNode buildTree(int[] preorder, int[] inorder) {

        if (preorder.length == 0) {
            return null;
        }

        if (preorder.length == 1) {
            return new TreeNode(preorder[0]);
        }

        // setup root
        /** NOTE !!!
         *
         *  we init root as below (within the recursion call),
         *  so we can keep `binding` (e.g. root.left = ..., root.right = ...) to their sub tree
         */
        TreeNode root = new TreeNode(preorder[0]);
        // get root idx
        int root_idx = 0;
        for (int i = 0; i < inorder.length; i++) {
            if (inorder[i] == root.val) {
                root_idx = i;
                break;
            }
        }
        /** NOTE !!! copy sub array syntax : Arrays.copyOfRange */
        /**
         *  NOTE !!!
         *
         *  1) we get root_idx from "inorder" to split tree
         *  2) root_idx will be used as "length" (root - sub left tree)
         *  3) root_idx will be used on both "preorder" and "inorder"
         *
         *  NOTE !!!
         *
         *   root_idx is a `radius` of tree actually
         *   e.g.
         *            8
         *        1        9
         *        <--->
         *       root_idx
         *
         *  4) for "preorder",
         *
         *      left sub tree : Arrays.copyOfRange(preorder, 1, root_idx + 1)
         *          -> left sub tree start from idx = 1
         *          -> left sub tree end at root_idx + 1 (root_idx is "length" of sub tree)
         *
         *      right sub tree: Arrays.copyOfRange(preorder, 1 + root_idx, preorder.length)
         *          -> right sub tree start from 1 + root_idx (root_idx is "length" of sub tree)
         *          -> right sub tree end at end of rest of preorder
         *
         */
        root.left = buildTree(
                Arrays.copyOfRange(preorder, 1, root_idx + 1),
                Arrays.copyOfRange(inorder, 0, root_idx)
        );

        root.right = buildTree(
                Arrays.copyOfRange(preorder, root_idx + 1, preorder.length),
                Arrays.copyOfRange(inorder, root_idx + 1, inorder.length)
        );

        return root;
    }

    // V0-1
    // IDEA : RECURSION (DFS) (transform below py code to java by GPT)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Recursion/construct-binary-tree-from-preorder-and-inorder-traversal.py#L36
    public TreeNode buildTree_0_1(int[] preorder, int[] inorder) {
        if (preorder.length == 0) {
            return null;
        }
        if (preorder.length == 1) {
            return new TreeNode(preorder[0]);
        }
        TreeNode root = new TreeNode(preorder[0]);
        int index = 0;
        for (int i = 0; i < inorder.length; i++) {
            if (inorder[i] == root.val) {
                index = i;
                break;
            }
        }
        root.left = buildTree_0_1(Arrays.copyOfRange(preorder, 1, index + 1), Arrays.copyOfRange(inorder, 0, index));
        // (below recursion parameter) both preorder, inorder are "start from" idx+1, and "end at" the length of tree array
        root.right = buildTree_0_1(Arrays.copyOfRange(preorder, index + 1, preorder.length), Arrays.copyOfRange(inorder, index + 1, inorder.length));
        return root;
    }

    // V0-2
    // IDEA: RECURSION (GPT)
    public TreeNode buildTree_0_2(int[] preorder, int[] inorder) {
        if (preorder.length == 0 || inorder.length == 0) {
            return null;
        }
        return buildT(preorder, inorder, 0, 0, inorder.length - 1);
    }

    private TreeNode buildT(int[] preorder, int[] inorder, int preStart, int inStart, int inEnd) {
        // Base case: no elements to build the tree
        if (preStart > preorder.length - 1 || inStart > inEnd) {
            return null;
        }

        // The first element of preorder is the root
        int rootVal = preorder[preStart];
        TreeNode root = new TreeNode(rootVal);

        // Find the root in inorder array
        int rootIdxInorder = getValIdx(inorder, rootVal, inStart, inEnd);

        // Recursively build the left and right subtrees
        root.left = buildT(preorder, inorder, preStart + 1, inStart, rootIdxInorder - 1);
        root.right = buildT(preorder, inorder, preStart + rootIdxInorder - inStart + 1, rootIdxInorder + 1, inEnd);

        return root;
    }

    private int getValIdx(int[] inorder, int val, int start, int end) {
        for (int i = start; i <= end; i++) {
            if (inorder[i] == val) {
                return i;
            }
        }
        return -1; // This should never happen if the input is valid
    }

    // V1
    // IDEA : The recursive structure in a Tree.
    // https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/editorial/
    int preorderIndex;
    Map<Integer, Integer> inorderIndexMap;
    public TreeNode buildTree_1(int[] preorder, int[] inorder) {
        preorderIndex = 0;
        // build a hashmap to store value -> its index relations
        inorderIndexMap = new HashMap<>();
        /** NOTE !!! hashmap save Inorder 's  val-index info */
        for (int i = 0; i < inorder.length; i++) {
            inorderIndexMap.put(inorder[i], i);
        }

        return arrayToTree(preorder, 0, preorder.length - 1);
    }

    private TreeNode arrayToTree(int[] preorder, int left, int right) {
        // if there are no elements to construct the tree
        if (left > right) return null;

        // select the preorder_index element as the root and increment it
        int rootValue = preorder[preorderIndex++];
        TreeNode root = new TreeNode(rootValue);

        // build left and right subtree
        // excluding inorderIndexMap[rootValue] element because it's the root
        root.left = arrayToTree(preorder, left, inorderIndexMap.get(rootValue) - 1);
        root.right = arrayToTree(preorder, inorderIndexMap.get(rootValue) + 1, right);
        return root;
    }

    // V2
    // https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/solutions/34541/5ms-java-clean-solution-with-caching/
    public TreeNode buildTree_2(int[] preorder, int[] inorder) {
        Map<Integer, Integer> inMap = new HashMap<Integer, Integer>();

        for(int i = 0; i < inorder.length; i++) {
            inMap.put(inorder[i], i);
        }

        TreeNode root = _buildTree(preorder, 0, preorder.length - 1, inorder, 0, inorder.length - 1, inMap);
        return root;
    }

    public TreeNode _buildTree(int[] preorder, int preStart, int preEnd, int[] inorder, int inStart, int inEnd, Map<Integer, Integer> inMap) {
        if(preStart > preEnd || inStart > inEnd) return null;

        TreeNode root = new TreeNode(preorder[preStart]);
        int inRoot = inMap.get(root.val);
        int numsLeft = inRoot - inStart;

        root.left = _buildTree(preorder, preStart + 1, preStart + numsLeft, inorder, inStart, inRoot - 1, inMap);
        root.right = _buildTree(preorder, preStart + numsLeft + 1, preEnd, inorder, inRoot + 1, inEnd, inMap);

        return root;
    }

    // V3
    // https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/solutions/1258712/js-python-java-c-easy-recursive-solution-w-explanation/
    public TreeNode buildTree_3(int[] P, int[] I) {
        Map<Integer, Integer> M = new HashMap<>();
        for (int i = 0; i < I.length; i++)
            M.put(I[i], i);
        return splitTree(P, M, 0, 0, I.length-1);
    }

    private TreeNode splitTree(int[] P, Map<Integer, Integer> M, int pix, int ileft, int iright) {
        int rval = P[pix], imid = M.get(rval);
        TreeNode root = new TreeNode(rval);
        if (imid > ileft)
            root.left = splitTree(P, M, pix+1, ileft, imid-1);
        if (imid < iright)
            root.right = splitTree(P, M, pix+imid-ileft+1, imid+1, iright);
        return root;
    }

    // V4
    private Map<Integer, Integer> inorderIndexMap_;
    private int preorderIndex_;

    public TreeNode buildTree_4(int[] preorder, int[] inorder) {
        if (preorder == null || preorder.length == 0)
            return null;

        // Build a HashMap for quick index lookup
        inorderIndexMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            inorderIndexMap.put(inorder[i], i);
        }

        preorderIndex_ = 0;
        return dfsBuildTree(preorder, 0, inorder.length - 1);
    }

    private TreeNode dfsBuildTree(int[] preorder, int left, int right) {
        if (left > right)
            return null;

        // Pick the current root from preorder
        int rootValue = preorder[preorderIndex_++];
        TreeNode root = new TreeNode(rootValue);

        // Find the root index in inorder using HashMap (O(1) lookup)
        int inorderIndex = inorderIndexMap.get(rootValue);

        // Build left and right subtrees
        root.left = dfsBuildTree(preorder, left, inorderIndex - 1);
        root.right = dfsBuildTree(preorder, inorderIndex + 1, right);

        return root;
    }

}
