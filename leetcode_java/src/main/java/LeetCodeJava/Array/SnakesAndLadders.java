package LeetCodeJava.Array;

// https://leetcode.com/problems/snakes-and-ladders/description/
// https://leetcode.cn/problems/snakes-and-ladders/description/

import java.util.*;

/**
 *  909. Snakes and Ladders
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * You are given an n x n integer matrix board where the cells are labeled from 1 to n2 in a Boustrophedon style starting from the bottom left of the board (i.e. board[n - 1][0]) and alternating direction each row.
 *
 *
 *  You start on square 1 of the board. In each move, starting from square curr, do the following:
 *
 * Choose a destination square next with a label in the range [curr + 1, min(curr + 6, n2)].
 * This choice simulates the result of a standard 6-sided die roll: i.e., there are always at most 6 destinations, regardless of the size of the board.
 * If next has a snake or ladder, you must move to the destination of that snake or ladder. Otherwise, you move to next.
 * The game ends when you reach the square n2.
 * A board square on row r and column c has a snake or ladder if board[r][c] != -1. The destination of that snake or ladder is board[r][c]. Squares 1 and n2 are not the starting points of any snake or ladder.
 *
 * Note that you only take a snake or ladder at most once per dice roll. If the destination to a snake or ladder is the start of another snake or ladder, you do not follow the subsequent snake or ladder.
 *
 * For example, suppose the board is [[-1,4],[-1,3]], and on the first move, your destination square is 2. You follow the ladder to square 3, but do not follow the subsequent ladder to 4.
 * Return the least number of dice rolls required to reach the square n2. If it is not possible to reach the square, return -1.
 *
 *
 *
 *   Example 1:
 *
 *
 * Input: board = [[-1,-1,-1,-1,-1,-1],[-1,-1,-1,-1,-1,-1],[-1,-1,-1,-1,-1,-1],[-1,35,-1,-1,13,-1],[-1,-1,-1,-1,-1,-1],[-1,15,-1,-1,-1,-1]]
 * Output: 4
 * Explanation:
 * In the beginning, you start at square 1 (at row 5, column 0).
 * You decide to move to square 2 and must take the ladder to square 15.
 * You then decide to move to square 17 and must take the snake to square 13.
 * You then decide to move to square 14 and must take the ladder to square 35.
 * You then decide to move to square 36, ending the game.
 * This is the lowest possible number of moves to reach the last square, so return 4.
 * Example 2:
 *
 * Input: board = [[-1,-1],[-1,3]]
 * Output: 1
 *
 *
 * Constraints:
 *
 * n == board.length == board[i].length
 * 2 <= n <= 20
 * board[i][j] is either -1 or in the range [1, n2].
 * The squares labeled 1 and n2 are not the starting points of any snake or ladder.
 *
 *
 */
public class SnakesAndLadders {

    // V0
//    public int snakesAndLadders(int[][] board) {
//
//    }

    // V1
    // IDEA: BFS
    // https://leetcode.ca/2018-05-27-909-Snakes-and-Ladders/
    private int n;
    public int snakesAndLadders_1(int[][] board) {
        n = board.length;
        Deque<Integer> q = new ArrayDeque<>();
        q.offer(1);
        boolean[] vis = new boolean[n * n + 1];
        vis[1] = true;
        int ans = 0;
        while (!q.isEmpty()) {
            for (int t = q.size(); t > 0; --t) {
                int curr = q.poll();
                if (curr == n * n) {
                    return ans;
                }
                for (int k = curr + 1; k <= Math.min(curr + 6, n * n); ++k) {
                    int[] p = get(k);
                    int next = k;
                    int i = p[0], j = p[1];
                    if (board[i][j] != -1) {
                        next = board[i][j];
                    }
                    if (!vis[next]) {
                        vis[next] = true;
                        q.offer(next);
                    }
                }
            }
            ++ans;
        }
        return -1;
    }

    private int[] get(int x) {
        int i = (x - 1) / n, j = (x - 1) % n;
        if (i % 2 == 1) {
            j = n - 1 - j;
        }
        return new int[] {n - 1 - i, j};
    }



    // V2
    // IDEA: BFS
    // https://leetcode.com/problems/snakes-and-ladders/solutions/6797437/using-bfs-with-images-example-walkthroug-22bs/
    public int snakesAndLadders_2(int[][] board) {
        int n = board.length;
        int[] min_rolls = new int[n * n + 1];
        Arrays.fill(min_rolls, -1);
        Queue<Integer> q = new LinkedList<>();
        min_rolls[1] = 0;
        q.offer(1);

        while (!q.isEmpty()) {
            int x = q.poll();
            for (int i = 1; i <= 6 && x + i <= n * n; i++) {
                int t = x + i;
                int row = (t - 1) / n;
                int col = (t - 1) % n;
                int v = board[n - 1 - row][(row % 2 == 1) ? (n - 1 - col) : col];
                int y = (v > 0 ? v : t);
                if (y == n * n)
                    return min_rolls[x] + 1;
                if (min_rolls[y] == -1) {
                    min_rolls[y] = min_rolls[x] + 1;
                    q.offer(y);
                }
            }
        }
        return -1;
    }

    // V3
    // https://leetcode.com/problems/snakes-and-ladders/solutions/3092376/day-24-easiest-beginner-friendly-solutio-e5kp/
    public int snakesAndLadders_3(int[][] board) {
        int n = board.length;
        int moves = 0;
        Queue<Integer> q = new LinkedList<>();
        boolean[][] visited = new boolean[n][n];
        q.add(1);
        visited[n - 1][0] = true;
        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                int currBoardVal = q.poll();
                if (currBoardVal == n * n)
                    return moves;
                for (int diceVal = 1; diceVal <= 6; diceVal++) {
                    if (currBoardVal + diceVal > n * n)
                        break;
                    int[] pos = findCoordinates(currBoardVal + diceVal, n);
                    int row = pos[0];
                    int col = pos[1];
                    if (visited[row][col] == false) {
                        visited[row][col] = true;
                        if (board[row][col] == -1) {
                            q.add(currBoardVal + diceVal);
                        } else {
                            q.add(board[row][col]);
                        }
                    }
                }
            }
            moves++;
        }
        return -1;
    }

    public int[] findCoordinates(int curr, int n) {
        int r = n - (curr - 1) / n - 1;
        int c = (curr - 1) % n;
        if (r % 2 == n % 2) {
            return new int[] { r, n - 1 - c };
        } else {
            return new int[] { r, c };
        }
    }


    // V4



}
