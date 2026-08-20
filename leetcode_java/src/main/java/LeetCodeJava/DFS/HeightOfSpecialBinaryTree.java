package LeetCodeJava.DFS;

// https://leetcode.com/problems/height-of-special-binary-tree/

import java.util.ArrayDeque;
import java.util.Deque;

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  2773. Height of Special Binary Tree
 *  Medium
 *
 *  You are given a root, which is the root of a special binary tree with n nodes. The
 *  nodes of the special binary tree are numbered from 1 to n. Suppose the tree has k
 *  leaves in the following order: b1 < b2 < ... < bk.
 *
 *  The leaves of this tree have a special property! That is, for every leaf bi:
 *    The right child of bi is bi+1 if i < k, and b1 otherwise.
 *    The left child of bi is bi-1 if i > 1, and bk otherwise.
 *
 *  Return the height of the given tree.
 *
 *  Note: The height of a binary tree is the length of the longest path from the root to
 *  any other node.
 *
 *  Example 1:
 *    Input: root = [1,2,3,null,null,4,5]
 *    Output: 2
 *
 *  Example 2:
 *    Input: root = [1,2]
 *    Output: 1
 *
 *  Example 3:
 *    Input: root = [1,2,3,null,null,4,null,5,6]
 *    Output: 3
 *
 *  Constraints:
 *    n == number of nodes in the tree
 *    2 <= n <= 10^4
 *    1 <= node.val <= n
 *    The input is generated such that each node.val is unique.
 */
public class HeightOfSpecialBinaryTree {

    // V0
    // IDEA: DFS THAT FILTERS OUT THE FAKE LEAF-TO-LEAF LINKS
    //       the extra pointers only ever join two LEAVES, and they always come in a
    //       mirrored pair: if leaf u's right pointer is leaf v, then v's left pointer
    //       points straight back at u. That back-pointer is the tell:
    //           u.left  is fake  <=>  u.left.right  == u
    //           u.right is fake  <=>  u.right.left  == u
    //       NOTE: a genuine parent-child edge can never satisfy this - it would form a
    //             2-cycle inside a real tree - so the test never discards a real edge.
    //       NOTE: the k == 1 case still works: the lone leaf points at ITSELF both ways,
    //             so u.left == u and u.left.right == u -> fake, as wanted.
    //       with the fake edges filtered the shape is an ordinary tree, and the height
    //       is the deepest node's depth in edges.
    //       ITERATIVE - n reaches 10^4 and the tree may be a chain.
    /**
     * time = O(n)
     * space = O(n)
     */
    public int heightOfTree(TreeNode root) {

        if (root == null) {
            return 0;
        }

        int res = 0;
        // entries : {node, depth}
        Deque<Object[]> stack = new ArrayDeque<>();
        stack.push(new Object[]{root, 0});

        while (!stack.isEmpty()) {
            Object[] cur = stack.pop();
            TreeNode node = (TreeNode) cur[0];
            int depth = (Integer) cur[1];

            res = Math.max(res, depth);

            if (node.left != null && node.left.right != node) {
                stack.push(new Object[]{node.left, depth + 1});
            }
            if (node.right != null && node.right.left != node) {
                stack.push(new Object[]{node.right, depth + 1});
            }
        }

        return res;
    }
}
