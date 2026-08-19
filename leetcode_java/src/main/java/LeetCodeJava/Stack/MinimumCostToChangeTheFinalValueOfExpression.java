package LeetCodeJava.Stack;

// https://leetcode.com/problems/minimum-cost-to-change-the-final-value-of-expression/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  1896. Minimum Cost to Change the Final Value of Expression
 *  Hard
 *
 *  You are given a valid boolean expression as a string expression consisting of the
 *  characters '1', '0', '&' (AND), '|' (OR), '(' and ')'.
 *
 *  Return the minimum cost to change the final value of the expression. The cost is the
 *  number of operations performed, where an operation is one of:
 *   - turn a '1' into a '0' (or the other way round)
 *   - turn a '&' into a '|' (or the other way round)
 *
 *  Note: '&' does NOT take precedence over '|'. Evaluate parentheses first, then
 *  strictly left-to-right.
 *
 *  Example 1:
 *  Input: expression = "1&(0|1)"
 *  Output: 1
 *
 *  Example 2:
 *  Input: expression = "(0&0)&(0&0&0)"
 *  Output: 3
 *
 *  Example 3:
 *  Input: expression = "(0|(1|0&1))"
 *  Output: 1
 *
 *  Constraints:
 *  1 <= expression.length <= 10^5
 *  expression only contains '1', '0', '&', '|', '(' and ')'
 *  All parentheses are properly matched, and there are no empty parentheses.
 */
public class MinimumCostToChangeTheFinalValueOfExpression {

    /** (value of a sub-expression, min cost to flip that value) */
    private static class Res {
        final boolean val;
        final int cost;

        Res(boolean val, int cost) {
            this.val = val;
            this.cost = cost;
        }
    }

    // V0
    // IDEA: STACK + DP PAIR — carry (value, minFlipCost) for the expression built so far,
    //       and merge it with the next operand through the operator; '(' saves the pending
    //       (operator, partial result) on a stack and ')' pops it back
    /**
     * time = O(n)
     * space = O(n)
     */
    public int minOperationsToFlip(String expression) {

        // stack of pending (operator, partial result) pairs opened by '('
        Deque<Character> opStack = new ArrayDeque<>();
        Deque<Res> resStack = new ArrayDeque<>();

        char op = 0;                       // 0 = "no pending operator"
        Res cur = new Res(true, 0);        // placeholder, never used while op == 0

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (ch == '0' || ch == '1') {
                cur = combine(op, cur, new Res(ch == '1', 1));
                op = 0;
            } else if (ch == '&' || ch == '|') {
                op = ch;
            } else if (ch == '(') {
                opStack.push(op);
                resStack.push(cur);
                op = 0;
            } else { // ')'
                char prevOp = opStack.pop();
                Res prev = resStack.pop();
                cur = combine(prevOp, prev, cur);
                op = 0;
            }
        }
        return cur.cost;
    }

    /**
     *  Merge `left <op> right`.
     *
     *  OR : both 1 -> turn '|' into '&' AND flip one side to 0   => min(cl, cr) + 1
     *       one  1 -> turn '|' into '&'                          => 1
     *       none 1 -> flip the cheaper side to 1                 => min(cl, cr)
     *
     *  AND: both 0 -> turn '&' into '|' AND flip one side to 1   => min(cl, cr) + 1
     *       one  0 -> turn '&' into '|'                          => 1
     *       none 0 -> flip the cheaper side to 0                 => min(cl, cr)
     */
    private Res combine(char op, Res left, Res right) {
        if (op == 0) {
            return right; // nothing pending -> the operand IS the expression so far
        }
        int minCost = Math.min(left.cost, right.cost);
        if (op == '|') {
            if (left.val && right.val) {
                return new Res(true, minCost + 1);
            }
            if (left.val || right.val) {
                return new Res(true, 1);
            }
            return new Res(false, minCost);
        }
        // op == '&'
        if (!left.val && !right.val) {
            return new Res(false, minCost + 1);
        }
        if (!left.val || !right.val) {
            return new Res(false, 1);
        }
        return new Res(true, minCost);
    }
}
