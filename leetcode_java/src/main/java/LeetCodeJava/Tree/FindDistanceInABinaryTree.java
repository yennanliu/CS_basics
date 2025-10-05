package LeetCodeJava.Tree;

// https://leetcode.com/problems/find-distance-in-a-binary-tree/description/
// https://leetcode.ca/all/1740.html

import LeetCodeJava.DataStructure.TreeNode;

/**
 * 1740. Find Distance in a Binary Tree
 * Given the root of a binary tree and two integers p and q, return the distance between the nodes of value p and value q in the tree.
 *
 * The distance between two nodes is the number of edges on the path from one to the other.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 0
 * Output: 3
 * Explanation: There are 3 edges between 5 and 0: 5-3-1-0.
 * Example 2:
 *
 *
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 7
 * Output: 2
 * Explanation: There are 2 edges between 5 and 7: 5-2-7.
 * Example 3:
 *
 *
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 5
 * Output: 0
 * Explanation: The distance between a node and itself is 0.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 104].
 * 0 <= Node.val <= 109
 * All Node.val are unique.
 * p and q are values in the tree.
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon
 */
public class FindDistanceInABinaryTree {

    // V0

    // V1-1
    // IDEA: alternative: hashmap to store distance of every node-pair during finding LCA, then just map look up
    // https://leetcode.ca/2021-03-23-1740-Find-Distance-in-a-Binary-Tree/
    public int findDistance_1_1(TreeNode root, int p, int q) {
        if (p == q)
            return 0;
        TreeNode ancestor = lowestCommonAncestor(root, p, q);
        return getDistance(ancestor, p) + getDistance(ancestor, q);
    }

    public TreeNode lowestCommonAncestor(TreeNode root, int p, int q) {
        if (root == null)
            return null;
        if (root.val == p || root.val == q)
            return root;
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if (left != null && right != null)
            return root;
        return left == null ? right : left;
    }

    public int getDistance(TreeNode node, int val) {
        if (node == null)
            return -1;
        if (node.val == val)
            return 0;
        int leftDistance = getDistance(node.left, val);
        int rightDistance = getDistance(node.right, val);
        int subDistance = Math.max(leftDistance, rightDistance);
        return subDistance >= 0 ? subDistance + 1 : -1;
    }

    // V1-2
    // IDEA: DFS
    // https://leetcode.ca/2021-03-23-1740-Find-Distance-in-a-Binary-Tree/
    public int findDistance_1_2(TreeNode root, int p, int q) {
        TreeNode g = lca(root, p, q);
        return dfs(g, p) + dfs(g, q);
    }

    private int dfs(TreeNode root, int v) {
        if (root == null) {
            return -1;
        }
        if (root.val == v) {
            return 0;
        }
        int left = dfs(root.left, v);
        int right = dfs(root.right, v);
        if (left == -1 && right == -1) {
            return -1;
        }
        return 1 + Math.max(left, right);
    }

    private TreeNode lca(TreeNode root, int p, int q) {
        if (root == null || root.val == p || root.val == q) {
            return root;
        }
        TreeNode left = lca(root.left, p, q);
        TreeNode right = lca(root.right, p, q);
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }
        return root;
    }

    // V2

    // V3



}
