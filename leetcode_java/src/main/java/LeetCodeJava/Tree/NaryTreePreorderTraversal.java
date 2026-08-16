package LeetCodeJava.Tree;

// https://leetcode.com/problems/n-ary-tree-preorder-traversal/description/

import java.util.Collections;
import java.util.Iterator;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 589. N-ary Tree Preorder Traversal
 * Easy
 *
 * Given the root of an n-ary tree, return the preorder traversal of its nodes' values.
 *
 * Nary-Tree input serialization is represented in their level order traversal.
 * Each group of children is separated by the null value (See examples)
 *
 * Example 1:
 *
 * Input: root = [1,null,3,2,4,null,5,6]
 * Output: [1,3,5,6,2,4]
 *
 * Example 2:
 *
 * Input: root = [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,
 *                null,13,null,null,14]
 * Output: [1,2,3,6,7,11,14,4,8,12,5,9,13,10]
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
public class NaryTreePreorderTraversal {

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
    // IDEA: DFS (recursion) -> visit node, THEN its children left to right
    /**
     *  time  = O(n)
     *  space = O(h)  // h = tree height (recursion stack)
     */
    public List<Integer> preorder(Node root) {
        List<Integer> res = new ArrayList<>();
        dfs(root, res);
        return res;
    }

    private void dfs(Node node, List<Integer> res) {
        if (node == null) {
            return;
        }
        res.add(node.val);
        if (node.children != null) {
            for (Node child : node.children) {
                dfs(child, res);
            }
        }
    }

    // V0-1
    // IDEA: ITERATIVE with a STACK (answers the FOLLOW UP)
    /**
     *  NOTE !!! children are pushed in REVERSED order, so the LEFTMOST child
     *           is the one popped first -- pushing them in normal order would
     *           visit the children right to left.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    public List<Integer> preorder_0_1(Node root) {
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
                for (int i = node.children.size() - 1; i >= 0; i--) {
                    stack.push(node.children.get(i));
                }
            }
        }

        return res;
    }


    // V1
    // IDEA: EXPLICIT (node, childIndex) STACK -- a faithful recursion simulation
    /**
     *  V0-1 has to push the children REVERSED so the leftmost pops first. Carrying
     *  a child CURSOR per frame instead reproduces the recursion exactly: visit the
     *  node, then advance through its children one at a time.
     *
     *  No reversal, and the stack contents mirror the call stack a debugger would
     *  show.
     *
     *  time  = O(n)
     *  space = O(h)
     */
    public List<Integer> preorder_1(Node root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }

        Deque<Object[]> stack = new ArrayDeque<>(); // {node, next child index}
        stack.push(new Object[] { root, 0 });
        res.add(root.val);

        while (!stack.isEmpty()) {
            Object[] frame = stack.peek();
            Node node = (Node) frame[0];
            int idx = (Integer) frame[1];

            if (node.children == null || idx >= node.children.size()) {
                stack.pop();
                continue;
            }
            frame[1] = idx + 1;
            Node child = node.children.get(idx);
            res.add(child.val);                     // PRE-order: visit on the way down
            stack.push(new Object[] { child, 0 });
        }
        return res;
    }

    // V2
    // IDEA: FUNCTIONAL RECURSION (each call RETURNS its own list)
    /**
     *  No shared accumulator: every node returns `[val] ++ concat(children)`.
     *
     *  Slower because of the list concatenation, but it is referentially
     *  transparent -- no mutable state threading through the recursion at all.
     *
     *  time  = O(n^2) worst case (repeated list copying)
     *  space = O(n)
     */
    public List<Integer> preorder_2(Node root) {
        if (root == null) {
            return new ArrayList<>();
        }
        List<Integer> res = new ArrayList<>();
        res.add(root.val);
        if (root.children != null) {
            for (Node c : root.children) {
                res.addAll(preorder_2(c));
            }
        }
        return res;
    }

    // V3
    // IDEA: STACK OF ITERATORS
    /**
     *  Keep a stack of the children ITERATORS rather than of nodes plus indices.
     *
     *  The iterator carries the position for us, so the frame shrinks to one
     *  object -- and this is the shape that generalises to a lazy / streaming
     *  traversal where the children are produced on demand.
     *
     *  time  = O(n)
     *  space = O(h)
     */
    public List<Integer> preorder_3(Node root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }

        res.add(root.val);
        Deque<Iterator<Node>> stack = new ArrayDeque<>();
        stack.push(root.children == null
                ? Collections.<Node>emptyList().iterator()
                : root.children.iterator());

        while (!stack.isEmpty()) {
            Iterator<Node> it = stack.peek();
            if (!it.hasNext()) {
                stack.pop();
                continue;
            }
            Node child = it.next();
            res.add(child.val);
            stack.push(child.children == null
                    ? Collections.<Node>emptyList().iterator()
                    : child.children.iterator());
        }
        return res;
    }

}
