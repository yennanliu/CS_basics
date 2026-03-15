package LeetCodeJava.Graph;

// https://leetcode.com/problems/minimum-number-of-visited-cells-in-a-grid/description/

import java.util.*;

/**
 *  2617. Minimum Number of Visited Cells in a Grid
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a 0-indexed m x n integer matrix grid. Your initial position is at the top-left cell (0, 0).
 *
 * Starting from the cell (i, j), you can move to one of the following cells:
 *
 * Cells (i, k) with j < k <= grid[i][j] + j (rightward movement), or
 * Cells (k, j) with i < k <= grid[i][j] + i (downward movement).
 * Return the minimum number of cells you need to visit to reach the bottom-right cell (m - 1, n - 1). If there is no valid path, return -1.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: grid = [[3,4,2,1],[4,2,3,1],[2,1,0,0],[2,4,0,0]]
 * Output: 4
 * Explanation: The image above shows one of the paths that visits exactly 4 cells.
 * Example 2:
 *
 *
 * Input: grid = [[3,4,2,1],[4,2,1,1],[2,1,1,0],[3,4,1,0]]
 * Output: 3
 * Explanation: The image above shows one of the paths that visits exactly 3 cells.
 * Example 3:
 *
 *
 * Input: grid = [[2,1,0],[1,0,0]]
 * Output: -1
 * Explanation: It can be proven that no path exists.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 105
 * 1 <= m * n <= 105
 * 0 <= grid[i][j] < m * n
 * grid[m - 1][n - 1] == 0
 *
 *
 */
public class MinimumNumberOfVisitedCellsInAGrid {

    // V0
//    public int minimumVisitedCells(int[][] grid) {
//
//    }


    // V0-1
    // IDEA: PQ (gemini)
    public int minimumVisitedCells_0_1(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[][] dist = new int[m][n];
        for (int[] row : dist)
            Arrays.fill(row, -1);

        // PQs for each row and column to store {distance, index}
        PriorityQueue<int[]>[] rowPQs = new PriorityQueue[m];
        PriorityQueue<int[]>[] colPQs = new PriorityQueue[n];

        for (int i = 0; i < m; i++)
            rowPQs[i] = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        for (int j = 0; j < n; j++)
            colPQs[j] = new PriorityQueue<>((a, b) -> a[0] - b[0]);

        dist[0][0] = 1;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 1. Update distance from previous cells in the same ROW
                while (!rowPQs[i].isEmpty()) {
                    int[] top = rowPQs[i].peek();
                    int prevJ = top[1];
                    // If the previous cell can reach (i, j)
                    if (prevJ + grid[i][prevJ] >= j) {
                        int d = top[0] + 1;
                        if (dist[i][j] == -1 || d < dist[i][j])
                            dist[i][j] = d;
                        break; // PQ is sorted by distance, so we found the best
                    }
                    rowPQs[i].poll(); // Cannot reach, useless for future cells in this row
                }

                // 2. Update distance from previous cells in the same COLUMN
                while (!colPQs[j].isEmpty()) {
                    int[] top = colPQs[j].peek();
                    int prevI = top[1];
                    // If the previous cell can reach (i, j)
                    if (prevI + grid[prevI][j] >= i) {
                        int d = top[0] + 1;
                        if (dist[i][j] == -1 || d < dist[i][j])
                            dist[i][j] = d;
                        break;
                    }
                    colPQs[j].poll(); // Cannot reach, useless for future cells in this column
                }

                // 3. If current cell is reachable, add it to PQs for future use
                if (dist[i][j] != -1 && grid[i][j] > 0) {
                    rowPQs[i].offer(new int[] { dist[i][j], j });
                    colPQs[j].offer(new int[] { dist[i][j], i });
                }
            }
        }

        return dist[m - 1][n - 1];
    }


    // V0-2
    // IDEA: PQ (gpt)
    public int minimumVisitedCells_0_2(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int INF = Integer.MAX_VALUE / 2;

        int[][] dist = new int[m][n];
        for (int[] row : dist)
            Arrays.fill(row, INF);

        PriorityQueue<int[]>[] rowPQ = new PriorityQueue[m];
        PriorityQueue<int[]>[] colPQ = new PriorityQueue[n];

        for (int i = 0; i < m; i++)
            rowPQ[i] = new PriorityQueue<>((a, b) -> a[0] - b[0]);

        for (int j = 0; j < n; j++)
            colPQ[j] = new PriorityQueue<>((a, b) -> a[0] - b[0]);

        dist[0][0] = 1;
        rowPQ[0].offer(new int[] { 1, 0 });
        colPQ[0].offer(new int[] { 1, 0 });

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {

                if (i == 0 && j == 0)
                    continue;

                // check row
                while (!rowPQ[i].isEmpty()) {
                    int[] top = rowPQ[i].peek();
                    int prevCol = top[1];
                    if (prevCol + grid[i][prevCol] < j)
                        rowPQ[i].poll();
                    else {
                        dist[i][j] = Math.min(dist[i][j], top[0] + 1);
                        break;
                    }
                }

                // check column
                while (!colPQ[j].isEmpty()) {
                    int[] top = colPQ[j].peek();
                    int prevRow = top[1];
                    if (prevRow + grid[prevRow][j] < i)
                        colPQ[j].poll();
                    else {
                        dist[i][j] = Math.min(dist[i][j], top[0] + 1);
                        break;
                    }
                }

                if (dist[i][j] != INF) {
                    rowPQ[i].offer(new int[] { dist[i][j], j });
                    colPQ[j].offer(new int[] { dist[i][j], i });
                }
            }
        }

        return dist[m - 1][n - 1] == INF ? -1 : dist[m - 1][n - 1];
    }



    // V1

    // V2
    // https://leetcode.com/problems/minimum-number-of-visited-cells-in-a-grid/solutions/3395709/c-java-python3-settreesetsortedlist-bfs-n9vgh/
    public int minimumVisitedCells_2(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        TreeSet<Integer>[] s0 = new TreeSet[m], s1 = new TreeSet[n];
        for (int i = 0; i < m; i++) {
            s0[i] = new TreeSet<>();
            for (int j = 0; j < n; j++)
                s0[i].add(j);
        }
        for (int j = 0; j < n; j++) {
            s1[j] = new TreeSet<>();
            for (int i = 0; i < m; i++)
                s1[j].add(i);
        }
        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[] { 0, 0, 1 });

        while (!q.isEmpty()) {
            int[] cell = q.poll();
            int i = cell[0], j = cell[1], d = cell[2];
            if (i == m - 1 && j == n - 1)
                return d;

            Integer k = s0[i].ceiling(j + 1);
            while (k != null && k <= j + grid[i][j]) {
                q.offer(new int[] { i, k.intValue(), d + 1 });
                s0[i].remove(k);
                s1[k.intValue()].remove(i);
                k = s0[i].ceiling(j + 1);
            }
            k = s1[j].ceiling(i + 1);
            while (k != null && k <= i + grid[i][j]) {
                q.offer(new int[] { k.intValue(), j, d + 1 });
                s1[j].remove(k);
                s0[k.intValue()].remove(j);
                k = s1[j].ceiling(i + 1);
            }
        }
        return -1;
    }


    // V3
    // https://leetcode.com/problems/minimum-number-of-visited-cells-in-a-grid/solutions/3396173/java-the-solution-that-records-max-is-fo-fffo/
    public int minimumVisitedCells_3(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        int[] horizontals = new int[m], verticals = new int[n];
        LinkedList<int[]> queue = new LinkedList<>();
        queue.add(new int[] { 0, 0 });
        horizontals[0] = 1;
        verticals[0] = 1;
        for (int output = 1; !queue.isEmpty(); output++)
            for (int i = queue.size(); i > 0; i--) {
                int[] t = queue.removeFirst();
                int x = t[0], y = t[1];
                if (x == m - 1 && y == n - 1)
                    return output;
                for (int j = Math.max(horizontals[x], y + 1); j <= grid[x][y] + y && j < n; j++)
                    if (!visited[x][j]) {
                        visited[x][j] = true;
                        queue.add(new int[] { x, j });
                    }
                while (horizontals[x] < n && visited[x][horizontals[x]])
                    horizontals[x]++;
                for (int j = Math.max(verticals[y], x + 1); j <= grid[x][y] + x && j < m; j++)
                    if (!visited[j][y]) {
                        visited[j][y] = true;
                        queue.add(new int[] { j, y });
                    }
                while (verticals[y] < m && visited[verticals[y]][y])
                    verticals[y]++;
            }
        return -1;
    }


    // V4
    // IDEA: MONOTONIC STACK
    // https://leetcode.com/problems/minimum-number-of-visited-cells-in-a-grid/solutions/6861660/monotonic-stack-omn-by-suiler-e9ez/
    public int minimumVisitedCells_4(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        if (m == 1 && n == 1) {
            return 1;
        }
        ArrayList<ArrayDeque<int[]>> rowDists = new ArrayList<>();
        ArrayList<ArrayDeque<int[]>> colDists = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            rowDists.add(new ArrayDeque<>());
        }
        for (int i = 0; i < n; i++) {
            colDists.add(new ArrayDeque<>());
        }
        rowDists.get(0).push(new int[] { 1, grid[0][0] });
        colDists.get(0).push(new int[] { 1, grid[0][0] });
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                while (!rowDists.get(i).isEmpty()) {
                    if (rowDists.get(i).peek()[1] < j) {
                        rowDists.get(i).pop();
                    } else {
                        break;
                    }
                }
                while (!colDists.get(j).isEmpty()) {
                    if (colDists.get(j).peek()[1] < i) {
                        colDists.get(j).pop();
                    } else {
                        break;
                    }
                }

                int rowDist = m + n;
                if (!rowDists.get(i).isEmpty()) {
                    rowDist = rowDists.get(i).peek()[0];
                }
                int colDist = m + n;
                if (!colDists.get(j).isEmpty()) {
                    colDist = colDists.get(j).peek()[0];
                }

                int dist = Math.min(rowDist, colDist) + 1;
                if (dist >= m + n) {
                    continue;
                }
                if (i == m - 1 && j == n - 1) {
                    return dist;
                }

                int rowReach = grid[i][j] + j;
                if (!rowDists.get(i).isEmpty()) {
                    int[] rowDistsHead = rowDists.get(i).peek();
                    if (dist < rowDistsHead[0]) {
                        rowDists.get(i).push(new int[] { dist, rowReach });
                    } else if (dist == rowDistsHead[0]) {
                        if (rowReach > rowDistsHead[1]) {
                            rowDists.get(i).pop();
                            rowDists.get(i).push(new int[] { dist, rowReach });
                        }
                    } else {
                        rowDists.get(i).pop();
                        if (!rowDists.get(i).isEmpty()) {
                            int[] rowDistsSecond = rowDists.get(i).peek();
                            if (dist == rowDistsSecond[0]) {
                                if (rowReach > rowDistsSecond[1]) {
                                    rowDists.get(i).pop();
                                    rowDists.get(i).push(new int[] { dist, rowReach });
                                }
                            } else {
                                rowDists.get(i).push(new int[] { dist, rowReach });
                            }
                        } else {
                            rowDists.get(i).push(new int[] { dist, rowReach });
                        }
                        rowDists.get(i).push(rowDistsHead);
                    }
                } else {
                    rowDists.get(i).push(new int[] { dist, rowReach });
                }

                int colReach = grid[i][j] + i;
                if (!colDists.get(j).isEmpty()) {
                    int[] colDistsHead = colDists.get(j).peek();
                    if (dist < colDistsHead[0]) {
                        colDists.get(j).push(new int[] { dist, colReach });
                    } else if (dist == colDistsHead[0]) {
                        if (colReach > colDistsHead[1]) {
                            colDists.get(j).pop();
                            colDists.get(j).push(new int[] { dist, colReach });
                        }
                    } else {
                        colDists.get(j).pop();
                        if (!colDists.get(j).isEmpty()) {
                            int[] colDistsSecond = colDists.get(j).peek();
                            if (dist == colDistsSecond[0]) {
                                if (colReach > colDistsSecond[1]) {
                                    colDists.get(j).pop();
                                    colDists.get(j).push(new int[] { dist, colReach });
                                }
                            } else {
                                colDists.get(j).push(new int[] { dist, colReach });
                            }
                        } else {
                            colDists.get(j).push(new int[] { dist, colReach });
                        }
                        colDists.get(j).push(colDistsHead);
                    }
                } else {
                    colDists.get(j).push(new int[] { dist, colReach });
                }
            }
        }
        return -1;
    }



    // V5
    // IDEA:  Dijkstra (OUT of memory error)
    // https://leetcode.com/problems/minimum-number-of-visited-cells-in-a-grid/solutions/3398401/java-dijkstra-by-cursed__03-9iqm/





}
