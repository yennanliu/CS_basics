package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/

import LeetCodeJava.DataStructure.TreeNode;

/**
 * 235. Lowest Common Ancestor of a Binary Search Tree
 * Solved
 * Medium
 * Topics
 * Companies
 * Given a binary search tree (BST), find the lowest common ancestor (LCA) node of two given nodes in the BST.
 *
 * According to the definition of LCA on Wikipedia: “The lowest common ancestor is defined between two nodes p and q as the lowest node in T that has both p and q as descendants (where we allow a node to be a descendant of itself).”
 *
 * Example 1:
 * Input: root = [6,2,8,0,4,7,9,null,null,3,5], p = 2, q = 8
 * Output: 6
 * Explanation: The LCA of nodes 2 and 8 is 6.
 *
 * Example 2:
 * Input: root = [6,2,8,0,4,7,9,null,null,3,5], p = 2, q = 4
 * Output: 2
 * Explanation: The LCA of nodes 2 and 4 is 2, since a node can be a descendant of itself according to the LCA definition.
 *
 * Example 3:
 * Input: root = [2,1], p = 2, q = 1
 * Output: 2
 *
 * Constraints:
 * The number of nodes in the tree is in the range [2, 105].
 * -109 <= Node.val <= 109
 * All Node.val are unique.
 * p != q
 * p and q will exist in the BST.
 */

/**
 * LCA :
 * “The lowest common ancestor is
 * defined between two nodes p and q as the lowest node
 * in T that has both p and q as descendants
 * (where we allow a node to be a descendant of itself).”
 */

/**
 * NOTE !!!
 *
 * Binary Search Tree (BST) property :
 * -> For each node
 * - `left sub node < than current node`
 * - `right sub node > than current node`
 *
 * (From LC)
 * Lets review properties of a BST:
 * - Left subtree of a node N contains nodes whose values are lesser than or equal to node N's value.
 * - Right subtree of a node N contains nodes whose values are greater than node N's value.
 * - Both left and right subtrees are also BSTs.
 */
public class LowestCommonAncestorOfABinarySearchTree {

    // V0
    // IDEA: BST PROPERTY + DFS
    /**
     *  Complexity:Time: $O(H)$ where $H$ is the height of the tree
     *  ($O(\log N)$ average, $O(N)$ worst).
     *
     *  Space: $O(H)$ due to the recursion stack.
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // 1. Base case
        if (root == null) {
            return null;
        }

        // 2. Determine directions: 1 for Right, -1 for Left, 0 for Match
        // Using Integer.compare or simple if-else to handle the 0 case
        int res1 = (p.val > root.val) ? 1 : (p.val < root.val ? -1 : 0);
        int res2 = (q.val > root.val) ? 1 : (q.val < root.val ? -1 : 0);

        // 3. If they are on different sides (one -1, one 1)
        // OR if one of them IS the current root (one of them is 0)
        // The product will be <= 0.
        if (res1 * res2 <= 0) {
            return root;
        }

        // 4. If both are on the left side (both are -1)
        if (res1 < 0) {
            return lowestCommonAncestor(root.left, p, q);
        }

        // 5. Otherwise, both must be on the right side (both are 1)
        return lowestCommonAncestor(root.right, p, q);
    }


    // V0-0-1
    // IDEA 1) BST PROPERTY + DFS (GPT)
    // Average time: O(log N)
    // Worst time: O(N)
    // Space: O(H)
    public TreeNode lowestCommonAncestor_0_0_1(TreeNode root, TreeNode p, TreeNode q) {

        // edge
        if (root == null) {
            return null;
        }

        // if root equals p or q
        if (root.val == p.val || root.val == q.val) {
            return root;
        }

        // both nodes in left subtree
        if (p.val < root.val && q.val < root.val) {
            return lowestCommonAncestor_0_0_1(root.left, p, q);
        }

        // both nodes in right subtree
        if (p.val > root.val && q.val > root.val) {
            return lowestCommonAncestor_0_0_1(root.right, p, q);
        }

        // split point → this is LCA
        return root;
    }


    // V0-1
    /**
     * time = O(H)
     * space = O(H)
     */
    public TreeNode lowestCommonAncestor_0_1(TreeNode root, TreeNode p, TreeNode q) {

        // below logic is optional
        if (root == null || root == p || root == q) {
            return root;
        }

        // BST property : right child always > root val
        if (root.val < p.val && root.val < q.val) {
            /**
             * NOTE !!! we need to return recursive call below
             * -> e.g. return lowestCommonAncestor(root.right, p, q) instead of
             * lowestCommonAncestor(root.right, p, q)
             */
            return lowestCommonAncestor_0_1(root.right, p, q);
            // BST property : left child always < root val
        } else if (root.val > p.val && root.val > q.val) {
            /**
             * NOTE !!! we need to return recursive call below
             * -> e.g. return lowestCommonAncestor(root.right, p, q) instead of
             * lowestCommonAncestor(root.right, p, q)
             */
            return lowestCommonAncestor_0_1(root.left, p, q);
        } else {
            return root;
        }
    }

    // V0-2
    // IDEA: RECURSIVE + BST property (GPT)
    /**
     * time = O(H)
     * space = O(H)
     */
    public TreeNode lowestCommonAncestor_0_2(TreeNode root, TreeNode p, TreeNode q) {
        // Base case: if root is null, return null
        if (root == null) {
            return null;
        }

        // If root matches either p or q, root is the LCA
        if (root.val == p.val || root.val == q.val) {
            return root;
        }

        // Recursively search in left and right subtrees
        /**
         * NOTE !!!
         * get sub left node's LCA, and sub right node's LCA
         */
        TreeNode left = lowestCommonAncestor_0_2(root.left, p, q);
        TreeNode right = lowestCommonAncestor_0_2(root.right, p, q);

        // If p and q are on different sides of root, root is the LCA
        if (left != null && right != null) {
            return root;
        }

        // If one side is null, return the non-null side
        return (left != null) ? left : right;
    }

    // V0-3
    // IDEA: DFS (gpt)
    /**
     * time = O(H)
     * space = O(H)
     */
    public TreeNode lowestCommonAncestor_0_3(TreeNode root, TreeNode p, TreeNode q) {
        // Base case
        if (root == null)
            return null;

        // If both p and q are smaller than root, LCA is in left subtree
        if (p.val < root.val && q.val < root.val) {
            return lowestCommonAncestor_0_3(root.left, p, q);
        }

        // If both p and q are greater than root, LCA is in right subtree
        if (p.val > root.val && q.val > root.val) {
            return lowestCommonAncestor_0_3(root.right, p, q);
        }

        // Else, root is the split point => LCA
        return root;
    }

    // V1
    // IDEA : Recursive Approach
    // https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/editorial/
    /**
     * time = O(H)
     * space = O(H)
     */
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
    /**
     * time = O(H)
     * space = O(1)
     */
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