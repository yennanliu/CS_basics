package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-ii/description/
// https://leetcode.ca/all/1644.html

import LeetCodeJava.DataStructure.TreeNode;

/**
 * 1644. Lowest Common Ancestor of a Binary Tree II
 * Given the root of a binary tree, return the lowest common ancestor (LCA) of two given nodes, p and q. If either node p or q does not exist in the tree, return null. All values of the nodes in the tree are unique.
 *
 * According to the definition of LCA on Wikipedia: "The lowest common ancestor of two nodes p and q in a binary tree T is the lowest node that has both p and q as descendants (where we allow a node to be a descendant of itself)". A descendant of a node x is a node y that is on the path from node x to some leaf node.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
 * Output: 3
 * Explanation: The LCA of nodes 5 and 1 is 3.
 * Example 2:
 *
 *
 *
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4
 * Output: 5
 * Explanation: The LCA of nodes 5 and 4 is 5. A node can be a descendant of itself according to the definition of LCA.
 * Example 3:
 *
 *
 *
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 10
 * Output: null
 * Explanation: Node 10 does not exist in the tree, so return null.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 104].
 * -109 <= Node.val <= 109
 * All Node.val are unique.
 * p != q
 *
 *
 * Follow up: Can you find the LCA traversing the tree, without checking nodes existence?
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * LinkedIn Microsoft
 *
 */
public class LowestCommonAncestorOfABinaryTree2 {

    /**
     *  NOTE !!!
     *
     *   tree in this problem is `Binary Tree` (LC 1644),
     *   but NOT `Binary Search Tree` (LC 235),
     *   so we CAN NOT reuse LC 235 code logic in this problem
     */

    // V0
//    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
//    }

    // V0-1
    // IDEA: DFS (gpt)
    // TODO: validate
    private boolean foundP = false;
    private boolean foundQ = false;

    public TreeNode lowestCommonAncestor_0_1(TreeNode root, TreeNode p, TreeNode q) {
        TreeNode lca = findLCA(root, p, q);
        return (foundP && foundQ) ? lca : null;
    }

    private TreeNode findLCA(TreeNode node, TreeNode p, TreeNode q) {
        if (node == null) return null;

        TreeNode left = findLCA(node.left, p, q);
        TreeNode right = findLCA(node.right, p, q);

        if (node == p) {
            foundP = true;
            return node;
        }

        if (node == q) {
            foundQ = true;
            return node;
        }

        if (left != null && right != null) {
            return node;
        }

        return left != null ? left : right;
    }


    // V1
    // https://leetcode.ca/2020-05-31-1644-Lowest-Common-Ancestor-of-a-Binary-Tree-II/
    public TreeNode ancestor = null;
    public TreeNode p = null, q = null;

    public TreeNode lowestCommonAncestor_1(TreeNode root, TreeNode p, TreeNode q) {
        findNodes(root, p, q);
        if (p == null || q == null)
            return null;
        depthFirstSearch(root, p, q);
        return ancestor;
    }

    public void findNodes(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null)
            return;
        if (root == p)
            this.p = p;
        else if (root == q)
            this.q = q;
        findNodes(root.left, p, q);
        findNodes(root.right, p, q);
    }

    public boolean depthFirstSearch(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null)
            return false;
        boolean left = depthFirstSearch(root.left, p, q);
        boolean right = depthFirstSearch(root.right, p, q);
        if (left && right || ((root.val == p.val || root.val == q.val) && (left || right)))
            ancestor = root;
        return left || right || (root.val == p.val || root.val == q.val);
    }

    // followUp
    // 计数找到了p和q的多少个节点
//    private int count = 0;
//
//    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
//        TreeNode res = lca(root, p, q);
//        // 如果都找到了，就可以返回res了，否则说明某个节点不存在，返回null
//        return count == 2 ? res : null;
//    }
//
//    // 功能是返回p与q都存在的情况下的lca
//    private TreeNode lca(TreeNode root, TreeNode p, TreeNode q) {
//        if (root == null) {
//            return null;
//        }
//
//        // 在左右两边找p和q
//        TreeNode left = lca(root.left, p, q), right = lca(root.right, p, q);
//
//        // 如果当前树根就是p或q，那计数加1，并且root就是lca（在p和q都存在于树的情况下）
//        if (root == p || root == q) {
//            count++;
//            return root;
//        }
//
//        // 如果左子树里找不到p和q，那lca就在右边，如果右子树找不到p和q那lca就在左边，
//        // 否则就是左右都找到了，返回当前树根
//        if (left == null) {
//            return right;
//        } else if (right == null) {
//            return left;
//        } else {
//            return root;
//        }
//    }


    // V2

}
