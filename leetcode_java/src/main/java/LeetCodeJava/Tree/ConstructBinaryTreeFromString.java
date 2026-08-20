package LeetCodeJava.Tree;

// https://leetcode.com/problems/construct-binary-tree-from-string/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.Stack;

/**
 *  536. Construct Binary Tree from String
 *  Medium
 *
 *  You need to construct a binary tree from a string consisting of
 *  parenthesis and integers.
 *
 *  The whole input represents a binary tree. It contains an integer followed
 *  by zero, one or two pairs of parenthesis. The integer represents the root's
 *  value and a pair of parenthesis contains a child binary tree with the same
 *  structure.
 *
 *  You always start to construct the left child node of the parent first if it
 *  exists.
 *
 *  Example 1:
 *
 *  Input: s = "4(2(3)(1))(6(5))"
 *  Output: [4,2,6,3,1,5]
 *
 *  Example 2:
 *
 *  Input: s = "4(2(3)(1))(6(5)(7))"
 *  Output: [4,2,6,3,1,5,7]
 *
 *  Example 3:
 *
 *  Input: s = "-4(2(3)(1))(6(5)(7))"
 *  Output: [-4,2,6,3,1,5,7]
 *
 *  Constraints:
 *
 *  0 <= s.length <= 3 * 10^4
 *  s consists of digits, '(', ')' and '-' only.
 */
public class ConstructBinaryTreeFromString {

    // shared cursor for V0's recursive descent
    private int idx;

    // V0
    // IDEA: recursive descent with a shared cursor
    //       -> read the (possibly negative, possibly multi-digit) number,
    //          then if next char is '(' parse the left subtree, and if the
    //          char after its ')' is '(' parse the right subtree.
    /**
     * time = O(n)
     * space = O(h)  // recursion depth, h = tree height (O(n) worst case)
     */
    public TreeNode str2tree(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        this.idx = 0;
        return build(s);
    }

    private TreeNode build(String s) {
        // 1) read the value (handle leading '-' and multi digit numbers)
        int start = idx;
        if (idx < s.length() && s.charAt(idx) == '-') {
            idx++;
        }
        while (idx < s.length() && Character.isDigit(s.charAt(idx))) {
            idx++;
        }
        if (start == idx) {
            // nothing consumed -> no node here (e.g. "()")
            return null;
        }
        TreeNode node = new TreeNode(Integer.parseInt(s.substring(start, idx)));

        // 2) left child
        if (idx < s.length() && s.charAt(idx) == '(') {
            idx++;              // skip '('
            node.left = build(s);
            idx++;              // skip ')'
        }

        // 3) right child
        if (idx < s.length() && s.charAt(idx) == '(') {
            idx++;              // skip '('
            node.right = build(s);
            idx++;              // skip ')'
        }

        return node;
    }

    // V1
    // IDEA: iterative, stack based (avoids deep recursion on a skewed input)
    /**
     * time = O(n)
     * space = O(h)
     */
    public TreeNode str2tree_1(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        Stack<TreeNode> st = new Stack<>();
        int i = 0;
        int n = s.length();
        while (i < n) {
            char c = s.charAt(i);
            if (c == ')') {
                st.pop();
                i++;
            } else if (c == '(') {
                i++;
            } else {
                // parse number
                int start = i;
                if (s.charAt(i) == '-') {
                    i++;
                }
                while (i < n && Character.isDigit(s.charAt(i))) {
                    i++;
                }
                TreeNode cur = new TreeNode(Integer.parseInt(s.substring(start, i)));
                if (!st.isEmpty()) {
                    TreeNode parent = st.peek();
                    if (parent.left == null) {
                        parent.left = cur;
                    } else {
                        parent.right = cur;
                    }
                }
                st.push(cur);
            }
        }
        // the root is the bottom of the stack (never popped, its ')' is absent)
        TreeNode root = null;
        while (!st.isEmpty()) {
            root = st.pop();
        }
        return root;
    }

}
