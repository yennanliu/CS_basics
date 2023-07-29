package LeetCodeJava.Recursion;

// https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

public class ConstructBinaryTreeFromPreorderAndInorderTraversal {

//    // V0
//    // IDEA : DFS
//    TreeNode root = new TreeNode();
//    public TreeNode buildTree(int[] preorder, int[] inorder) {
//
//        if (preorder == null && inorder == null){
//            return null;
//        }
//
//        if (preorder.length == 1 && inorder.length == 1){
//            return new TreeNode(preorder[0]);
//        }
//
//        // help func
//        //this._buildTree(preorder, inorder);
//        return this.root;
//    }


//    private TreeNode  _buildTree(int[] preOrder, int[] inOrder){
//
//        //Arrays.copyOfRange(preOrder, 2, 3);
//        //if (preOrder == null)
//        if (preOrder.equals(inOrder)){
//            return null;
//        }
//
//        if (preOrder.equals(null) || inOrder.equals(null)){
//            return null;
//        }
//        int _root = preOrder[0];
//        int _left = inOrder[0];
//        int idxRightPreorder = Arrays.asList(preOrder).indexOf(_left)+1;
//        int idxRightInorder = Arrays.asList(inOrder).indexOf(_root)+1;
//
//        this.resNode = new TreeNode(_root);
//        this.resNode.left = new TreeNode(_left);
//        this.resNode.left = this._buildTree(
//                Arrays.copyOfRange(preOrder, idxRightPreorder, preOrder.length-1),
//                Arrays.copyOfRange(inOrder, idxRightInorder, inOrder.length-1)
//        );
//
//        return null;
//    }

    // V1
    // IDEA : The recursive structure in a Tree.
    // https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/editorial/
    int preorderIndex;
    Map<Integer, Integer> inorderIndexMap;
    public TreeNode buildTree_2(int[] preorder, int[] inorder) {
        preorderIndex = 0;
        // build a hashmap to store value -> its index relations
        inorderIndexMap = new HashMap<>();
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
