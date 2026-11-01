package LeetCodeJava.Math;

// https://leetcode.com/problems/moving-stones-until-consecutive/description/

/**
 * 1033. Moving Stones Until Consecutive
 * Medium
 *
 * There are three stones in different positions on the X-axis. You are given three
 * integers a, b, and c, the positions of the stones.
 *
 * In one move, you pick up a stone at an endpoint (i.e., either the lowest or highest
 * position stone), and move it to an unoccupied position between those endpoints.
 * Formally, let's say the stones are currently at positions x, y, and z with x < y < z.
 * You pick up the stone at either position x or position z, and move that stone to an
 * integer position k, with x < k < z and k != y.
 *
 * The game ends when you cannot make any more moves (i.e., the stones are in three
 * consecutive positions).
 *
 * Return an integer array answer of length 2 where:
 *
 * answer[0] is the minimum number of moves you can play, and
 * answer[1] is the maximum number of moves you can play.
 *
 *
 * Example 1:
 *
 * Input: a = 1, b = 2, c = 5
 * Output: [1,2]
 * Explanation: Move the stone from 5 to 3, or move the stone from 5 to 4 to 3.
 *
 * Example 2:
 *
 * Input: a = 4, b = 3, c = 2
 * Output: [0,0]
 * Explanation: We cannot make any moves.
 *
 * Example 3:
 *
 * Input: a = 3, b = 5, c = 1
 * Output: [1,2]
 * Explanation: Move the stone from 1 to 4; or move the stone from 1 to 2 to 4.
 *
 *
 * Constraints:
 *
 * 1 <= a, b, c <= 100
 * a, b, and c have different values.
 *
 */
public class MovingStonesUntilConsecutive {

    // V0
    // IDEA: SORT THE 3 POSITIONS + CASE ANALYSIS
    //       with x < y < z :
    //       - already consecutive (z - x == 2)               -> [0, 0]
    //       - one gap is tiny (y - x <= 2 or z - y <= 2)     -> 1 move finishes
    //         (either a neighbour is adjacent, or a stone fits in the 1-wide hole)
    //       - otherwise                                       -> 2 moves
    //       max : every move can shrink the span by exactly 1, so it is the
    //       number of empty slots inside the span : (z - x - 2)
    /**
     * time = O(1)
     * space = O(1)
     */
    public int[] numMovesStones(int a, int b, int c) {
        // sort the three positions without an array: smallest, largest, middle
        int low = Math.min(a, Math.min(b, c));
        int high = Math.max(a, Math.max(b, c));
        int mid = a + b + c - low - high;

        // already three consecutive positions -> the game is over
        if (high - low == 2) {
            return new int[] {0, 0};
        }

        int minMoves;
        if (mid - low <= 2 || high - mid <= 2) {
            // one endpoint is adjacent to mid, or can drop into the single slot
            minMoves = 1;
        } else {
            // move each endpoint next to mid
            minMoves = 2;
        }

        // each move fills one empty slot between the endpoints
        int maxMoves = high - low - 2;
        return new int[] {minMoves, maxMoves};
    }
}
