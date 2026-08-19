package LeetCodeJava.DFS;

// https://leetcode.com/problems/find-leaves-of-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.List;

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
}
