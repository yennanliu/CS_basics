package LeetCodeJava.Tree;

// https://leetcode.com/problems/smallest-subtree-with-all-the-deepest-nodes/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.HashMap;
import java.util.Map;

/**
 *  865. Smallest Subtree with all the Deepest Nodes
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given the root of a binary tree, the depth of each node is the shortest distance to the root.
 *
 * Return the smallest subtree such that it contains all the deepest nodes in the original tree.
 *
 * A node is called the deepest if it has the largest depth possible among any node in the entire tree.
 *
 * The subtree of a node is a tree consisting of that node, plus the set of all descendants of that node.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4]
 * Output: [2,7,4]
 * Explanation: We return the node with value 2, colored in yellow in the diagram.
 * The nodes coloured in blue are the deepest nodes of the tree.
 * Notice that nodes 5, 3 and 2 contain the deepest nodes in the tree but node 2 is the smallest subtree among them, so we return it.
 * Example 2:
 *
 * Input: root = [1]
 * Output: [1]
 * Explanation: The root is the deepest node in the tree.
 * Example 3:
 *
 * Input: root = [0,1,3,null,2]
 * Output: [2]
 * Explanation: The deepest node in the tree is 2, the valid subtrees are the subtrees of nodes 2, 1 and 0 but the subtree of node 2 is the smallest.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree will be in the range [1, 500].
 * 0 <= Node.val <= 500
 * The values of the nodes in the tree are unique.
 *
 *
 * Note: This question is the same as 1123: https://leetcode.com/problems/lowest-common-ancestor-of-deepest-leaves/
 *
 *
 */
public class SmallestSubtreeWithAllTheDeepestNodes {

    // V0
//    public TreeNode subtreeWithAllDeepest(TreeNode root) {
//
//    }
    

    // V0-1
    // We create a simple helper class to return both the Node and its max Depth
    class Result_0_1 {
        TreeNode node;
        int dist;
        Result_0_1(TreeNode node, int dist) {
            this.node = node;
            this.dist = dist;
        }
    }

    public TreeNode subtreeWithAllDeepest(TreeNode root) {
        return dfs_0_1(root).node;
    }

    private Result_0_1 dfs_0_1(TreeNode node) {
        if (node == null) {
            return new Result_0_1(null, 0);
        }

        Result_0_1 left = dfs_0_1(node.left);
        Result_0_1 right = dfs_0_1(node.right);

        // Case 1: Left side is deeper. The result must be in the left subtree.
        if (left.dist > right.dist) {
            return new Result_0_1(left.node, left.dist + 1);
        }

        // Case 2: Right side is deeper. The result must be in the right subtree.
        if (right.dist > left.dist) {
            return new Result_0_1(right.node, right.dist + 1);
        }

        // Case 3: Depths are EQUAL.
        // This current node is the "Lowest Common Ancestor" for all deepest
        // nodes found so far in its subtrees.
        return new Result_0_1(node, left.dist + 1);
    }


    // V0-2
    public TreeNode subtreeWithAllDeepest_0_2(TreeNode root) {
        return dfs_0_2(root).node;
    }

    private Pair dfs_0_2(TreeNode node) {
        if (node == null) {
            return new Pair(null, 0);
        }

        Pair left = dfs_0_2(node.left);
        Pair right = dfs_0_2(node.right);

        if (left.depth > right.depth) {
            return new Pair(left.node, left.depth + 1);
        } else if (right.depth > left.depth) {
            return new Pair(right.node, right.depth + 1);
        } else {
            return new Pair(node, left.depth + 1);
        }
    }

    class Pair {
        TreeNode node;
        int depth;

        Pair(TreeNode node, int depth) {
            this.node = node;
            this.depth = depth;
        }
    }



    // V1-1
    // IDEA: Paint Deepest Nodes
    // https://leetcode.com/problems/smallest-subtree-with-all-the-deepest-nodes/editorial/
    Map<TreeNode, Integer> depth;
    int max_depth;

    public TreeNode subtreeWithAllDeepest_1_1(TreeNode root) {
        depth = new HashMap();
        depth.put(null, -1);
        dfs(root, null);
        max_depth = -1;
        for (Integer d : depth.values())
            max_depth = Math.max(max_depth, d);

        return answer(root);
    }

    public void dfs(TreeNode node, TreeNode parent) {
        if (node != null) {
            depth.put(node, depth.get(parent) + 1);
            dfs(node.left, node);
            dfs(node.right, node);
        }
    }

    public TreeNode answer(TreeNode node) {
        if (node == null || depth.get(node) == max_depth)
            return node;
        TreeNode L = answer(node.left),
                R = answer(node.right);
        if (L != null && R != null)
            return node;
        if (L != null)
            return L;
        if (R != null)
            return R;
        return null;
    }


    // V1-2
    // IDEA: RECURSION
    // https://leetcode.com/problems/smallest-subtree-with-all-the-deepest-nodes/editorial/
    class Result {
        TreeNode node;
        int dist;

        Result(TreeNode n, int d) {
            node = n;
            dist = d;
        }
    }

    public TreeNode subtreeWithAllDeepest_1_2(TreeNode root) {
        return dfs(root).node;
    }

    // Return the result of the subtree at this node.
    public Result dfs(TreeNode node) {
        if (node == null)
            return new Result(null, 0);
        Result L = dfs(node.left),
                R = dfs(node.right);
        if (L.dist > R.dist)
            return new Result(L.node, L.dist + 1);
        if (L.dist < R.dist)
            return new Result(R.node, R.dist + 1);
        return new Result(node, L.dist + 1);
    }



    // V2





}
