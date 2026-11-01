package LeetCodeJava.BFS;

// https://leetcode.com/problems/cat-and-mouse-ii/description/

import java.util.*;

/**
 * 1728. Cat and Mouse II
 * Hard
 *
 * A game is played by a cat and a mouse named Cat and Mouse.
 *
 * The environment is represented by a grid of size rows x cols, where each element is a wall,
 * floor, player (Cat, Mouse), or food.
 *
 * Players are represented by the characters 'C'(Cat),'M'(Mouse).
 * Floors are represented by the character '.' and can be walked on.
 * Walls are represented by the character '#' and cannot be walked on.
 * Food is represented by the character 'F' and can be walked on.
 * There is only one of each character 'C', 'M', and 'F' in grid.
 *
 * Mouse and Cat play according to the following rules:
 *
 * Mouse moves first, then they take turns to move.
 * During each turn, Cat and Mouse can jump in one of the four directions (left, right, up, down).
 * They cannot jump over the wall nor outside of the grid.
 * catJump, mouseJump are the maximum lengths Cat and Mouse can jump at a time, respectively.
 * Cat and Mouse can jump less than the maximum length.
 * Staying in the same position is allowed.
 * Mouse can jump over Cat.
 *
 * The game can end in 4 ways:
 *
 * If Cat occupies the same position as Mouse, Cat wins.
 * If Cat reaches the food first, Cat wins.
 * If Mouse reaches the food first, Mouse wins.
 * If Mouse cannot get to the food within 1000 turns, Cat wins.
 *
 * Given a rows x cols matrix grid and two integers catJump and mouseJump, return true if Mouse
 * can win the game if both Cat and Mouse play optimally, otherwise return false.
 *
 *
 * Example 1:
 *
 * Input: grid = ["####F","#C...","M...."], catJump = 1, mouseJump = 2
 * Output: true
 * Explanation: Cat cannot catch Mouse on its turn nor can it get the food before Mouse.
 *
 * Example 2:
 *
 * Input: grid = ["M.C...F"], catJump = 1, mouseJump = 4
 * Output: true
 *
 * Example 3:
 *
 * Input: grid = ["M.C...F"], catJump = 1, mouseJump = 3
 * Output: false
 *
 *
 * Constraints:
 *
 * rows == grid.length
 * cols = grid[i].length
 * 1 <= rows, cols <= 8
 * grid[i][j] consist only of characters 'C', 'M', 'F', '.', and '#'.
 * There is only one of each character 'C', 'M', and 'F' in grid.
 * 1 <= catJump, mouseJump <= 8
 *
 */
public class CatAndMouseII {

    private static final int MOUSE_TURN = 0;
    private static final int CAT_TURN = 1;

    private static final int UNKNOWN = 0;
    private static final int MOUSE_WIN = 1;
    private static final int CAT_WIN = 2;

    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    // V0
    // IDEA: GAME THEORY, RETROGRADE BFS FROM THE TERMINAL STATES
    //  state = (mouse cell, cat cell, whose turn): at most 64 * 64 * 2 states.
    //  Forward minimax loops forever on cycles, so propagate results BACKWARDS:
    //    terminal: cat on mouse -> CAT_WIN, cat on food -> CAT_WIN, mouse on food -> MOUSE_WIN
    //  for a predecessor whose mover is P and a resolved successor won by W:
    //    - P == W  -> P simply picks this move: predecessor resolved as W
    //    - P != W  -> resolved as W only once EVERY move of P loses (degree hits 0)
    //  Moves are symmetric (a clear jump works both ways), so the forward move lists
    //  double as the "where could the mover have come from" lists.
    //  States never resolved are draws, which the 1000-turn rule scores as a cat win.
    /**
     * time = O((R * C)^2 * (R + C))
     * space = O((R * C)^2)
     */
    public boolean canMouseWin(String[] grid, int catJump, int mouseJump) {
        int rows = grid.length;
        int cols = grid[0].length();
        int cellCount = rows * cols;

        int mouseStart = 0;
        int catStart = 0;
        int food = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char ch = grid[r].charAt(c);
                if (ch == 'M') {
                    mouseStart = r * cols + c;
                } else if (ch == 'C') {
                    catStart = r * cols + c;
                } else if (ch == 'F') {
                    food = r * cols + c;
                }
            }
        }

        List<List<Integer>> mouseMoves = buildMoves(grid, mouseJump);
        List<List<Integer>> catMoves = buildMoves(grid, catJump);

        // result[mouse][cat][turn]; degree = how many moves of the mover are still unresolved
        int[][][] result = new int[cellCount][cellCount][2];
        int[][][] degree = new int[cellCount][cellCount][2];
        for (int mouse = 0; mouse < cellCount; mouse++) {
            for (int cat = 0; cat < cellCount; cat++) {
                degree[mouse][cat][MOUSE_TURN] = mouseMoves.get(mouse).size();
                degree[mouse][cat][CAT_TURN] = catMoves.get(cat).size();
            }
        }

        // seed every terminal state exactly once
        Deque<int[]> queue = new ArrayDeque<>();
        for (int mouse = 0; mouse < cellCount; mouse++) {
            for (int cat = 0; cat < cellCount; cat++) {
                for (int turn = 0; turn < 2; turn++) {
                    int outcome = UNKNOWN;
                    if (mouse == cat || cat == food) {
                        outcome = CAT_WIN;
                    } else if (mouse == food) {
                        outcome = MOUSE_WIN;
                    }
                    if (outcome != UNKNOWN) {
                        result[mouse][cat][turn] = outcome;
                        queue.offer(new int[] {mouse, cat, turn});
                    }
                }
            }
        }

        while (!queue.isEmpty()) {
            int[] state = queue.poll();
            int mouse = state[0];
            int cat = state[1];
            int turn = state[2];
            int winner = result[mouse][cat][turn];

            // the player who moved INTO this state
            int prevTurn = 1 - turn;
            List<int[]> predecessors = new ArrayList<>();
            if (prevTurn == CAT_TURN) {
                for (int prevCat : catMoves.get(cat)) {
                    if (result[mouse][prevCat][CAT_TURN] == UNKNOWN) {
                        predecessors.add(new int[] {mouse, prevCat});
                    }
                }
            } else {
                for (int prevMouse : mouseMoves.get(mouse)) {
                    if (result[prevMouse][cat][MOUSE_TURN] == UNKNOWN) {
                        predecessors.add(new int[] {prevMouse, cat});
                    }
                }
            }

            // does the mover of the predecessor state win by moving here?
            boolean moverWins = (prevTurn == MOUSE_TURN && winner == MOUSE_WIN)
                    || (prevTurn == CAT_TURN && winner == CAT_WIN);

            for (int[] prev : predecessors) {
                int prevMouse = prev[0];
                int prevCat = prev[1];
                if (moverWins) {
                    result[prevMouse][prevCat][prevTurn] = winner;
                    queue.offer(new int[] {prevMouse, prevCat, prevTurn});
                } else {
                    // one more losing option; resolved only when all options lose
                    degree[prevMouse][prevCat][prevTurn]--;
                    if (degree[prevMouse][prevCat][prevTurn] == 0) {
                        result[prevMouse][prevCat][prevTurn] = winner;
                        queue.offer(new int[] {prevMouse, prevCat, prevTurn});
                    }
                }
            }
        }
        return result[mouseStart][catStart][MOUSE_TURN] == MOUSE_WIN;
    }

    /** For every open cell, the cells reachable in one jump of length 0..maxJump (stay included). */
    private List<List<Integer>> buildMoves(String[] grid, int maxJump) {
        int rows = grid.length;
        int cols = grid[0].length();
        List<List<Integer>> moves = new ArrayList<>();
        for (int i = 0; i < rows * cols; i++) {
            moves.add(new ArrayList<>());
        }
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r].charAt(c) == '#') {
                    continue;
                }
                int cell = r * cols + c;
                // staying put is a legal move
                moves.get(cell).add(cell);
                for (int[] dir : DIRECTIONS) {
                    for (int length = 1; length <= maxJump; length++) {
                        int nr = r + dir[0] * length;
                        int nc = c + dir[1] * length;
                        // cannot jump over a wall or off the grid
                        if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || grid[nr].charAt(nc) == '#') {
                            break;
                        }
                        moves.get(cell).add(nr * cols + nc);
                    }
                }
            }
        }
        return moves;
    }
}
