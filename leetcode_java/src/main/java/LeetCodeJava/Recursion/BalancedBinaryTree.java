package LeetCodeJava.Recursion;

// https://leetcode.com/problems/balanced-binary-tree/
/**
 * 110. Balanced Binary Tree
 * Solved
 * Easy
 * Topics
 * Companies
 * Given a binary tree, determine if it is height-balanced.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [3,9,20,null,null,15,7]
 * Output: true
 * Example 2:
 *
 *
 * Input: root = [1,2,2,3,3,null,null,4,4]
 * Output: false
 * Example 3:
 *
 * Input: root = []
 * Output: true
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [0, 5000].
 * -104 <= Node.val <= 104
 */
import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

public class BalancedBinaryTree {

    /** NOTE !!!
     *
     *  depth of tree is the distance from `root` to the node
     */
    // tree depth : https://github.com/yennanliu/CS_basics/blob/master/doc/pic/tree_depth_vs_height.jpeg

    // V0
    // IDEA : DFS
    // https://www.bilibili.com/video/BV1Ug411S7my/?share_source=copy_web
    public boolean isBalanced(TreeNode root) {
        // edge
        if (root == null) {
            return true;
        }
        if (root.left == null && root.right == null) {
            return true;
        }

        int leftDepth = getDepthDFS(root.left);
        int rightDepth = getDepthDFS(root.right);

        // check if `current` node is `balanced`
        if (Math.abs(leftDepth - rightDepth) > 1) {
            return false;
        }

        // dfs call
        // recursively check if `sub left node` and  `sub right node` are `balanced`
        return isBalanced(root.left) && isBalanced(root.right);
    }

    // LC 104
    public int getDepthDFS(TreeNode root) {
        if (root == null) {
            return 0;
        }

      return Math.max(getDepthDFS(root.left), getDepthDFS(root.right)) + 1;
    }

    // V0-2
    // IDEA: BFS (gpt)
    public boolean isBalanced_0_2(TreeNode root) {
        if (root == null)
            return true;

        Map<TreeNode, Integer> heightMap = new HashMap<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        // Post-order-like BFS
        Stack<TreeNode> stack = new Stack<>();
        Set<TreeNode> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            stack.push(node);

            if (node.left != null)
                queue.add(node.left);
            if (node.right != null)
                queue.add(node.right);
        }

        // Process in reverse (bottom-up)
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();

            int leftHeight = node.left == null ? 0 : heightMap.get(node.left);
            int rightHeight = node.right == null ? 0 : heightMap.get(node.right);

            if (Math.abs(leftHeight - rightHeight) > 1)
                return false;

            heightMap.put(node, Math.max(leftHeight, rightHeight) + 1);
        }

        return true;
    }

    // V1-1
    // https://neetcode.io/problems/balanced-binary-tree
    // IDEA: BRUTE FORCE
    public boolean isBalanced_1_1(TreeNode root) {
        if (root == null) return true;

        int left = height_1_1(root.left);
        int right = height_1_1(root.right);
        if (Math.abs(left - right) > 1) return false;
        return isBalanced_1_1(root.left) && isBalanced_1_1(root.right);
    }

    public int height_1_1(TreeNode root) {
        if (root == null) {
            return 0;
        }

        return 1 + Math.max(height_1_1(root.left), height_1_1(root.right));
    }


    // V1-2
    // https://neetcode.io/problems/balanced-binary-tree
    // IDEA: DFS
    public boolean isBalanced_1_2(TreeNode root) {
        return dfs(root)[0] == 1;
    }

    private int[] dfs(TreeNode root) {
        if (root == null) {
            return new int[]{1, 0};
        }

        int[] left = dfs(root.left);
        int[] right = dfs(root.right);

        boolean balanced = (left[0] == 1 && right[0] == 1) &&
                (Math.abs(left[1] - right[1]) <= 1);
        int height = 1 + Math.max(left[1], right[1]);

        return new int[]{balanced ? 1 : 0, height};
    }


    // V1-3
    // https://neetcode.io/problems/balanced-binary-tree
    // IDEA: Depth First Search (Stack)
    public boolean isBalanced_1_3(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode node = root, last = null;
        Map<TreeNode, Integer> depths = new HashMap<>();

        while (!stack.isEmpty() || node != null) {
            if (node != null) {
                stack.push(node);
                node = node.left;
            } else {
                node = stack.peek();
                if (node.right == null || last == node.right) {
                    stack.pop();
                    int left = depths.getOrDefault(node.left, 0);
                    int right = depths.getOrDefault(node.right, 0);
                    if (Math.abs(left - right) > 1) return false;
                    depths.put(node, 1 + Math.max(left, right));
                    last = node;
                    node = null;
                } else {
                    node = node.right;
                }
            }
        }
        return true;
    }


    // V2
    // IDEA :  TOP DOWN RECURSION
    // https://leetcode.com/problems/balanced-binary-tree/editorial/
    // Recursively obtain the height of a tree. An empty tree has -1 height
    private int height(TreeNode root) {
        // An empty tree has height -1
        if (root == null) {
            return -1;
        }
        return 1 + Math.max(height(root.left), height(root.right));
    }

    public boolean isBalanced_2(TreeNode root) {
        // An empty tree satisfies the definition of a balanced tree
        if (root == null) {
            return true;
        }

        // Check if subtrees have height within 1. If they do, check if the
        // subtrees are balanced
        return Math.abs(height(root.left) - height(root.right)) < 2
                && isBalanced_2(root.left)
                && isBalanced_2(root.right);
    }

    // V3
    // IDEA :  BOTTOM UP RECURSION
    // https://leetcode.com/problems/balanced-binary-tree/editorial/

    // define custom data structure
    final class TreeInfo {
        public final int height;
        public final boolean balanced;

        public TreeInfo(int height, boolean balanced) {
            this.height = height;
            this.balanced = balanced;
        }
    }

    // the tree's height in a reference variable.
    private TreeInfo isBalancedTreeHelper(TreeNode root) {
        // An empty tree is balanced and has height = -1
        if (root == null) {
            return new TreeInfo(-1, true);
        }

        // Check subtrees to see if they are balanced.
        TreeInfo left = isBalancedTreeHelper(root.left);
        if (!left.balanced) {
            return new TreeInfo(-1, false);
        }
        TreeInfo right = isBalancedTreeHelper(root.right);
        if (!right.balanced) {
            return new TreeInfo(-1, false);
        }

        // Use the height obtained from the recursive calls to
        // determine if the current node is also balanced.
        if (Math.abs(left.height - right.height) < 2) {
            return new TreeInfo(Math.max(left.height, right.height) + 1, true);
        }
        return new TreeInfo(-1, false);
    }

    public boolean isBalanced_3(TreeNode root) {
        return isBalancedTreeHelper(root).balanced;
    }

}
