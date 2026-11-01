package LeetCodeJava.BFS;

// https://leetcode.com/problems/find-the-level-of-tree-with-minimum-sum/description/
// https://leetcode.ca/2024-06-04-3157-Find-the-Level-of-Tree-with-Minimum-Sum/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * 3157. Find the Level of Tree with Minimum Sum
 * Medium
 *
 * Given the root of a binary tree root where each node has a value, return the level of the tree that has the minimum sum of values among all the levels (in case of a tie, return the lowest level).
 *
 * Note that the root of the tree is at level 1 and the level of any other node is its distance from the root + 1.
 *
 *
 * Example 1:
 *
 * Input: root = [50,6,2,30,80,7]
 * Output: 2
 * Explanation:
 * The sum of values at level 1 is 50, at level 2 is 6 + 2 = 8, and at level 3 is 30 + 80 + 7 = 117.
 * Level 2 has the minimum sum, so the answer is 2.
 *
 * Example 2:
 *
 * Input: root = [36,17,10,null,null,24]
 * Output: 3
 * Explanation:
 * The sum of values at level 1 is 36, at level 2 is 17 + 10 = 27, and at level 3 is 24.
 * Level 3 has the minimum sum, so the answer is 3.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 10^5].
 * 1 <= Node.val <= 10^9
 *
 */
public class FindTheLevelOfTreeWithMinimumSum {

    // V0
    // IDEA: LEVEL-ORDER BFS, TRACK THE SMALLEST LEVEL SUM AND ITS LEVEL
    //   a strict "<" keeps the first (lowest) level on a tie. Sums reach
    //   10^5 * 10^9, so they are long.
    /**
     * time = O(N)
     * space = O(W)   (W = widest level)
     */
    public int minimumLevel(TreeNode root) {
        if (root == null) {
            return 0;
        }
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        int level = 0;
        int bestLevel = 0;
        long bestSum = Long.MAX_VALUE;

        while (!queue.isEmpty()) {
            level++;
            int levelSize = queue.size();
            long levelSum = 0;
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
            if (levelSum < bestSum) {
                bestSum = levelSum;
                bestLevel = level;
            }
        }
        return bestLevel;
    }
}
