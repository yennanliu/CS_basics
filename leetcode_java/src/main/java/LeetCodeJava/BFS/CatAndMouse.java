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


    // V1
    // IDEA: MEMOIZED MINIMAX with a TURN BOUND
    /**
     *  Play the game forward with minimax, memoising on (mouse, cat, turn).
     *
     *  The `repeated position = draw` rule is handled by a TURN CAP: after 2n plies
     *  a position must have repeated (there are only n * n * 2 distinct states), so
     *  we declare a draw.
     *
     *  Reads exactly like the game rules, unlike V0's backwards propagation.
     *
     *  time  = O(n^3)
     *  space = O(n^3)
     */
    public int catMouseGame_1(int[][] graph) {
        int n = graph.length;
        Integer[][][] memo = new Integer[n][n][2 * n + 1];
        return minimax(graph, 1, 2, 0, memo);
    }

    private int minimax(int[][] graph, int mouse, int cat, int turn, Integer[][][] memo) {
        if (mouse == 0) {
            return 1; // mouse reached the hole
        }
        if (mouse == cat) {
            return 2; // cat caught the mouse
        }
        int n = graph.length;
        if (turn >= 2 * n) {
            return 0; // a position must have repeated by now -> draw
        }
        if (memo[mouse][cat][turn] != null) {
            return memo[mouse][cat][turn];
        }

        boolean mouseTurn = turn % 2 == 0;
        int res;

        if (mouseTurn) {
            res = 2; // assume the worst for the mouse
            for (int nxt : graph[mouse]) {
                int sub = minimax(graph, nxt, cat, turn + 1, memo);
                if (sub == 1) {
                    res = 1;
                    break; // the mouse can force a win
                }
                if (sub == 0) {
                    res = 0; // a draw beats a loss
                }
            }
        } else {
            res = 1;
            for (int nxt : graph[cat]) {
                if (nxt == 0) {
                    continue; // the cat may not enter the hole
                }
                int sub = minimax(graph, mouse, nxt, turn + 1, memo);
                if (sub == 2) {
                    res = 2;
                    break;
                }
                if (sub == 0) {
                    res = 0;
                }
            }
        }

        memo[mouse][cat][turn] = res;
        return res;
    }

    // V2
    // IDEA: FIXED-POINT ITERATION (repeat full sweeps until nothing changes)
    /**
     *  Instead of a queue with degree counters, sweep EVERY state repeatedly and
     *  recompute its label from its children until a whole pass makes no change.
     *
     *  Much simpler to write and to argue about -- it is just `keep applying the
     *  rules until stable` -- at the cost of O(n) extra sweeps.
     *
     *  time  = O(n^4)
     *  space = O(n^2)
     */
    public int catMouseGame_2(int[][] graph) {
        int n = graph.length;
        int[][][] color = new int[n][n][2];

        for (int c = 1; c < n; c++) {
            color[0][c][0] = 1; // mouse home -> mouse wins
            color[0][c][1] = 1;
        }
        for (int i = 1; i < n; i++) {
            color[i][i][0] = 2; // caught -> cat wins
            color[i][i][1] = 2;
        }

        boolean changed = true;
        while (changed) {
            changed = false;
            for (int m = 1; m < n; m++) {
                for (int c = 1; c < n; c++) {
                    if (m == c) {
                        continue;
                    }
                    for (int t = 0; t < 2; t++) {
                        if (color[m][c][t] != 0) {
                            continue;
                        }
                        int want = t == 0 ? 1 : 2;   // the mover's winning label
                        int lose = t == 0 ? 2 : 1;

                        boolean canWin = false;
                        boolean allLose = true;
                        for (int nxt : (t == 0 ? graph[m] : graph[c])) {
                            if (t == 1 && nxt == 0) {
                                continue;
                            }
                            int child = t == 0 ? color[nxt][c][1] : color[m][nxt][0];
                            if (child == want) {
                                canWin = true;
                                break;
                            }
                            if (child != lose) {
                                allLose = false; // a draw or an undecided child
                            }
                        }

                        if (canWin) {
                            color[m][c][t] = want;
                            changed = true;
                        } else if (allLose) {
                            color[m][c][t] = lose;
                            changed = true;
                        }
                    }
                }
            }
        }

        return color[1][2][0];
    }

    // V3
    // IDEA: BOTTOM-UP TABULATION over the REMAINING move budget
    /**
     *  dp[t][m][c][turn] = the outcome when at most t plies remain.
     *
     *  Filling t upward turns the recursion of V1 into a plain loop -- no stack, no
     *  memo table lookups -- and the layer index makes the `2n plies then draw`
     *  argument explicit rather than hidden in a base case.
     *
     *  time  = O(n^3)
     *  space = O(n^2) (two rolling layers)
     */
    public int catMouseGame_3(int[][] graph) {
        int n = graph.length;
        int limit = 2 * n;

        // layer[m][c][turn] for the CURRENT budget
        int[][][] prev = new int[n][n][2];
        for (int m = 0; m < n; m++) {
            for (int c = 0; c < n; c++) {
                for (int t = 0; t < 2; t++) {
                    prev[m][c][t] = m == 0 ? 1 : (m == c ? 2 : 0);
                }
            }
        }

        for (int budget = 1; budget <= limit; budget++) {
            int[][][] cur = new int[n][n][2];
            for (int m = 0; m < n; m++) {
                for (int c = 0; c < n; c++) {
                    for (int t = 0; t < 2; t++) {
                        if (m == 0) {
                            cur[m][c][t] = 1;
                            continue;
                        }
                        if (m == c) {
                            cur[m][c][t] = 2;
                            continue;
                        }
                        int want = t == 0 ? 1 : 2;
                        int lose = t == 0 ? 2 : 1;

                        boolean canWin = false;
                        boolean allLose = true;
                        for (int nxt : (t == 0 ? graph[m] : graph[c])) {
                            if (t == 1 && nxt == 0) {
                                continue;
                            }
                            int child = t == 0 ? prev[nxt][c][1] : prev[m][nxt][0];
                            if (child == want) {
                                canWin = true;
                                break;
                            }
                            if (child != lose) {
                                allLose = false;
                            }
                        }
                        cur[m][c][t] = canWin ? want : (allLose ? lose : 0);
                    }
                }
            }
            prev = cur;
        }

        return prev[1][2][0];
    }

}
