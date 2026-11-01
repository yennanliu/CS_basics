package LeetCodeJava.BFS;

// https://leetcode.com/problems/find-nearest-right-node-in-binary-tree/description/

import java.util.*;
import LeetCodeJava.DataStructure.TreeNode;

/**
 * 1602. Find Nearest Right Node in Binary Tree
 * Medium
 *
 * Given the root of a binary tree and a node u in the tree, return the nearest node on the
 * same level that is to the right of u, or return null if u is the rightmost node in its level.
 *
 *
 * Example 1:
 *
 * Input: root = [1,2,3,null,4,5,6], u = 4
 * Output: 5
 * Explanation: The nearest node on the same level to the right of node 4 is node 5.
 *
 * Example 2:
 *
 * Input: root = [3,null,4,2], u = 2
 * Output: null
 * Explanation: There are no nodes to the right of 2.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 10^5].
 * 1 <= Node.val <= 10^5
 * All values in the tree are distinct.
 * u is a node in the binary tree rooted at root.
 *
 */
public class FindNearestRightNodeInBinaryTree {

    // V0
    // IDEA: LEVEL-ORDER BFS (THE NEAREST RIGHT NODE IS THE NEXT QUEUE ENTRY OF THE SAME LEVEL)
    //  NOTE: compare node identity (==), not values -- identity is what "node u" means.
    /**
     * time = O(N)
     * space = O(W), W = max level width
     */
    public TreeNode findNearestRightNode(TreeNode root, TreeNode u) {
        if (root == null) {
            return null;
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                if (node == u) {
                    // last node of this level -> nothing to its right
                    if (i == levelSize - 1) {
                        return null;
                    }
                    return queue.peek();
                }
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return null;
    }
}
