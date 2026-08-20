package LeetCodeJava.Sort;

// https://leetcode.com/problems/minimum-moves-to-get-a-peaceful-board/

import java.util.Arrays;

/**
 *  3189. Minimum Moves to Get a Peaceful Board
 *  Medium
 *  (premium / locked problem)
 *
 *  Given a 2D array rooks of length n, where rooks[i] = [x_i, y_i] indicates the
 *  position of a rook on an n x n chess board. Your task is to move the rooks one
 *  cell at a time vertically or horizontally (to an adjacent cell) such that the
 *  board becomes peaceful.
 *
 *  A board is peaceful if there is exactly one rook in each row and each column.
 *
 *  Return the minimum number of moves required to get a peaceful board.
 *
 *  Note that at no point can there be two rooks in the same cell.
 *
 *  Example 1:
 *    Input: rooks = [[0,0],[1,0],[1,1]]
 *    Output: 3
 *
 *  Example 2:
 *    Input: rooks = [[0,0],[0,1],[0,2],[0,3]]
 *    Output: 6
 *
 *  Constraints:
 *    1 <= n == rooks.length <= 500
 *    0 <= x_i, y_i <= n - 1
 *    The input is generated such that there are no 2 rooks in the same cell.
 */
public class MinimumMovesToGetAPeacefulBoard {

    // V0
    // IDEA: THE TWO AXES ARE INDEPENDENT - SORT EACH AND MATCH IN ORDER
    //       a vertical move changes only a rook's row, a horizontal one only its
    //       column, so the axes never interact and the cost splits into
    //       "fix the rows" + "fix the columns".
    //
    //       on one axis the n rooks must land on the n distinct values 0..n-1, and
    //       for points on a line the cheapest assignment is the SORTED one (any
    //       crossing pair can be uncrossed without increasing the total distance).
    //
    //       -> answer = sum |sorted_x[i] - i| + sum |sorted_y[i] - i|
    //
    //       NOTE: the "no two rooks share a cell mid-way" clause never costs extra,
    //             the moves can always be sequenced to avoid collisions.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int minMoves(int[][] rooks) {
        int n = rooks.length;
        int[] xs = new int[n];
        int[] ys = new int[n];
        for (int i = 0; i < n; i++) {
            xs[i] = rooks[i][0];
            ys[i] = rooks[i][1];
        }
        Arrays.sort(xs);
        Arrays.sort(ys);

        int res = 0;
        for (int i = 0; i < n; i++) {
            res += Math.abs(xs[i] - i) + Math.abs(ys[i] - i);
        }
        return res;
    }
}
