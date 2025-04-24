package LeetCodeJava.HashTable;

// https://leetcode.com/problems/island-perimeter/description/

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * 463. Island Perimeter
 * Solved
 * Easy
 * Topics
 * Companies
 * You are given row x col grid representing a map where grid[i][j] = 1 represents land and grid[i][j] = 0 represents water.
 *
 * Grid cells are connected horizontally/vertically (not diagonally). The grid is completely surrounded by water, and there is exactly one island (i.e., one or more connected land cells).
 *
 * The island doesn't have "lakes", meaning the water inside isn't connected to the water around the island. One cell is a square with side length 1. The grid is rectangular, width and height don't exceed 100. Determine the perimeter of the island.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: grid = [[0,1,0,0],[1,1,1,0],[0,1,0,0],[1,1,0,0]]
 * Output: 16
 * Explanation: The perimeter is the 16 yellow stripes in the image above.
 * Example 2:
 *
 * Input: grid = [[1]]
 * Output: 4
 * Example 3:
 *
 * Input: grid = [[1,0]]
 * Output: 4
 *
 *
 * Constraints:
 *
 * row == grid.length
 * col == grid[i].length
 * 1 <= row, col <= 100
 * grid[i][j] is 0 or 1.
 * There is exactly one island in grid.
 *
 *
 */
public class IslandPerimeter {

    // V0
//    public int islandPerimeter(int[][] grid) {
//
//    }

    // V1
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0463-island-perimeter.java
    class RecursiveBiFunction<A, B, C> {
        BiFunction<A, B, C> func;
    }

    public int islandPerimeter_1(int[][] _grid) {
        final int[][] grid = _grid;
        final Set<Integer> visit = new HashSet<>();

        final RecursiveBiFunction<Integer, Integer, Integer> dfs = new RecursiveBiFunction();
        dfs.func = (i, j) -> {
            if(i >= grid.length || j >= grid[0].length || i < 0 || j < 0 || grid[i][j] == 0)
                return 1;
            //convert 2D-Coordinate to 1D-Coordinate
            int flatCoord = i*grid[0].length + j;
            if(visit.contains(flatCoord))
                return 0;

            visit.add(flatCoord);
            int perim = dfs.func.apply(i, j + 1);
            perim += dfs.func.apply(i + 1, j);
            perim += dfs.func.apply(i, j - 1);
            perim += dfs.func.apply(i - 1, j);
            return perim;
        };

        for(int i = 0; i < grid.length; i++)
            for(int j = 0; j < grid[0].length; j++)
                if(grid[i][j] != 0)
                    return dfs.func.apply(i, j);
        return -1;
    }


    // V2
    // https://leetcode.com/problems/island-perimeter/solutions/5039036/fasterlesser2-methodsdetailed-approachco-5805/
    public int islandPerimeter_2(int[][] grid) {
        int perimeter = 0;
        int rows = grid.length;
        int cols = grid[0].length;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 1) {
                    perimeter += 4;
                    if (r > 0 && grid[r - 1][c] == 1) {
                        perimeter -= 2;
                    }
                    if (c > 0 && grid[r][c - 1] == 1) {
                        perimeter -= 2;
                    }
                }
            }
        }

        return perimeter;
    }

    // V3
    // https://leetcode.com/problems/island-perimeter/submissions/1616175020/
    public int islandPerimeter_3(int[][] grid) {
        Queue<int[]> q = new LinkedList<>();
        int n = grid.length;
        int m = grid[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 1) {
                    q.offer(new int[] { i, j });
                    grid[i][j] = 2;
                    break;
                }
            }
        }
        int ans = 0;
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int row = cur[0];
            int col = cur[1];
            if (row > 0) {
                if (grid[row - 1][col] == 1) {
                    q.offer(new int[] { row - 1, col });
                    grid[row - 1][col] = 2;
                } else if (grid[row - 1][col] == 0) {
                    ans++;
                }
            } else {
                ans++;
            }
            if (row < n - 1) {
                if (grid[row + 1][col] == 1) {
                    q.offer(new int[] { row + 1, col });
                    grid[row + 1][col] = 2;
                } else if (grid[row + 1][col] == 0) {
                    ans++;
                }
            } else {
                ans++;
            }
            if (col > 0) {
                if (grid[row][col - 1] == 1) {
                    q.offer(new int[] { row, col - 1 });
                    grid[row][col - 1] = 2;
                } else if (grid[row][col - 1] == 0) {
                    ans++;
                }
            } else {
                ans++;
            }
            if (col < m - 1) {
                if (grid[row][col + 1] == 1) {
                    q.offer(new int[] { row, col + 1 });
                    grid[row][col + 1] = 2;
                } else if (grid[row][col + 1] == 0) {
                    ans++;
                }
            } else {
                ans++;
            }
        }
        return ans;
    }

    // V4
    // https://leetcode.com/problems/island-perimeter/solutions/6600010/easiest-code-java-soln-beats-9982-leetco-3par/
    public int islandPerimeter_4(int[][] grid) {
        int perimeter = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 1) {
                    if (i == 0 || grid[i - 1][j] == 0)
                        perimeter++;
                    if (i == grid.length - 1 || grid[i + 1][j] == 0)
                        perimeter++;
                    if (j == 0 || grid[i][j - 1] == 0)
                        perimeter++;
                    if (j == grid[i].length - 1 || grid[i][j + 1] == 0)
                        perimeter++;
                }
            }
        }

        return perimeter;
    }


}
