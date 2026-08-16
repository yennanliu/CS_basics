package LeetCodeJava.DFS;

// https://leetcode.com/problems/contain-virus/description/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 749. Contain Virus
 * Hard
 *
 * A virus is spreading rapidly, and your task is to quarantine the infected area by
 * installing walls.
 *
 * The world is modeled as an m x n binary grid isInfected, where isInfected[i][j] == 0
 * represents uninfected cells, and isInfected[i][j] == 1 represents cells contaminated
 * with the virus. A wall (and only one wall) can be installed between any two
 * 4-directionally adjacent cells, on the shared boundary.
 *
 * Every night, the virus spreads to all neighboring cells in all four directions unless
 * blocked by a wall. Resources are limited. Each day, you can install walls around only
 * one region (i.e., the affected area (continuous block of infected cells) that threatens
 * the most uninfected cells the following night). There will never be a tie.
 *
 * Return the number of walls used to quarantine all the infected regions.
 * If the world will become fully infected, return the number of walls used.
 *
 *
 * Example 1:
 *
 * Input: isInfected = [[0,1,0,0,0,0,0,1],[0,1,0,0,0,0,0,1],[0,0,0,0,0,0,0,1],
 *                      [0,0,0,0,0,0,0,0]]
 * Output: 10
 * Explanation: There are 2 contaminated regions.
 * On the first day, add 5 walls to quarantine the viral region on the left.
 * On the second day, add 5 walls to quarantine the viral region on the right.
 * The virus is fully contained.
 *
 * Example 2:
 *
 * Input: isInfected = [[1,1,1],[1,0,1],[1,1,1]]
 * Output: 4
 * Explanation: Even though there is only one cell saved, there are 4 walls built.
 * Notice that walls are only built on the shared boundary of two different cells.
 *
 * Example 3:
 *
 * Input: isInfected = [[1,1,1,0,0,0,0,0,0],[1,0,1,0,1,1,1,1,1],[1,1,1,0,0,0,0,0,0]]
 * Output: 13
 * Explanation: The region on the left only builds two new walls.
 *
 *
 * Constraints:
 *
 * m == isInfected.length
 * n == isInfected[i].length
 * 1 <= m, n <= 50
 * isInfected[i][j] is either 0 or 1.
 * There is always a contiguous viral region throughout the described process that will
 * infect strictly more uncontaminated squares in the next round.
 *
 */
public class ContainVirus {

    // V0
    // IDEA: DFS (FLOOD FILL) PER REGION + ROUND BY ROUND SIMULATION
    /**
     *   Each day:
     *     1) FLOOD FILL every connected region of 1s, recording for each region
     *          - its cells
     *          - the SET of DISTINCT uninfected neighbours it threatens (frontier)
     *          - the COUNT of infected/uninfected adjacencies (= walls needed;
     *            one 0-cell touched from 3 sides needs 3 walls)
     *
     *     2) QUARANTINE the region with the largest frontier: add its wall count and
     *        mark its cells 2 (walled off forever, and 2 is neither 0 nor 1 so it
     *        naturally blocks later flood fills)
     *
     *     3) every OTHER region SPREADS: each of its frontier cells becomes infected
     *
     *   Stop once no region threatens any uninfected cell.
     *
     *   NOTE !!! `walls` and `frontier.size()` are DIFFERENT numbers -- a single
     *            uninfected cell touched from 3 sides costs 3 walls but is 1 threat.
     *
     *   time  = O((m*n)^2) -- at most O(m*n) rounds, each scanning the whole grid
     *   space = O(m*n)
     */

    private static final int[][] DIRS = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

    /** one contaminated region found during a day's scan */
    private static class Region {
        List<int[]> cells = new ArrayList<>();
        Set<Integer> frontier = new HashSet<>(); // encoded r * n + c
        int walls = 0;
    }

    public int containVirus(int[][] isInfected) {
        int m = isInfected.length;
        int n = isInfected[0].length;
        int totalWalls = 0;

        while (true) {
            boolean[][] seen = new boolean[m][n];
            List<Region> regions = new ArrayList<>();

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (isInfected[i][j] != 1 || seen[i][j]) {
                        continue;
                    }

                    Region reg = new Region();

                    // ITERATIVE DFS over one contaminated region
                    Deque<int[]> stack = new ArrayDeque<>();
                    stack.push(new int[] { i, j });
                    seen[i][j] = true;

                    while (!stack.isEmpty()) {
                        int[] cur = stack.pop();
                        int r = cur[0];
                        int c = cur[1];
                        reg.cells.add(cur);

                        for (int[] d : DIRS) {
                            int nr = r + d[0];
                            int nc = c + d[1];
                            if (nr < 0 || nr >= m || nc < 0 || nc >= n) {
                                continue;
                            }
                            if (isInfected[nr][nc] == 1 && !seen[nr][nc]) {
                                seen[nr][nc] = true;
                                stack.push(new int[] { nr, nc });
                            } else if (isInfected[nr][nc] == 0) {
                                reg.frontier.add(nr * n + nc); // DISTINCT cells threatened
                                reg.walls += 1;                // one wall per shared boundary
                            }
                        }
                    }

                    // a region with NO frontier is already sealed in
                    if (!reg.frontier.isEmpty()) {
                        regions.add(reg);
                    }
                }
            }

            if (regions.isEmpty()) {
                break;
            }

            // quarantine the region threatening the MOST uninfected cells
            Region target = regions.get(0);
            for (Region reg : regions) {
                if (reg.frontier.size() > target.frontier.size()) {
                    target = reg;
                }
            }

            totalWalls += target.walls;
            for (int[] cell : target.cells) {
                isInfected[cell[0]][cell[1]] = 2; // 2 = walled off, blocks neighbours forever
            }

            // all REMAINING regions spread by one cell overnight
            for (Region reg : regions) {
                if (reg == target) {
                    continue;
                }
                for (int code : reg.frontier) {
                    isInfected[code / n][code % n] = 1;
                }
            }
        }

        return totalWalls;
    }


    // V1
    // IDEA: BFS FLOOD FILL per region (queue instead of stack)
    /**
     *  Identical round-by-round simulation, but each region is discovered with a
     *  QUEUE rather than a stack.
     *
     *  Worth having because the grid can be 50 x 50: a recursive DFS would be at
     *  risk on a snake-shaped region, and the iterative order also makes the
     *  frontier easier to inspect while debugging.
     *
     *  time  = O((m*n)^2)
     *  space = O(m*n)
     */
    public int containVirus_1(int[][] isInfected) {
        int m = isInfected.length;
        int n = isInfected[0].length;
        int totalWalls = 0;

        while (true) {
            boolean[][] seen = new boolean[m][n];
            List<List<int[]>> cells = new ArrayList<>();
            List<Set<Integer>> fronts = new ArrayList<>();
            List<Integer> walls = new ArrayList<>();

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (isInfected[i][j] != 1 || seen[i][j]) {
                        continue;
                    }
                    List<int[]> cs = new ArrayList<>();
                    Set<Integer> fr = new HashSet<>();
                    int w = 0;

                    Deque<int[]> q = new ArrayDeque<>();
                    q.offer(new int[] { i, j });
                    seen[i][j] = true;
                    while (!q.isEmpty()) {
                        int[] cur = q.poll();
                        cs.add(cur);
                        for (int[] d : DIRS_V) {
                            int nr = cur[0] + d[0];
                            int nc = cur[1] + d[1];
                            if (nr < 0 || nr >= m || nc < 0 || nc >= n) {
                                continue;
                            }
                            if (isInfected[nr][nc] == 1 && !seen[nr][nc]) {
                                seen[nr][nc] = true;
                                q.offer(new int[] { nr, nc });
                            } else if (isInfected[nr][nc] == 0) {
                                fr.add(nr * n + nc);
                                w += 1;
                            }
                        }
                    }
                    if (!fr.isEmpty()) {
                        cells.add(cs);
                        fronts.add(fr);
                        walls.add(w);
                    }
                }
            }

            if (cells.isEmpty()) {
                break;
            }

            int target = 0;
            for (int i = 1; i < fronts.size(); i++) {
                if (fronts.get(i).size() > fronts.get(target).size()) {
                    target = i;
                }
            }
            totalWalls += walls.get(target);
            for (int[] c : cells.get(target)) {
                isInfected[c[0]][c[1]] = 2;
            }
            for (int i = 0; i < fronts.size(); i++) {
                if (i == target) {
                    continue;
                }
                for (int code : fronts.get(i)) {
                    isInfected[code / n][code % n] = 1;
                }
            }
        }

        return totalWalls;
    }

    private static final int[][] DIRS_V = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

    // V2
    // IDEA: UNION FIND to label the regions each round
    /**
     *  Instead of flood filling, union horizontally / vertically adjacent infected
     *  cells and read the regions off the roots.
     *
     *  The frontier and wall tallies are then accumulated per ROOT in one extra
     *  sweep -- no recursion, no queue, and the region structure is available as
     *  data rather than as control flow.
     *
     *  time  = O((m*n)^2 * alpha)
     *  space = O(m*n)
     */
    public int containVirus_2(int[][] isInfected) {
        int m = isInfected.length;
        int n = isInfected[0].length;
        int totalWalls = 0;

        while (true) {
            int[] par = new int[m * n];
            for (int i = 0; i < m * n; i++) {
                par[i] = i;
            }
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (isInfected[i][j] != 1) {
                        continue;
                    }
                    if (i + 1 < m && isInfected[i + 1][j] == 1) {
                        unionV(par, i * n + j, (i + 1) * n + j);
                    }
                    if (j + 1 < n && isInfected[i][j + 1] == 1) {
                        unionV(par, i * n + j, i * n + j + 1);
                    }
                }
            }

            Map<Integer, Set<Integer>> fronts = new HashMap<>();
            Map<Integer, Integer> walls = new HashMap<>();
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (isInfected[i][j] != 1) {
                        continue;
                    }
                    int root = findV(par, i * n + j);
                    for (int[] d : DIRS_V) {
                        int nr = i + d[0];
                        int nc = j + d[1];
                        if (nr >= 0 && nr < m && nc >= 0 && nc < n && isInfected[nr][nc] == 0) {
                            fronts.computeIfAbsent(root, x -> new HashSet<>()).add(nr * n + nc);
                            walls.merge(root, 1, Integer::sum);
                        }
                    }
                }
            }

            if (fronts.isEmpty()) {
                break;
            }

            int target = -1;
            for (int root : fronts.keySet()) {
                if (target == -1 || fronts.get(root).size() > fronts.get(target).size()) {
                    target = root;
                }
            }
            totalWalls += walls.get(target);

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (isInfected[i][j] == 1 && findV(par, i * n + j) == target) {
                        isInfected[i][j] = 2;
                    }
                }
            }
            for (Map.Entry<Integer, Set<Integer>> e : fronts.entrySet()) {
                if (e.getKey() == target) {
                    continue;
                }
                for (int code : e.getValue()) {
                    isInfected[code / n][code % n] = 1;
                }
            }
        }

        return totalWalls;
    }

    private int findV(int[] par, int x) {
        while (par[x] != x) {
            par[x] = par[par[x]];
            x = par[x];
        }
        return x;
    }

    private void unionV(int[] par, int a, int b) {
        int ra = findV(par, a);
        int rb = findV(par, b);
        if (ra != rb) {
            par[ra] = rb;
        }
    }

    // V3
    // IDEA: RECURSIVE DFS PAINTING REGION IDS INTO A SEPARATE GRID
    /**
     *  Paint each region with its own id in an `int[][] regionOf` layer, so the
     *  frontier/wall tallies can be indexed by id directly instead of carried in
     *  per-region objects.
     *
     *  The id layer also survives the round, which makes it easy to answer
     *  follow-ups like `which region did this cell belong to?`.
     *
     *  time  = O((m*n)^2)
     *  space = O(m*n)
     */
    public int containVirus_3(int[][] isInfected) {
        int m = isInfected.length;
        int n = isInfected[0].length;
        int totalWalls = 0;

        while (true) {
            int[][] regionOf = new int[m][n];
            for (int[] row : regionOf) {
                Arrays.fill(row, -1);
            }

            int regions = 0;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (isInfected[i][j] == 1 && regionOf[i][j] == -1) {
                        paint(isInfected, regionOf, i, j, regions);
                        regions += 1;
                    }
                }
            }
            if (regions == 0) {
                break;
            }

            List<Set<Integer>> fronts = new ArrayList<>();
            int[] walls = new int[regions];
            for (int r = 0; r < regions; r++) {
                fronts.add(new HashSet<>());
            }

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    int r = regionOf[i][j];
                    if (r == -1) {
                        continue;
                    }
                    for (int[] d : DIRS_V) {
                        int nr = i + d[0];
                        int nc = j + d[1];
                        if (nr >= 0 && nr < m && nc >= 0 && nc < n && isInfected[nr][nc] == 0) {
                            fronts.get(r).add(nr * n + nc);
                            walls[r] += 1;
                        }
                    }
                }
            }

            int target = -1;
            for (int r = 0; r < regions; r++) {
                if (fronts.get(r).isEmpty()) {
                    continue;
                }
                if (target == -1 || fronts.get(r).size() > fronts.get(target).size()) {
                    target = r;
                }
            }
            if (target == -1) {
                break;
            }

            totalWalls += walls[target];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (regionOf[i][j] == target) {
                        isInfected[i][j] = 2;
                    }
                }
            }
            for (int r = 0; r < regions; r++) {
                if (r == target) {
                    continue;
                }
                for (int code : fronts.get(r)) {
                    isInfected[code / n][code % n] = 1;
                }
            }
        }

        return totalWalls;
    }

    private void paint(int[][] grid, int[][] regionOf, int r, int c, int id) {
        if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length
                || grid[r][c] != 1 || regionOf[r][c] != -1) {
            return;
        }
        regionOf[r][c] = id;
        paint(grid, regionOf, r - 1, c, id);
        paint(grid, regionOf, r + 1, c, id);
        paint(grid, regionOf, r, c - 1, id);
        paint(grid, regionOf, r, c + 1, id);
    }

}
