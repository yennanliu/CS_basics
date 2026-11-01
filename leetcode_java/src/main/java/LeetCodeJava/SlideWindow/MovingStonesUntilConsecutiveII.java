package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/moving-stones-until-consecutive-ii/description/

import java.util.Arrays;

/**
 * 1040. Moving Stones Until Consecutive II
 * Medium
 *
 * There are some stones in different positions on the X-axis. You are given an integer
 * array stones, the positions of the stones.
 *
 * Call a stone an endpoint stone if it has the smallest or largest position.
 * In one move, you pick up an endpoint stone and move it to an unoccupied position so
 * that it is no longer an endpoint stone.
 *
 * In particular, if the stones are at say, stones = [1,2,5], you cannot move the
 * endpoint stone at position 5, since moving it to any position (such as 0, or 3)
 * will still keep that stone as an endpoint stone.
 *
 * The game ends when you cannot make any more moves (i.e., the stones are in
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
 * Input: stones = [7,4,9]
 * Output: [1,2]
 * Explanation: We can move 4 -> 8 for one move to finish the game.
 * Or, we can move 9 -> 5, 4 -> 6 for two moves to finish the game.
 *
 * Example 2:
 *
 * Input: stones = [6,5,4,3,10]
 * Output: [2,3]
 * Explanation: We can move 3 -> 8 then 10 -> 7 to finish the game.
 * Or, we can move 3 -> 7, 4 -> 8, 5 -> 9 to finish the game.
 * Notice we cannot move 10 -> 2 to finish the game, because that would be an
 * illegal move.
 *
 *
 * Constraints:
 *
 * 3 <= stones.length <= 10^4
 * 1 <= stones[i] <= 10^9
 * All the values of stones are unique.
 *
 */
public class MovingStonesUntilConsecutiveII {

    // V0
    // IDEA: SORT + SLIDING WINDOW (min) + MATH (max)
    //       MAX : the first move must throw away one of the two outer gaps
    //             (the endpoint jumps just inside the other end). After that every
    //             move can fill exactly one empty slot, so
    //             max = max(s[n-1] - s[1], s[n-2] - s[0]) - (n - 2)
    //       MIN : the final n stones occupy a window of width n. Slide a window of
    //             width n over the sorted stones; holding `count` stones means
    //             n - count moves bring the rest in.
    //             SPECIAL CASE : n-1 stones already consecutive and one far away
    //             (e.g. [1,2,3,4,10]) -> that stone cannot land at an end in one
    //             move (it would stay an endpoint), so it costs 2.
    /**
     * time = O(N log N)
     * space = O(1)
     */
    public int[] numMovesStonesII(int[] stones) {
        int n = stones.length;
        int[] sorted = stones.clone();
        Arrays.sort(sorted);

        // ---- max ----
        // empty slots in [s[1], s[n-1]] or in [s[0], s[n-2]], whichever is larger
        int maxMoves = Math.max(sorted[n - 1] - sorted[1], sorted[n - 2] - sorted[0]) - (n - 2);

        // ---- min : window of width n over positions ----
        int minMoves = n;
        int left = 0;
        for (int right = 0; right < n; right++) {
            // shrink until the window [sorted[left], sorted[right]] spans at most n positions
            while (sorted[right] - sorted[left] + 1 > n) {
                left++;
            }
            int count = right - left + 1;
            boolean consecutiveExceptOne = (count == n - 1) && (sorted[right] - sorted[left] == n - 2);
            if (consecutiveExceptOne) {
                // the lone outside stone needs 2 moves, not 1
                minMoves = Math.min(minMoves, 2);
            } else {
                minMoves = Math.min(minMoves, n - count);
            }
        }

        return new int[] {minMoves, maxMoves};
    }
}
