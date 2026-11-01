package LeetCodeJava.Graph;

// https://leetcode.com/problems/minimum-path-cost-in-a-hidden-grid/description/

import java.util.*;

/**
 * 1810. Minimum Path Cost in a Hidden Grid
 * Medium
 *
 * This is an interactive problem.
 *
 * There is a robot in a hidden grid, and you are trying to get it from its starting cell to the target cell
 * in this grid. The grid is of size m x n, and each cell in the grid is either empty or blocked. It is
 * guaranteed that the starting cell and the target cell are different, and neither of them is blocked.
 *
 * Each cell has a cost that you need to pay each time you move to the cell. The starting cell's cost is not
 * applied before the robot moves.
 *
 * You want to find the minimum total cost to move the robot to the target cell. However, you do not know
 * the grid's dimensions, the starting cell, nor the target cell. You are only allowed to ask queries to the
 * GridMaster object.
 *
 * The GridMaster class has the following functions:
 *
 * boolean canMove(char direction) Returns true if the robot can move in that direction. Otherwise, it
 * returns false.
 * int move(char direction) Moves the robot in that direction and returns the cost of moving to that cell.
 * If this move would move the robot to a blocked cell or off the grid, the move will be ignored, the robot
 * will remain in the same position, and the function will return -1.
 * boolean isTarget() Returns true if the robot is currently on the target cell. Otherwise, it returns false.
 *
 * Note that direction in the above functions should be a character from {'U','D','L','R'}, representing
 * the directions up, down, left, and right, respectively.
 *
 * Return the minimum total cost to get the robot from its initial starting cell to the target cell. If
 * there is no valid path between the cells, return -1.
 *
 * Custom testing:
 *
 * The test input is read as a 2D matrix grid of size m x n and four integers r1, c1, r2, and c2 where:
 *
 * grid[i][j] == 0 indicates that the cell (i, j) is blocked.
 * grid[i][j] >= 1 indicates that the cell (i, j) is empty and grid[i][j] is the cost to move to that cell.
 * (r1, c1) is the starting cell of the robot.
 * (r2, c2) is the target cell of the robot.
 *
 * Remember that you will not have this information in your code.
 *
 *
 * Example 1:
 *
 * Input: grid = [[2,3],[1,1]], r1 = 0, c1 = 1, r2 = 1, c2 = 0
 * Output: 2
 *
 * Example 2:
 *
 * Input: grid = [[0,3,1],[3,4,2],[1,2,0]], r1 = 2, c1 = 0, r2 = 0, c2 = 2
 * Output: 9
 * Explanation: The minimum cost path is (2,0) -> (2,1) -> (1,1) -> (1,2) -> (0,2).
 *
 * Example 3:
 *
 * Input: grid = [[1,0],[0,1]], r1 = 0, c1 = 0, r2 = 1, c2 = 1
 * Output: -1
 * Explanation: There is no path from the robot to the target cell.
 *
 *
 * Constraints:
 *
 * 1 <= n, m <= 100
 * m == grid.length
 * n == grid[i].length
 * 0 <= grid[i][j] <= 100
 *
 */
public class MinimumPathCostInAHiddenGrid {

    /**
     * This is the GridMaster's API interface (provided by LeetCode).
     * Declared here so this file compiles standalone - do NOT implement it.
     */
    public interface GridMaster {
        boolean canMove(char direction);

        int move(char direction);

        boolean isTarget();
    }

    // the grid is at most 100 x 100 and the start is unknown, so a 200 x 200 board centred
    // on the start holds every reachable cell
    private static final int SIZE = 200;
    private static final int START = 100;
    private static final char[] MOVES = {'U', 'R', 'D', 'L'};
    private static final char[] UNDO = {'D', 'L', 'U', 'R'};
    private static final int[] DELTA_ROW = {-1, 0, 1, 0};
    private static final int[] DELTA_COL = {0, 1, 0, -1};

    // cost[r][c] = price of entering the cell; -1 = blocked or never reached
    private int[][] cost;
    private int targetRow;
    private int targetCol;

    // V0
    // IDEA: DFS TO EXPLORE (map the hidden grid) + DIJKSTRA ON THE MAP
    // 1) DFS with backtracking from the board centre: step into each unvisited open
    //    neighbour, record the cost move() returns, explore from there, then step back with
    //    the opposite direction so the robot is where the DFS expects. isTarget() locates
    //    the target on the way.
    // 2) the costs are now known: Dijkstra from the start, where entering a cell pays its
    //    cost and the start cell itself is free.
    // NOTE: the DFS is iterative -- a 100 x 100 grid can be a 10^4-cell snake, deep enough to
    // overflow a small thread stack if written recursively.
    /**
     * time = O(M * N * log(M * N))
     * space = O(M * N)
     */
    public int findShortestPath(GridMaster master) {
        cost = new int[SIZE][SIZE];
        for (int[] row : cost) {
            Arrays.fill(row, -1);
        }
        cost[START][START] = 0;
        targetRow = -1;
        targetCol = -1;

        explore(master, START, START);
        if (targetRow == -1) {
            return -1;
        }

        // Dijkstra over the mapped cells; entry = {total cost, row, col}
        int[][] best = new int[SIZE][SIZE];
        for (int[] row : best) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        best[START][START] = 0;
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        heap.offer(new int[] {0, START, START});
        while (!heap.isEmpty()) {
            int[] top = heap.poll();
            int total = top[0];
            int row = top[1];
            int col = top[2];
            if (total > best[row][col]) {
                continue;
            }
            if (row == targetRow && col == targetCol) {
                return total;
            }
            for (int dir = 0; dir < 4; dir++) {
                int nextRow = row + DELTA_ROW[dir];
                int nextCol = col + DELTA_COL[dir];
                if (!inBoard(nextRow, nextCol) || cost[nextRow][nextCol] == -1) {
                    continue;
                }
                int candidate = total + cost[nextRow][nextCol];
                if (candidate < best[nextRow][nextCol]) {
                    best[nextRow][nextCol] = candidate;
                    heap.offer(new int[] {candidate, nextRow, nextCol});
                }
            }
        }
        return -1;
    }

    /** Maps every cell reachable from (row, col); the robot ends where it started. */
    private void explore(GridMaster master, int row, int col) {
        // frame = {row, col, next direction to try, index of the move that entered it (-1 = start)}
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[] {row, col, 0, -1});
        if (master.isTarget()) {
            targetRow = row;
            targetCol = col;
        }
        while (!stack.isEmpty()) {
            int[] frame = stack.peek();
            // all four directions tried: leave this cell, walking the robot back
            if (frame[2] == 4) {
                stack.pop();
                if (frame[3] != -1) {
                    master.move(UNDO[frame[3]]);
                }
                continue;
            }
            int dir = frame[2];
            frame[2]++;

            int nextRow = frame[0] + DELTA_ROW[dir];
            int nextCol = frame[1] + DELTA_COL[dir];
            if (!inBoard(nextRow, nextCol) || cost[nextRow][nextCol] != -1 || !master.canMove(MOVES[dir])) {
                continue;
            }
            // step in and record the price of entering this cell
            cost[nextRow][nextCol] = master.move(MOVES[dir]);
            if (master.isTarget()) {
                targetRow = nextRow;
                targetCol = nextCol;
            }
            stack.push(new int[] {nextRow, nextCol, 0, dir});
        }
    }

    /** True when (row, col) is inside the board. */
    private boolean inBoard(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }
}
