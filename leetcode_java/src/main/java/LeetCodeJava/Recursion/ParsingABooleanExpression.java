package LeetCodeJava.Recursion;

// https://leetcode.com/problems/parsing-a-boolean-expression/

/**
 *  1106. Parsing A Boolean Expression
 *  Hard
 *
 *  A boolean expression is an expression that evaluates to either true or false.
 *  It can be in one of the following shapes:
 *   - 't' that evaluates to true.
 *   - 'f' that evaluates to false.
 *   - '!(subExpr)' that evaluates to the logical NOT of the inner expression subExpr.
 *   - '&(subExpr1, subExpr2, ..., subExprn)' that evaluates to the logical AND of
 *     the inner expressions subExpr1 ... subExprn where n >= 1.
 *   - '|(subExpr1, subExpr2, ..., subExprn)' that evaluates to the logical OR of
 *     the inner expressions subExpr1 ... subExprn where n >= 1.
 *
 *  Given a string expression that represents a boolean expression, return the
 *  evaluation of that expression.
 *
 *  It is guaranteed that the given expression is valid and follows the given rules.
 *
 *  Example 1:
 *    Input: expression = "&(|(f))"
 *    Output: false
 *    Explanation: First, evaluate |(f) --> f. The expression is now "&(f)".
 *                 Then, evaluate &(f) --> f. Finally, return false.
 *
 *  Example 2:
 *    Input: expression = "|(f,f,f,t)"
 *    Output: true
 *    Explanation: The evaluation of (false OR false OR false OR true) is true.
 *
 *  Example 3:
 *    Input: expression = "!(&(f,t))"
 *    Output: true
 *
 *  Constraints:
 *    1 <= expression.length <= 2 * 10^4
 *    expression[i] is one of the characters: '(', ')', '&', '|', '!', 't', 'f', ','
 */
public class ParsingABooleanExpression {

    // V0
    // IDEA: STACK - EVALUATE THE INNER MOST GROUP ON EVERY ')'
    //       push 't' / 'f' / operators on a stack. on ')' pop the boolean literals
    //       of the current group while counting true / false, pop the operator, and
    //       push back the single resulting literal.
    //       '(' and ',' are pure separators -> ignored.
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean parseBoolExpr(String expression) {
        int n = expression.length();
        char[] stack = new char[n];
        int top = 0;

        for (int idx = 0; idx < n; idx++) {
            char c = expression.charAt(idx);
            if (c == 't' || c == 'f' || c == '!' || c == '&' || c == '|') {
                stack[top++] = c;
            } else if (c == ')') {
                int trueCnt = 0;
                int falseCnt = 0;
                // NOTE !!! drain ALL literals of the current group
                while (top > 0 && (stack[top - 1] == 't' || stack[top - 1] == 'f')) {
                    if (stack[--top] == 't') {
                        trueCnt++;
                    } else {
                        falseCnt++;
                    }
                }
                char op = stack[--top];
                char res;
                if (op == '!') {
                    res = falseCnt > 0 ? 't' : 'f';
                } else if (op == '&') {
                    res = falseCnt > 0 ? 'f' : 't';
                } else { // op == '|'
                    res = trueCnt > 0 ? 't' : 'f';
                }
                stack[top++] = res;
            }
            // '(' and ',' -> skip
        }

        return stack[top - 1] == 't';
    }

    // V1
    // IDEA: RECURSIVE DESCENT PARSER (SHARED INDEX POINTER)
    //       parse() consumes exactly ONE sub expression starting at `pos` and
    //       returns its value; the operator's operand list is read until ')'.
    /**
     * time = O(N)
     * space = O(N)   // recursion depth = nesting depth
     */
    private int pos;
    private String src;

    public boolean parseBoolExpr_1(String expression) {
        this.pos = 0;
        this.src = expression;
        return parse();
    }

    private boolean parse() {
        char c = src.charAt(pos++);
        if (c == 't') {
            return true;
        }
        if (c == 'f') {
            return false;
        }

        // c is one of '!' / '&' / '|'  -> the next char is '('
        pos++; // skip '('
        boolean anyTrue = false;
        boolean anyFalse = false;
        while (src.charAt(pos) != ')') {
            if (src.charAt(pos) == ',') {
                pos++;
                continue;
            }
            if (parse()) {
                anyTrue = true;
            } else {
                anyFalse = true;
            }
        }
        pos++; // skip ')'

        if (c == '!') {
            return anyFalse;
        }
        if (c == '&') {
            return !anyFalse;
        }
        return anyTrue;
    }
}
