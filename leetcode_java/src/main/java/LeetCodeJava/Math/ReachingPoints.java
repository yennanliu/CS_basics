package LeetCodeJava.Math;

// https://leetcode.com/problems/reaching-points/description/

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
/**
 * 780. Reaching Points
 * Hard
 *
 * Given four integers sx, sy, tx, and ty, return true if it is possible to convert the
 * point (sx, sy) to the point (tx, ty) through some operations, or false otherwise.
 *
 * The allowed operation on some point (x, y) is to convert it to either (x, x + y)
 * or (x + y, y).
 *
 *
 * Example 1:
 *
 * Input: sx = 1, sy = 1, tx = 3, ty = 5
 * Output: true
 * Explanation:
 * One series of moves that transforms the starting point to the target is:
 * (1, 1) -> (1, 2)
 * (1, 2) -> (3, 2)
 * (3, 2) -> (3, 5)
 *
 * Example 2:
 *
 * Input: sx = 1, sy = 1, tx = 2, ty = 2
 * Output: false
 *
 * Example 3:
 *
 * Input: sx = 1, sy = 1, tx = 1, ty = 1
 * Output: true
 *
 *
 * Constraints:
 *
 * 1 <= sx, sy, tx, ty <= 10^9
 *
 */
public class ReachingPoints {

    // V0
    // IDEA: WORK BACKWARDS WITH MODULO
    /**
     *   FORWARDS, (x, y) branches in two directions -- too slow. BACKWARDS it is
     *   DETERMINISTIC: since all values stay positive, the last move must have grown
     *   the LARGER coordinate, so (tx, ty) came from (tx - ty, ty) when tx > ty,
     *   else from (tx, ty - tx).
     *
     *   Repeatedly SUBTRACTING is O(value), so use MODULO to do all the subtractions
     *   of one coordinate in a single step (a Euclidean-algorithm style reduction).
     *
     *   Stop as soon as a coordinate drops to or below its start value, then handle
     *   the TAIL: if one coordinate already matches, the other only had that FIXED
     *   value added repeatedly -> a DIVISIBILITY check.
     *
     *   time  = O(log(max(tx, ty)))
     *   space = O(1)
     */
    public boolean reachingPoints(int sx, int sy, int tx, int ty) {
        // both coordinates still ABOVE the start -> keep reducing the larger one
        while (tx > sx && ty > sy) {
            if (tx > ty) {
                tx %= ty;
            } else {
                ty %= tx;
            }
        }

        if (tx == sx && ty == sy) {
            return true;
        }

        /** NOTE !!!
         *
         *  only ty still needs shrinking: it grew by repeatedly adding tx (== sx)
         */
        if (tx == sx && ty > sy) {
            return (ty - sy) % tx == 0;
        }

        // only tx still needs shrinking: it grew by repeatedly adding ty (== sy)
        if (ty == sy && tx > sx) {
            return (tx - sx) % ty == 0;
        }

        return false;
    }


    // V1
    // IDEA: WORK BACKWARDS BY REPEATED SUBTRACTION
    /**
     *  The same deterministic backwards walk, but subtracting one step at a time
     *  instead of jumping with modulo.
     *
     *  O(value) so it is far too slow for 10^9, yet it is the literal inverse of
     *  the forward moves -- the oracle showing the modulo shortcut is equivalent.
     *
     *  time  = O(max(tx, ty))
     *  space = O(1)
     */
    public boolean reachingPoints_1(int sx, int sy, int tx, int ty) {
        while (tx >= sx && ty >= sy) {
            if (tx == sx && ty == sy) {
                return true;
            }
            if (tx > ty) {
                tx -= ty;
            } else {
                ty -= tx;
            }
        }
        return false;
    }

    // V2
    // IDEA: RECURSIVE FORMULATION OF THE MODULO REDUCTION
    /**
     *  reach(tx, ty) = reach(tx % ty, ty) or reach(tx, ty % tx), whichever
     *  coordinate is larger, with the divisibility tail as the base case.
     *
     *  Reads as the Euclidean algorithm it really is; the recursion depth is
     *  O(log) for the same reason gcd's is.
     *
     *  time  = O(log(max(tx, ty)))
     *  space = O(log(max(tx, ty)))
     */
    public boolean reachingPoints_2(int sx, int sy, int tx, int ty) {
        if (tx < sx || ty < sy) {
            return false;
        }
        if (tx == sx && ty == sy) {
            return true;
        }
        /** NOTE !!!
         *
         *  tx == ty here means neither coordinate can shrink any further without
         *  going negative, and we already know it is not the start -> dead end.
         *  Without this guard the recursion below would bounce forever whenever
         *  one coordinate divides the other.
         */
        if (tx == ty) {
            return false;
        }

        if (tx > ty) {
            if (ty > sy) {
                return reachingPoints_2(sx, sy, tx % ty, ty);
            }
            // ty is already at its start value -> tx grew by adding ty repeatedly
            return (tx - sx) % ty == 0;
        }

        if (tx > sx) {
            return reachingPoints_2(sx, sy, tx, ty % tx);
        }
        // tx is already at its start value -> ty grew by adding tx repeatedly
        return (ty - sy) % tx == 0;
    }

    // V3
    // IDEA: FORWARD BFS from (sx, sy)
    /**
     *  Expand (x, y) -> (x, x+y) and (x+y, y), pruning anything past the target.
     *
     *  Exponential branching, so only usable for small targets -- but it explores
     *  the moves in the direction the STATEMENT describes, which the backwards
     *  versions deliberately invert.
     *
     *  time  = O(number of reachable states below the target)
     *  space = O(same)
     */
    public boolean reachingPoints_3(int sx, int sy, int tx, int ty) {
        Deque<int[]> q = new ArrayDeque<>();
        Set<Long> seen = new HashSet<>();
        q.offer(new int[] { sx, sy });
        seen.add((long) sx * 2000000000L + sy);

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            if (x == tx && y == ty) {
                return true;
            }
            if (x > tx || y > ty) {
                continue;   // both coordinates only ever grow
            }
            long a = (long) x * 2000000000L + (x + y);
            if (x + y <= ty && seen.add(a)) {
                q.offer(new int[] { x, x + y });
            }
            long b = (long) (x + y) * 2000000000L + y;
            if (x + y <= tx && seen.add(b)) {
                q.offer(new int[] { x + y, y });
            }
        }
        return false;
    }

}
