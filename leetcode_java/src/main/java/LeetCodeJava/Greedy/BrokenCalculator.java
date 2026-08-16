package LeetCodeJava.Greedy;

// https://leetcode.com/problems/broken-calculator/description/
/**
 * 991. Broken Calculator
 * Medium
 *
 * There is a broken calculator that has the integer startValue on its display initially.
 * In one operation, you can:
 *
 * multiply the number on display by 2, or
 * subtract 1 from the number on display.
 *
 * Given two integers startValue and target, return the minimum number of operations
 * needed to display target on the calculator.
 *
 * Example 1:
 *
 * Input: startValue = 2, target = 3
 * Output: 2
 * Explanation: Use double operation and then decrement operation {2 -> 4 -> 3}.
 *
 * Example 2:
 *
 * Input: startValue = 5, target = 8
 * Output: 2
 * Explanation: Use decrement and then double {5 -> 4 -> 8}.
 *
 * Example 3:
 *
 * Input: startValue = 3, target = 10
 * Output: 3
 * Explanation: Use double, decrement and double {3 -> 6 -> 5 -> 10}.
 *
 * Constraints:
 *
 * 1 <= startValue, target <= 10^9
 *
 */
public class BrokenCalculator {

    // V0
    // IDEA: GREEDY, WORK BACKWARDS from target
    /**
     *  Going FORWARD the choice (double? decrement?) is AMBIGUOUS.
     *  Going BACKWARDS from target the move is FORCED:
     *
     *     - target ODD  -> the last forward op must have been "-1", so undo it: target += 1
     *     - target EVEN -> halving is never worse than adding, so: target /= 2
     *
     *  Once target <= startValue, only DECREMENTS remain: add (startValue - target).
     *
     *  Why halving is safe on even numbers: any "+1" applied BEFORE a halve costs
     *  2 ops (+1 then /2 ...) vs 1 op AFTER halving, so delaying additions wins.
     *
     *  time  = O(log(target))
     *  space = O(1)
     */
    public int brokenCalc(int startValue, int target) {
        int ops = 0;

        while (target > startValue) {
            if (target % 2 == 1) {
                target += 1;
            } else {
                target /= 2;
            }
            ops += 1;
        }

        // target is now <= startValue : only "-1" steps left
        return ops + (startValue - target);
    }

}
