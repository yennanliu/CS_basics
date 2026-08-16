package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/least-operators-to-express-number/description/

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Map;

/**
 * 964. Least Operators to Express Number
 * Hard
 *
 * Given a single positive integer x, we will write an expression of the form
 * x (op1) x (op2) x (op3) x ... where each operator op1, op2, etc. is either addition,
 * subtraction, multiplication, or division (+, -, *, or /). For example, with x = 3, we
 * might write 3 * 3 / 3 + 3 - 3 which is a value of 3.
 *
 * When writing such an expression, we adhere to the following conventions:
 *
 * The division operator (/) returns rational numbers.
 * There are no parentheses placed anywhere.
 * We use the usual order of operations: multiplication and division happen before
 * addition and subtraction.
 * It is not allowed to use the unary negation operator (-). For example, "x - x" is a
 * valid expression as it only uses subtraction, but "-x + x" is not because it uses
 * negation.
 *
 * We would like to write an expression with the least number of operators such that the
 * expression equals the given target. Return the least number of operators used.
 *
 * Example 1:
 *
 * Input: x = 3, target = 19
 * Output: 5
 * Explanation: 3 * 3 + 3 * 3 + 3 / 3.
 * The expression contains 5 operations.
 *
 * Example 2:
 *
 * Input: x = 5, target = 501
 * Output: 8
 * Explanation: 5 * 5 * 5 * 5 - 5 * 5 * 5 + 5 / 5.
 * The expression contains 8 operations.
 *
 * Example 3:
 *
 * Input: x = 100, target = 100000000
 * Output: 3
 * Explanation: 100 * 100 * 100 * 100.
 * The expression contains 3 operations.
 *
 * Constraints:
 *
 * 2 <= x <= 100
 * 1 <= target <= 2 * 10^8
 *
 */
public class LeastOperatorsToExpressNumber {

    // V0
    // IDEA: MEMOIZED DFS on the BASE-x REPRESENTATION
    /**
     *  Any expression is a SUM/DIFFERENCE of terms, each term being x^k.
     *
     *  COST convention used here (each term is charged its JOINING +/- too):
     *     - term x^k for k >= 1 costs k        (k-1 multiplications + 1 joining op)
     *     - term x^0 == x/x     costs 2        (1 division          + 1 joining op)
     *
     *  NOTE !!! the VERY FIRST term needs no joining operator,
     *           so we SUBTRACT 1 at the end.
     *
     *  dfs(v) = min cost to build value v out of such terms.
     *     - if v <= x : either use v copies of (x/x)          -> 2 * v
     *                   or start from one x and SUBTRACT      -> 1 + 2 * (x - v)
     *     - else      : let k be the SMALLEST power with x^k >= v, then
     *                   go OVER  : k     + dfs(x^k - v)
     *                   go UNDER : (k-1) + dfs(v - x^(k-1))
     *       (the OVER branch only helps when x^k - v < v, otherwise it does not shrink
     *        the problem and the recursion would not terminate)
     *
     *  time  = O(log_x(target) ^ 2) states, each O(log_x(target)) work
     *  space = O(number of memoized states)
     */

    private int x;
    private Map<Long, Integer> memo;

    public int leastOpsExpressTarget(int x, int target) {
        this.x = x;
        this.memo = new HashMap<>();
        // the LEADING term does not need a joining operator
        return dfs(target) - 1;
    }

    private int dfs(long v) {
        Integer cached = memo.get(v);
        if (cached != null) {
            return cached;
        }

        if (x >= v) {
            // cheap base case: build v directly from x's
            int res = (int) Math.min(v * 2, (x - v) * 2 + 1);
            memo.put(v, res);
            return res;
        }

        // smallest k with x^k >= v  (k >= 2 here, since x < v)
        int k = 2;
        long pow = (long) x * x;
        while (pow < v) {
            pow *= x;
            k += 1;
        }
        long powPrev = pow / x; // x^(k-1)

        // go UNDER: use x^(k-1), then build the remainder
        int res = (k - 1) + dfs(v - powPrev);

        // go OVER: use x^k, then subtract the excess (only if it SHRINKS the problem)
        if (pow - v < v) {
            res = Math.min(res, k + dfs(pow - v));
        }

        memo.put(v, res);
        return res;
    }


    // V1
    // IDEA: BOTTOM-UP OVER THE BASE-x DIGITS (Horner style)
    /**
     *  Write target in base x. Processing the digits from the LEAST significant
     *  end, each digit d at position k can be paid for either directly
     *  (d terms costing k each) or by borrowing from the next position
     *  ((x - d) terms plus a carry).
     *
     *  Two running costs -- `no carry` and `carry` -- replace the recursion
     *  entirely.
     *
     *  time  = O(log_x(target))
     *  space = O(1)
     */
    public int leastOpsExpressTarget_1(int x, int target) {
        long noCarry = 0;   // cost so far assuming no borrow into this position
        long carry = 0;     // cost so far assuming we borrowed
        int k = 0;
        long t = target;

        while (t > 0) {
            long d = t % x;
            t /= x;

            if (k == 0) {
                // x^0 costs 2 per term (x / x), and borrowing needs (x - d) of them
                noCarry = d * 2;
                carry = (x - d) * 2;
            } else {
                long nextNo = Math.min(noCarry + d * k, carry + (d + 1) * k);
                long nextCarry = Math.min(noCarry + (x - d) * k,
                                          carry + (x - d - 1) * k);
                noCarry = nextNo;
                carry = nextCarry;
            }
            k += 1;
        }

        return (int) Math.min(noCarry, carry + k) - 1;
    }

    // V2
    // IDEA: DIJKSTRA over the remaining value
    /**
     *  Treat `the value still to be produced` as a node; from v the moves are
     *  `pay the current digit` or `borrow one from the next power`, each with a
     *  known cost.
     *
     *  Shortest path rather than recursion, so it needs no argument about which of
     *  the two branches can be pruned -- the queue decides.
     *
     *  time  = O(log_x(target)^2 * log)
     *  space = O(number of reachable values)
     */
    public int leastOpsExpressTarget_2(int x, int target) {
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));
        Map<Long, Long> best = new HashMap<>();
        pq.add(new long[] { 0, target });
        best.put((long) target, 0L);

        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            long cost = cur[0];
            long v = cur[1];

            if (v == 0) {
                return (int) (cost - 1);   // the leading term needs no joining op
            }
            if (cost > best.getOrDefault(v, Long.MAX_VALUE)) {
                continue;
            }

            if (v <= x) {
                long direct = cost + v * 2;                 // v copies of x/x
                long viaX = cost + 1 + (x - v) * 2;         // one x, then subtract
                long done = Math.min(direct, viaX);
                if (done < best.getOrDefault(0L, Long.MAX_VALUE)) {
                    best.put(0L, done);
                    pq.add(new long[] { done, 0 });
                }
                continue;
            }

            int k = 1;
            long pow = x;
            while (pow * x <= v) {
                pow *= x;
                k += 1;
            }
            // UNDER: use x^k, keep going with the remainder
            push(pq, best, cost + k, v - pow);
            // OVER: use x^(k+1), subtract the excess -- only if it shrinks v
            if (pow * x - v < v) {
                push(pq, best, cost + k + 1, pow * x - v);
            }
        }
        return -1;
    }

    private void push(PriorityQueue<long[]> pq, Map<Long, Long> best, long cost, long v) {
        if (cost < best.getOrDefault(v, Long.MAX_VALUE)) {
            best.put(v, cost);
            pq.add(new long[] { cost, v });
        }
    }

    // V3
    // IDEA: BFS OVER THE ACHIEVABLE VALUES (tiny targets only)
    /**
     *  Build up the reachable values by repeatedly adding or subtracting a power of
     *  x, layered by total cost.
     *
     *  Explodes quickly, so it is limited to small targets -- but it explores the
     *  expressions the statement actually describes, which is what validates the
     *  base-x cost model.
     *
     *  time  = exponential
     *  space = exponential
     */
    public int leastOpsExpressTarget_3(int x, int target) {
        Map<Integer, Integer> best = new HashMap<>();
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.add(new int[] { 0, 0 });
        best.put(0, 0);

        int limit = target * 2 + x;
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int cost = cur[0];
            int val = cur[1];
            if (val == target) {
                return cost - 1;
            }
            if (cost > best.getOrDefault(val, Integer.MAX_VALUE)) {
                continue;
            }

            long pow = 1;
            for (int k = 0; k <= 20; k++) {
                /** NOTE !!!
                 *
                 *  the SAME cost convention as V0: x^k for k >= 1 costs k
                 *  (k-1 multiplications + 1 joining op), and x^0 == x/x costs 2.
                 *  The leading term's joining op is refunded by the final `- 1`.
                 */
                int termCost = k == 0 ? 2 : k;
                for (int sign = -1; sign <= 1; sign += 2) {
                    long nv = val + sign * pow;
                    if (nv < 0 || nv > limit) {
                        continue;
                    }
                    int nc = cost + termCost;
                    if (nc < best.getOrDefault((int) nv, Integer.MAX_VALUE)) {
                        best.put((int) nv, nc);
                        pq.add(new int[] { nc, (int) nv });
                    }
                }
                pow *= x;
                if (pow > limit) {
                    break;
                }
            }
        }
        return -1;
    }

}
