package LeetCodeJava.DFS;

// https://leetcode.com/problems/find-leaves-of-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  366. Find Leaves of Binary Tree
 *  Medium
 *
 *  Given the root of a binary tree, collect a tree's nodes as if you were doing this:
 *   - Collect all the leaf nodes.
 *   - Remove all the leaf nodes.
 *   - Repeat until the tree is empty.
 *
 *  Example 1:
 *  Input: root = [1,2,3,4,5]
 *  Output: [[4,5,3],[2],[1]]
 *  Explanation: [[3,5,4],[2],[1]] and [[3,4,5],[2],[1]] are also considered correct answers
 *  since per each level it does not matter the order on which elements are returned.
 *
 *  Example 2:
 *  Input: root = [1]
 *  Output: [[1]]
 *
 *  Constraints:
 *  The number of nodes in the tree is in the range [1, 100].
 *  -100 <= Node.val <= 100
 */
public class FindLeavesOfBinaryTree {

    // V0
    // IDEA: DFS on "height from bottom" - a node's height decides which bucket it lands in,
    //       height(node) = 1 + max(height(left), height(right)), leaves have height 1
    /**
     * time = O(n)
     * space = O(n)
     */
    public List<List<Integer>> findLeaves(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        getHeight(root, res);
        return res;
    }

    private int getHeight(TreeNode node, List<List<Integer>> res) {
        if (node == null) {
            return 0;
        }
        int left = getHeight(node.left, res);
        int right = getHeight(node.right, res);
        int height = Math.max(left, right) + 1;
        // height is 1-based -> bucket index is height - 1
        if (res.size() < height) {
            res.add(new ArrayList<>());
        }
        res.get(height - 1).add(node.val);
        return height;
    }

    // V1
    // IDEA: brute force simulation - literally do what the statement says: strip every leaf,
    //       repeat on what is left. Kept as a readable correctness reference. The tree is
    //       cloned first so the caller's tree is not destroyed.
    /**
     * time = O(n * h)   // one full pass per removed layer
     * space = O(n)
     */
    public List<List<Integer>> findLeaves_1(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        TreeNode cur = cloneTree_1(root);
        while (cur != null) {
            List<Integer> level = new ArrayList<>();
            cur = prune_1(cur, level);
            res.add(level);
        }
        return res;
    }

    private TreeNode cloneTree_1(TreeNode node) {
        if (node == null) {
            return null;
        }
        return new TreeNode(node.val, cloneTree_1(node.left), cloneTree_1(node.right));
    }

    // removes every current leaf, returns the remaining subtree (null if it vanished)
    private TreeNode prune_1(TreeNode node, List<Integer> collected) {
        if (node == null) {
            return null;
        }
        if (node.left == null && node.right == null) {
            collected.add(node.val);
            return null;
        }
        node.left = prune_1(node.left, collected);
        node.right = prune_1(node.right, collected);
        return node;
    }

    // V2
    // IDEA: KAHN style peeling (topological, bottom-up) - build parent pointers + a
    //       "children still attached" counter, start from the leaf frontier and release a
    //       parent only once all of its children have been peeled. No recursion at all.
    /**
     * time = O(n)
     * space = O(n)
     */
    public List<List<Integer>> findLeaves_2(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) {
            return res;
        }

        Map<TreeNode, TreeNode> parent = new HashMap<>();
        Map<TreeNode, Integer> remainingChildren = new HashMap<>();
        List<TreeNode> frontier = new ArrayList<>();

        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);
        parent.put(root, null);
        while (!stack.isEmpty()) {
            TreeNode cur = stack.pop();
            int cnt = 0;
            if (cur.left != null) {
                cnt++;
                parent.put(cur.left, cur);
                stack.push(cur.left);
            }
            if (cur.right != null) {
                cnt++;
                parent.put(cur.right, cur);
                stack.push(cur.right);
            }
            remainingChildren.put(cur, cnt);
            if (cnt == 0) {
                frontier.add(cur);
            }
        }

        while (!frontier.isEmpty()) {
            List<Integer> level = new ArrayList<>();
            List<TreeNode> next = new ArrayList<>();
            for (TreeNode node : frontier) {
                level.add(node.val);
                TreeNode p = parent.get(node);
                if (p != null) {
                    int left = remainingChildren.get(p) - 1;
                    remainingChildren.put(p, left);
                    if (left == 0) {
                        next.add(p);
                    }
                }
            }
            res.add(level);
            frontier = next;
        }
        return res;
    }
}
