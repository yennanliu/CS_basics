package LeetCodeJava.Graph;

// https://leetcode.com/problems/minimum-obstacle-removal-to-reach-corner/description/

import java.util.*;

/**
 * 2290. Minimum Obstacle Removal to Reach Corner
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a 0-indexed 2D integer array grid of size m x n. Each cell has one of two values:
 *
 * 0 represents an empty cell,
 * 1 represents an obstacle that may be removed.
 * You can move up, down, left, or right from and to an empty cell.
 *
 * Return the minimum number of obstacles to remove so you can move from the upper left corner (0, 0) to the lower right corner (m - 1, n - 1).
 *
 *
 *
 * Example 1:
 *
 *
 * Input: grid = [[0,1,1],[1,1,0],[1,1,0]]
 * Output: 2
 * Explanation: We can remove the obstacles at (0, 1) and (0, 2) to create a path from (0, 0) to (2, 2).
 * It can be shown that we need to remove at least 2 obstacles, so we return 2.
 * Note that there may be other ways to remove 2 obstacles to create a path.
 * Example 2:
 *
 *
 * Input: grid = [[0,1,0,0,0],[0,1,0,1,0],[0,0,0,1,0]]
 * Output: 0
 * Explanation: We can move from (0, 0) to (2, 4) without removing any obstacles, so we return 0.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 105
 * 2 <= m * n <= 105
 * grid[i][j] is either 0 or 1.
 * grid[0][0] == grid[m - 1][n - 1] == 0
 *
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 *
 */
public class MinimumObstacleRemovalToReachCorner {

    // V0
    // IDEA: Dijkstra's Algorithm (fixed by gpt)
    /**
     *  NOTE !!!
     *
     * ✅ Summary:
     * 	•	Single cost var won’t work → need dist[][] to track per-cell minimum cost.
     * 	•	No explicit visited needed → the dist[][] + early skip (if (cost > dist[y][x]) continue) handles that.
     *
     */
    public int minimumObstacles(int[][] grid) {
        // edge
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        // length
        int l = grid.length;
        // width
        int w = grid[0].length;

        // Costs to reach each cell
        int[][] costs = new int[l][w];
        /**
         *  NOTE !!!
         *   fill costs grid via below trick
         */
        for (int i = 0; i < l; i++) {
            Arrays.fill(costs[i], Integer.MAX_VALUE);
        }

        /** NOTE !!!
         *
         *  init costs[0][0] as 0 (starting point)
         */
        costs[0][0] = 0;

        // Directions: right, left, down, up
        Integer[][] moves = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

        // PriorityQueue: min-heap on cost
        // V1: lambda style
        //PriorityQueue<List<Integer>> pq = new PriorityQueue<>((a, b) -> a.get(0) - b.get(0));
        // V2: formal Comparator syntax
        PriorityQueue<List<Integer>> pq = new PriorityQueue<>(new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> o1, List<Integer> o2) {
                int diff = o1.get(0) - o2.get(0);
                return diff;
            }
        });

        // Start at (0,0) with 0 cost
        List<Integer> start = new ArrayList<>();
        start.add(0); // cost
        start.add(0); // x
        start.add(0); // y

        pq.add(start);

        while (!pq.isEmpty()) {
            List<Integer> cur = pq.poll();
            int cost = cur.get(0);
            int x = cur.get(1);
            int y = cur.get(2);

            // Early exit
            if (x == w - 1 && y == l - 1) {
                /** NOTE !!!
                 *
                 *  for Early exit,
                 *  return the `cost` pop from latest element from PQ directly
                 */
                return cost;
            }

            for (Integer[] mv : moves) {
                int nx = x + mv[0];
                int ny = y + mv[1];

                /** NOTE !!!
                 *
                 *  the `validation` ONLY checks if the x, y is OUT of boundary
                 *  for cost, we'll handle inside the `if` code block
                 */
                if (nx >= 0 && nx < w && ny >= 0 && ny < l) {

                    /** NOTE !!!
                     *
                     *  get `newCost` from `prev cost` and `current` cost (grid[ny][nx]),
                     *  but NOTE that this is just a `tmp` newCost.
                     *  e.g. we'll ONLY proceed with this newCost if `it costs LESS`
                     *     e.g. if(newCost < costs[ny][nx])
                     */
                    int newCost = cost + grid[ny][nx]; // add obstacle cost
                    /** NOTE !!!
                     *
                     *  ONLY proceed with newCost if `it costs LESS`
                     */
                    if (newCost < costs[ny][nx]) {
                        /** NOTE !!!
                         *
                         *  update cost grid
                         */
                        costs[ny][nx] = newCost;

                        List<Integer> next = new ArrayList<>();
                        next.add(newCost);
                        next.add(nx);
                        next.add(ny);
                        /** NOTE !!!
                         *
                         *  add new element to PQ
                         */
                        pq.add(next);
                    }
                }
            }
        }

        return -1; // unreachable
    }

    // V0-1
    // IDEA: Dijkstra's Algorithm (fixed by gpt)
    /**
     *  NOTE !!!
     *
     * ✅ Summary:
     * 	•	Single cost var won’t work → need dist[][] to track per-cell minimum cost.
     * 	•	No explicit visited needed → the dist[][] + early skip (if (cost > dist[y][x]) continue) handles that.
     *
     */
    public int minimumObstacles_0_1(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        int m = grid.length; // rows
        int n = grid[0].length; // cols

        /**
         *   NOTE !!!
         *
         *    we need a 2D array to save the cost when BFS loop over the grid
         *    (CAN'T just use a single var (cost))
         *
         * ---
         *
         * 1. Why keep a dist[][] array instead of a single cost variable?
         *
         *
         * 	•	The minimum cost to reach a cell (x,y) is not unique across the grid.
         * 	•	For example, you might reach (2,2) with cost 3 via one path, but later find a better path with cost 2.
         * 	•	If you only had a single global cost variable, you couldn’t distinguish the costs of different cells — you’d lose information.
         *
         * That’s why:
         * 	•	dist[y][x] keeps track of the best cost found so far for each specific cell.
         * 	•	Dijkstra works by always expanding the lowest-cost node next, and updating neighbors only if we find a cheaper path.
         *
         * Without dist[][], you’d either:
         * 	•	Revisit nodes unnecessarily (potential infinite loops), or
         * 	•	Miss better paths (return wrong result).
         */
        // distance[y][x] = min obstacles to reach (y,x)
        int[][] dist = new int[m][n];
        for (int[] row : dist) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        dist[0][0] = 0;

        // PQ stores [cost, x, y]
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[] { 0, 0, 0 }); // start at (0,0) with cost=0

        int[][] moves = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int cost = cur[0], x = cur[1], y = cur[2];

            // Reached destination
            if (x == n - 1 && y == m - 1) {
                return cost;
            }

            // Skip if we already found better
            /**
             *  NOTE !!!
             *
             *   why DON'T need to maintain a `visited` var
             *   to prevent repeating visit ?
             *
             *  -----
             *
             *   2. Why no explicit visited array?
             *
             * This is subtle. In Dijkstra:
             * 	•	A node is considered “visited” (finalized) once it’s dequeued from the priority queue with its minimum cost.
             * 	•	Because of the if (cost > dist[y][x]) continue; check, we automatically ignore revisits that don’t improve cost.
             *
             *
             *  So, the role of visited is effectively played by:
             *
             *      ```
             *      if (cost > dist[y][x]) continue;
             *      ```
             *
             *   This guarantees:
             * 	•	The first time you pop a cell with its minimum cost, you expand it.
             * 	•	If another path later tries to reach the same cell with a higher cost, it gets ignored.
             *
             * 👉 That’s why visited isn’t needed in Dijkstra — the dist[][] array + priority queue ensure correctness.
             *
             */
            if (cost > dist[y][x])
                continue;

            for (int[] mv : moves) {
                int nx = x + mv[0];
                int ny = y + mv[1];

                if (nx >= 0 && nx < n && ny >= 0 && ny < m) {
                    int newCost = cost + grid[ny][nx];
                    if (newCost < dist[ny][nx]) {
                        dist[ny][nx] = newCost;
                        pq.offer(new int[] { newCost, nx, ny });
                    }
                }
            }
        }

        return -1; // should never happen
    }

    // V1-1
    // IDEA: Dijkstra's Algorithm
    // https://leetcode.com/problems/minimum-obstacle-removal-to-reach-corner/editorial/
    // Directions for movement: right, left, down, up
    private final int[][] directions = {
            { 0, 1 },
            { 0, -1 },
            { 1, 0 },
            { -1, 0 },
    };

    public int minimumObstacles_1_1(int[][] grid) {
        int m = grid.length, n = grid[0].length;

        int[][] minObstacles = new int[m][n];

        // Initialize all cells with a large value, representing unvisited cells
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                minObstacles[i][j] = Integer.MAX_VALUE;
            }
        }

        // Start from the top-left corner, accounting for its obstacle value
        minObstacles[0][0] = grid[0][0];

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);

        // Add the starting cell to the priority queue
        pq.add(new int[] { minObstacles[0][0], 0, 0 });

        while (!pq.isEmpty()) {
            // Process the cell with the fewest obstacles removed so far
            int[] current = pq.poll();
            int obstacles = current[0], row = current[1], col = current[2];

            // If we've reached the bottom-right corner, return the result
            if (row == m - 1 && col == n - 1) {
                return obstacles;
            }

            // Explore all four possible directions from the current cell
            for (int[] dir : directions) {
                int newRow = row + dir[0], newCol = col + dir[1];

                if (isValid(grid, newRow, newCol)) {
                    // Calculate the obstacles removed if moving to the new cell
                    int newObstacles = obstacles + grid[newRow][newCol];

                    // Update if we've found a path with fewer obstacles to the new cell
                    if (newObstacles < minObstacles[newRow][newCol]) {
                        minObstacles[newRow][newCol] = newObstacles;
                        pq.add(new int[] { newObstacles, newRow, newCol });
                    }
                }
            }
        }

        return minObstacles[m - 1][n - 1];
    }

    // Helper method to check if the cell is within the grid bounds
    private boolean isValid(int[][] grid, int row, int col) {
        return (row >= 0 && col >= 0 && row < grid.length && col < grid[0].length);
    }

    // V1-2
    // IDEA: BFS
    // https://leetcode.com/problems/minimum-obstacle-removal-to-reach-corner/editorial/
    // Directions for movement: right, left, down, up
    private final int[][] directions_1_2 = {
            { 0, 1 },
            { 0, -1 },
            { 1, 0 },
            { -1, 0 },
    };

    public int minimumObstacles_1_2(int[][] grid) {
        int m = grid.length, n = grid[0].length;

        // Distance matrix to store the minimum obstacles removed to reach each cell
        int[][] minObstacles = new int[m][n];

        // Initialize all cells with a large value, representing unvisited cells
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                minObstacles[i][j] = Integer.MAX_VALUE;
            }
        }

        minObstacles[0][0] = 0;

        Deque<int[]> deque = new ArrayDeque<>();
        deque.add(new int[] { 0, 0, 0 }); // {obstacles, row, col}

        while (!deque.isEmpty()) {
            int[] current = deque.poll();
            int obstacles = current[0], row = current[1], col = current[2];

            // Explore all four possible directions from the current cell
            for (int[] dir : directions_1_2) {
                int newRow = row + dir[0], newCol = col + dir[1];

                if (isValid_1_2(grid, newRow, newCol) &&
                        minObstacles[newRow][newCol] == Integer.MAX_VALUE) {
                    if (grid[newRow][newCol] == 1) {
                        // If it's an obstacle, add 1 to obstacles and push to the back
                        minObstacles[newRow][newCol] = obstacles + 1;
                        deque.addLast(
                                new int[] { obstacles + 1, newRow, newCol });
                    } else {
                        // If it's an empty cell, keep the obstacle count and push to the front
                        minObstacles[newRow][newCol] = obstacles;
                        deque.addFirst(new int[] { obstacles, newRow, newCol });
                    }
                }
            }
        }

        return minObstacles[m - 1][n - 1];
    }

    // Helper method to check if the cell is within the grid bounds
    private boolean isValid_1_2(int[][] grid, int row, int col) {
        return (row >= 0 && col >= 0 && row < grid.length && col < grid[0].length);
    }

    // V2
    // IDEA: BFS
    // https://leetcode.com/problems/minimum-obstacle-removal-to-reach-corner/solutions/6090483/minimum-obstacle-removal-bfs-approach-be-906q/
    public int minimumObstacles_2(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[][] distance = new int[m][n];
        for (int[] row : distance)
            Arrays.fill(row, Integer.MAX_VALUE);
        Deque<int[]> dq = new ArrayDeque<>();

        distance[0][0] = 0;
        dq.offerFirst(new int[] { 0, 0 });
        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

        while (!dq.isEmpty()) {
            int[] cell = dq.pollFirst();
            int x = cell[0], y = cell[1];
            for (int[] dir : directions) {
                int nx = x + dir[0], ny = y + dir[1];
                if (nx >= 0 && nx < m && ny >= 0 && ny < n) {
                    int newDist = distance[x][y] + grid[nx][ny];
                    if (newDist < distance[nx][ny]) {
                        distance[nx][ny] = newDist;
                        if (grid[nx][ny] == 0) {
                            dq.offerFirst(new int[] { nx, ny });
                        } else {
                            dq.offerLast(new int[] { nx, ny });
                        }
                    }
                }
            }
        }
        return distance[m - 1][n - 1];
    }


    // V3
    // https://leetcode.com/problems/minimum-obstacle-removal-to-reach-corner/solutions/2086036/lets-solve-this-problem-based-on-never-g-rxep/

}
