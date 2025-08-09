package LeetCodeJava.Tree;

// https://leetcode.com/problems/change-the-root-of-a-binary-tree/
// https://leetcode.ca/all/1666.html

/**
 * 1666. Change the Root of a Binary Tree
 * Given the root of a binary tree and a leaf node, reroot the tree so that the leaf is the new root.
 *
 * You can reroot the tree with the following steps for each node cur on the path starting from the leaf up to the root​​​ excluding the root:
 *
 * If cur has a left child, then that child becomes cur's right child. Note that it is guaranteed that cur will have at most one child.
 * cur's original parent becomes cur's left child.
 * Return the new root of the rerooted tree.
 *
 * Note: Ensure that your solution sets the Node.parent pointers correctly after rerooting or you will receive "Wrong Answer".
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4], leaf = 7
 * Output: [7,2,null,5,4,3,6,null,null,null,1,null,null,0,8]
 * Example 2:
 *
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4], leaf = 0
 * Output: [0,1,null,3,8,5,null,null,null,6,2,null,null,7,4]
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [2, 100].
 * -109 <= Node.val <= 109
 * All Node.val are unique.
 * leaf exist in the tree.
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Google
 *
 */
public class ChangeTheRootOfABinaryTree {

    class Node {
        public int val;
        public Node left;
        public Node right;
        public Node parent;
    };

    // V0
//    public Node flipBinaryTree(Node root, Node leaf) {
//
//    }

    // V1

    // V2
    // https://leetcode.ca/2020-06-22-1666-Change-the-Root-of-a-Binary-Tree/
    public Node flipBinaryTree_2(Node root, Node leaf) {
        Node cur = leaf;
        Node p = cur.parent;
        while (cur != root) {
            Node gp = p.parent;
            if (cur.left != null) {
                cur.right = cur.left;
            }
            cur.left = p;
            p.parent = cur;
            if (p.left == cur) {
                p.left = null;
            } else if (p.right == cur) {
                p.right = null;
            }
            cur = p;
            p = gp;
        }
        leaf.parent = null;
        return leaf;
    }

    // V3
    // https://github.com/doocs/leetcode/blob/main/solution/1600-1699/1666.Change%20the%20Root%20of%20a%20Binary%20Tree/README_EN.md

}
