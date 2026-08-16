package LeetCodeJava.Greedy;

// https://leetcode.com/problems/broken-calculator/description/

import java.util.ArrayDeque;
import java.util.Deque;
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


    // V1
    // IDEA: COUNT THE BITS OF target / startValue
    /**
     *  Working backwards, halving an even number is a right shift and `+1` on an
     *  odd number clears the low bit. So the whole run is decided by the BINARY
     *  form of target once it is above startValue:
     *
     *      shifts   = number of bits below the leading one
     *      plusOnes = number of ONE bits below the leading one
     *
     *  This turns the loop into a popcount, and shows why the answer is
     *  logarithmic rather than linear.
     *
     *  time  = O(log target)
     *  space = O(1)
     */
    public int brokenCalc_1(int startValue, int target) {
        if (startValue >= target) {
            return startValue - target;
        }

        int ops = 0;
        long t = target;
        while (t > startValue) {
            if ((t & 1) == 1) {
                t += 1;   // undo a "-1"
            } else {
                t >>= 1;  // undo a "*2"
            }
            ops += 1;
        }
        return (int) (ops + (startValue - t));
    }

    // V2
    // IDEA: BFS FORWARD from startValue
    /**
     *  Treat each reachable value as a node with edges `*2` and `-1`, and BFS.
     *
     *  Exponentially worse than the backwards greedy, but it makes NO optimality
     *  claim -- it is the oracle proving that `halve when even, add one when odd`
     *  really is minimal.
     *
     *  A bound of 2 * target keeps the state space finite.
     *
     *  time  = O(target)
     *  space = O(target)
     */
    public int brokenCalc_2(int startValue, int target) {
        if (startValue >= target) {
            return startValue - target;
        }

        int limit = 2 * target + 2;
        boolean[] seen = new boolean[limit];
        Deque<Integer> q = new ArrayDeque<>();
        q.offer(startValue);
        seen[startValue] = true;

        int steps = 0;
        while (!q.isEmpty()) {
            int levelSize = q.size();
            for (int t = 0; t < levelSize; t++) {
                int cur = q.poll();
                if (cur == target) {
                    return steps;
                }
                long dbl = (long) cur * 2;
                if (dbl < limit && !seen[(int) dbl]) {
                    seen[(int) dbl] = true;
                    q.offer((int) dbl);
                }
                int dec = cur - 1;
                if (dec > 0 && !seen[dec]) {
                    seen[dec] = true;
                    q.offer(dec);
                }
            }
            steps += 1;
        }
        return -1;
    }

    // V3
    // IDEA: RECURSION ON THE PARITY OF target
    /**
     *  The same backwards rule expressed as a recurrence:
     *
     *      f(t) = 0                       when t <= s   (plus s - t decrements)
     *      f(t) = 1 + f(t + 1)            when t is odd
     *      f(t) = 1 + f(t / 2)            when t is even
     *
     *  Reads as the proof rather than as a loop; the recursion depth is O(log t).
     *
     *  time  = O(log target)
     *  space = O(log target)
     */
    public int brokenCalc_3(int startValue, int target) {
        if (startValue >= target) {
            return startValue - target;
        }
        if ((target & 1) == 1) {
            return 1 + brokenCalc_3(startValue, target + 1);
        }
        return 1 + brokenCalc_3(startValue, target / 2);
    }

}
