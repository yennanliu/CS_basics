package LeetCodeJava.Tree;

// https://leetcode.com/problems/check-completeness-of-a-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 *  958. Check Completeness of a Binary Tree
 *  Medium
 *
 *  Given the root of a binary tree, determine if it is a complete binary tree.
 *
 *  In a complete binary tree, every level, except possibly the last, is
 *  completely filled, and all nodes in the last level are as far left as
 *  possible. It can have between 1 and 2^h nodes inclusive at the last level h.
 *
 *  Example 1:
 *
 *  Input: root = [1,2,3,4,5,6]
 *  Output: true
 *
 *  Example 2:
 *
 *  Input: root = [1,2,3,4,5,null,7]
 *  Output: false
 *  Explanation: the node with value 7 is not as far left as possible.
 *
 *  Constraints:
 *
 *  The number of nodes in the tree is in the range [1, 100].
 *  1 <= Node.val <= 1000
 */
public class CheckCompletenessOfABinaryTree {

    // V0
    // IDEA: BFS pushing null children too. Once a null is popped, no non-null
    //       node may follow -> otherwise there is a "hole" and the tree is not
    //       complete.
    /**
     * time = O(n)
     * space = O(n)
     */
    public boolean isCompleteTree(TreeNode root) {
        if (root == null) {
            return true;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        boolean seenNull = false;
        while (!queue.isEmpty()) {
            TreeNode cur = queue.poll();
            if (cur == null) {
                seenNull = true;
                continue;
            }
            if (seenNull) {
                return false;
            }
            queue.offer(cur.left);
            queue.offer(cur.right);
        }
        return true;
    }
}
