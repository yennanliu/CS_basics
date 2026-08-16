package LeetCodeJava.Tree;

// https://leetcode.com/problems/encode-n-ary-tree-to-binary-tree/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 431. Encode N-ary Tree to Binary Tree
 * Hard
 * Lock: Prime
 *
 * Design an algorithm to encode an N-ary tree into a binary tree and decode the
 * binary tree to get the original N-ary tree. An N-ary tree is a rooted tree in
 * which each node has no more than N children. Similarly, a binary tree is a
 * rooted tree in which each node has no more than 2 children. There is no
 * restriction on how your encode/decode algorithm should work. You just need to
 * ensure that an N-ary tree can be encoded to a binary tree and this binary tree
 * can be decoded to the original N-ary tree structure.
 *
 * Nary-Tree input serialization is represented in their level order traversal,
 * each group of children is separated by the null value.
 *
 * Example 1:
 *
 * Input: root = [1,null,3,2,4,null,5,6]
 * Output: [1,null,3,2,4,null,5,6]
 *
 * Example 2:
 *
 * Input: root = [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,
 *                null,13,null,null,14]
 * Output: [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,
 *          null,null,14]
 *
 * Example 3:
 *
 * Input: root = []
 * Output: []
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [0, 10^4].
 * 0 <= Node.val <= 10^4
 * The height of the n-ary tree is less than or equal to 1000
 * Do not use class member/global/static variables to store states.
 * Your encode and decode algorithms should be stateless.
 *
 */
public class EncodeNaryTreeToBinaryTree {

    // Definition for a Node (offered by LC platform)
    static class Node {
        public int val;
        public List<Node> children;

        public Node() {}

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, List<Node> _children) {
            val = _val;
            children = _children;
        }
    }

    // NOTE: the binary tree node is the shared LeetCodeJava.DataStructure.TreeNode

    /**
     * Your Codec object will be instantiated and called as such:
     * Codec codec = new Codec();
     * codec.decode(codec.encode(root));
     */

    // V0
    // IDEA: `LEFT-CHILD / RIGHT-SIBLING` REPRESENTATION
    /**
     *  The classic trick for turning an N-ary tree into a binary one:
     *
     *     binary.left  = the FIRST child of the n-ary node
     *     binary.right = the NEXT SIBLING of the n-ary node
     *
     *  So a node's WHOLE children list becomes a RIGHT-GOING CHAIN hanging off
     *  its left pointer:
     *
     *      n-ary            binary
     *        1                1
     *      / | \             /
     *     3  2  4           3
     *    / \                 \
     *   5   6                 2
     *                          \
     *                           4
     *      (3's own children 5,6 hang off 3.left -> 5 -> right -> 6)
     *
     *  Decoding just walks that RIGHT CHAIN back into a children list.
     *
     *  NOTE !!! this is LOSSLESS precisely because a binary node's `right` is
     *           reserved for siblings only -- it never means `second child`.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    class Codec {

        // Encodes an n-ary tree to a binary tree.
        public TreeNode encode(Node root) {
            if (root == null) {
                return null;
            }

            TreeNode node = new TreeNode(root.val);

            if (root.children != null && !root.children.isEmpty()) {
                // FIRST child goes on the LEFT
                node.left = encode(root.children.get(0));

                // the REST form a RIGHT chain off that first child
                TreeNode cur = node.left;
                for (int i = 1; i < root.children.size(); i++) {
                    cur.right = encode(root.children.get(i));
                    cur = cur.right;
                }
            }

            return node;
        }

        // Decodes your binary tree to an n-ary tree.
        public Node decode(TreeNode data) {
            if (data == null) {
                return null;
            }

            Node node = new Node(data.val, new ArrayList<>());

            TreeNode cur = data.left; // first child
            while (cur != null) {
                node.children.add(decode(cur));
                cur = cur.right;      // next sibling
            }

            return node;
        }
    }

}
