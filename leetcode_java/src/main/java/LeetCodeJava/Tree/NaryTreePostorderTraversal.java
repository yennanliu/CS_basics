package LeetCodeJava.Tree;

// https://leetcode.com/problems/n-ary-tree-postorder-traversal/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * 590. N-ary Tree Postorder Traversal
 * Medium
 *
 * Given the root of an n-ary tree, return the postorder traversal of its nodes' values.
 *
 * Nary-Tree input serialization is represented in their level order traversal.
 * Each group of children is separated by the null value (See examples)
 *
 * Example 1:
 *
 * Input: root = [1,null,3,2,4,null,5,6]
 * Output: [5,6,3,2,4,1]
 *
 * Example 2:
 *
 * Input: root = [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,
 *                null,13,null,null,14]
 * Output: [2,6,14,11,7,3,12,8,4,13,9,10,5,1]
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [0, 10^4].
 * 0 <= Node.val <= 10^4
 * The height of the n-ary tree is less than or equal to 1000.
 *
 *
 * Follow up: Recursive solution is trivial, could you do it iteratively?
 *
 */
public class NaryTreePostorderTraversal {

    // Definition for a Node (offered by LC platform)
    class Node {
        public int val;
        public List<Node> children;

        public Node() {}

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, List<Node> _children) {
            val = _val;
            children = _children;
        }
    }

    // V0
    // IDEA: DFS (recursion) -> visit ALL children left to right, THEN the node itself
    /**
     *  time  = O(n)
     *  space = O(h)  // h = tree height (recursion stack)
     */
    public List<Integer> postorder(Node root) {
        List<Integer> res = new ArrayList<>();
        dfs(root, res);
        return res;
    }

    private void dfs(Node node, List<Integer> res) {
        if (node == null) {
            return;
        }
        if (node.children != null) {
            for (Node child : node.children) {
                dfs(child, res);
            }
        }
        res.add(node.val);
    }

    // V0-1
    // IDEA: ITERATIVE with a STACK (answers the FOLLOW UP)
    /**
     *  do a `root - right ... left` traversal (children pushed in NORMAL order,
     *  so the LAST child is popped first), then REVERSE the result
     *  -> that gives `left ... right - root`, which IS postorder.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    public List<Integer> postorder_0_1(Node root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }

        Deque<Node> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            Node node = stack.pop();
            res.add(node.val);

            if (node.children != null) {
                // NORMAL order push -> last child is popped (visited) first
                for (Node child : node.children) {
                    stack.push(child);
                }
            }
        }

        Collections.reverse(res);
        return res;
    }


    // V1
    // IDEA: EXPLICIT (node, childIndex) STACK -- TRUE postorder, no reversal
    /**
     *  V0-1 produces the answer backwards and reverses it. Carrying a child cursor
     *  lets us emit each node only AFTER its children are exhausted, which is
     *  postorder in the true order.
     *
     *  Matters when the output is streamed and cannot be reversed at the end.
     *
     *  time  = O(n)
     *  space = O(h)
     */
    public List<Integer> postorder_1(Node root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }

        Deque<Object[]> stack = new ArrayDeque<>(); // {node, next child index}
        stack.push(new Object[] { root, 0 });

        while (!stack.isEmpty()) {
            Object[] frame = stack.peek();
            Node node = (Node) frame[0];
            int idx = (Integer) frame[1];

            if (node.children == null || idx >= node.children.size()) {
                res.add(node.val);      // all children done -> emit now
                stack.pop();
                continue;
            }
            frame[1] = idx + 1;
            stack.push(new Object[] { node.children.get(idx), 0 });
        }
        return res;
    }

    // V2
    // IDEA: TWO STACKS
    /**
     *  Push nodes onto stack A; every pop goes onto stack B and its children go
     *  back onto A. Draining B yields postorder.
     *
     *  The classic `two stacks` trick: it is the reverse-preorder idea of V0-1 but
     *  with the reversal done by the second STACK instead of by
     *  Collections.reverse.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    public List<Integer> postorder_2(Node root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }

        Deque<Node> a = new ArrayDeque<>();
        Deque<Node> b = new ArrayDeque<>();
        a.push(root);

        while (!a.isEmpty()) {
            Node node = a.pop();
            b.push(node);
            if (node.children != null) {
                for (Node c : node.children) {
                    a.push(c);
                }
            }
        }
        while (!b.isEmpty()) {
            res.add(b.pop().val);
        }
        return res;
    }

    // V3
    // IDEA: FUNCTIONAL RECURSION (each call RETURNS its own list)
    /**
     *  concat(children) ++ [val], with no shared accumulator.
     *
     *  time  = O(n^2) worst case
     *  space = O(n)
     */
    public List<Integer> postorder_3(Node root) {
        if (root == null) {
            return new ArrayList<>();
        }
        List<Integer> res = new ArrayList<>();
        if (root.children != null) {
            for (Node c : root.children) {
                res.addAll(postorder_3(c));
            }
        }
        res.add(root.val);
        return res;
    }

}
