package LeetCodeJava.BFS;

// https://leetcode.com/problems/maximum-level-sum-of-a-binary-tree/description/

import java.util.*;
import LeetCodeJava.DataStructure.TreeNode;

/**
 * 1161. Maximum Level Sum of a Binary Tree
 * Medium
 *
 * Given the root of a binary tree, the level of its root is 1, the level of its children is 2,
 * and so on.
 *
 * Return the smallest level x such that the sum of all the values of nodes at level x
 * is maximal.
 *
 *
 * Example 1:
 *
 * Input: root = [1,7,0,7,-8,null,null]
 * Output: 2
 * Explanation:
 * Level 1 sum = 1.
 * Level 2 sum = 7 + 0 = 7.
 * Level 3 sum = 7 + -8 = -1.
 * So we return the level with the maximum sum which is level 2.
 *
 * Example 2:
 *
 * Input: root = [989,null,10250,98693,-89388,null,null,null,-32127]
 * Output: 2
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 10^4].
 * -10^5 <= Node.val <= 10^5
 *
 */
public class MaximumLevelSumOfABinaryTree {

    // V0
    // IDEA: BFS (LEVEL ORDER TRAVERSAL), SUM EACH LEVEL
    //  NOTE: use a STRICT > so a tie keeps the SMALLEST level
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxLevelSum(TreeNode root) {
        if (root == null) {
            return 0;
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);

        int bestLevel = 1;
        long bestSum = Long.MIN_VALUE;
        int level = 0;

        while (!queue.isEmpty()) {
            level++;
            long levelSum = 0;
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
            if (levelSum > bestSum) {
                bestSum = levelSum;
                bestLevel = level;
            }
        }
        return bestLevel;
    }

    // V1
    // IDEA: DFS, ACCUMULATE A SUM PER DEPTH IN A LIST
    /**
     * time = O(N)
     * space = O(H)
     */
    public int maxLevelSum_1(TreeNode root) {
        if (root == null) {
            return 0;
        }
        // levelSums.get(d) = sum of all node values at depth d (0-indexed)
        List<Long> levelSums = new ArrayList<>();
        collectLevelSums(root, 0, levelSums);

        int bestLevel = 1;
        long bestSum = levelSums.get(0);
        for (int depth = 1; depth < levelSums.size(); depth++) {
            if (levelSums.get(depth) > bestSum) {
                bestSum = levelSums.get(depth);
                bestLevel = depth + 1;
            }
        }
        return bestLevel;
    }

    /** Adds node.val into the running sum of its depth, then recurses. */
    private void collectLevelSums(TreeNode node, int depth, List<Long> levelSums) {
        if (node == null) {
            return;
        }
        if (depth == levelSums.size()) {
            levelSums.add(0L);
        }
        levelSums.set(depth, levelSums.get(depth) + node.val);
        collectLevelSums(node.left, depth + 1, levelSums);
        collectLevelSums(node.right, depth + 1, levelSums);
    }
}
