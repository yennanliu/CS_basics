package LeetCodeJava.Tree;

// https://leetcode.com/problems/n-ary-tree-preorder-traversal/description/

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

}
