package LeetCodeJava.BFS;

// https://leetcode.com/problems/kth-largest-sum-in-a-binary-tree/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 2583. Kth Largest Sum in a Binary Tree
 * Medium
 *
 * You are given the root of a binary tree and a positive integer k.
 *
 * The level sum in the tree is the sum of the values of the nodes that are on the same level.
 *
 * Return the kth largest level sum in the tree (not necessarily distinct).
 * If there are fewer than k levels in the tree, return -1.
 *
 * Note that two nodes are on the same level if they have the same distance from the root.
 *
 *
 * Example 1:
 *
 * Input: root = [5,8,9,2,1,3,7,4,6], k = 2
 * Output: 13
 * Explanation: The level sums are the following:
 * - Level 1: 5.
 * - Level 2: 8 + 9 = 17.
 * - Level 3: 2 + 1 + 3 + 7 = 13.
 * - Level 4: 4 + 6 = 10.
 * The 2nd largest level sum is 13.
 *
 * Example 2:
 *
 * Input: root = [1,2,null,3], k = 1
 * Output: 3
 * Explanation: The largest level sum is 3.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is n.
 * 2 <= n <= 10^5
 * 1 <= Node.val <= 10^6
 * 1 <= k <= n
 *
 */
public class KthLargestSumInABinaryTree {

    // V0
    // IDEA: LEVEL-ORDER BFS FOR EACH LEVEL SUM + MIN HEAP OF SIZE K
    //   the heap keeps the k largest sums seen; its top is the k-th largest.
    //   Fewer than k levels -> -1. Sums reach 10^5 * 10^6, so they are long.
    /**
     * time = O(N + L log K)   (L = number of levels)
     * space = O(N)
     */
    public long kthLargestLevelSum(TreeNode root, int k) {
        if (root == null) {
            return -1;
        }
        PriorityQueue<Long> largestSums = new PriorityQueue<>();
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            // the queue holds exactly one level at the top of each iteration
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
            largestSums.offer(levelSum);
            // drop the smallest once more than k sums are held
            if (largestSums.size() > k) {
                largestSums.poll();
            }
        }

        if (largestSums.size() < k) {
            return -1;
        }
        return largestSums.peek();
    }
}
