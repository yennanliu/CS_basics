package LeetCodeJava.Stack;

// https://leetcode.com/problems/parse-lisp-expression/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 736. Parse Lisp Expression
 * Hard
 *
 * You are given a string expression representing a Lisp-like expression to return
 * the integer value of.
 *
 * The syntax for these expressions is given as follows.
 *
 *   - An expression is either an integer, let expression, add expression, mult
 *     expression, or an assigned variable. Expressions always evaluate to a single
 *     integer.
 *   - (An integer could be positive or negative.)
 *   - A let expression takes the form "(let v1 e1 v2 e2 ... vn en expr)", where let is
 *     always the string "let", then there are one or more pairs of alternating variables
 *     and expressions, meaning that the first variable v1 is assigned the value of the
 *     expression e1, and so on sequentially; and then the value of this let expression
 *     is the value of the expression expr.
 *   - An add expression takes the form "(add e1 e2)".
 *   - A mult expression takes the form "(mult e1 e2)".
 *   - A variable starts with a lowercase letter, then zero or more lowercase letters or
 *     digits. The names "add", "let", and "mult" are protected and will never be used
 *     as variable names.
 *   - Finally, there is the concept of scope. When an expression of a variable name is
 *     evaluated, the innermost scope (in terms of parentheses) is checked first for the
 *     value of that variable, and then outer scopes are checked sequentially.
 *
 *
 * Example 1:
 *
 * Input: expression = "(let x 2 (mult x (let x 3 y 4 (add x y))))"
 * Output: 14
 * Explanation: In the expression (add x y), when checking for the value of the variable
 * x, we check from the innermost scope to the outermost. Since x = 3 is found first,
 * the value of x is 3.
 *
 * Example 2:
 *
 * Input: expression = "(let x 3 x 2 x)"
 * Output: 2
 * Explanation: Assignment in let statements is processed sequentially.
 *
 * Example 3:
 *
 * Input: expression = "(let x 1 y 2 x (add x y) (add x y))"
 * Output: 5
 * Explanation: The first (add x y) evaluates as 3, and is assigned to x.
 * The second (add x y) evaluates as 3+2 = 5.
 *
 *
 * Constraints:
 *
 * 1 <= expression.length <= 2000
 * There are no leading or trailing spaces in expression.
 * All tokens are separated by a single space in expression.
 * The answer and all intermediate calculations of that answer are guaranteed to fit
 * in a 32-bit integer.
 * The expression is guaranteed to be legal and evaluate to an integer.
 *
 */
public class ParseLispExpression {

    // V0
    // IDEA: RECURSION + SCOPE STACK
    /**
     *   scope: var -> STACK of values. Pushing on entry to a `let` and popping on
     *   exit gives exactly the `innermost scope wins` rule FOR FREE -- the top of
     *   scope[v] is always the value from the CLOSEST enclosing binding.
     *
     *   splitTokens() splits the body of a "( ... )" on TOP LEVEL spaces only
     *   (tracking paren DEPTH), so a nested sub-expression stays ONE single token.
     *
     *   time  = O(n^2) worst case (n = expression.length) -- each level re-slices its body
     *   space = O(n)
     */
    public int evaluate(String expression) {
        return evaluateExpr(expression, new HashMap<>());
    }

    private int evaluateExpr(String expr, Map<String, Deque<Integer>> scope) {

        // atom: an INTEGER literal, or a VARIABLE lookup
        if (expr.charAt(0) != '(') {
            char c = expr.charAt(0);
            if (c == '-' || Character.isDigit(c)) {
                return Integer.parseInt(expr);
            }
            return scope.get(expr).peek();
        }

        List<String> tokens = splitTokens(expr.substring(1, expr.length() - 1));
        String op = tokens.get(0);

        if (op.equals("add")) {
            return evaluateExpr(tokens.get(1), scope) + evaluateExpr(tokens.get(2), scope);
        }
        if (op.equals("mult")) {
            return evaluateExpr(tokens.get(1), scope) * evaluateExpr(tokens.get(2), scope);
        }

        /** NOTE !!!
         *
         *  "let v1 e1 v2 e2 ... expr"
         *  -> bind the pairs SEQUENTIALLY (a later e_i can see earlier v_j),
         *     then evaluate the trailing expression
         */
        List<String> bound = new ArrayList<>();
        int i = 1;
        while (i + 1 < tokens.size()) {
            String var = tokens.get(i);
            int val = evaluateExpr(tokens.get(i + 1), scope);
            scope.computeIfAbsent(var, k -> new ArrayDeque<>()).push(val);
            bound.add(var);
            i += 2;
        }

        int res = evaluateExpr(tokens.get(i), scope);

        // leaving the let -> DROP every binding it introduced
        for (String var : bound) {
            scope.get(var).pop();
        }
        return res;
    }

    /** split on spaces at paren depth 0 -> nested exprs stay WHOLE */
    private List<String> splitTokens(String body) {
        List<String> tokens = new ArrayList<>();
        int depth = 0;
        StringBuilder cur = new StringBuilder();

        for (int i = 0; i < body.length(); i++) {
            char ch = body.charAt(i);
            if (ch == '(') {
                depth += 1;
            } else if (ch == ')') {
                depth -= 1;
            }

            if (ch == ' ' && depth == 0) {
                tokens.add(cur.toString());
                cur = new StringBuilder();
            } else {
                cur.append(ch);
            }
        }
        tokens.add(cur.toString());
        return tokens;
    }

}
