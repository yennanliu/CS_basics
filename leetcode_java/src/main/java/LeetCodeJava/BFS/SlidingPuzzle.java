package LeetCodeJava.BFS;

// https://leetcode.com/problems/sliding-puzzle/description/

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
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


    // V1
    // IDEA: BIDIRECTIONAL BFS (expand from the start AND the goal)
    /**
     *  Search from both ends and stop when the frontiers meet. Each side only has
     *  to reach depth d/2, so the explored state count drops from b^d to 2 * b^(d/2).
     *
     *  Always expand the SMALLER frontier -- that is what keeps the branching under
     *  control.
     *
     *  time  = O(6!) worst case, typically far fewer states than V0
     *  space = O(6!)
     */
    public int slidingPuzzle_1(int[][] board) {
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

        Set<String> head = new HashSet<>();
        Set<String> tail = new HashSet<>();
        Set<String> seen = new HashSet<>();
        head.add(start);
        tail.add(target);
        seen.add(start);
        seen.add(target);

        int steps = 0;
        while (!head.isEmpty() && !tail.isEmpty()) {
            // always expand the SMALLER side
            if (head.size() > tail.size()) {
                Set<String> t = head;
                head = tail;
                tail = t;
            }
            steps += 1;

            Set<String> next = new HashSet<>();
            for (String state : head) {
                int zero = state.indexOf('0');
                for (int nxt : NEIGHBORS[zero]) {
                    String cand = swapAt(state, zero, nxt);
                    if (tail.contains(cand)) {
                        return steps;
                    }
                    if (seen.add(cand)) {
                        next.add(cand);
                    }
                }
            }
            head = next;
        }

        return -1;
    }

    private String swapAt(String state, int i, int j) {
        char[] c = state.toCharArray();
        char t = c[i];
        c[i] = c[j];
        c[j] = t;
        return new String(c);
    }

    // V2
    // IDEA: A* WITH THE MANHATTAN-DISTANCE HEURISTIC
    /**
     *  f(state) = movesSoFar + sum over tiles of |dr| + |dc| to its goal cell.
     *
     *  That heuristic is ADMISSIBLE (each move fixes at most one tile by one step),
     *  so the first time the goal is popped from the priority queue its cost is
     *  optimal -- and the search visits far fewer states than a blind BFS.
     *
     *  time  = O(states log states), with a much smaller constant than BFS
     *  space = O(states)
     */
    public int slidingPuzzle_2(int[][] board) {
        final String target = "123450";
        StringBuilder sb = new StringBuilder();
        for (int[] row : board) {
            for (int v : row) {
                sb.append(v);
            }
        }
        String start = sb.toString();

        PriorityQueue<Object[]> pq =
                new PriorityQueue<>(Comparator.comparingInt(o -> (Integer) o[0]));
        pq.add(new Object[] { manhattan(start), 0, start });

        Map<String, Integer> bestCost = new HashMap<>();
        bestCost.put(start, 0);

        while (!pq.isEmpty()) {
            Object[] cur = pq.poll();
            int g = (Integer) cur[1];
            String state = (String) cur[2];

            if (state.equals(target)) {
                return g;
            }
            if (g > bestCost.getOrDefault(state, Integer.MAX_VALUE)) {
                continue; // a stale queue entry
            }

            int zero = state.indexOf('0');
            for (int nxt : NEIGHBORS[zero]) {
                String cand = swapAt(state, zero, nxt);
                int ng = g + 1;
                if (ng < bestCost.getOrDefault(cand, Integer.MAX_VALUE)) {
                    bestCost.put(cand, ng);
                    pq.add(new Object[] { ng + manhattan(cand), ng, cand });
                }
            }
        }

        return -1;
    }

    /** sum of tile distances to their goal cells (the blank is not counted) */
    private int manhattan(String state) {
        int total = 0;
        for (int i = 0; i < 6; i++) {
            char c = state.charAt(i);
            if (c == '0') {
                continue;
            }
            int goal = c - '1'; // tile '1' belongs at flat index 0
            total += Math.abs(i / 3 - goal / 3) + Math.abs(i % 3 - goal % 3);
        }
        return total;
    }

    // V3
    // IDEA: PRECOMPUTE THE WHOLE STATE SPACE ONCE (BFS from the goal)
    /**
     *  There are only 6! = 720 boards. One BFS from "123450" labels EVERY reachable
     *  state with its distance, after which any query is a hash lookup.
     *
     *  -> answering m boards costs O(6! + m) instead of O(m * 6!).
     *
     *  Unreachable states simply never appear in the table, which doubles as the
     *  parity check.
     *
     *  time  = O(6! * 6) once, then O(1) per query
     *  space = O(6!)
     */
    private static Map<String, Integer> distFromGoal;

    public int slidingPuzzle_3(int[][] board) {
        if (distFromGoal == null) {
            distFromGoal = buildTable();
        }
        StringBuilder sb = new StringBuilder();
        for (int[] row : board) {
            for (int v : row) {
                sb.append(v);
            }
        }
        return distFromGoal.getOrDefault(sb.toString(), -1);
    }

    private Map<String, Integer> buildTable() {
        Map<String, Integer> dist = new HashMap<>();
        String goal = "123450";
        dist.put(goal, 0);

        Deque<String> q = new ArrayDeque<>();
        q.offer(goal);
        while (!q.isEmpty()) {
            String state = q.poll();
            int d = dist.get(state);
            int zero = state.indexOf('0');
            for (int nxt : NEIGHBORS[zero]) {
                String cand = swapAt(state, zero, nxt);
                if (!dist.containsKey(cand)) {
                    dist.put(cand, d + 1);
                    q.offer(cand);
                }
            }
        }
        return dist;
    }

}
