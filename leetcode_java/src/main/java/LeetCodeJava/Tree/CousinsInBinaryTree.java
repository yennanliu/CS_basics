package LeetCodeJava.Tree;

// https://leetcode.com/problems/cousins-in-binary-tree/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * 993. Cousins in Binary Tree
 * Easy
 *
 * Given the root of a binary tree with unique values and the values of two different
 * nodes of the tree x and y, return true if the nodes corresponding to the values x
 * and y in the tree are cousins, or false otherwise.
 *
 * Two nodes of a binary tree are cousins if they have the same depth with different
 * parents.
 *
 * Note that in a binary tree, the root node is at the depth 0, and children of each
 * depth k node are at the depth k + 1.
 *
 * Example 1:
 *
 * Input: root = [1,2,3,4], x = 4, y = 3
 * Output: false
 *
 * Example 2:
 *
 * Input: root = [1,2,3,null,4,null,5], x = 5, y = 4
 * Output: true
 *
 * Example 3:
 *
 * Input: root = [1,2,3,null,4], x = 2, y = 3
 * Output: false
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [2, 100].
 * 1 <= Node.val <= 100
 * Each node has a unique value.
 * x != y
 * x and y exist in the tree.
 *
 */
public class CousinsInBinaryTree {

    // V0
    // IDEA: DFS collecting (depth, parent) for both targets
    /**
     *  Cousins  <=>  SAME depth AND DIFFERENT parent.
     *
     *  A single DFS records (depth, parentValue) for x and y, then compares.
     *
     *  NOTE !!! we must NOT short-circuit on the first hit - we need BOTH nodes.
     *
     *  time  = O(n)  // n = number of tree nodes
     *  space = O(h)  // h = tree height (recursion stack)
     */

    private int depthX = -1;
    private int depthY = -1;
    private int parentX = -1;
    private int parentY = -1;

    public boolean isCousins(TreeNode root, int x, int y) {
        this.depthX = -1;
        this.depthY = -1;
        this.parentX = -1;
        this.parentY = -1;

        dfs(root, -1, 0, x, y);

        return depthX == depthY && parentX != parentY;
    }

    private void dfs(TreeNode node, int parent, int depth, int x, int y) {
        if (node == null) {
            return;
        }
        if (node.val == x) {
            depthX = depth;
            parentX = parent;
        } else if (node.val == y) {
            depthY = depth;
            parentY = parent;
        }
        dfs(node.left, node.val, depth + 1, x, y);
        dfs(node.right, node.val, depth + 1, x, y);
    }

    // V0-1
    // IDEA: BFS level by level
    /**
     *  Scan ONE level at a time. Within a level, note whether x and y were seen
     *  and whether they came from the SAME parent (i.e. are siblings).
     *  If both appear on the same level and are NOT siblings -> cousins.
     *
     *  time  = O(n)  // n = number of tree nodes
     *  space = O(w)  // w = max tree width (queue)
     */
    public boolean isCousins_0_1(TreeNode root, int x, int y) {
        Deque<TreeNode[]> queue = new ArrayDeque<>(); // {node, parent}
        queue.offer(new TreeNode[] { root, null });

        while (!queue.isEmpty()) {
            int found = 0;
            Set<TreeNode> parents = new HashSet<>();

            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                TreeNode[] cur = queue.poll();
                TreeNode node = cur[0];
                TreeNode parent = cur[1];

                if (node.val == x || node.val == y) {
                    found += 1;
                    parents.add(parent);
                }
                if (node.left != null) {
                    queue.offer(new TreeNode[] { node.left, node });
                }
                if (node.right != null) {
                    queue.offer(new TreeNode[] { node.right, node });
                }
            }

            if (found == 1) {
                // only ONE of them sits on this level -> different depths
                return false;
            }
            if (found == 2) {
                // both here: cousins only when the parents DIFFER
                return parents.size() == 2;
            }
        }

        return false;
    }

}
