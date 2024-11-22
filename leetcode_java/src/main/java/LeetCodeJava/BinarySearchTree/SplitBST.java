package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/split-bst/description/
// https://leetcode.ca/all/776.html

import LeetCodeJava.DataStructure.TreeNode;

/**
 * 776. Split BST
 * Given a Binary Search Tree (BST) with root node root, and a target value V, split the tree into two subtrees where one subtree has nodes that are all smaller or equal to the target value, while the other subtree has all nodes that are greater than the target value.  It's not necessarily the case that the tree contains a node with value V.
 *
 * Additionally, most of the structure of the original tree should remain.  Formally, for any child C with parent P in the original tree, if they are both in the same subtree after the split, then node C should still have the parent P.
 *
 * You should output the root TreeNode of both subtrees after splitting, in any order.
 *
 * Example 1:
 *
 * Input: root = [4,2,6,1,3,5,7], V = 2
 * Output: [[2,1],[4,3,6,null,null,5,7]]
 * Explanation:
 * Note that root, output[0], and output[1] are TreeNode objects, not arrays.
 *
 * The given tree [4,2,6,1,3,5,7] is represented by the following diagram:
 *
 *           4
 *         /   \
 *       2      6
 *      / \    / \
 *     1   3  5   7
 *
 * while the diagrams for the outputs are:
 *
 *           4
 *         /   \
 *       3      6      and    2
 *             / \           /
 *            5   7         1
 * Note:
 *
 * The size of the BST will not exceed 50.
 * The BST is always valid and each node's value is different.
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Coupang Google
 *
 */
public class SplitBST {

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
//    public TreeNode[] splitBST(TreeNode root, int target) {
//    }

    // V1
    // IDEA : DFS
    // https://leetcode.ca/2018-01-14-776-Split-BST/
    private int t;

    public TreeNode[] splitBST_1(TreeNode root, int target) {
        t = target;
        return dfs(root);
    }

    private TreeNode[] dfs(TreeNode root) {
        if (root == null) {
            return new TreeNode[] {null, null};
        }
        if (root.val <= t) {
            TreeNode[] ans = dfs(root.right);
            root.right = ans[0];
            ans[0] = root;
            return ans;
        } else {
            TreeNode[] ans = dfs(root.left);
            root.left = ans[1];
            ans[1] = root;
            return ans;
        }
    }

    // V2
    // IDEA : DFS (gpt)
    // TODO : validate
    /**
     * Explanation
     *
     * 	1.	Base Case:
     * 	    •	If the root is null, return two null subtrees because there’s nothing to split.
     *
     * 	2.	Recursive Case:
     * 	    •	If the root’s value is  <= V :
     * 	            •	Keep the root as part of the left subtree.
     * 	            •	Recursively split the right child since nodes greater than  V  might be in the right subtree.
     *
     * 	    •	If the root’s value is  > V :
     * 	            •	Keep the root as part of the right subtree.
     * 	            •	Recursively split the left child since nodes  <= V  might be in the left subtree.
     * 	3.	Reassign Children:
     * 	    •	After the recursive split, adjust the child pointers of the root node to maintain the BST structure.
     * 	4.	Result:
     * 	    •	The first element in the result array is the root of the subtree  <= V .
     * 	    •	The second element is the root of the subtree  > V .
     *
     *
     *      Time Complexity
     *
     * 	        •	Each node is visited exactly once, so the time complexity is  O(n) , where  n  is the number of nodes in the BST.
     *
     *      Space Complexity
     *
     * 	        •	The space complexity is  O(h) , where  h  is the height of the tree, due to the recursive stack.
     *
     */
    public TreeNode[] splitBST_2(TreeNode root, int V) {
        // Base case: If the root is null, both subtrees are null
        if (root == null) {
            return new TreeNode[]{null, null};
        }

        // Array to hold the two resulting subtrees
        TreeNode[] result = new TreeNode[2];

        // If the root's value is less than or equal to V
        if (root.val <= V) {
            // Recursively split the right subtree
            TreeNode[] splitRight = splitBST_2(root.right, V);

            // Root becomes the head of the left subtree
            result[0] = root;
            root.right = splitRight[0]; // Reassign the right child to the left subtree
            result[1] = splitRight[1]; // Right subtree from the split becomes the second result
        } else {
            // If root's value is greater than V, recursively split the left subtree
            TreeNode[] splitLeft = splitBST_2(root.left, V);

            // Root becomes the head of the right subtree
            result[1] = root;
            root.left = splitLeft[1]; // Reassign the left child to the right subtree
            result[0] = splitLeft[0]; // Left subtree from the split becomes the first result
        }

        return result;
    }

    // V3
    // https://www.cnblogs.com/grandyang/p/8993143.html


}
