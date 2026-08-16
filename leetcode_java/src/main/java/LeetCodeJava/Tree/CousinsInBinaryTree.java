package LeetCodeJava.Tree;

// https://leetcode.com/problems/cousins-in-binary-tree/description/

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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


    // V1
    // IDEA: ITERATIVE DFS carrying (node, parent, depth) on a stack
    /**
     *  Same information as V0 but gathered without recursion, so the traversal can
     *  bail out the moment BOTH targets are known instead of finishing the tree.
     *
     *  time  = O(n)
     *  space = O(h)
     */
    public boolean isCousins_1(TreeNode root, int x, int y) {
        Deque<Object[]> stack = new ArrayDeque<>(); // {node, parentVal, depth}
        stack.push(new Object[] { root, -1, 0 });

        int dx = -1;
        int px = -1;
        int dy = -1;
        int py = -1;

        while (!stack.isEmpty()) {
            Object[] cur = stack.pop();
            TreeNode node = (TreeNode) cur[0];
            int parent = (Integer) cur[1];
            int depth = (Integer) cur[2];

            if (node.val == x) {
                dx = depth;
                px = parent;
            } else if (node.val == y) {
                dy = depth;
                py = parent;
            }
            if (dx != -1 && dy != -1) {
                break;   // both found -> stop early
            }

            if (node.left != null) {
                stack.push(new Object[] { node.left, node.val, depth + 1 });
            }
            if (node.right != null) {
                stack.push(new Object[] { node.right, node.val, depth + 1 });
            }
        }

        return dx == dy && px != py;
    }

    // V2
    // IDEA: BFS BUILDING depth / parent MAPS for the whole tree
    /**
     *  Record depth and parent for EVERY node, then answer the question from the
     *  maps.
     *
     *  O(n) memory rather than O(h), but the maps answer `are u and v cousins?` for
     *  any pair in O(1) afterwards -- the right shape for repeated queries.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    public boolean isCousins_2(TreeNode root, int x, int y) {
        Map<Integer, Integer> depth = new HashMap<>();
        Map<Integer, Integer> parent = new HashMap<>();

        Deque<Object[]> q = new ArrayDeque<>();
        q.offer(new Object[] { root, -1, 0 });
        while (!q.isEmpty()) {
            Object[] cur = q.poll();
            TreeNode node = (TreeNode) cur[0];
            depth.put(node.val, (Integer) cur[2]);
            parent.put(node.val, (Integer) cur[1]);
            if (node.left != null) {
                q.offer(new Object[] { node.left, node.val, (Integer) cur[2] + 1 });
            }
            if (node.right != null) {
                q.offer(new Object[] { node.right, node.val, (Integer) cur[2] + 1 });
            }
        }

        return depth.get(x).equals(depth.get(y))
                && !parent.get(x).equals(parent.get(y));
    }

    // V3
    // IDEA: COMPARE THE ROOT-TO-NODE PATHS
    /**
     *  Find the path from the root to each target. Then
     *      same depth       <=>  the paths have equal length
     *      different parent <=>  the second-to-last entries differ
     *
     *  Slightly more work, but the paths also answer `what is their lowest common
     *  ancestor?`, which the depth/parent versions have already discarded.
     *
     *  time  = O(n)
     *  space = O(h)
     */
    public boolean isCousins_3(TreeNode root, int x, int y) {
        List<TreeNode> px = new ArrayList<>();
        List<TreeNode> py = new ArrayList<>();
        findPath(root, x, px);
        findPath(root, y, py);

        if (px.size() != py.size() || px.size() < 2) {
            return false;
        }
        // same depth, different parent
        return px.get(px.size() - 2) != py.get(py.size() - 2);
    }

    private boolean findPath(TreeNode node, int target, List<TreeNode> path) {
        if (node == null) {
            return false;
        }
        path.add(node);
        if (node.val == target) {
            return true;
        }
        if (findPath(node.left, target, path) || findPath(node.right, target, path)) {
            return true;
        }
        path.remove(path.size() - 1);
        return false;
    }

}
