package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-ii/description/
// https://leetcode.ca/all/1644.html

import LeetCodeJava.DataStructure.TreeNode;

import java.util.HashSet;
import java.util.Set;

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
    // Commented out - no implementation
//    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
//    }

    // V0-0-1
    // IDEA: DFS + LC 236 (fixed by gpt)
    // TODO: validate
    /**
     *
     *  IDEA:
     *
     *  1. check if p or q is null via `collect all nodes` and check existence
     *  2. LC 236 : find LCA on binary tree
     */
    Set<Integer> nodeSet = new HashSet<>();

    /**

     * time = O(N)

     * space = O(N)
     */


    public TreeNode lowestCommonAncestor_0_0_1(TreeNode root, TreeNode p, TreeNode q) {
        // Collect all node values
        collectNodes(root);

        // Check existence
        if (!nodeSet.contains(p.val) || !nodeSet.contains(q.val)) {
            return null;
        }

        // Find LCA
        return findLCA_0(root, p, q);
    }

    private void collectNodes(TreeNode root) {
        if (root == null) return;
        nodeSet.add(root.val);
        collectNodes(root.left);
        collectNodes(root.right);
    }

    private TreeNode findLCA_0(TreeNode root, TreeNode p, TreeNode q) {

        /**
         *  NOTE !!!
         *
         *  // -> so, we will return right away if `root equals p` or `root equals q`
         */
        if (root == null || root == p || root == q) {
            return root;
        }

        TreeNode left = findLCA_0(root.left, p, q);
        TreeNode right = findLCA_0(root.right, p, q);

        /**
         *  NOTE !!!
         *
         *   if `both left is NOT null and right is NOT null`
         */
        if (left != null && right != null) {
            return root;
        }

        return left != null ? left : right;
    }

    // V0-1
    // IDEA: DFS (gpt)
    // TODO: validate
    private boolean foundP = false;
    private boolean foundQ = false;

    /**

     * time = O(N)

     * space = O(H)
     */


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

    // V0-2
    // IDEA: DFS (gpt)
    /**
     * time = O(N)
     * space = O(N)
     */

    public TreeNode lowestCommonAncestor_0_2(TreeNode root, TreeNode p, TreeNode q) {
        // Start the DFS recursion to find the LCA
        TreeNode[] result = new TreeNode[1];  // We use an array to return the result by reference
        if (dfs(root, p, q, result)) {
            return result[0];
        }
        return null;  // If we can't find both p and q
    }

    private boolean dfs(TreeNode node, TreeNode p, TreeNode q, TreeNode[] result) {
        if (node == null) return false;

        // Check if current node is p or q
        boolean foundInLeft = dfs(node.left, p, q, result);
        boolean foundInRight = dfs(node.right, p, q, result);

        // If this node is p or q, mark as found
        boolean isCurrentNodePOrQ = (node == p || node == q);

        // If this node is LCA
        if (isCurrentNodePOrQ && (foundInLeft || foundInRight)) {
            result[0] = node;  // This is the LCA
            return true;
        }

        // If this is the first node where both left and right subtrees contain p and q
        if (foundInLeft && foundInRight || (isCurrentNodePOrQ && (foundInLeft || foundInRight))) {
            result[0] = node;
            return true;
        }

        // Return true if node is p or q or found any node in left or right subtree
        return isCurrentNodePOrQ || foundInLeft || foundInRight;
    }


    // V1
    // https://leetcode.ca/2020-05-31-1644-Lowest-Common-Ancestor-of-a-Binary-Tree-II/
    public TreeNode ancestor = null;
    public TreeNode p = null, q = null;

    /**

     * time = O(N)

     * space = O(H)
     */


    public TreeNode lowestCommonAncestor_1(TreeNode root, TreeNode p, TreeNode q) {
        findNodes(root, p, q);
        if (p == null || q == null)
            return null;
        depthFirstSearch(root, p, q);
        return ancestor;
    }

    /**

     * time = O(N)

     * space = O(H)
     */


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

    /**

     * time = O(N)

     * space = O(H)
     */


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
