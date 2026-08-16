package LeetCodeJava.Tree;

// https://leetcode.com/problems/maximum-binary-tree-ii/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import LeetCodeJava.DataStructure.TreeNode;

/**
 * 998. Maximum Binary Tree II
 * Medium
 *
 * A maximum tree is a tree where every node has a value greater than any other value
 * in its subtree.
 *
 * You are given the root of a maximum binary tree and an integer val.
 *
 * Just as in the previous problem, the given tree was constructed from a list a
 * (root = Construct(a)) recursively with the following Construct(a) routine:
 *
 * If a is empty, return null.
 * Otherwise, let a[i] be the largest element of a. Create a root node with the value a[i].
 * The left child of root will be Construct([a[0], a[1], ..., a[i - 1]]).
 * The right child of root will be Construct([a[i + 1], a[i + 2], ..., a[a.length - 1]]).
 * Return root.
 *
 * Note that we were not given a directly, only a root node root = Construct(a).
 *
 * Suppose b is a copy of a with the value val appended to it. It is guaranteed that b
 * has unique values.
 *
 * Return Construct(b).
 *
 * Example 1:
 *
 * Input: root = [4,1,3,null,null,2], val = 5
 * Output: [5,4,null,1,3,null,null,2]
 * Explanation: a = [1,4,2,3], b = [1,4,2,3,5]
 *
 * Example 2:
 *
 * Input: root = [5,2,4,null,1], val = 3
 * Output: [5,2,4,null,1,null,3]
 * Explanation: a = [2,1,5,4], b = [2,1,5,4,3]
 *
 * Example 3:
 *
 * Input: root = [5,2,3,null,1], val = 4
 * Output: [5,2,4,null,1,3]
 * Explanation: a = [2,1,5,3], b = [2,1,5,3,4]
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 100].
 * 1 <= Node.val <= 100
 * All the values of the tree are unique.
 * 1 <= val <= 100
 *
 */
public class MaximumBinaryTree2 {

    // V0
    // IDEA: WALK THE RIGHT SPINE (val is appended at the END of the list)
    /**
     *  KEY OBSERVATION: `val` is the LAST element of b, so in Construct(b) it can
     *  only ever end up on the RIGHT SPINE of the tree (root -> right -> right ...).
     *  Everything that comes BEFORE it in the list stays to its LEFT.
     *
     *  So walk down the right spine until we meet the first node whose value is
     *  SMALLER than val. That WHOLE subtree becomes the LEFT child of the new node,
     *  and the new node takes its place.
     *
     *  time  = O(h)  // h = tree height (length of the right spine at worst)
     *  space = O(h)  // recursion stack
     */
    public TreeNode insertIntoMaxTree(TreeNode root, int val) {
        // val is bigger than everything here -> it becomes the new subtree root
        if (root == null || val > root.val) {
            TreeNode node = new TreeNode(val);
            node.left = root;
            return node;
        }

        // otherwise val belongs somewhere further down the RIGHT spine
        root.right = insertIntoMaxTree(root.right, val);
        return root;
    }


    // V1
    // IDEA: ITERATIVE WALK DOWN THE RIGHT SPINE
    /**
     *  Same right-spine insight as V0, but done with a `prev` pointer instead of
     *  recursion, so the stack depth is constant.
     *
     *  time  = O(h)
     *  space = O(1)
     */
    public TreeNode insertIntoMaxTree_1(TreeNode root, int val) {
        if (root == null || val > root.val) {
            TreeNode node = new TreeNode(val);
            node.left = root;
            return node;
        }

        TreeNode prev = root;
        TreeNode cur = root.right;
        while (cur != null && cur.val > val) {
            prev = cur;
            cur = cur.right;
        }

        TreeNode node = new TreeNode(val);
        node.left = cur;      // the subtree it displaces
        prev.right = node;
        return root;
    }

    // V2
    // IDEA: REBUILD FROM THE ORIGINAL ARRAY
    /**
     *  Recover the list `a` by an IN-ORDER traversal (Construct preserves in-order),
     *  append val, and run Construct again from scratch.
     *
     *  O(n^2) rather than O(h), but it proves the right-spine argument correct by
     *  literally re-running the construction the problem describes.
     *
     *  time  = O(n^2)
     *  space = O(n)
     */
    public TreeNode insertIntoMaxTree_2(TreeNode root, int val) {
        List<Integer> a = new ArrayList<>();
        inorder(root, a);
        a.add(val);
        return construct(a, 0, a.size() - 1);
    }

    private void inorder(TreeNode node, List<Integer> out) {
        if (node == null) {
            return;
        }
        inorder(node.left, out);
        out.add(node.val);
        inorder(node.right, out);
    }

    private TreeNode construct(List<Integer> a, int lo, int hi) {
        if (lo > hi) {
            return null;
        }
        int best = lo;
        for (int i = lo + 1; i <= hi; i++) {
            if (a.get(i) > a.get(best)) {
                best = i;
            }
        }
        TreeNode node = new TreeNode(a.get(best));
        node.left = construct(a, lo, best - 1);
        node.right = construct(a, best + 1, hi);
        return node;
    }

    // V3
    // IDEA: MONOTONIC STACK REBUILD (the standard `maximum binary tree` build)
    /**
     *  Recover the array, then build with the classic decreasing stack: pop while
     *  the top is smaller (those become the new node's LEFT subtree), and attach
     *  the new node as the RIGHT child of whatever remains.
     *
     *  O(n) construction instead of the O(n^2) divide-and-conquer of V2 -- the
     *  general technique behind LC 654.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    public TreeNode insertIntoMaxTree_3(TreeNode root, int val) {
        List<Integer> a = new ArrayList<>();
        inorder(root, a);
        a.add(val);

        Deque<TreeNode> stack = new ArrayDeque<>(); // values strictly decreasing
        for (int v : a) {
            TreeNode node = new TreeNode(v);
            while (!stack.isEmpty() && stack.peek().val < v) {
                node.left = stack.pop();
            }
            if (!stack.isEmpty()) {
                stack.peek().right = node;
            }
            stack.push(node);
        }

        TreeNode res = null;
        while (!stack.isEmpty()) {
            res = stack.pop();
        }
        return res;
    }

}
