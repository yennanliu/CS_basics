package LeetCodeJava.BFS;

// https://leetcode.com/problems/sliding-puzzle/description/

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * 773. Sliding Puzzle
 * Hard
 *
 * On an 2 x 3 board, there are five tiles labeled from 1 to 5, and an empty square
 * represented by 0. A move consists of choosing 0 and a 4-directionally adjacent number
 * and swapping it.
 *
 * The state of the board is solved if and only if the board is [[1,2,3],[4,5,0]].
 *
 * Given the puzzle board board, return the least number of moves required so that the
 * state of the board is solved. If it is impossible for the state of the board to be
 * solved, return -1.
 *
 *
 * Example 1:
 *
 * Input: board = [[1,2,3],[4,0,5]]
 * Output: 1
 * Explanation: Swap the 0 and the 5 in one move.
 *
 * Example 2:
 *
 * Input: board = [[1,2,3],[5,4,0]]
 * Output: -1
 * Explanation: No number of moves will make the board solved.
 *
 * Example 3:
 *
 * Input: board = [[4,1,2],[5,0,3]]
 * Output: 5
 * Explanation: 5 is the smallest number of moves that solves the board.
 * An example path:
 * After move 0: [[4,1,2],[5,0,3]]
 * After move 1: [[4,1,2],[0,5,3]]
 * After move 2: [[0,1,2],[4,5,3]]
 * After move 3: [[1,0,2],[4,5,3]]
 * After move 4: [[1,2,0],[4,5,3]]
 * After move 5: [[1,2,3],[4,5,0]]
 *
 *
 * Constraints:
 *
 * board.length == 2
 * board[i].length == 3
 * 0 <= board[i][j] <= 5
 * Each value board[i][j] is unique.
 *
 */
public class SlidingPuzzle {

    // V0
    // IDEA: BFS ON FLATTENED BOARD STATES
    /**
     *   FLATTEN the 2x3 board into a 6-char string so a whole state is ONE hashable key:
     *       [[1,2,3],[4,5,0]] -> "123450" (the goal)
     *
     *   BFS from the start state gives the MINIMUM number of moves; the search space is
     *   only 6! = 720 states, half of which are unreachable (PARITY) -> return -1 then.
     *
     *   NEIGHBORS[i] lists the flat indices adjacent to index i on the 2x3 grid:
     *       0 1 2
     *       3 4 5
     *
     *   time  = O(6! * 6)  -> effectively O(1), the state space is FIXED
     *   space = O(6!)
     */

    /** flat-index adjacency on the 2x3 grid */
    private static final int[][] NEIGHBORS = {
            { 1, 3 },
            { 0, 2, 4 },
            { 1, 5 },
            { 0, 4 },
            { 1, 3, 5 },
            { 2, 4 }
    };

    public int slidingPuzzle(int[][] board) {
        final String target = "123450";

        StringBuilder sb = new StringBuilder();
        for (int[] row : board) {
            for (int v : row) {
                sb.append(v);
            }
        }
        String start = sb.toString();

        if (start.equals(target)) {
            return 0;
        }

        Set<String> seen = new HashSet<>();
        seen.add(start);

        Deque<String> queue = new ArrayDeque<>();
        queue.offer(start);
        int steps = 0;

        while (!queue.isEmpty()) {
            steps += 1;

            // process one whole BFS level -> `steps` stays the move count
            int levelSize = queue.size();
            for (int t = 0; t < levelSize; t++) {
                String state = queue.poll();
                int zero = state.indexOf('0');

                for (int nxt : NEIGHBORS[zero]) {
                    char[] chars = state.toCharArray();
                    /** NOTE !!!
                     *
                     *  a `move` is just SWAPPING the blank with an adjacent tile
                     */
                    char tmp = chars[zero];
                    chars[zero] = chars[nxt];
                    chars[nxt] = tmp;

                    String newState = new String(chars);

                    if (newState.equals(target)) {
                        return steps;
                    }
                    if (seen.add(newState)) {
                        queue.offer(newState);
                    }
                }
            }
        }

        return -1;
    }

}
