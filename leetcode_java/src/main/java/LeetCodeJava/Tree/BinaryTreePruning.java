package LeetCodeJava.Tree;

// https://leetcode.com/problems/binary-tree-pruning/description/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  814. Binary Tree Pruning
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given the root of a binary tree, return the same tree where every subtree (of the given tree) not containing a 1 has been removed.
 *
 * A subtree of a node node is node plus every node that is a descendant of node.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [1,null,0,0,1]
 * Output: [1,null,0,null,1]
 * Explanation:
 * Only the red nodes satisfy the property "every subtree not containing a 1".
 * The diagram on the right represents the answer.
 * Example 2:
 *
 *
 * Input: root = [1,0,1,0,0,0,1]
 * Output: [1,null,1,null,1]
 * Example 3:
 *
 *
 * Input: root = [1,1,0,1,1,0,1,0]
 * Output: [1,1,0,1,1,null,1]
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 200].
 * Node.val is either 0 or 1.
 *
 *
 */
public class BinaryTreePruning {

    // V0
    // IDEA: POST ORDER DFS (fixed by gemini)
    public TreeNode pruneTree(TreeNode root) {
        if (root == null) {
            return null;
        }

        // 1. Recurse down to the children first (Post-order)
        root.left = pruneTree(root.left);
        root.right = pruneTree(root.right);

        // 2. After children are processed, check if this node should be pruned
        // If left is null, right is null, and current value is 0 -> prune this node
        if (root.left == null && root.right == null && root.val == 0) {
            return null; // Returning null "prunes" this node from its parent
        }

        // 3. Otherwise, keep this node
        return root;
    }

    // V0-0-1
    // IDEA: POST ORDER DFS (fixed by GPT)
    public TreeNode pruneTree_0_0_1(TreeNode root) {

        return deleteHelper(root);
    }

    // return pruned subtree
    private TreeNode deleteHelper(TreeNode root) {

        if (root == null) {
            return null;
        }

        // prune left & right first (post-order)
        root.left = deleteHelper(root.left);
        root.right = deleteHelper(root.right);

        // if current node should be deleted
        if (root.val == 0 && root.left == null && root.right == null) {
            return null;
        }

        return root;
    }


    // V0-1
    // IDEA: DFS (gemini)
    public TreeNode pruneTree_0_1(TreeNode root) {
        // 1. Base Case: if the node is null, we return null.
        if (root == null) {
            return null;
        }

        /** NOTE !!! post order DFS traverse  (left -> right -> root) */
        // 2. Process children first (Bottom-Up / Post-Order)
        // We assign the result of the recursion back to the left/right pointers.
        root.left = pruneTree_0_1(root.left);
        root.right = pruneTree_0_1(root.right);

        /**  NOTE !!!
         *
         *   the condition whether you should prune a node
         *
         *        // - Its value is 0
         *        // - AND it has no left child (either originally or just pruned)
         *        // - AND it has no right child (either originally or just pruned)
         */
        // 3. Decide if the current node should be pruned:
        // We prune this node (return null) IF:
        // - Its value is 0
        // - AND it has no left child (either originally or just pruned)
        // - AND it has no right child (either originally or just pruned)
        if (root.val == 0 && root.left == null && root.right == null) {
            return null;
        }

        // 4. If it's a 1, or it's a 0 that still has a 1-containing child, keep it.
        return root;
    }


    // V0-2
    // IDEA: DFS (gpt)
    public TreeNode pruneTree_0_2(TreeNode root) {

        if (root == null) {
            return null;
        }

        return helper2(root);
    }

    private TreeNode helper2(TreeNode root) {

        if (root == null) {
            return null;
        }


        /** NOTE !!! post order DFS traverse  (left -> right -> root) */
        TreeNode _left = helper2(root.left);
        TreeNode _right = helper2(root.right);

        root.left = _left;
        root.right = _right;

        /**  NOTE !!!
         *
         *   the condition whether you should prune a node
         *
         *        // - Its value is 0
         *        // - AND it has no left child (either originally or just pruned)
         *        // - AND it has no right child (either originally or just pruned)
         */
        // prune if subtree has no 1
        if (root.val == 0 && _left == null && _right == null) {
            return null;
        }

        return root;
    }



    // V1
    // https://leetcode.com/problems/binary-tree-pruning/solutions/7520786/simple-postorder-dfs-approach-by-gopal_v-2l8i/
    public TreeNode pruneTree_1(TreeNode root) {
        return prune(root);
    }

    public TreeNode prune(TreeNode root) {
        if (root == null) {
            return null;
        }
        root.left = prune(root.left);
        root.right = prune(root.right);
        // if i am a leaf node and i am 0 then i am not needed LMAO DEAD!!!
        if (root.left == null && root.right == null && root.val == 0) {
            return null;
        }
        return root;
    }


    // V2
    // https://leetcode.com/problems/binary-tree-pruning/solutions/7565495/this-is-easy-approach-to-solve-this-by-m-ssom/
    public TreeNode pruneTree_2(TreeNode root) {
        if (root == null) {
            return null;
        }

        root.left = pruneTree_2(root.left);
        root.right = pruneTree_2(root.right);

        if (root.left == null && root.right == null && root.val == 0) {
            return null;
        }

        return root;
    }



    // V3
    



}
