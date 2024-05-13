package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  LCA :
 *  “The lowest common ancestor is
 *  defined between two nodes p and q as the lowest node
 *  in T that has both p and q as descendants
 *  (where we allow a node to be a descendant of itself).”
 *  
 */

/** NOTE !!!
 *
 *  Binary Search Tree (BST) property :
 *      -> For each node
 *          - `left sub node < than current node`
 *          - `right sub node > than current node`
 *
 *
 * (From LC)
 * Lets review properties of a BST:
 *
 *      - Left subtree of a node N contains nodes whose values are lesser than or equal to node N's value.
 *      - Right subtree of a node N contains nodes whose values are greater than node N's value.
 *      - Both left and right subtrees are also BSTs.
 *
 */

public class LowestCommonAncestorOfABinarySearchTree {

    class Solution {

        // V0
        // IDEA : RECURSIVE + BST PROPERTY
        public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {

            // if root equals p or q, return root as LCA
            if (root.equals(p) || root.equals(q)) {
                return root;
            }

            /** NOTE !!! BST property : right > root > left */
            // search on right sub tree
            if (p.val > root.val && q.val > root.val){
                return this.lowestCommonAncestor(root.right, p, q);
            }
            // search on left sub tree
            if (p.val < root.val && q.val < root.val){
                return this.lowestCommonAncestor(root.left, p, q);
            }

            // p, q are in different side (sub tree), then return root as LCA directly
            return root;
        }

        // V0'
        // IDEA : RECURSIVE + BST PROPERTY
        public TreeNode lowestCommonAncestor_0(TreeNode root, TreeNode p, TreeNode q) {

            // below logic is optional
            if (root == null || root == p || root == q){
                return root;
            }

            // BST property : right child always > root val
            if (root.val < p.val && root.val < q.val){
                /** NOTE !!! we need to return recursive call below
                 *
                 *   -> e.g. return lowestCommonAncestor(root.right, p, q) instead of lowestCommonAncestor(root.right, p, q)
                 */
                return lowestCommonAncestor_0(root.right, p, q);
            // BST property : left child always < root val
            }else if (root.val > p.val && root.val > q.val) {
                /** NOTE !!! we need to return recursive call below
                 *
                 *   -> e.g. return lowestCommonAncestor(root.right, p, q) instead of lowestCommonAncestor(root.right, p, q)
                 */
                return lowestCommonAncestor_0(root.left, p, q);
            }else{
                return root;
            }
        }

        // V1
        // IDEA : Recursive Approach
        // https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/editorial/
        public TreeNode lowestCommonAncestor_2(TreeNode root, TreeNode p, TreeNode q) {

            // Value of current node or parent node.
            int parentVal = root.val;

            // Value of p
            int pVal = p.val;

            // Value of q;
            int qVal = q.val;

            if (pVal > parentVal && qVal > parentVal) {
                // If both p and q are greater than parent
                return lowestCommonAncestor_2(root.right, p, q);
            } else if (pVal < parentVal && qVal < parentVal) {
                // If both p and q are lesser than parent
                return lowestCommonAncestor_2(root.left, p, q);
            } else {
                // We have found the split point, i.e. the LCA node.
                return root;
            }
        }


        // V2
        // IDEA : Iterative Approach
        // https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/editorial/
        public TreeNode lowestCommonAncestor_3(TreeNode root, TreeNode p, TreeNode q) {

            // Value of p
            int pVal = p.val;

            // Value of q;
            int qVal = q.val;

            // Start from the root node of the tree
            TreeNode node = root;

            // Traverse the tree
            while (node != null) {

                // Value of ancestor/parent node.
                int parentVal = node.val;

                if (pVal > parentVal && qVal > parentVal) {
                    // If both p and q are greater than parent
                    node = node.right;
                } else if (pVal < parentVal && qVal < parentVal) {
                    // If both p and q are lesser than parent
                    node = node.left;
                } else {
                    // We have found the split point, i.e. the LCA node.
                    return node;
                }
            }
            return null;
        }

    }

}
