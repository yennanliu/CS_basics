package LeetCodeJava.Recursion;

// https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/description/
// https://leetcode.ca/all/298.html

import LeetCodeJava.DataStructure.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 *  298. Binary Tree Longest Consecutive Sequence
 * Given a binary tree, find the length of the longest consecutive sequence path.
 *
 * The path refers to any sequence of nodes from some starting node to any node in the tree along the parent-child connections. The longest consecutive path need to be from parent to child (cannot be the reverse).
 *
 * Example 1:
 *
 * Input:
 *
 *    1
 *     \
 *      3
 *     / \
 *    2   4
 *         \
 *          5
 *
 * Output: 3
 *
 * Explanation: Longest consecutive sequence path is 3-4-5, so return 3.
 * Example 2:
 *
 * Input:
 *
 *    2
 *     \
 *      3
 *     /
 *    2
 *   /
 *  1
 *
 * Output: 2
 *
 * Explanation: Longest consecutive sequence path is 2-3, not 3-2-1, so return 2.
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Facebook Google Uber
 *
 */
public class BinaryTreeLongestConsecutiveSequence {

    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     *     int val;
     *     TreeNode left;
     *     TreeNode right;
     *     TreeNode() {}
     *     TreeNode(int val) { this.val = val; }
     *     TreeNode(int val, TreeNode left, TreeNode right) {
     *         this.val = val;
     *         this.left = left;
     *         this.right = right;
     *     }
     * }
     */
    // V0
    // IDEA : DFS + BFS (fix by gpt)
    // TODO : validate it
    int longestCnt = 0;

    public int longestConsecutive_0_1(TreeNode root) {
        if (root == null) {
            return 0;
        }

        // bfs
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);

        while (!q.isEmpty()) {
            TreeNode node = q.poll();  // Poll the node from the queue
            int length = dfsCount(node);  // Use the node for dfs
            longestCnt = Math.max(longestCnt, length);

            if (node.left != null) {
                q.add(node.left);
            }
            if (node.right != null) {
                q.add(node.right);
            }
        }

        return longestCnt;
    }

    private int dfsCount(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int leftCount = 0, rightCount = 0;

        if (root.left != null && root.val + 1 == root.left.val) {
            leftCount = 1 + dfsCount(root.left);  // Count consecutive path on the left
        }

        if (root.right != null && root.val + 1 == root.right.val) {
            rightCount = 1 + dfsCount(root.right);  // Count consecutive path on the right
        }

        /**
         * NOTE !!!
         *
         *  simply compare leftCount and rightCount,
         *  no need to do below :
         *
         *
         *  return Math.max(
         *                 Math.max(this.dfsCount(root.left), this.dfsCount(root.right)),
         *                 this.dfsCount(root)
         *         );
         *
         * above code may have issue below :
         *
         * 	1.	Redundant Call: this.dfsCount(root) is included in the comparison with dfsCount(root.left) and dfsCount(root.right), leading to redundant recursion. This will likely cause an infinite loop or unnecessary repeated calculations since dfsCount(root) will keep calling itself.
         * 	2.	Logic Error: The comparison includes the root itself in a way that doesn’t make sense in the context of a consecutive sequence. You want to evaluate the left and right children, not compare the root node against itself.
         * 	3.	Performance Degradation: Since this.dfsCount(root) is unnecessarily called multiple times, it can dramatically increase the recursion depth and the overall computational complexity.
         *
         */
        // Return the max length between left and right consecutive paths
        return Math.max(leftCount, rightCount) + 1;  // Include current node
    }

    // V1
    // IDEA : TREE, RECURSION
    // https://leetcode.ca/2016-09-23-298-Binary-Tree-Longest-Consecutive-Sequence/
    private int ans;

    public int longestConsecutive_1_1(TreeNode root) {
        dfs_1(root);
        return ans;
    }

    private int dfs_1(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int l = dfs_1(root.left) + 1;
        int r = dfs_1(root.right) + 1;
        if (root.left != null && root.left.val - root.val != 1) {
            l = 1;
        }
        if (root.right != null && root.right.val - root.val != 1) {
            r = 1;
        }
        int t = Math.max(l, r);
        ans = Math.max(ans, t);
        return t;
    }

    // V2
    // IDEA : DFS
    // https://walkccc.me/LeetCode/problems/298/#__tabbed_1_2
    public int longestConsecutive_2_1(TreeNode root) {
        if (root == null)
            return 0;
        return dfs_2(root, -1, 0, 1);
    }

    private int dfs_2(TreeNode root, int target, int length, int mx) {
        if (root == null)
            return mx;
        if (root.val == target)
            mx = Math.max(mx, ++length);
        else
            length = 1;
        return Math.max(dfs_2(root.left, root.val + 1, length, mx),
                dfs_2(root.right, root.val + 1, length, mx));
    }

    // V3
    // IDEA : DFS
    // https://blog.csdn.net/qq_46105170/article/details/108481001
    private int res;

    public int longestConsecutive_3(TreeNode root) {
        dfs_3(root);
        return res;
    }

    // The dfs function starts at the root and returns the length of the longest consecutive sequence
    private int dfs_3(TreeNode root) {
        if (root == null) {
            return 0;
        }

        // Initialize the length to 1
        int len = 1;

        // Get the longest consecutive path from left and right child nodes
        int left = dfs_3(root.left), right = dfs_3(root.right);

        // If the left child is consecutive, increase the length
        if (root.left != null && root.left.val == root.val + 1) {
            len = Math.max(len, 1 + left);
        }

        // If the right child is consecutive, increase the length
        if (root.right != null && root.right.val == root.val + 1) {
            len = Math.max(len, 1 + right);
        }

        // Update the global result
        res = Math.max(res, len);

        // Return the length of the longest consecutive path starting from the current node
        return len;
    }

}
