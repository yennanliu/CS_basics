package LeetCodeJava.Tree;

// https://leetcode.com/problems/lowest-common-ancestor-of-deepest-leaves/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

/**
 * 1123. Lowest Common Ancestor of Deepest Leaves
 * Medium
 *
 * Given the root of a binary tree, return the lowest common ancestor of its deepest leaves.
 *
 * Recall that:
 *
 * - The node of a binary tree is a leaf if and only if it has no children.
 * - The depth of the root of the tree is 0. If the depth of a node is d, the depth of each
 *   of its children is d + 1.
 * - The lowest common ancestor of a set S of nodes, is the node A with the largest depth
 *   such that every node in S is in the subtree with root A.
 *
 *
 * Example 1:
 *
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4]
 * Output: [2,7,4]
 * Explanation: We return the node with value 2, colored in yellow in the diagram.
 * The nodes coloured in blue are the deepest leaf-nodes of the tree.
 * Note that nodes 6, 0, and 8 are also leaf nodes, but the depth of them is 2, but the
 * depth of nodes 7 and 4 is 3.
 *
 * Example 2:
 *
 * Input: root = [1]
 * Output: [1]
 * Explanation: The root is the deepest node in the tree, and it's the lca of itself.
 *
 * Example 3:
 *
 * Input: root = [0,1,3,null,2]
 * Output: [2]
 * Explanation: The deepest leaf node in the tree is 2, the lca of one node is itself.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree will be in the range [1, 1000].
 * 0 <= Node.val <= 1000
 * The values of the nodes in the tree are unique.
 *
 *
 * Note: This question is the same as 865: https://leetcode.com/problems/smallest-subtree-with-all-the-deepest-nodes/
 *
 */
public class LowestCommonAncestorOfDeepestLeaves {

    // V0
    // IDEA: POST-ORDER DFS, EACH CALL RETURNS (SUBTREE HEIGHT, LCA OF ITS DEEPEST LEAVES)
    /**
     *  left taller  -> every deepest leaf is on the left, so pass the left's LCA up
     *  right taller -> same, on the right
     *  equal        -> deepest leaves sit on BOTH sides, so THIS node is their LCA
     *
     *  e.g. [3,5,1,6,2,0,8,null,null,7,4]
     *    node 2: h(7)=1, h(4)=1 -> equal        -> LCA = 2, h = 2
     *    node 5: h(6)=1, h(2)=2 -> right taller -> LCA = 2, h = 3
     *    node 3: h(5)=3, h(1)=2 -> left taller  -> LCA = 2
     */
    /**
     * time = O(N)
     * space = O(H)
     */
    public TreeNode lcaDeepestLeaves(TreeNode root) {
        return heightAndLca(root).lca;
    }

    /** A subtree's height together with the LCA of its deepest leaves. */
    private static class Result {
        int height;
        TreeNode lca;

        Result(int height, TreeNode lca) {
            this.height = height;
            this.lca = lca;
        }
    }

    /** Post-order: both children report before this node decides. */
    private Result heightAndLca(TreeNode node) {
        // an empty subtree has height 0 and no leaves
        if (node == null) {
            return new Result(0, null);
        }

        Result left = heightAndLca(node.left);
        Result right = heightAndLca(node.right);

        if (left.height > right.height) {
            return new Result(left.height + 1, left.lca);
        }
        if (right.height > left.height) {
            return new Result(right.height + 1, right.lca);
        }

        // NOTE !!! equal heights -> deepest leaves are split across both sides
        //          (or this is a leaf), so this node is their LCA
        return new Result(left.height + 1, node);
    }

    // V1
    // IDEA: TWO PASSES -- FIND THE MAX DEPTH, THEN LC 236's LCA SEARCH FOR THE NODES AT IT
    /**
     *  NOTE: same O(N) but two traversals -- V0 is the one to give in an interview;
     *        this is the natural first idea if you already know LC 236.
     */
    /**
     * time = O(N)
     * space = O(H)
     */
    public TreeNode lcaDeepestLeaves_1(TreeNode root) {
        if (root == null) {
            return null;
        }
        int maxDepth = maxDepth(root, 0);
        return findLca(root, 0, maxDepth);
    }

    /** Returns the largest depth of any node in node's subtree. */
    private int maxDepth(TreeNode node, int depth) {
        if (node == null) {
            // the parent was the last real node on this path
            return depth - 1;
        }
        return Math.max(maxDepth(node.left, depth + 1), maxDepth(node.right, depth + 1));
    }

    /** Returns the LCA of the nodes at maxDepth inside node's subtree, or null if none. */
    private TreeNode findLca(TreeNode node, int depth, int maxDepth) {
        if (node == null) {
            return null;
        }
        // a deepest node is the LCA of itself
        if (depth == maxDepth) {
            return node;
        }

        TreeNode leftFound = findLca(node.left, depth + 1, maxDepth);
        TreeNode rightFound = findLca(node.right, depth + 1, maxDepth);

        // deepest nodes on both sides meet here
        if (leftFound != null && rightFound != null) {
            return node;
        }
        // otherwise every deepest node (if any) is on one side
        if (leftFound != null) {
            return leftFound;
        }
        return rightFound;
    }

    // V2
    // IDEA: BFS LEVEL ORDER + PARENT MAP, THEN CLIMB THE LAST LEVEL UNTIL IT MERGES
    /**
     *  BFS ends holding the last level -- exactly the deepest leaves. Replace each by its
     *  parent, one level at a time; they climb in lockstep, so the first time the set has
     *  ONE node, that node is their LCA.
     *
     *  e.g. last level {7, 4} -> parents {2} -> answer 2
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public TreeNode lcaDeepestLeaves_2(TreeNode root) {
        if (root == null) {
            return null;
        }

        Map<TreeNode, TreeNode> parent = new HashMap<>();
        parent.put(root, null);
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        List<TreeNode> lastLevel = new ArrayList<>();

        // level-order traversal; after the loop lastLevel is the deepest level
        while (!queue.isEmpty()) {
            lastLevel = new ArrayList<>(queue);
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                if (node.left != null) {
                    parent.put(node.left, node);
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    parent.put(node.right, node);
                    queue.offer(node.right);
                }
            }
        }

        // climb all deepest nodes together until they collapse into one
        Set<TreeNode> current = new HashSet<>(lastLevel);
        while (current.size() > 1) {
            Set<TreeNode> nextLevel = new HashSet<>();
            for (TreeNode node : current) {
                nextLevel.add(parent.get(node));
            }
            current = nextLevel;
        }

        return current.iterator().next();
    }
}
