package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/build-binary-expression-tree-from-infix-expression/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  1597. Build Binary Expression Tree From Infix Expression
 *  Hard
 *
 *  A binary expression tree is a kind of binary tree used to represent arithmetic
 *  expressions. Each node has either zero or two children. Leaf nodes correspond
 *  to operands (numbers), and internal nodes correspond to the operators
 *  '+', '-', '*' and '/'.
 *
 *  For each internal node with operator o, the infix expression it represents is
 *  (A o B), where A is the expression of the left subtree and B that of the right.
 *
 *  You are given a string s, an infix expression containing operands, the
 *  operators described above, and parentheses '(' and ')'.
 *
 *  Return any valid binary expression tree whose in-order traversal reproduces s
 *  after omitting the parentheses. Order of operations applies: parentheses
 *  first, then '*' and '/', then '+' and '-'. Operands must appear in the same
 *  order in s and in the in-order traversal.
 *
 *  Example 1:
 *   Input: s = "3*4-2*5"
 *   Output: [-,*,*,3,4,2,5]
 *
 *  Example 2:
 *   Input: s = "2-3/(5*2)+1"
 *   Output: [+,-,1,2,/,null,null,null,null,3,*,null,null,5,2]
 *
 *  Constraints:
 *   1 <= s.length <= 1000
 *   s consists of digits and the characters '+', '-', '*', '/', '(' and ')'.
 *   Operands in s are exactly 1 digit.
 *   It is guaranteed that s is a valid expression.
 */
public class BuildBinaryExpressionTreeFromInfixExpression {

    // LC "Node" for this problem (char val + left/right), defined locally since
    // the shared LeetCodeJava.DataStructure.Node is the clone-graph shape.
    public static class NodeX {
        public char val;
        public NodeX left;
        public NodeX right;

        public NodeX() {
            this.val = ' ';
        }

        public NodeX(char val) {
            this.val = val;
        }

        public NodeX(char val, NodeX left, NodeX right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    // V0
    // IDEA: shunting-yard (same skeleton as LC 224 Basic Calculator) with two
    //       stacks: operands become leaves, and whenever an operator of >= 
    //       precedence is already on top we pop and build a subtree.
    /**
     * time = O(n)
     * space = O(n)
     */
    public NodeX expTree(String s) {
        Deque<NodeX> nums = new ArrayDeque<>();
        Deque<Character> ops = new ArrayDeque<>();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (Character.isDigit(c)) {
                nums.push(new NodeX(c));
            } else if (c == '(') {
                ops.push(c);
            } else if (c == ')') {
                while (!ops.isEmpty() && ops.peek() != '(') {
                    build(nums, ops);
                }
                ops.pop(); // drop '('
            } else {
                // operator: flush everything with >= precedence
                while (!ops.isEmpty() && ops.peek() != '('
                        && precedence(ops.peek()) >= precedence(c)) {
                    build(nums, ops);
                }
                ops.push(c);
            }
        }

        while (!ops.isEmpty()) {
            build(nums, ops);
        }
        return nums.isEmpty() ? null : nums.pop();
    }

    private void build(Deque<NodeX> nums, Deque<Character> ops) {
        NodeX right = nums.pop();
        NodeX left = nums.pop();
        nums.push(new NodeX(ops.pop(), left, right));
    }

    private int precedence(char op) {
        if (op == '*' || op == '/') {
            return 2;
        }
        if (op == '+' || op == '-') {
            return 1;
        }
        return 0;
    }
}
