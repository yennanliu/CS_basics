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
//    public TreeNode pruneTree(TreeNode root) {
//
//    }


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
