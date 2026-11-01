package LeetCodeJava.Array;

// https://leetcode.com/problems/find-all-groups-of-farmland/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * 1992. Find All Groups of Farmland
 * Medium
 *
 * You are given a 0-indexed m x n binary matrix land where a 0 represents a hectare of forested land
 * and a 1 represents a hectare of farmland.
 *
 * To keep the land organized, there are designated rectangular areas of hectares that consist
 * entirely of farmland. These rectangular areas are called groups. No two groups are adjacent,
 * meaning farmland in one group is not four-directionally adjacent to another farmland in a
 * different group.
 *
 * land can be represented by a coordinate system where the top left corner of land is (0, 0) and the
 * bottom right corner of land is (m-1, n-1). Find the coordinates of the top left and bottom right
 * corner of each group of farmland. A group of farmland with a top left corner at (r1, c1) and a
 * bottom right corner at (r2, c2) is represented by the 4-length array [r1, c1, r2, c2].
 *
 * Return a 2D array containing the 4-length arrays described above for each group of farmland in
 * land. If there are no groups of farmland, return an empty array. You may return the answer in any
 * order.
 *
 *
 * Example 1:
 *
 * Input: land = [[1,0,0],[0,1,1],[0,1,1]]
 * Output: [[0,0,0,0],[1,1,2,2]]
 *
 * Example 2:
 *
 * Input: land = [[1,1],[1,1]]
 * Output: [[0,0,1,1]]
 *
 * Example 3:
 *
 * Input: land = [[0]]
 * Output: []
 *
 *
 * Constraints:
 *
 * m == land.length
 * n == land[i].length
 * 1 <= m, n <= 300
 * land consists of only 0's and 1's.
 * Groups of farmland are rectangular in shape.
 *
 */
public class FindAllGroupsOfFarmland {

    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    // V0
    // IDEA: SCAN FOR TOP-LEFT CORNERS, THEN WALK THE RECTANGLE'S EDGES
    // every group is a solid rectangle and groups never touch, so (i, j) is a top-left corner iff it
    // is farmland with no farmland directly above or to the left. From there walk down column j to
    // find the bottom row, then right along that row to find the right column.
    /**
     * time = O(M * N)
     * space = O(1) besides the output
     */
    public int[][] findFarmland(int[][] land) {
        int rows = land.length;
        int cols = land[0].length;
        List<int[]> groups = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (land[i][j] == 0) {
                    continue;
                }
                boolean farmlandAbove = i > 0 && land[i - 1][j] == 1;
                boolean farmlandLeft = j > 0 && land[i][j - 1] == 1;
                if (farmlandAbove || farmlandLeft) {
                    continue;
                }

                int bottom = i;
                while (bottom + 1 < rows && land[bottom + 1][j] == 1) {
                    bottom++;
                }
                int right = j;
                while (right + 1 < cols && land[bottom][right + 1] == 1) {
                    right++;
                }
                groups.add(new int[]{i, j, bottom, right});
            }
        }
        return groups.toArray(new int[0][]);
    }

    // V1
    // IDEA: BFS FLOOD FILL, TRACKING EACH COMPONENT'S BOUNDING BOX
    // does not rely on the rectangle guarantee for the walk: every unvisited farmland cell starts a
    // BFS, and the min / max row and column it reaches are the group's corners.
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int[][] findFarmland_1(int[][] land) {
        int rows = land.length;
        int cols = land[0].length;
        boolean[][] visited = new boolean[rows][cols];
        List<int[]> groups = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (land[i][j] == 0 || visited[i][j]) {
                    continue;
                }
                int minRow = i;
                int minCol = j;
                int maxRow = i;
                int maxCol = j;
                Queue<int[]> queue = new ArrayDeque<>();
                queue.offer(new int[]{i, j});
                visited[i][j] = true;
                while (!queue.isEmpty()) {
                    int[] cell = queue.poll();
                    minRow = Math.min(minRow, cell[0]);
                    minCol = Math.min(minCol, cell[1]);
                    maxRow = Math.max(maxRow, cell[0]);
                    maxCol = Math.max(maxCol, cell[1]);
                    for (int[] direction : DIRECTIONS) {
                        int nextR = cell[0] + direction[0];
                        int nextC = cell[1] + direction[1];
                        if (nextR >= 0 && nextR < rows && nextC >= 0 && nextC < cols
                                && land[nextR][nextC] == 1 && !visited[nextR][nextC]) {
                            visited[nextR][nextC] = true;
                            queue.offer(new int[]{nextR, nextC});
                        }
                    }
                }
                groups.add(new int[]{minRow, minCol, maxRow, maxCol});
            }
        }
        return groups.toArray(new int[0][]);
    }
}
