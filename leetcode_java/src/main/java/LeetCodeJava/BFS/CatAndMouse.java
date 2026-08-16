package LeetCodeJava.BFS;

// https://leetcode.com/problems/cat-and-mouse/description/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 913. Cat and Mouse
 * Hard
 *
 * A game on an undirected graph is played by two players, Mouse and Cat,
 * who alternate turns.
 *
 * The graph is given as follows: graph[a] is a list of all nodes b such that ab is an
 * edge of the graph.
 *
 * The mouse starts at node 1 and goes first, the cat starts at node 2 and goes second,
 * and there is a hole at node 0.
 *
 * During each player's turn, they must travel along one edge of the graph that meets
 * where they are. For example, if the Mouse is at node 1, it must travel to any node
 * in graph[1].
 *
 * Additionally, it is not allowed for the Cat to travel to the Hole (node 0).
 *
 * Then, the game can end in three ways:
 *
 * If ever the Cat occupies the same node as the Mouse, the Cat wins.
 * If ever the Mouse reaches the Hole, the Mouse wins.
 * If ever a position is repeated (i.e., the players are in the same position as a
 * previous turn, and it is the same player's turn to move), the game is a draw.
 *
 * Given a graph, and assuming both players play optimally, return
 *
 * 1 if the mouse wins the game,
 * 2 if the cat wins the game, or
 * 0 if the game is a draw.
 *
 *
 * Example 1:
 *
 * Input: graph = [[2,5],[3],[0,4,5],[1,4,5],[2,3],[0,2,3]]
 * Output: 0
 *
 * Example 2:
 *
 * Input: graph = [[1,3],[0],[3],[0,2]]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 3 <= graph.length <= 50
 * 1 <= graph[i].length < graph.length
 * 0 <= graph[i][j] < graph.length
 * graph[i][j] != i
 * graph[i] is unique.
 * The mouse and the cat can always move.
 *
 */
public class CatAndMouse {

    // V0
    // IDEA: RETROGRADE ANALYSIS (BFS / topological sort from the TERMINAL states)
    /**
     *  A game state is (mousePos, catPos, whoseTurn). There are only n * n * 2 of them.
     *
     *  TERMINAL (known) states:
     *     - mouse at the hole (0)        -> MOUSE_WIN
     *     - cat and mouse on same node   -> CAT_WIN
     *
     *  We propagate BACKWARDS from those known states. For a predecessor state where
     *  player P is to move:
     *
     *     - if the known child is a WIN for P -> P just picks that move
     *                                           -> the state is a win for P
     *
     *     - otherwise -> that move is BAD for P; decrement its `degree`
     *                    (the number of untried moves). When degree hits 0,
     *                    EVERY move loses, so the state takes the opponent's result.
     *
     *  Anything STILL unlabelled when the queue empties is a DRAW.
     *
     *  NOTE !!! the cat can NEVER step into the hole, so the cat's move count for a
     *           node adjacent to node 0 is one LESS.
     *
     *  time  = O(n^3)
     *  space = O(n^2)
     */

    private static final int HOLE = 0;
    private static final int MOUSE_TURN = 0;
    private static final int CAT_TURN = 1;
    private static final int DRAW = 0;
    private static final int MOUSE_WIN = 1;
    private static final int CAT_WIN = 2;

    public int catMouseGame(int[][] graph) {
        int n = graph.length;

        // result[m][c][t], degree[m][c][t]
        int[][][] result = new int[n][n][2];
        int[][][] degree = new int[n][n][2];

        for (int m = 0; m < n; m++) {
            for (int c = 1; c < n; c++) {
                degree[m][c][MOUSE_TURN] = graph[m].length;
                degree[m][c][CAT_TURN] = graph[c].length;
            }
            /** NOTE !!!
             *
             *  the cat is NOT allowed to enter the hole -> drop that option
             *  from the move count of every cat-node adjacent to the hole
             */
            for (int c : graph[HOLE]) {
                degree[m][c][CAT_TURN] -= 1;
            }
        }

        // {mouse, cat, turn}
        Deque<int[]> q = new ArrayDeque<>();

        // terminal: mouse reached the hole
        for (int c = 1; c < n; c++) {
            result[0][c][MOUSE_TURN] = MOUSE_WIN;
            result[0][c][CAT_TURN] = MOUSE_WIN;
            q.offer(new int[] { 0, c, MOUSE_TURN });
            q.offer(new int[] { 0, c, CAT_TURN });
        }

        // terminal: cat caught the mouse
        for (int i = 1; i < n; i++) {
            result[i][i][MOUSE_TURN] = CAT_WIN;
            result[i][i][CAT_TURN] = CAT_WIN;
            q.offer(new int[] { i, i, MOUSE_TURN });
            q.offer(new int[] { i, i, CAT_TURN });
        }

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int m = cur[0];
            int c = cur[1];
            int t = cur[2];
            int outcome = result[m][c][t];

            for (int[] prev : prevStates(graph, m, c, t)) {
                int pm = prev[0];
                int pc = prev[1];
                int pt = prev[2];

                if (result[pm][pc][pt] != DRAW) {
                    continue; // already decided
                }

                boolean moverWins = (outcome == MOUSE_WIN && pt == MOUSE_TURN)
                        || (outcome == CAT_WIN && pt == CAT_TURN);

                if (moverWins) {
                    // the moving player can FORCE this outcome
                    result[pm][pc][pt] = outcome;
                    q.offer(new int[] { pm, pc, pt });
                } else {
                    degree[pm][pc][pt] -= 1;
                    if (degree[pm][pc][pt] == 0) {
                        // EVERY single move loses -> forced
                        result[pm][pc][pt] = outcome;
                        q.offer(new int[] { pm, pc, pt });
                    }
                }
            }
        }

        // mouse starts at 1, cat starts at 2, mouse moves first
        return result[1][2][MOUSE_TURN];
    }

    /** states that could have led to (m, c, t) in ONE move */
    private int[][] prevStates(int[][] graph, int m, int c, int t) {
        int pt = t ^ 1; // the player that moved LAST

        if (pt == CAT_TURN) {
            // the cat moved into c, so it came from a neighbour of c (NEVER the hole)
            int cnt = 0;
            for (int pc : graph[c]) {
                if (pc != HOLE) {
                    cnt++;
                }
            }
            int[][] res = new int[cnt][3];
            int idx = 0;
            for (int pc : graph[c]) {
                if (pc != HOLE) {
                    res[idx++] = new int[] { m, pc, pt };
                }
            }
            return res;
        }

        // the mouse moved into m, so it came from a neighbour of m
        int[][] res = new int[graph[m].length][3];
        for (int i = 0; i < graph[m].length; i++) {
            res[i] = new int[] { graph[m][i], c, pt };
        }
        return res;
    }

}
