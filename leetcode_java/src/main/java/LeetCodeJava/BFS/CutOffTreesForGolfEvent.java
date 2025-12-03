package LeetCodeJava.BFS;

// https://leetcode.com/problems/cut-off-trees-for-golf-event/description/

import java.util.*;

/**
 * 675. Cut Off Trees for Golf Event
 * Solved
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * You are asked to cut off all the trees in a forest for a golf event. The forest is represented as an m x n matrix. In this matrix:
 *
 * 0 means the cell cannot be walked through.
 * 1 represents an empty cell that can be walked through.
 * A number greater than 1 represents a tree in a cell that can be walked through, and this number is the tree's height.
 * In one step, you can walk in any of the four directions: north, east, south, and west. If you are standing in a cell with a tree, you can choose whether to cut it off.
 *
 * You must cut off the trees in order from shortest to tallest. When you cut off a tree, the value at its cell becomes 1 (an empty cell).
 *
 * Starting from the point (0, 0), return the minimum steps you need to walk to cut off all the trees. If you cannot cut off all the trees, return -1.
 *
 * Note: The input is generated such that no two trees have the same height, and there is at least one tree needs to be cut off.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: forest = [[1,2,3],[0,0,4],[7,6,5]]
 * Output: 6
 * Explanation: Following the path above allows you to cut off the trees from shortest to tallest in 6 steps.
 * Example 2:
 *
 *
 * Input: forest = [[1,2,3],[0,0,0],[7,6,5]]
 * Output: -1
 * Explanation: The trees in the bottom row cannot be accessed as the middle row is blocked.
 * Example 3:
 *
 * Input: forest = [[2,3,4],[0,0,5],[8,7,6]]
 * Output: 6
 * Explanation: You can follow the same path as Example 1 to cut off all the trees.
 * Note that you can cut off the first tree at (0, 0) before making any steps.
 *
 *
 * Constraints:
 *
 * m == forest.length
 * n == forest[i].length
 * 1 <= m, n <= 50
 * 0 <= forest[i][j] <= 109
 * Heights of all trees are distinct.
 */
public class CutOffTreesForGolfEvent {

    // V0
//    public int cutOffTree(List<List<Integer>> forest) {
//
//        return 0;
//    }

    // V0-0-1
    // IDEA: val Sort + Loop & call BFS (fixed by gemini)
    private int rows;
    private int cols;
    private final int[][] MOVES = new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

    public int cutOffTree_0_0_1(List<List<Integer>> forest) {
        if (forest == null || forest.isEmpty() || forest.get(0).isEmpty()) {
            return 0;
        }

        this.rows = forest.size();
        this.cols = forest.get(0).size();

        // 1. Collect and Sort all trees by height
        // List stores: [height, row, col]
        List<int[]> trees = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // Only consider trees with height > 1
                if (forest.get(r).get(c) > 1) {
                    trees.add(new int[] { forest.get(r).get(c), r, c });
                }
            }
        }

        // Sort trees by height (ascending). This defines the order of travel.
        // [0] is height, [1] is row, [2] is col
        Collections.sort(trees, Comparator.comparingInt(a -> a[0]));

        int totalSteps = 0;
        // Start position is always (0, 0).
        int startR = 0;
        int startC = 0;

        // 2. Iterate through sorted trees and find the distance (BFS) between each pair.
        for (int[] tree : trees) {
            int targetR = tree[1];
            int targetC = tree[2];

            // Find the shortest path distance from current start point to the next tree.
            int steps = bfs_0_0_1(forest, startR, startC, targetR, targetC);

            // If steps is -1, the target tree is unreachable.
            if (steps == -1) {
                return -1;
            }

            totalSteps += steps;

            // The target tree now becomes the starting point for the next path.
            startR = targetR;
            startC = targetC;
        }

        return totalSteps;
    }

    /**
     * Performs BFS to find the minimum steps from (sr, sc) to (tr, tc).
     * @return Minimum steps, or -1 if the target is unreachable.
     */
    private int bfs_0_0_1(List<List<Integer>> forest, int startR, int startC, int targetR, int targetC) {
        if (startR == targetR && startC == targetC) {
            return 0; // Already at the target.
        }

        // Queue stores coordinates [row, col]
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[] { startR, startC });

        // Visited array to prevent cycles and redundant visits.
        boolean[][] visited = new boolean[rows][cols];
        visited[startR][startC] = true;

        int steps = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();
            steps++; // Increment steps for the next layer

            for (int i = 0; i < size; i++) {
                int[] current = queue.poll();
                int r = current[0];
                int c = current[1];

                for (int[] move : MOVES) {
                    int nextR = r + move[0];
                    int nextC = c + move[1];

                    // Check bounds
                    if (nextR >= 0 && nextR < rows && nextC >= 0 && nextC < cols) {

                        // Check if unvisited AND the cell is not water (value > 0)
                        if (!visited[nextR][nextC] && forest.get(nextR).get(nextC) > 0) {

                            // Reached the target
                            if (nextR == targetR && nextC == targetC) {
                                return steps;
                            }

                            visited[nextR][nextC] = true;
                            queue.offer(new int[] { nextR, nextC });
                        }
                    }
                }
            }
        }

        // Target tree was unreachable
        return -1;
    }


    // V0-1
    // IDEA: val Sort + Loop & call BFS (fixed by gpt)
    public int cutOffTree_0_1(List<List<Integer>> forest) {
        if (forest == null || forest.size() == 0 || forest.get(0).size() == 0) {
            return -1;
        }

        int m = forest.size();
        int n = forest.get(0).size();

        // Step 1: Collect all trees (height > 1)
        List<int[]> trees = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int height = forest.get(i).get(j);
                if (height > 1) {
                    trees.add(new int[] { i, j, height });
                }
            }
        }

        // Step 2: Sort trees by height (ascending)
        trees.sort(Comparator.comparingInt(a -> a[2]));

        // Step 3: Start from (0,0), walk to each tree in order
        int totalSteps = 0;
        int startX = 0, startY = 0;

        for (int[] tree : trees) {
            int targetX = tree[0];
            int targetY = tree[1];

            int steps = bfs(forest, startX, startY, targetX, targetY);
            if (steps == -1) {
                return -1; // unreachable tree
            }

            totalSteps += steps;
            startX = targetX;
            startY = targetY;
        }

        return totalSteps;
    }

    // BFS to find shortest path between (sx,sy) and (tx,ty)
    private int bfs(List<List<Integer>> forest, int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty)
            return 0;

        int m = forest.size();
        int n = forest.get(0).size();
        boolean[][] visited = new boolean[m][n];
        int[][] dirs = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        Queue<int[]> q = new LinkedList<>();
        q.add(new int[] { sx, sy, 0 });
        visited[sx][sy] = true;

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            int steps = cur[2];

            for (int[] d : dirs) {
                int nx = x + d[0];
                int ny = y + d[1];

                if (nx < 0 || nx >= m || ny < 0 || ny >= n)
                    continue;
                if (visited[nx][ny] || forest.get(nx).get(ny) == 0)
                    continue;

                if (nx == tx && ny == ty) {
                    return steps + 1;
                }

                visited[nx][ny] = true;
                q.add(new int[] { nx, ny, steps + 1 });
            }
        }

        return -1; // unreachable
    }

    // V0-2
    // IDEA: val Sort + Loop & call BFS (fixed by gemini)
    // Global variables for grid dimensions
    private int R, C;
    public int cutOffTree_0_2(List<List<Integer>> forest) {
        if (forest == null || forest.isEmpty() || forest.get(0).isEmpty()) {
            return 0;
        }

        R = forest.size();
        C = forest.get(0).size();

        // 1. Collect all trees with height > 1 and sort them by height.
        // Each array stores: [height, row, col]
        List<int[]> trees = new ArrayList<>();
        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                if (forest.get(r).get(c) > 1) {
                    trees.add(new int[] { forest.get(r).get(c), r, c });
                }
            }
        }

        // Sort trees by height (o1[0] - o2[0])
        trees.sort(Comparator.comparingInt(a -> a[0]));

        int totalSteps = 0;
        // Start position is (0, 0)
        int startR = 0;
        int startC = 0;

        // 2. Sequentially move from one tree to the next.
        for (int[] tree : trees) {
            int targetR = tree[1];
            int targetC = tree[2];

            // Find the shortest path (minimum steps) from the current position to the next target tree.
            int steps = bfs_0_2(forest, startR, startC, targetR, targetC);

            // If the path is impossible, return -1 immediately.
            if (steps == -1) {
                return -1;
            }

            // Add the steps to the total count.
            totalSteps += steps;

            // Update the starting position for the next iteration.
            startR = targetR;
            startC = targetC;
        }

        return totalSteps;
    }

    /**
     * BFS to find the shortest path (min steps) between two points in the forest.
     * @return Minimum steps, or -1 if unreachable.
     */
    private int bfs_0_2(List<List<Integer>> forest, int startR, int startC, int targetR, int targetC) {
        // Already at the target. (Shouldn't happen for the first move, but good practice)
        if (startR == targetR && startC == targetC) {
            return 0;
        }

        // Queue stores [row, col]
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[] { startR, startC });

        // Visited array to prevent cycles and re-visiting cells
        boolean[][] visited = new boolean[R][C];
        visited[startR][startC] = true;

        // Allowed movements: up, down, left, right
        int[][] directions = new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
        int steps = 0;

        while (!queue.isEmpty()) {
            steps++; // Increment steps for the next layer
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                int[] current = queue.poll();
                int r = current[0];
                int c = current[1];

                for (int[] dir : directions) {
                    int nextR = r + dir[0];
                    int nextC = c + dir[1];

                    // Check boundaries, if already visited, or if the cell is blocked (value 0).
                    if (nextR < 0 || nextR >= R || nextC < 0 || nextC >= C ||
                            visited[nextR][nextC] || forest.get(nextR).get(nextC) == 0) {
                        continue;
                    }

                    // Found the target!
                    if (nextR == targetR && nextC == targetC) {
                        return steps;
                    }

                    // Not target, but valid path.
                    visited[nextR][nextC] = true;
                    queue.offer(new int[] { nextR, nextC });
                }
            }
        }

        // Target is unreachable
        return -1;
    }

    // V1
    // https://leetcode.ca/2017-10-05-675-Cut-Off-Trees-for-Golf-Event/
    private int[] dist = new int[3600];
    private List<List<Integer>> forest;
    private int m;
    private int n;

    public int cutOffTree_1(List<List<Integer>> forest) {
        this.forest = forest;
        m = forest.size();
        n = forest.get(0).size();
        List<int[]> trees = new ArrayList<>();
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (forest.get(i).get(j) > 1) {
                    trees.add(new int[] {forest.get(i).get(j), i * n + j});
                }
            }
        }
        trees.sort(Comparator.comparingInt(a -> a[0]));
        int ans = 0;
        int start = 0;
        for (int[] tree : trees) {
            int end = tree[1];
            int t = bfs(start, end);
            if (t == -1) {
                return -1;
            }
            ans += t;
            start = end;
        }
        return ans;
    }

    private int bfs(int start, int end) {
        PriorityQueue<int[]> q = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        q.offer(new int[] {f(start, end), start});
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[start] = 0;
        int[] dirs = {-1, 0, 1, 0, -1};
        while (!q.isEmpty()) {
            int state = q.poll()[1];
            if (state == end) {
                return dist[state];
            }
            for (int k = 0; k < 4; ++k) {
                int x = state / n + dirs[k];
                int y = state % n + dirs[k + 1];
                if (x >= 0 && x < m && y >= 0 && y < n && forest.get(x).get(y) > 0) {
                    if (dist[x * n + y] > dist[state] + 1) {
                        dist[x * n + y] = dist[state] + 1;
                        q.offer(new int[] {dist[x * n + y] + f(x * n + y, end), x * n + y});
                    }
                }
            }
        }
        return -1;
    }

    private int f(int start, int end) {
        int a = start / n;
        int b = start % n;
        int c = end / n;
        int d = end % n;
        return Math.abs(a - c) + Math.abs(b - d);
    }

    // V2
    // https://leetcode.com/problems/cut-off-trees-for-golf-event/solutions/7200480/cut-off-trees-for-golf-event-leetcode-67-5i1z/
    int[] dx = { 0, 0, 1, -1 };
    int[] dy = { 1, -1, 0, 0 };

    public int cutOffTree_2(List<List<Integer>> forest) {
        if (forest.get(0).get(0) == 0)
            return -1;
        int count = 0;
        int r = forest.size(), c = forest.get(0).size();

        // Collect all trees in priority queue sorted by height
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (forest.get(i).get(j) > 1) {
                    pq.offer(new int[] { forest.get(i).get(j), i, j });
                }
            }
        }

        int oldr = 0, oldc = 0;
        while (!pq.isEmpty()) {
            boolean[][] visited = new boolean[r][c];
            visited[oldr][oldc] = true;
            int[] cell = pq.poll();
            int newr = cell[1], newc = cell[2];

            Queue<int[]> queue = new LinkedList<>();
            queue.add(new int[] { oldr, oldc });
            int steps = 0;
            boolean found = false;

            loop: while (!queue.isEmpty()) {
                int size = queue.size();
                for (int i = 0; i < size; i++) {
                    int[] box = queue.poll();
                    int x = box[0], y = box[1];

                    if (x == newr && y == newc) {
                        count += steps;
                        found = true;
                        break loop;
                    }

                    for (int d = 0; d < 4; d++) {
                        int X = x + dx[d], Y = y + dy[d];
                        if (X < 0 || X >= r || Y < 0 || Y >= c)
                            continue;
                        if (!visited[X][Y] && forest.get(X).get(Y) > 0) {
                            visited[X][Y] = true;
                            queue.add(new int[] { X, Y });
                        }
                    }
                }
                steps++;
            }

            if (!found)
                return -1;
            oldr = newr;
            oldc = newc;
        }

        return count;
    }

    // V3
    // IDEA: BFS + MIN HEAP (TLE)
    // https://leetcode.com/problems/cut-off-trees-for-golf-event/solutions/6815292/bfs-minheap-by-nikhill04-akcg/


    // V4
    // IDEA: BFS + MIN HEAP
    // https://leetcode.com/problems/cut-off-trees-for-golf-event/solutions/1033258/python-bfs-priorityqueue-w-comments-and-qcyqs/

    // V5
    // IDEA: A* Search algorithm (gemini)
//    private int rows;
//    private int cols;
//    private final int[][] MOVES = new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

    public int cutOffTree_5(List<List<Integer>> forest) {
        if (forest == null || forest.isEmpty() || forest.get(0).isEmpty()) {
            return 0;
        }

        this.rows = forest.size();
        this.cols = forest.get(0).size();

        // 1. Collect and Sort trees by height
        List<int[]> trees = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (forest.get(r).get(c) > 1) {
                    trees.add(new int[] { forest.get(r).get(c), r, c });
                }
            }
        }
        Collections.sort(trees, Comparator.comparingInt(a -> a[0]));

        int totalSteps = 0;
        int startR = 0;
        int startC = 0;

        // 2. Iterate and find distance using A* Search
        for (int[] tree : trees) {
            int targetR = tree[1];
            int targetC = tree[2];

            int steps = aStarSearch(forest, startR, startC, targetR, targetC);

            if (steps == -1) {
                return -1;
            }

            totalSteps += steps;
            startR = targetR;
            startC = targetC;
        }

        return totalSteps;
    }

    /**
     * Heuristic function: Manhattan Distance (estimated cost h(n))
     * @return |r1 - r2| + |c1 - c2|
     */
    private int heuristic(int r1, int c1, int r2, int c2) {
        return Math.abs(r1 - r2) + Math.abs(c1 - c2);
    }

    /**
     * A* Search to find the minimum steps from (sr, sc) to (tr, tc).
     * Uses a PriorityQueue (PQ) ordered by f(n) = g(n) + h(n).
     * @return Minimum steps, or -1 if the target is unreachable.
     */
    private int aStarSearch(List<List<Integer>> forest, int startR, int startC, int targetR, int targetC) {
        if (startR == targetR && startC == targetC) {
            return 0;
        }

        // PQ stores: [f_cost, g_cost, row, col]
        // Sorted by f_cost (g + h) ascending.
        PriorityQueue<int[]> pq = new PriorityQueue<>(
                (a, b) -> a[0] - b[0] // Compare by f_cost
        );

        // Distance matrix to store the actual shortest distance from the start (g_cost)
        // This is crucial for A* to determine if a shorter path to a cell is found.
        int[][] dist = new int[rows][cols];
        for (int[] row : dist) {
            java.util.Arrays.fill(row, Integer.MAX_VALUE);
        }

        // Initial node: f = h, g = 0
        int h = heuristic(startR, startC, targetR, targetC);
        pq.offer(new int[] { h, 0, startR, startC }); // [f, g, r, c]
        dist[startR][startC] = 0; // g_cost at start is 0

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int fCost = current[0];
            int gCost = current[1];
            int r = current[2];
            int c = current[3];

            // If we found a shorter path previously (due to better heuristics), skip this.
            if (gCost > dist[r][c]) {
                continue;
            }

            // Target Reached
            if (r == targetR && c == targetC) {
                return gCost; // gCost is the minimum distance (steps)
            }

            for (int[] move : MOVES) {
                int nextR = r + move[0];
                int nextC = c + move[1];

                // Check bounds and ensure it's not water (value > 0)
                if (nextR >= 0 && nextR < rows && nextC >= 0 && nextC < cols && forest.get(nextR).get(nextC) > 0) {

                    int newGCost = gCost + 1; // Cost to move is 1

                    // Found a shorter path to nextR, nextC
                    if (newGCost < dist[nextR][nextC]) {

                        dist[nextR][nextC] = newGCost;

                        // f = g + h
                        int newHCost = heuristic(nextR, nextC, targetR, targetC);
                        int newFCost = newGCost + newHCost;

                        pq.offer(new int[] { newFCost, newGCost, nextR, nextC });
                    }
                }
            }
        }

        // Target unreachable
        return -1;
    }

}
