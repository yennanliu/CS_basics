package LeetCodeJava.Design;

// https://leetcode.com/problems/design-an-expression-tree-with-evaluate-function/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  1628. Design an Expression Tree With Evaluate Function
 *  Medium
 *
 *  Given the postfix tokens of an arithmetic expression, build and return the binary
 *  expression tree that represents this expression.
 *
 *  Postfix notation is a notation for writing arithmetic expressions in which the operands
 *  (numbers) appear before their operators. For example, the postfix tokens of the expression
 *  4*(5-(7+2)) are represented in the array postfix = ["4","5","7","2","+","-","*"].
 *
 *  Leaf nodes (0 children) correspond to operands (numbers), and internal nodes (2 children)
 *  correspond to the operators '+', '-', '*' and '/'.
 *
 *  This class plays the role of the TreeBuilder in the original problem: buildTree() returns
 *  the root Node, and Node.evaluate() computes the value of the expression tree.
 *
 *  Example 1:
 *  Input: s = ["3","4","+","2","*","7","/"]
 *  Output: 2      // ((3+4)*2)/7 = 14/7 = 2
 *
 *  Example 2:
 *  Input: s = ["4","5","2","7","+","-","*"]
 *  Output: -16    // 4*(5-(2+7)) = 4*(-4) = -16
 *
 *  Constraints:
 *
 *   1 <= s.length < 100, s.length is odd
 *   s consists of numbers and the characters '+', '-', '*', and '/'
 *   It is guaranteed that s is a valid expression, no division by zero
 */
public class DesignAnExpressionTreeWithEvaluateFunction {

    /** The Node interface required by the problem. */
    public static abstract class Node {
        public abstract int evaluate();
    }

    /** Concrete node: an operand leaf, or an operator with 2 children. */
    public static class ExpNode extends Node {
        public String val;
        public Node left;
        public Node right;

        public ExpNode(String val) {
            this.val = val;
        }

        public ExpNode(String val, Node left, Node right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }

        /**
         * time = O(n), n = number of nodes
         * space = O(h), h = tree height (recursion stack)
         */
        @Override
        public int evaluate() {
            if (left == null && right == null) {
                return Integer.parseInt(val);
            }
            int l = left.evaluate();
            int r = right.evaluate();
            if ("+".equals(val)) {
                return l + r;
            } else if ("-".equals(val)) {
                return l - r;
            } else if ("*".equals(val)) {
                return l * r;
            }
            return l / r;
        }
    }

    // V0
    // IDEA: STACK OVER POSTFIX TOKENS
    //       operand -> push leaf; operator -> pop right, pop left, push new subtree.
    /**
     * time = O(n), n = postfix.length
     * space = O(n)
     */
    public Node buildTree(String[] postfix) {
        Deque<Node> stack = new ArrayDeque<>();
        for (String token : postfix) {
            if (isOperator(token)) {
                Node right = stack.pop();
                Node left = stack.pop();
                stack.push(new ExpNode(token, left, right));
            } else {
                stack.push(new ExpNode(token));
            }
        }
        return stack.peek();
    }

    private boolean isOperator(String token) {
        return token.length() == 1
                && ("+".equals(token) || "-".equals(token) || "*".equals(token) || "/".equals(token));
    }
}
