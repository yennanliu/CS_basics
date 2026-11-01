package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-moves-to-reach-target-in-grid/description/

/**
 * 3609. Minimum Moves to Reach Target in Grid
 * Hard
 *
 * You are given four integers sx, sy, tx, and ty, representing two points (sx, sy)
 * and (tx, ty) on an infinitely large 2D grid.
 *
 * You start at (sx, sy).
 *
 * At any point (x, y), define m = max(x, y). You can either:
 *
 * Move to (x + m, y), or
 * Move to (x, y + m).
 *
 * Return the minimum number of moves required to reach (tx, ty). If it is
 * impossible to reach the target, return -1.
 *
 *
 * Example 1:
 *
 * Input: sx = 1, sy = 2, tx = 5, ty = 4
 * Output: 2
 *
 * Example 2:
 *
 * Input: sx = 0, sy = 1, tx = 2, ty = 3
 * Output: 3
 *
 * Example 3:
 *
 * Input: sx = 1, sy = 1, tx = 2, ty = 2
 * Output: -1
 *
 *
 * Constraints:
 *
 * 0 <= sx <= tx <= 10^9
 * 0 <= sy <= ty <= 10^9
 *
 */
public class MinimumMovesToReachTargetInGrid {

    // V0
    // IDEA: WALK BACKWARDS FROM THE TARGET -- THE PREDECESSOR IS FORCED
    //   at (X, Y) with X > Y the last move must have grown x. if the old x was <= Y
    //   then X = x + Y, so the predecessor is (X - Y, Y) (legal when X <= 2Y); if the
    //   old x was >= Y then X = 2x, so it is (X / 2, Y) (legal when X > 2Y and X even).
    //   the predecessor is unique, so the backward chain IS the only path.
    //   the tie X == Y > 0 forces the other coordinate to have been 0: go to (0, X)
    //   if sx == 0, (X, 0) if sy == 0, otherwise it is unreachable.
    /**
     * time = O(log(tx) + log(ty))
     * space = O(1)
     */
    public int minMoves(int sx, int sy, int tx, int ty) {
        long x = tx;
        long y = ty;
        int steps = 0;

        while (x != sx || y != sy) {
            // forward moves never decrease a coordinate
            if (x < sx || y < sy) {
                return -1;
            }

            if (x > y) {
                if (x > 2 * y) {
                    // previous x was the max: X = 2x, so X must be even
                    if (x % 2 != 0) {
                        return -1;
                    }
                    x /= 2;
                } else {
                    // previous max was y: X = x + Y
                    x -= y;
                }
            } else if (y > x) {
                if (y > 2 * x) {
                    if (y % 2 != 0) {
                        return -1;
                    }
                    y /= 2;
                } else {
                    y -= x;
                }
            } else {
                // x == y (and not the start): the other coordinate must have been 0
                if (sx == 0) {
                    x = 0;
                } else if (sy == 0) {
                    y = 0;
                } else {
                    return -1;
                }
            }
            steps++;
        }
        return steps;
    }
}
