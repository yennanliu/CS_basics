package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/expression-add-operators/description/

import java.util.*;

/**
 * 282. Expression Add Operators
 * Hard
 *
 * Given a string num that contains only digits and an integer target, return all
 * possibilities to insert the binary operators '+', '-', and/or '*' between the
 * digits of num so that the resultant expression evaluates to the target value.
 *
 * Note that operands in the returned expressions should not contain leading zeros.
 *
 *
 * Example 1:
 *
 * Input: num = "123", target = 6
 * Output: ["1*2*3","1+2+3"]
 * Explanation: Both "1*2*3" and "1+2+3" evaluate to 6.
 *
 * Example 2:
 *
 * Input: num = "232", target = 8
 * Output: ["2*3+2","2+3*2"]
 * Explanation: Both "2*3+2" and "2+3*2" evaluate to 8.
 *
 * Example 3:
 *
 * Input: num = "3456237490", target = 9191
 * Output: []
 * Explanation: There are no expressions that can be created from "3456237490" to evaluate to 9191.
 *
 *
 * Constraints:
 *
 * 1 <= num.length <= 10
 * num consists of only digits.
 * -2^31 <= target <= 2^31 - 1
 *
 */
public class ExpressionAddOperators {

    // V0
    // IDEA: BACKTRACKING (CARRY THE LAST OPERAND SO '*' CAN BE UNDONE)
    //
    //   '*' binds tighter than '+' / '-', so a running total is not enough: when we
    //   append "*x" we take the previous operand back out of the total and re-add it
    //   multiplied.
    //
    //     total, prev  --"*x"-->  total - prev + prev * x,  new prev = prev * x
    //
    //   e.g. "2+3*2": after "2+3" total = 5, prev = 3; then "*2" -> 5 - 3 + 3*2 = 8.
    //
    //   NOTE: an operand may not have a leading zero, so once it starts with '0'
    //         we stop extending it (break, not continue).
    /**
     * time = O(4^N * N)
     * space = O(N)
     */
    public List<String> addOperators(String num, int target) {
        List<String> result = new ArrayList<>();
        // edge case: nothing to place operators between
        if (num == null || num.isEmpty()) {
            return result;
        }
        backtrack(num, target, 0, "", 0L, 0L, result);
        return result;
    }

    /**
     * Places the next operand num[start..end] behind every operator.
     * total and prevOperand are long: a 10-digit operand (or a product) overflows int.
     */
    private void backtrack(String num, int target, int start, String expression,
                           long total, long prevOperand, List<String> result) {
        // every digit is used: keep the expression if it hits the target
        if (start == num.length()) {
            if (total == target) {
                result.add(expression);
            }
            return;
        }

        for (int end = start; end < num.length(); end++) {
            // "05" is not a legal operand, and neither is anything longer
            if (end > start && num.charAt(start) == '0') {
                break;
            }

            String operandText = num.substring(start, end + 1);
            long operand = Long.parseLong(operandText);

            if (start == 0) {
                // the first operand has no operator in front of it
                backtrack(num, target, end + 1, operandText, operand, operand, result);
            } else {
                backtrack(num, target, end + 1, expression + "+" + operandText,
                        total + operand, operand, result);
                // subtracting is adding the negated operand, so '*' can undo it the same way
                backtrack(num, target, end + 1, expression + "-" + operandText,
                        total - operand, -operand, result);
                // undo the last operand, then add it back multiplied
                long product = prevOperand * operand;
                backtrack(num, target, end + 1, expression + "*" + operandText,
                        total - prevOperand + product, product, result);
            }
        }
    }
}
