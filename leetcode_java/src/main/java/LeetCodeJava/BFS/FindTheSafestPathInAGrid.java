package LeetCodeJava.BFS;

// https://leetcode.com/problems/find-the-safest-path-in-a-grid/description/

import java.util.*;

/**
 * 2812. Find the Safest Path in a Grid
 * Medium
 *
 * You are given a 0-indexed 2D matrix grid of size n x n, where (r, c) represents:
 *
 * A cell containing a thief if grid[r][c] = 1
 * An empty cell if grid[r][c] = 0
 *
 * You are initially positioned at cell (0, 0). In one move, you can move to any
 * adjacent cell in the grid, including cells containing thieves.
 *
 * The safeness factor of a path on the grid is defined as the minimum manhattan
 * distance from any cell in the path to any thief in the grid.
 *
 * Return the maximum safeness factor of all paths leading to cell (n - 1, n - 1).
 *
 * An adjacent cell of cell (r, c), is one of the cells (r, c + 1), (r, c - 1),
 * (r + 1, c) and (r - 1, c) if it exists.
 *
 * The Manhattan distance between two cells (a, b) and (x, y) is equal to
 * |a - x| + |b - y|, where |val| denotes the absolute value of val.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,0,0],[0,0,0],[0,0,1]]
 * Output: 0
 * Explanation: All paths from (0, 0) to (n - 1, n - 1) go through the thieves in cells (0, 0) and (n - 1, n - 1).
 *
 * Example 2:
 *
 * Input: grid = [[0,0,1],[0,0,0],[0,0,0]]
 * Output: 2
 * Explanation: The path depicted in the picture above has a safeness factor of 2 since:
 * - The closest cell of the path to the thief at cell (0, 2) is cell (0, 0). The distance between them is | 0 - 0 | + | 0 - 2 | = 2.
 * It can be shown that there are no other paths with a higher safeness factor.
 *
 * Example 3:
 *
 * Input: grid = [[0,0,0,1],[0,0,0,0],[0,0,0,0],[1,0,0,0]]
 * Output: 2
 * Explanation: The path depicted in the picture above has a safeness factor of 2 since:
 * - The closest cell of the path to the thief at cell (0, 3) is cell (1, 2). The distance between them is | 0 - 1 | + | 3 - 2 | = 2.
 * - The closest cell of the path to the thief at cell (3, 0) is cell (3, 2). The distance between them is | 3 - 3 | + | 0 - 2 | = 2.
 * It can be shown that there are no other paths with a higher safeness factor.
 *
 *
 * Constraints:
 *
 * 1 <= grid.length == n <= 400
 * grid[i].length == n
 * grid[i][j] is either 0 or 1.
 * There is at least one thief in the grid.
 *
 */
public class FindTheSafestPathInAGrid {

    private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    // V0
    // IDEA: MULTI-SOURCE BFS + MAX-MIN (WIDEST PATH) DIJKSTRA
    //
    //   Step 1: BFS from EVERY thief at once gives dist[r][c] = manhattan distance
    //           from (r, c) to the nearest thief (on an open 4-neighbour grid, BFS
    //           layers ARE manhattan distance).
    //   Step 2: find the path (0,0) -> (n-1,n-1) whose MINIMUM dist is largest --
    //           the bottleneck / widest-path problem. Dijkstra with a max-heap where a
    //           path's value is min(value so far, dist[next]); the first time the
    //           destination is popped, its value is optimal.
    //
    //   NOTE: a thief on (0,0) or (n-1,n-1) makes the answer 0; seeding the heap with
    //         dist[0][0] handles that without a special case.
    /**
     * time = O(N^2 * log N)
     * space = O(N^2)
     */
    public int maximumSafenessFactor(List<List<Integer>> grid) {
        int n = grid.size();
        int[][] dist = distanceToNearestThief(grid);

        // best[r][c] = the largest bottleneck found so far for a path ending at (r, c)
        int[][] best = new int[n][n];
        for (int[] row : best) {
            Arrays.fill(row, -1);
        }
        best[0][0] = dist[0][0];

        // max-heap of {bottleneck, r, c}, largest bottleneck first
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b[0], a[0]));
        maxHeap.offer(new int[]{dist[0][0], 0, 0});

        while (!maxHeap.isEmpty()) {
            int[] current = maxHeap.poll();
            int bottleneck = current[0];
            int r = current[1];
            int c = current[2];

            // a stale entry: (r, c) was already reached with a better bottleneck
            if (bottleneck < best[r][c]) {
                continue;
            }
            // popped in decreasing order, so the destination's first pop is optimal
            if (r == n - 1 && c == n - 1) {
                return bottleneck;
            }

            for (int[] direction : DIRECTIONS) {
                int nextR = r + direction[0];
                int nextC = c + direction[1];
                if (nextR < 0 || nextR >= n || nextC < 0 || nextC >= n) {
                    continue;
                }
                // the path is only as safe as its least safe cell
                int nextBottleneck = Math.min(bottleneck, dist[nextR][nextC]);
                if (nextBottleneck > best[nextR][nextC]) {
                    best[nextR][nextC] = nextBottleneck;
                    maxHeap.offer(new int[]{nextBottleneck, nextR, nextC});
                }
            }
        }
        return 0;
    }

    // V1
    // IDEA: MULTI-SOURCE BFS + BINARY SEARCH ON THE ANSWER + BFS REACHABILITY
    //
    //   "Can we reach the end walking only on cells with dist >= k?" is monotone in k
    //   (true for k, then true for every smaller k), so binary search the largest k
    //   for which a plain BFS still reaches (n-1, n-1).
    //   Same idea as V0 but with an extra log factor; easier to get right under time
    //   pressure if Dijkstra-on-max-min feels shaky.
    /**
     * time = O(N^2 * log N)
     * space = O(N^2)
     */
    public int maximumSafenessFactor_1(List<List<Integer>> grid) {
        int n = grid.size();
        int[][] dist = distanceToNearestThief(grid);

        // the answer can never exceed the safeness of the start or the end cell
        int left = 0;
        int right = Math.min(dist[0][0], dist[n - 1][n - 1]);
        int answer = 0;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (canReach(dist, mid)) {
                // mid is achievable; try for a safer path
                answer = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return answer;
    }

    /** True if (0,0) reaches (n-1,n-1) stepping only on cells with dist >= minSafeness. */
    private boolean canReach(int[][] dist, int minSafeness) {
        int n = dist.length;
        if (dist[0][0] < minSafeness) {
            return false;
        }

        boolean[][] visited = new boolean[n][n];
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{0, 0});
        visited[0][0] = true;

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            if (cell[0] == n - 1 && cell[1] == n - 1) {
                return true;
            }
            for (int[] direction : DIRECTIONS) {
                int nextR = cell[0] + direction[0];
                int nextC = cell[1] + direction[1];
                if (nextR < 0 || nextR >= n || nextC < 0 || nextC >= n) {
                    continue;
                }
                // skip cells already seen, and cells too close to a thief
                if (visited[nextR][nextC] || dist[nextR][nextC] < minSafeness) {
                    continue;
                }
                visited[nextR][nextC] = true;
                queue.offer(new int[]{nextR, nextC});
            }
        }
        return false;
    }

    /** Multi-source BFS from every thief: dist[r][c] = distance to the nearest thief. */
    private int[][] distanceToNearestThief(List<List<Integer>> grid) {
        int n = grid.size();
        int[][] dist = new int[n][n];
        Queue<int[]> queue = new LinkedList<>();

        // every thief is a source at distance 0; -1 marks "not reached yet"
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (grid.get(r).get(c) == 1) {
                    dist[r][c] = 0;
                    queue.offer(new int[]{r, c});
                } else {
                    dist[r][c] = -1;
                }
            }
        }

        // the first time BFS reaches a cell is via its nearest thief
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            for (int[] direction : DIRECTIONS) {
                int nextR = cell[0] + direction[0];
                int nextC = cell[1] + direction[1];
                if (nextR < 0 || nextR >= n || nextC < 0 || nextC >= n) {
                    continue;
                }
                if (dist[nextR][nextC] != -1) {
                    continue;
                }
                dist[nextR][nextC] = dist[cell[0]][cell[1]] + 1;
                queue.offer(new int[]{nextR, nextC});
            }
        }
        return dist;
    }
}
