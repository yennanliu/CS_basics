package LeetCodeJava.Recursion;

// https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

public class ConstructBinaryTreeFromPreorderAndInorderTraversal {

    // V0
    public TreeNode buildTree(int[] preorder, int[] inorder) {

        if (preorder.length == 0) {
            return null;
        }

        if (preorder.length == 1) {
            return new TreeNode(preorder[0]);
        }

        // setup root
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

    // V0'
    // IDEA : RECURSION (DFS) (transform below py code to java by GPT)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Recursion/construct-binary-tree-from-preorder-and-inorder-traversal.py#L36
    public TreeNode buildTree_0(int[] preorder, int[] inorder) {
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
        root.left = buildTree_0(Arrays.copyOfRange(preorder, 1, index + 1), Arrays.copyOfRange(inorder, 0, index));
        root.right = buildTree_0(Arrays.copyOfRange(preorder, index + 1, preorder.length), Arrays.copyOfRange(inorder, index + 1, inorder.length));
        return root;
    }

    // V1
    // IDEA : The recursive structure in a Tree.
    // https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/editorial/
    int preorderIndex;
    Map<Integer, Integer> inorderIndexMap;
    public TreeNode buildTree_2(int[] preorder, int[] inorder) {
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
    public TreeNode buildTree_3(int[] preorder, int[] inorder) {
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
    public TreeNode buildTree_4(int[] P, int[] I) {
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

}
