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

    // cursor used by the V1 recursive-descent parser
    private int pos_1;

    // V1
    // IDEA: recursive-descent parser over the grammar
    //         expr   := term (('+'|'-') term)*
    //         term   := factor (('*'|'/') factor)*
    //         factor := digit | '(' expr ')'
    //       Precedence is encoded in the grammar itself instead of in a stack,
    //       and the left-folding loop gives left associativity.
    /**
     * time = O(n)
     * space = O(n)   // recursion depth on nested parentheses
     */
    public NodeX expTree_1(String s) {
        pos_1 = 0;
        return parseExpr_1(s);
    }

    private NodeX parseExpr_1(String s) {
        NodeX left = parseTerm_1(s);
        while (pos_1 < s.length() && (s.charAt(pos_1) == '+' || s.charAt(pos_1) == '-')) {
            char op = s.charAt(pos_1++);
            NodeX right = parseTerm_1(s);
            left = new NodeX(op, left, right);
        }
        return left;
    }

    private NodeX parseTerm_1(String s) {
        NodeX left = parseFactor_1(s);
        while (pos_1 < s.length() && (s.charAt(pos_1) == '*' || s.charAt(pos_1) == '/')) {
            char op = s.charAt(pos_1++);
            NodeX right = parseFactor_1(s);
            left = new NodeX(op, left, right);
        }
        return left;
    }

    private NodeX parseFactor_1(String s) {
        char c = s.charAt(pos_1);
        if (c == '(') {
            pos_1++;                      // consume '('
            NodeX inner = parseExpr_1(s);
            pos_1++;                      // consume ')'
            return inner;
        }
        pos_1++;
        return new NodeX(c);
    }

    // V2
    // IDEA: divide & conquer on the substring. Strip redundant wrapping parens,
    //       then split at the RIGHTMOST top-level (depth 0) operator of the
    //       LOWEST precedence -- that operator is evaluated last, so it is the
    //       root -- and recurse on the two halves.
    /**
     * time = O(n^2) worst case (rescans each substring)
     * space = O(n)
     */
    public NodeX expTree_2(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        return buildDC_2(s, 0, s.length() - 1);
    }

    private NodeX buildDC_2(String s, int lo, int hi) {
        // "(...)" wrapping the whole range adds nothing -> peel it off
        while (lo < hi && s.charAt(lo) == '(' && closesAt_2(s, lo, hi)) {
            lo++;
            hi--;
        }
        if (lo == hi) {
            return new NodeX(s.charAt(lo));
        }

        int addSub = -1;
        int mulDiv = -1;
        int depth = 0;
        for (int i = lo; i <= hi; i++) {
            char c = s.charAt(i);
            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
            } else if (depth == 0) {
                if (c == '+' || c == '-') {
                    addSub = i;           // keep the rightmost -> left associative
                } else if (c == '*' || c == '/') {
                    mulDiv = i;
                }
            }
        }

        int split = addSub != -1 ? addSub : mulDiv;
        return new NodeX(s.charAt(split),
                buildDC_2(s, lo, split - 1),
                buildDC_2(s, split + 1, hi));
    }

    // does the '(' at index lo close exactly at index hi ?
    private boolean closesAt_2(String s, int lo, int hi) {
        int depth = 0;
        for (int i = lo; i <= hi; i++) {
            if (s.charAt(i) == '(') {
                depth++;
            } else if (s.charAt(i) == ')') {
                depth--;
                if (depth == 0) {
                    return i == hi;
                }
            }
        }
        return false;
    }
}
