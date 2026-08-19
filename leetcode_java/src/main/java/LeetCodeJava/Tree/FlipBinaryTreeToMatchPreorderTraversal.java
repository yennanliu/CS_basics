package LeetCodeJava.Tree;

// https://leetcode.com/problems/flip-binary-tree-to-match-preorder-traversal/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 *  971. Flip Binary Tree To Match Preorder Traversal
 *  Medium
 *
 *  You are given the root of a binary tree with n nodes, where each node is
 *  uniquely assigned a value from 1 to n. You are also given a sequence of n
 *  values `voyage`, which is the desired pre-order traversal of the tree.
 *
 *  Any node in the binary tree can be flipped by swapping its left and right
 *  subtrees. For example, flipping node 1 will have the following effect:
 *
 *  Flip the smallest number of nodes so that the pre-order traversal of the
 *  tree matches `voyage`.
 *
 *  Return a list of the values of all flipped nodes. You may return the answer
 *  in any order. If it is impossible to flip the nodes in the tree to make the
 *  pre-order traversal match `voyage`, return the list [-1].
 *
 *  Example 1:
 *    Input: root = [1,2], voyage = [2,1]
 *    Output: [-1]
 *
 *  Example 2:
 *    Input: root = [1,2,3], voyage = [1,3,2]
 *    Output: [1]
 *
 *  Example 3:
 *    Input: root = [1,2,3], voyage = [1,2,3]
 *    Output: []
 *
 *  Constraints:
 *    The number of nodes in the tree is n.
 *    n == voyage.length
 *    1 <= n <= 100
 *    1 <= Node.val, voyage[i] <= n
 *    All the values in the tree are unique.
 *    All the values in voyage are unique.
 */
public class FlipBinaryTreeToMatchPreorderTraversal {

    // V0
    // IDEA: PRE-ORDER DFS + greedy flip
    //       walk the tree in pre-order while consuming `voyage`;
    //       whenever the next expected value is NOT the left child,
    //       we must flip the current node (visit right subtree first).
    /**
     * time = O(N)
     * space = O(H)   // recursion stack
     */
    private int idx;
    private List<Integer> flipped;

    public List<Integer> flipMatchVoyage(TreeNode root, int[] voyage) {
        this.idx = 0;
        this.flipped = new ArrayList<>();

        if (!dfs(root, voyage)) {
            List<Integer> bad = new ArrayList<>();
            bad.add(-1);
            return bad;
        }
        return this.flipped;
    }

    private boolean dfs(TreeNode node, int[] voyage) {
        if (node == null) {
            return true;
        }
        if (this.idx >= voyage.length || node.val != voyage[this.idx]) {
            return false;
        }
        this.idx++;

        // next expected value does not match the left child -> flip current node
        if (node.left != null
                && this.idx < voyage.length
                && node.left.val != voyage[this.idx]) {
            this.flipped.add(node.val);
            return dfs(node.right, voyage) && dfs(node.left, voyage);
        }

        return dfs(node.left, voyage) && dfs(node.right, voyage);
    }
}
