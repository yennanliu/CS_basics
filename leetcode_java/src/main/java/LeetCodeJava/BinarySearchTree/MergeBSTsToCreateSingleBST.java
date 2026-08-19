package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/merge-bsts-to-create-single-bst/

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  1932. Merge BSTs to Create Single BST
 *  Hard
 *
 *  You are given n BST (binary search tree) root nodes for n separate BSTs stored
 *  in an array trees (0-indexed). Each BST in trees has at most 3 nodes, and no
 *  two roots have the same value. In one operation, you can:
 *   - Select two distinct indices i and j such that the value stored at one of the
 *     leaves of trees[i] is equal to the root value of trees[j].
 *   - Replace the leaf node in trees[i] with trees[j].
 *   - Remove trees[j] from trees.
 *
 *  Return the root of the resulting BST if it is possible to form a valid BST after
 *  performing n - 1 operations, or null if it is impossible to create a valid BST.
 *
 *  Example 1:
 *    Input: trees = [[2,1],[3,2,5],[5,4]]
 *    Output: [3,2,5,1,null,4]
 *
 *  Example 2:
 *    Input: trees = [[5,3,8],[3,2,6]]
 *    Output: []
 *    Explanation: the only possible merge does not yield a valid BST.
 *
 *  Example 3:
 *    Input: trees = [[5,4],[3]]
 *    Output: []
 *    Explanation: it is impossible to perform any operation.
 *
 *  Constraints:
 *    n == trees.length
 *    1 <= n <= 5 * 10^4
 *    The number of nodes in each tree is in the range [1, 3].
 *    Each node in the input may have children but no grandchildren.
 *    No two roots of trees have the same value.
 *    All the trees in the input are valid BSTs.
 *    1 <= TreeNode.val <= 5 * 10^4
 */
public class MergeBSTsToCreateSingleBST {

    // V0
    // IDEA: FIND THE UNIQUE OVERALL ROOT, THEN SPLICE + VALIDATE IN ONE DFS
    //       count how many times every value appears over ALL nodes (roots and
    //       leaves). the final root must be a tree root whose value is used exactly
    //       once - if it also appeared as some leaf, that tree would have to
    //       swallow it, so it could not be the top. there is at most one such
    //       candidate.
    //       then walk down from that candidate carrying (lo, hi) BST bounds.
    //       whenever we stand on a LEAF whose value is the root of a still unused
    //       tree, graft that tree in place (copy its two children onto the leaf)
    //       and re-examine the node.
    //       the merge is valid iff every visited node respects its bounds AND all
    //       n trees got consumed exactly once.
    //       NOTE: the merged chain can be 5*10^4 deep -> the DFS must be ITERATIVE.
    /**
     * time = O(N)
     * space = O(N)
     */
    public TreeNode canMerge(List<TreeNode> trees) {
        Map<Integer, Integer> cnt = new HashMap<>();
        Map<Integer, TreeNode> byRoot = new HashMap<>();

        for (int i = 0; i < trees.size(); i++) {
            TreeNode t = trees.get(i);
            byRoot.put(t.val, t);
            bump(cnt, t.val);
            if (t.left != null) {
                bump(cnt, t.left.val);
            }
            if (t.right != null) {
                bump(cnt, t.right.val);
            }
        }

        TreeNode root = null;
        for (int i = 0; i < trees.size(); i++) {
            TreeNode t = trees.get(i);
            if (cnt.get(t.val) == 1) {
                root = t;
                break;
            }
        }
        if (root == null) {
            return null;
        }

        byRoot.remove(root.val);
        int used = 1;

        Deque<TreeNode> nodeStack = new ArrayDeque<>();
        Deque<long[]> boundStack = new ArrayDeque<>();
        nodeStack.push(root);
        boundStack.push(new long[]{Long.MIN_VALUE, Long.MAX_VALUE});

        while (!nodeStack.isEmpty()) {
            TreeNode node = nodeStack.pop();
            long[] bound = boundStack.pop();
            long lo = bound[0];
            long hi = bound[1];

            if (!(lo < node.val && node.val < hi)) {
                return null;
            }

            if (node.left == null && node.right == null) {
                TreeNode sub = byRoot.remove(node.val);
                if (sub != null) {
                    node.left = sub.left;
                    node.right = sub.right;
                    used++;
                    // re-examine the node, now that it has children
                    nodeStack.push(node);
                    boundStack.push(new long[]{lo, hi});
                }
                continue;
            }
            if (node.left != null) {
                nodeStack.push(node.left);
                boundStack.push(new long[]{lo, node.val});
            }
            if (node.right != null) {
                nodeStack.push(node.right);
                boundStack.push(new long[]{node.val, hi});
            }
        }

        return used == trees.size() ? root : null;
    }

    private void bump(Map<Integer, Integer> cnt, int key) {
        Integer cur = cnt.get(key);
        cnt.put(key, cur == null ? 1 : cur + 1);
    }
}
