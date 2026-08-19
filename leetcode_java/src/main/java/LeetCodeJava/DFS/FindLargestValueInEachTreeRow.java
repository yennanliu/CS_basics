package LeetCodeJava.DFS;

// https://leetcode.com/problems/find-largest-value-in-each-tree-row/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 *  515. Find Largest Value in Each Tree Row
 *  Medium
 *
 *  Given the root of a binary tree, return an array of the largest value in each row
 *  of the tree (0-indexed).
 *
 *  Example 1:
 *  Input: root = [1,3,2,5,3,null,9]
 *  Output: [1,3,9]
 *
 *  Example 2:
 *  Input: root = [1,2,3]
 *  Output: [1,3]
 *
 *  Constraints:
 *  The number of nodes in the tree will be in the range [0, 10^4].
 *  -2^31 <= Node.val <= 2^31 - 1
 */
public class FindLargestValueInEachTreeRow {

    // V0
    // IDEA: BFS level order, take the max of every level
    /**
     * time = O(n)
     * space = O(n)
     */
    public List<Integer> largestValues(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            int levelMax = Integer.MIN_VALUE;
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                levelMax = Math.max(levelMax, node.val);
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            res.add(levelMax);
        }
        return res;
    }

    // V1
    // IDEA: DFS carrying the depth, keep max per depth slot
    /**
     * time = O(n)
     * space = O(h)
     */
    public List<Integer> largestValues_1(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        dfs(root, 0, res);
        return res;
    }

    private void dfs(TreeNode node, int depth, List<Integer> res) {
        if (node == null) {
            return;
        }
        if (depth == res.size()) {
            res.add(node.val);
        } else {
            res.set(depth, Math.max(res.get(depth), node.val));
        }
        dfs(node.left, depth + 1, res);
        dfs(node.right, depth + 1, res);
    }
}
