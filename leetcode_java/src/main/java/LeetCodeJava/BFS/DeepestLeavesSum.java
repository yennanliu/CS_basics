package LeetCodeJava.BFS;

// https://leetcode.com/problems/deepest-leaves-sum/description/

import java.util.*;
import LeetCodeJava.DataStructure.TreeNode;

/**
 * 1302. Deepest Leaves Sum
 * Medium
 *
 * Given the root of a binary tree, return the sum of values of its deepest leaves.
 *
 *
 * Example 1:
 *
 * Input: root = [1,2,3,4,5,null,6,7,null,null,null,null,8]
 * Output: 15
 *
 * Example 2:
 *
 * Input: root = [6,7,8,2,7,1,3,9,null,1,4,null,null,null,5]
 * Output: 19
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 10^4].
 * 1 <= Node.val <= 100
 *
 */
public class DeepestLeavesSum {

    // V0
    // IDEA: BFS LEVEL ORDER (KEEP ONLY THE LAST LEVEL'S SUM)
    //  the sum computed for the final level is the answer -- no depth bookkeeping.
    /**
     * time = O(N)
     * space = O(W), W = max level width
     */
    public int deepestLeavesSum(TreeNode root) {
        if (root == null) {
            return 0;
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        int levelSum = 0;

        while (!queue.isEmpty()) {
            // reset per level; the last value standing belongs to the deepest level
            levelSum = 0;
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                levelSum += node.val;
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return levelSum;
    }

    // V1
    // IDEA: DFS WITH (maxDepth, sumAtMaxDepth)
    //  a deeper node resets the sum; a node at the current max depth adds to it.
    /**
     * time = O(N)
     * space = O(H)
     */
    public int deepestLeavesSum_1(TreeNode root) {
        // state[0] = deepest depth seen so far, state[1] = sum of values at that depth
        int[] state = new int[] {-1, 0};
        collectDeepest(root, 0, state);
        return state[1];
    }

    /** Updates state with node's value if node is at (or deeper than) the deepest depth seen. */
    private void collectDeepest(TreeNode node, int depth, int[] state) {
        if (node == null) {
            return;
        }
        if (depth > state[0]) {
            state[0] = depth;
            state[1] = node.val;
        } else if (depth == state[0]) {
            state[1] += node.val;
        }
        collectDeepest(node.left, depth + 1, state);
        collectDeepest(node.right, depth + 1, state);
    }
}
