package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/search-in-a-binary-search-tree/
/**
 * 700. Search in a Binary Search Tree
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * You are given the root of a binary search tree (BST) and an integer val.
 *
 * Find the node in the BST that the node's value equals val and return the subtree rooted with that node. If such a node does not exist, return null.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [4,2,7,1,3], val = 2
 * Output: [2,1,3]
 * Example 2:
 *
 *
 * Input: root = [4,2,7,1,3], val = 5
 * Output: []
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 5000].
 * 1 <= Node.val <= 107
 * root is a binary search tree.
 * 1 <= val <= 107
 *
 */
import LeetCodeJava.DataStructure.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

public class SearchInABinarySearchTree {

    // V0
    // IDEA: DFS + BST PROPERTY
    /**
     * time = O(H)
     * space = O(H)
     */

    public TreeNode searchBST(TreeNode root, int val) {
        // edge
        if (root == null) {
            return null;
        }

        if (root.val == val) {
            return root;
        }

        if (root.val < val) {
            return searchBST(root.right, val);
        }

        return searchBST(root.left, val);
    }

    // V0-1
    // IDEA: ITERATIVE + BST PROPERTY
    /**
     * time = O(H)
     * space = O(1)
     */

    public TreeNode searchBST_0_1(TreeNode root, int val) {
        // edge
        if (root == null) {
            return null;
        }

        while (root != null) {
            if (root.val == val) {
                return root;
            } else if (root.val < val) {
                root = root.right;
            } else {
                root = root.left;
            }
        }

        return null;
    }

    // V0-2
    // IDEA : RECURSION
    /**
     * time = O(H)
     * space = O(H)
     */

    public TreeNode searchBST_0_2(TreeNode root, int val) {

        if (root.left == null && root.right == null){
            return new TreeNode();
        }

        // DFS
        return _help(root, val);
    }

    private TreeNode _help(TreeNode root, int val){

        if (root == null){
            return root;
        }

        if (root.val == val){
            return root;
        }

        /** NOTE !!!
         *
         *  we have if-else logic to either
         *  return dfs call on left child node or right child node
         *
         *  -> but NOT return root as final result.
         *  e.g. below is WRONG:
         *
         *     ```
         *
         *     _help(root.right, val);
         *     _help(root.left, val);
         *
         *     // ....
         *     return root
         *     ```
         *
         */
        if(root.val < val){
            return _help(root.right, val);
        }else{
            return _help(root.left, val);
        }
    }

    // V0-3
    // IDEA: BFS
    /**
     * time = O(N)
     * space = O(N)
     */

    public TreeNode searchBST_0_3(TreeNode root, int val) {
        // edge
        if(root == null){
            return null;
        }
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while(!q.isEmpty()){
            TreeNode cur = q.poll();
            if(cur.val == val){
                return cur;
            }
            if(cur.left != null){
                q.add(cur.left);
            }
            if(cur.right != null){
                q.add(cur.right);
            }
        }

        return null;
    }

    // V0-4
    // IDEA: PURE DFS (without use BST property)
    /**
     * time = O(N)
     * space = O(H)
     */

    public TreeNode searchBST_0_4(TreeNode root, int val) {
        // edge
        if (root == null) {
            return null;
        }
        return dfsCheck(root, val);
    }

    private TreeNode dfsCheck(TreeNode root, int val) {
        // edge
        if (root == null) {
            return null;
        }
        //System.out.println(">>> root val = " + root.val);
        // dfs
        if (root.val == val) {
            return root;
        }

        TreeNode _left = dfsCheck(root.left, val);
        TreeNode _right = dfsCheck(root.right, val);

        /** NOTE !!! below condition
         *
         *
         *  we either return left if left is NOT null
         *  and right if right is NOT null
         */
        return _left == null ? _right : _left;
    }

    // V0-5
    // IDEA: DFS + BST (fixed by gpt)
    /**
     * time = O(H)
     * space = O(H)
     */

    public TreeNode searchBST_0_5(TreeNode root, int val) {
        if (root == null) {
            return null;
        }

        if (root.val == val) {
            return root;
        }

        /** NOTE !!!
         *
         *  we have if-else logic to either
         *  return dfs call on left child node or right child node
         *
         *  -> but NOT return root as final result.
         *  e.g. below is WRONG:
         *
         *     ```
         *
         *     _help(root.right, val);
         *     _help(root.left, val);
         *
         *     // ....
         *     return root
         *     ```
         *
         */
        if (val < root.val) {
            return searchBST_0_5(root.left, val);
        } else {
            return searchBST_0_5(root.right, val);
        }
    }

    // V1
    // IDEA : RECURSION
    // https://leetcode.com/problems/search-in-a-binary-search-tree/editorial/
    /**
     * time = O(H)
     * space = O(H)
     */

    public TreeNode searchBST_1(TreeNode root, int val) {
        if (root == null || val == root.val) return root;

        return val < root.val ? searchBST_1(root.left, val) : searchBST_1(root.right, val);
    }


    // V2
    // IDEA : ITERATION
    // https://leetcode.com/problems/search-in-a-binary-search-tree/editorial/
    /**
     * time = O(H)
     * space = O(1)
     */

    public TreeNode searchBST_2(TreeNode root, int val) {
        while (root != null && val != root.val)
            root = val < root.val ? root.left : root.right;
        return root;
    }


}
