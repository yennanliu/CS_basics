package LeetCodeJava.Tree;

// https://leetcode.com/problems/falling-squares/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 699. Falling Squares
 * Hard
 *
 * There are several squares being dropped onto the X-axis of a 2D plane.
 *
 * You are given a 2D integer array positions where positions[i] = [left_i, sideLength_i]
 * represents the ith square with a side length of sideLength_i that is dropped with its
 * left edge aligned with X-coordinate left_i.
 *
 * Each square is dropped one at a time from a height above any landed squares.
 * It then falls downward (negative Y direction) until it either lands on the top side
 * of another square or on the X-axis. A square brushing the left/right side of another
 * square does not count as landing on it. Once it lands, it freezes in place and cannot
 * be moved.
 *
 * After each square is dropped, you must record the height of the current tallest stack
 * of squares.
 *
 * Return an integer array ans where ans[i] represents the height described above
 * after dropping the ith square.
 *
 *
 * Example 1:
 *
 * Input: positions = [[1,2],[2,3],[6,1]]
 * Output: [2,5,5]
 * Explanation:
 * After the first drop, the tallest stack is square 1 with a height of 2.
 * After the second drop, the tallest stack is squares 1 and 2 with a height of 5.
 * After the third drop, the tallest stack is still squares 1 and 2 with a height of 5.
 * Thus, we return an answer of [2, 5, 5].
 *
 * Example 2:
 *
 * Input: positions = [[100,100],[200,100]]
 * Output: [100,100]
 * Explanation:
 * After the first drop, the tallest stack is square 1 with a height of 100.
 * After the second drop, the tallest stack is either square 1 or square 2, both with
 * heights of 100.
 * Thus, we return an answer of [100, 100].
 * Note that square 2 only brushes the right side of square 1, which does not count as
 * landing on it.
 *
 *
 * Constraints:
 *
 * 1 <= positions.length <= 1000
 * 1 <= left_i <= 10^8
 * 1 <= sideLength_i <= 10^6
 *
 */
public class FallingSquares {

    // V0
    // IDEA: BRUTE FORCE + INTERVAL OVERLAP
    /**
     *   Keep every landed square as (left, right, topHeight).
     *   A new square [l, r) lands on the TALLEST square it *STRICTLY* overlaps with.
     *
     *   NOTE !!! touching edges do NOT count as landing
     *            -> the test must be `left < r && l < right` (STRICT on both sides),
     *               not `<=`, otherwise example 2 would stack to 200.
     *
     *   Then track the RUNNING max height (the answer is a prefix maximum, not the
     *   height of the square just dropped).
     *
     *   time  = O(n^2)
     *   space = O(n)
     */
    public List<Integer> fallingSquares(int[][] positions) {
        List<int[]> landed = new ArrayList<>(); // {left, right, top height}
        List<Integer> res = new ArrayList<>();
        int curMax = 0;

        for (int[] p : positions) {
            int left = p[0];
            int side = p[1];
            int right = left + side; // half open interval [left, right)

            // the square rests on top of the HIGHEST square it overlaps with
            int base = 0;
            for (int[] sq : landed) {
                int l = sq[0];
                int r = sq[1];
                int h = sq[2];
                // STRICT overlap only: brushing a side is not landing on it
                if (left < r && l < right) {
                    base = Math.max(base, h);
                }
            }

            int top = base + side;
            landed.add(new int[] { left, right, top });

            curMax = Math.max(curMax, top);
            res.add(curMax);
        }

        return res;
    }

}
