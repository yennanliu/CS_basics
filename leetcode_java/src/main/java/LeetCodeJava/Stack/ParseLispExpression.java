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


    // V1
    // IDEA: IMMUTABLE SCOPE (copy the map per let, no undo step)
    /**
     *  V0 pushes/pops a shared scope, which means every early return has to
     *  remember to unwind. Copying the scope on entry to a `let` makes the
     *  recursion PURE -- the caller's scope is untouched by construction.
     *
     *  O(vars) extra per let, but there is no unwind path to get wrong.
     *
     *  time  = O(n^2)
     *  space = O(n * vars)
     */
    public int evaluate_1(String expression) {
        return evalImmutable(expression, new HashMap<>());
    }

    private int evalImmutable(String expr, Map<String, Integer> scope) {
        if (expr.charAt(0) != '(') {
            char c = expr.charAt(0);
            if (c == '-' || Character.isDigit(c)) {
                return Integer.parseInt(expr);
            }
            return scope.get(expr);
        }

        List<String> tokens = splitTop(expr.substring(1, expr.length() - 1));
        String op = tokens.get(0);

        if (op.equals("add")) {
            return evalImmutable(tokens.get(1), scope) + evalImmutable(tokens.get(2), scope);
        }
        if (op.equals("mult")) {
            return evalImmutable(tokens.get(1), scope) * evalImmutable(tokens.get(2), scope);
        }

        Map<String, Integer> local = new HashMap<>(scope); // the COPY
        int i = 1;
        while (i + 1 < tokens.size()) {
            local.put(tokens.get(i), evalImmutable(tokens.get(i + 1), local));
            i += 2;
        }
        return evalImmutable(tokens.get(i), local);
    }

    /** split on top-level spaces (shared by the variants below) */
    private List<String> splitTop(String body) {
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

    // V2
    // IDEA: TOKENISE ONCE, THEN WALK A CURSOR (no repeated substring splitting)
    /**
     *  V0 re-slices the body string at every nesting level, which is where its
     *  O(n^2) comes from. Padding the parens and splitting on whitespace ONCE gives
     *  a flat token array; the evaluator then just advances a cursor.
     *
     *  -> linear in the token count rather than quadratic in the text length.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    private String[] toks736;
    private int cur736;

    public int evaluate_2(String expression) {
        this.toks736 = expression.replace("(", " ( ").replace(")", " ) ").trim().split("\\s+");
        this.cur736 = 0;
        return evalTokens(new HashMap<>());
    }

    private int evalTokens(Map<String, Integer> scope) {
        String tok = toks736[cur736];

        if (!tok.equals("(")) {
            cur736 += 1;
            char c = tok.charAt(0);
            if (c == '-' || Character.isDigit(c)) {
                return Integer.parseInt(tok);
            }
            return scope.get(tok);
        }

        cur736 += 1;                    // consume '('
        String op = toks736[cur736++];
        int res;

        if (op.equals("add")) {
            res = evalTokens(scope) + evalTokens(scope);
        } else if (op.equals("mult")) {
            res = evalTokens(scope) * evalTokens(scope);
        } else {
            Map<String, Integer> local = new HashMap<>(scope);
            // pairs continue while the token after the next one is not the closer
            while (!toks736[cur736].equals("(")
                    && cur736 + 1 < toks736.length
                    && !toks736[cur736 + 1].equals(")")) {
                String var = toks736[cur736++];
                local.put(var, evalTokens(local));
            }
            res = evalTokens(local);
        }

        cur736 += 1;                    // consume ')'
        return res;
    }

    // V3
    // IDEA: PARSE TO AN AST FIRST, THEN EVALUATE THE TREE
    /**
     *  Split the job in two: build a node tree, then walk it.
     *
     *  The tree can be evaluated MANY times under different scopes, printed, or
     *  optimised -- none of which the parse-as-you-evaluate versions allow.
     *
     *  time  = O(n) parse + O(nodes) per evaluation
     *  space = O(n)
     */
    private static class Lisp {
        String atom;               // null for a call node
        String op;
        List<Lisp> args = new ArrayList<>();
    }

    public int evaluate_3(String expression) {
        String[] toks = expression.replace("(", " ( ").replace(")", " ) ").trim().split("\\s+");
        int[] p = { 0 };
        Lisp root = parseLisp(toks, p);
        return evalAst(root, new HashMap<>());
    }

    private Lisp parseLisp(String[] toks, int[] p) {
        Lisp node = new Lisp();
        if (!toks[p[0]].equals("(")) {
            node.atom = toks[p[0]++];
            return node;
        }
        p[0] += 1;                       // '('
        node.op = toks[p[0]++];
        while (!toks[p[0]].equals(")")) {
            node.args.add(parseLisp(toks, p));
        }
        p[0] += 1;                       // ')'
        return node;
    }

    private int evalAst(Lisp node, Map<String, Integer> scope) {
        if (node.atom != null) {
            char c = node.atom.charAt(0);
            if (c == '-' || Character.isDigit(c)) {
                return Integer.parseInt(node.atom);
            }
            return scope.get(node.atom);
        }
        if (node.op.equals("add")) {
            return evalAst(node.args.get(0), scope) + evalAst(node.args.get(1), scope);
        }
        if (node.op.equals("mult")) {
            return evalAst(node.args.get(0), scope) * evalAst(node.args.get(1), scope);
        }

        Map<String, Integer> local = new HashMap<>(scope);
        int i = 0;
        while (i + 1 < node.args.size()) {
            local.put(node.args.get(i).atom, evalAst(node.args.get(i + 1), local));
            i += 2;
        }
        return evalAst(node.args.get(i), local);
    }

}
